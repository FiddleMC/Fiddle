package org.fiddlemc.fiddle.impl.packetmapping.item.reverse;

import com.mojang.serialization.DynamicOps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

/**
 * A class, bound to a particular client session,
 * that allows for the reversing of the mapping of item stacks sent to the client.
 * In other words, it allows for the mapping of the data sent to the client (or received from it)
 * back to the original server-side data.
 */
public class ItemMappingReverser {

    private static final String ID_TAG_KEY = "fiddle_server_side_item";
    private static final long MAX_REMEMBER_MAPPING_TIME = 600_000_000_000L; // 10 minutes
    private static final int MAX_REMEMBER_MAPPING_COUNT = 10_000;
    private static final long INITIAL_TIME = System.nanoTime();

    /**
     * The cached return value for {@link #tagOps},
     * or null if not cached yet.
     */
    private static @Nullable DynamicOps<Tag> TAG_OPS;

    private static DynamicOps<Tag> tagOps() {
        if (TAG_OPS == null) {
            TAG_OPS = MinecraftServer.getServer().registryAccess().createSerializationContext(NbtOps.INSTANCE);
        }
        return TAG_OPS;
    }

    private final Semaphore lock = new Semaphore(1);
    private int nextId = 0;

    /**
     * All current {@link ExistingMapping} by their {@link ExistingMapping#id}.
     */
    private final Int2ObjectMap<ExistingMapping> existingMappingsById = new Int2ObjectOpenHashMap<>();

    /**
     * All current {@link ExistingMapping}s sorted by their {@link ExistingMapping#timeLastUsed}.
     */
    private final TreeSet<ExistingMapping> existingMappingsByTimeLastUsed = new TreeSet<>(
        Comparator.comparingLong((ExistingMapping mapping) -> mapping.timeLastUsed).thenComparingInt(mapping -> mapping.id)
    );

    /**
     * All current {@link ExistingMapping}s by their {@link ExistingMapping#sentBeforeAddingIdWithCount1Tag}.
     */
    private final Map<Tag, List<ExistingMapping>> existingMappingsBySentBeforeAddingIdTag = new HashMap<>();

    private static class ExistingMapping {

        final int id;

        /**
         * The last {@link System#nanoTime()} at which this instance was accessed,
         * minus the {@link #INITIAL_TIME}.
         */
        long timeLastUsed;

        /**
         * The server-side item stack, with {@linkplain ItemStack#getCount() count} 1.
         */
        final ItemStack serverSideWithCount1;

        /**
         * The server-side item stack, with {@linkplain ItemStack#getCount() count} 1, as a {@link Tag}.
         */
        final Tag serverSideWithCount1Tag;

        /**
         * The item stack sent to the client
         * before adding the id tag, with {@linkplain ItemStack#getCount() count} 1, as a {@link Tag}.
         */
        final Tag sentBeforeAddingIdWithCount1Tag;

        /**
         * The item stack sent to the client
         * after adding the id tag, with {@linkplain ItemStack#getCount() count} 1.
         */
        final ItemStack sentAfterAddingIdWithCount1;

        ExistingMapping(int id, final ItemStack serverSideWithCount1, final Tag serverSideWithCount1Tag, final Tag sentBeforeAddingIdWithCount1Tag, final ItemStack sentAfterAddingIdWithCount1) {
            this.id = id;
            this.setTimeLastUsed();
            this.serverSideWithCount1 = serverSideWithCount1;
            this.serverSideWithCount1Tag = serverSideWithCount1Tag;
            this.sentBeforeAddingIdWithCount1Tag = sentBeforeAddingIdWithCount1Tag;
            this.sentAfterAddingIdWithCount1 = sentAfterAddingIdWithCount1;
        }

        void setTimeLastUsed() {
            this.timeLastUsed = System.nanoTime() - INITIAL_TIME;
        }

        void updateTimeLastUsed(ItemMappingReverser reverser) {
            reverser.existingMappingsByTimeLastUsed.remove(this);
            this.setTimeLastUsed();
            reverser.existingMappingsByTimeLastUsed.add(this);
        }

        void removeFromAllExceptByTimeLastUsed(ItemMappingReverser reverser) {
            reverser.existingMappingsById.remove(this.id);
            reverser.existingMappingsBySentBeforeAddingIdTag.compute(this.sentBeforeAddingIdWithCount1Tag, ($, mappings) -> {
                if (mappings == null) {
                    return null;
                }
                mappings.remove(this);
                return mappings.isEmpty() ? null : mappings;
            });
        }

    }

    /**
     * Makes the sent item stack to {@linkplain #reverseMappingIfPossible reversible}
     * into the server-side item stack.
     *
     * @param sent       The {@link ItemStack} being sent to a client.
     *                   This may be modified.
     * @param serverSide The server-side {@link ItemStack} that should be enclosed inside.
     * @return The {@link ItemStack} to be sent, which may be {@code sent}.
     */
    public ItemStack makeReversible(ItemStack sent, ItemStack serverSide) {

        Tag sentWithCount1Tag = ItemStack.CODEC.encodeStart(tagOps(), sent.copyWithCount(1)).getOrThrow();
        ItemStack serverSideWithCount1 = serverSide.copyWithCount(1);
        Tag serverSideWithCount1Tag = ItemStack.CODEC.encodeStart(tagOps(), serverSideWithCount1).getOrThrow();

        this.lock.acquireUninterruptibly();
        try {

            // Check existing mappings
            List<ExistingMapping> existingMappings = this.existingMappingsBySentBeforeAddingIdTag.computeIfAbsent(sentWithCount1Tag, $ -> new ArrayList<>(1));
            for (ExistingMapping existingMapping : existingMappings) {
                if (existingMapping.serverSideWithCount1Tag.equals(serverSideWithCount1Tag)) {
                    existingMapping.updateTimeLastUsed(this);
                    return existingMapping.sentAfterAddingIdWithCount1.copyWithCount(sent.getCount());
                }
            }

            // Create a new mapping
            int id = this.nextId++;
            @Nullable CustomData customData = sent.get(DataComponents.CUSTOM_DATA);
            if (customData == null) {
                customData = CustomData.of(new CompoundTag());
            }
            IntTag idTag = IntTag.valueOf(id);
            CustomData newCustomData = customData.update(tag -> tag.put(ID_TAG_KEY, idTag));
            sent.set(DataComponents.CUSTOM_DATA, newCustomData);
            ItemStack sentAfterAddingIdWithCount1 = sent.copyWithCount(1);
            ExistingMapping existingMapping = new ExistingMapping(
                id,
                serverSideWithCount1,
                serverSideWithCount1Tag,
                sentWithCount1Tag,
                sentAfterAddingIdWithCount1
            );
            this.existingMappingsById.put(id, existingMapping);
            this.existingMappingsByTimeLastUsed.add(existingMapping);
            existingMappings.add(existingMapping);

            // Remove mappings if outdated or too many
            while (this.existingMappingsByTimeLastUsed.size() >= MAX_REMEMBER_MAPPING_COUNT) {
                ExistingMapping firstMapping = this.existingMappingsByTimeLastUsed.pollFirst();
                firstMapping.removeFromAllExceptByTimeLastUsed(this);
            }
            while (!this.existingMappingsByTimeLastUsed.isEmpty()) {
                ExistingMapping firstMapping = this.existingMappingsByTimeLastUsed.first();
                if (System.nanoTime() - INITIAL_TIME - firstMapping.timeLastUsed > MAX_REMEMBER_MAPPING_TIME) {
                    this.existingMappingsByTimeLastUsed.pollFirst();
                    firstMapping.removeFromAllExceptByTimeLastUsed(this);
                } else {
                    break;
                }
            }

            // Return the sent item stack
            return sent;

        } finally {
            this.lock.release();
        }

    }

    /**
     * Reverses the mapping that was applied to the {@code received} item stack.
     *
     * @param received The {@link ItemStack} that was received from a client.
     * @return The original server-side {@link ItemStack},
     * or {@code received} if none could be determined.
     */
    public ItemStack reverseMappingIfPossible(ItemStack received) {
        @Nullable CustomData customData = received.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            Tag idTag = customData.getUnsafe().get(ID_TAG_KEY);
            if (idTag instanceof IntTag intTag) {
                int id = intTag.intValue();
                @Nullable ExistingMapping existingMapping;
                this.lock.acquireUninterruptibly();
                try {
                    existingMapping = this.existingMappingsById.get(id);
                    if (existingMapping != null) {
                        existingMapping.updateTimeLastUsed(this);
                    }
                } finally {
                    this.lock.release();
                }
                if (existingMapping != null) {
                    return existingMapping.serverSideWithCount1.copyWithCount(received.getCount());
                }
            }
        }
        return received;
    }


}

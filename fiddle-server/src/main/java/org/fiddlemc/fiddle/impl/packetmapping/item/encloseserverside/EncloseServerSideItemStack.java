package org.fiddlemc.fiddle.impl.packetmapping.item.encloseserverside;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jspecify.annotations.Nullable;

/**
 * Provides functionality to enclose the server-side {@link ItemStack}
 * inside {@link ItemStack}s sent to clients, and parse it from them again.
 */
public final class EncloseServerSideItemStack {

    private EncloseServerSideItemStack() {
        throw new UnsupportedOperationException();
    }

    private final static String TAG_KEY = "fiddle_server_side_item";

    /**
     * Writes the server-side item stack into the sent item stack.
     *
     * @param sent       The {@link ItemStack} being sent to a client.
     *                   This instance will be modified.
     * @param serverSide The server-side {@link ItemStack} that should be enclosed inside.
     */
    public static void encloseServerSide(ItemStack sent, ItemStack serverSide) {
        @Nullable CustomData customData = sent.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            customData = CustomData.of(new CompoundTag());
        }
        Tag serverSideAsTag = ItemStack.CODEC.encodeStart(MinecraftServer.getServer().registryAccess().createSerializationContext(NbtOps.INSTANCE), serverSide).getOrThrow();
        CustomData newCustomData = customData.update(tag -> tag.put(TAG_KEY, serverSideAsTag));
        sent.set(DataComponents.CUSTOM_DATA, newCustomData);
    }

    /**
     * Extracts the server-side item stack that is enclosed inside the received item stack.
     *
     * @param received The {@link ItemStack} that was received from a client.
     * @return The server-side {@link ItemStack} that was enclosed inside,
     * or null if none was found.
     */
    public static ItemStack extractServerSide(ItemStack received) {
        @Nullable CustomData customData = received.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return null;
        }
        Tag serverSideAsTag = customData.getUnsafe().get(TAG_KEY);
        if (!(serverSideAsTag instanceof CompoundTag)) {
            return null;
        }
        try {
            return ItemStack.CODEC.parse(MinecraftServer.getServer().registryAccess().createSerializationContext(NbtOps.INSTANCE), serverSideAsTag).getOrThrow();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Convenience function that returns the same as {@link #extractServerSide},
     * except it returns {@code received} instead of null.
     */
    public static ItemStack extractServerSideOrKeepReceived(ItemStack received) {
        @Nullable ItemStack extracted = extractServerSide(received);
        return extracted != null ? extracted : received;
    }

}

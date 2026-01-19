package org.fiddlemc.fiddle.minecraft.packet.mapping.item;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.PrioritizableLifecycleEventType;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.client.ClientProfile;
import org.fiddlemc.fiddle.minecraft.registries.ItemRegistry;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A pipeline of registered {@link ItemMapping}s.
 */
public class ItemMappingPipeline implements ItemMappingRegistrar {

    private static ItemMappingPipeline INSTANCE;

    public static ItemMappingPipeline get() {
        if (INSTANCE == null) {
            INSTANCE = new ItemMappingPipeline();
        }
        return INSTANCE;
    }

    public static final class ComposeEvent implements PaperLifecycleEvent {

        public ItemMappingRegistrar getRegistrar() {
            return get();
        }

        @Override
        public void invalidate() {
            get().registrationOpen = false;
            PaperLifecycleEvent.super.invalidate();
        }
    }

    public static final class ComposeEventType extends PrioritizableLifecycleEventType.Simple<BootstrapContext, ComposeEvent> {

        private ComposeEventType() {
            super("fiddle_item_mapping/compose", BootstrapContext.class);
        }

    }

    private static ComposeEventType COMPOSE_EVENT_TYPE;

    public static ComposeEventType compose() {
        if (COMPOSE_EVENT_TYPE == null) {
            COMPOSE_EVENT_TYPE = new ComposeEventType();
        }
        return COMPOSE_EVENT_TYPE;
    }

    /**
     * The registered item mappings.
     * <p>
     * The mappings are organized in an array where {@link ClientProfile.AwarenessLevel#ordinal()}
     * is the index, and then in a map where {@link Item} is the key.
     * The array does not contain null values, but some maps may not contain every item as a key.
     * The maps do not contain empty lists.
     * </p>
     */
    private final Map<Item, List<ItemMapping>>[] mappings;

    /**
     * Whether registration is open.
     */
    private boolean registrationOpen = true;

    private ItemMappingPipeline() {
        this.mappings = new Map[ClientProfile.AwarenessLevel.values().length];
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new HashMap<>();
        }
    }

    private Map<Item, List<ItemMapping>> getForAwarenessLevel(ClientProfile.AwarenessLevel awarenessLevel) {
        return ItemMappingPipeline.this.mappings[awarenessLevel.ordinal()];
    }

    private void verifyRegistrationOpen() {
        if (!this.registrationOpen) {
            throw new IllegalStateException("Tried to register item mapping after registration finished");
        }
    }

    @Override
    public void register(ClientProfile.AwarenessLevel awarenessLevel, Item item, ItemMapping mapping) {

        this.verifyRegistrationOpen();

        Map<Item, List<ItemMapping>> mapForAwarenessLevel = ItemMappingPipeline.this.getForAwarenessLevel(awarenessLevel);
        List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
        listForItem.add(mapping);

    }

    @Override
    public void register(ClientProfile.AwarenessLevel[] awarenessLevels, Item item, ItemMapping mapping) {

        this.verifyRegistrationOpen();

        for (ClientProfile.AwarenessLevel awarenessLevel : awarenessLevels) {
            Map<Item, List<ItemMapping>> mapForAwarenessLevel = ItemMappingPipeline.this.getForAwarenessLevel(awarenessLevel);
            List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        }

    }

    @Override
    public void register(ClientProfile.AwarenessLevel awarenessLevel, Item[] items, ItemMapping mapping) {

        this.verifyRegistrationOpen();

        Map<Item, List<ItemMapping>> mapForAwarenessLevel = ItemMappingPipeline.this.getForAwarenessLevel(awarenessLevel);
        for (Item item : items) {
            List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        }

    }

    @Override
    public void register(ClientProfile.AwarenessLevel[] awarenessLevels, Item[] items, ItemMapping mapping) {

        this.verifyRegistrationOpen();

        for (ClientProfile.AwarenessLevel awarenessLevel : awarenessLevels) {
            Map<Item, List<ItemMapping>> mapForAwarenessLevel = ItemMappingPipeline.this.getForAwarenessLevel(awarenessLevel);
            for (Item item : items) {
                List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
                listForItem.add(mapping);
            }
        }

    }

    @Override
    public void registerForAllItems(ClientProfile.AwarenessLevel awarenessLevel, ItemMapping mapping) {

        this.verifyRegistrationOpen();

        Map<Item, List<ItemMapping>> mapForAwarenessLevel = ItemMappingPipeline.this.getForAwarenessLevel(awarenessLevel);
        ItemRegistry.get().stream().forEach(item -> {
            List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        });

    }

    @Override
    public void registerForAllItems(ClientProfile.AwarenessLevel[] awarenessLevels, ItemMapping mapping) {

        this.verifyRegistrationOpen();

        for (ClientProfile.AwarenessLevel awarenessLevel : awarenessLevels) {
            Map<Item, List<ItemMapping>> mapForAwarenessLevel = ItemMappingPipeline.this.getForAwarenessLevel(awarenessLevel);
            ItemRegistry.get().stream().forEach(item -> {
                List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
                listForItem.add(mapping);
            });
        }

    }

    @Override
    public void changeRegistered(final ClientProfile.AwarenessLevel awarenessLevel, final Item item, final Consumer<List<ItemMapping>> consumer) {

        this.verifyRegistrationOpen();

        Map<Item, List<ItemMapping>> mapForAwarenessLevel = ItemMappingPipeline.this.getForAwarenessLevel(awarenessLevel);
        @Nullable List<ItemMapping> original = mapForAwarenessLevel.get(item);
        List<ItemMapping> passed = original != null ? original : new ArrayList<>(1);
        consumer.accept(passed);
        if (passed.isEmpty()) {
            if (original != null) {
                mapForAwarenessLevel.remove(item);
            }
        } else {
            mapForAwarenessLevel.put(item, passed);
        }

    }

    /**
     * Multiple lists in {@link #mappings} may be the same. This attempts to make sure that if
     * {@link #mappings} contains two lists that are equal, that they are the same instance.
     */
    public void minimizeListDuplication() {
        Map<List<ItemMapping>, List<IntObjectPair<Item>>> transposed = new HashMap<>();
        for (int awarenessLevelI = 0; awarenessLevelI < this.mappings.length; awarenessLevelI++) {
            for (Map.Entry<Item, List<ItemMapping>> entry : this.mappings[awarenessLevelI].entrySet()) {
                transposed.computeIfAbsent(entry.getValue(), $ -> new ArrayList<>()).add(IntObjectPair.of(awarenessLevelI, entry.getKey()));
            }
            this.mappings[awarenessLevelI] = new HashMap<>();
        }
        for (Map.Entry<List<ItemMapping>, List<IntObjectPair<Item>>> entry : transposed.entrySet()) {
            for (IntObjectPair<Item> target : entry.getValue()) {
                this.mappings[target.firstInt()].put(target.second(), entry.getKey());
            }
        }
    }

    /**
     * {@linkplain ItemMapping#apply Applies} all applicable mappings to the item stack.
     * <p>
     * This method may modify the given {@code itemStack}.
     * </p>
     *
     * @param context The context of this mapping, where the {@link ItemMapping.Context#getOriginal} is null.
     * @return The resulting {@link ItemStack}, which may or may not be the given {@code itemStack}.
     */
    public ItemStack apply(ItemStack itemStack, ItemMappingContextImpl context) {
        Map<Item, List<ItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(context.getClientProfile().getAwarenessLevel());
        @Nullable List<ItemMapping> mappings = mapForAwarenessLevel.get(itemStack.getItem());
        if (mappings == null) {
            return itemStack;
        }
        ItemStack current = itemStack;
        ItemMappingContextImpl contextWithOriginal = new ItemMappingContextImpl(itemStack.copy(), context.getClientProfile(), context.isItemStackInItemFrame(), context.isStonecutterRecipeResult());
        for (ItemMapping mapping : mappings) {
            @Nullable ItemStack result = mapping.apply(current, contextWithOriginal);
            if (result != null) {
                current = result;
            }
        }
        return current;
    }

}

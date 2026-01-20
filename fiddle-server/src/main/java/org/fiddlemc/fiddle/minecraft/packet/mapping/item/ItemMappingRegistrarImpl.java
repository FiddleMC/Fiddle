package org.fiddlemc.fiddle.minecraft.packet.mapping.item;

import net.minecraft.world.item.Item;
import org.fiddlemc.fiddle.client.ClientView;
import org.fiddlemc.fiddle.minecraft.registries.ItemRegistry;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class ItemMappingRegistrarImpl implements ItemMappingRegistrar {

    /**
     * The registered item mappings.
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in a map where {@link Item} is the key.
     * The array does not contain null values, but some maps may not contain every item as a key.
     * The maps do not contain empty lists.
     * </p>
     */
    final Map<Item, List<ItemMapping>>[] mappings;

    public ItemMappingRegistrarImpl() {
        this.mappings = new Map[ClientView.AwarenessLevel.values().length];
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new HashMap<>();
        }
    }

    @Override
    public void register(ClientView.AwarenessLevel awarenessLevel, Item item, ItemMapping mapping) {
        Map<Item, List<ItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
        listForItem.add(mapping);
    }

    @Override
    public void register(ClientView.AwarenessLevel[] awarenessLevels, Item item, ItemMapping mapping) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            Map<Item, List<ItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        }
    }

    @Override
    public void register(ClientView.AwarenessLevel awarenessLevel, Item[] items, ItemMapping mapping) {
        Map<Item, List<ItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        for (Item item : items) {
            List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        }
    }

    @Override
    public void register(ClientView.AwarenessLevel[] awarenessLevels, Item[] items, ItemMapping mapping) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            Map<Item, List<ItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            for (Item item : items) {
                List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
                listForItem.add(mapping);
            }
        }
    }

    @Override
    public void registerForAllItems(ClientView.AwarenessLevel awarenessLevel, ItemMapping mapping) {
        Map<Item, List<ItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        ItemRegistry.get().stream().forEach(item -> {
            List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        });
    }

    @Override
    public void registerForAllItems(ClientView.AwarenessLevel[] awarenessLevels, ItemMapping mapping) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            Map<Item, List<ItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            ItemRegistry.get().stream().forEach(item -> {
                List<ItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
                listForItem.add(mapping);
            });
        }
    }

    @Override
    public void changeRegistered(final ClientView.AwarenessLevel awarenessLevel, final Item item, final Consumer<List<ItemMapping>> consumer) {
        Map<Item, List<ItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
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

    private Map<Item, List<ItemMapping>> getForAwarenessLevel(ClientView.AwarenessLevel awarenessLevel) {
        return this.mappings[awarenessLevel.ordinal()];
    }

}

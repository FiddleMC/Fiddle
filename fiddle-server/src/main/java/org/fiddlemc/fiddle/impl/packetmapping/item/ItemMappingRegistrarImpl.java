package org.fiddlemc.fiddle.impl.packetmapping.item;

import net.minecraft.world.item.Item;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMapping;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingRegistrar;
import org.fiddlemc.fiddle.impl.moredatadriven.minecraft.ItemRegistry;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The implementation of {@link NMSItemMappingRegistrar}.
 */
public final class ItemMappingRegistrarImpl implements NMSItemMappingRegistrar {

    /**
     * The registered mappings.
     *
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in a map where {@link Item} is the key.
     * The array does not contain null values, but some maps may not contain every item as a key.
     * The maps do not contain empty lists.
     * </p>
     */
    final Map<Item, List<NMSItemMapping>>[] mappings;

    public ItemMappingRegistrarImpl() {
        this.mappings = new Map[ClientView.AwarenessLevel.values().length];
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new HashMap<>();
        }
    }

    @Override
    public void register(ClientView.AwarenessLevel awarenessLevel, Item item, NMSItemMapping mapping) {
        Map<Item, List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
        listForItem.add(mapping);
    }

    @Override
    public void register(ClientView.AwarenessLevel[] awarenessLevels, Item item, NMSItemMapping mapping) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            Map<Item, List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        }
    }

    @Override
    public void register(ClientView.AwarenessLevel awarenessLevel, Item[] items, NMSItemMapping mapping) {
        Map<Item, List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        for (Item item : items) {
            List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        }
    }

    @Override
    public void register(ClientView.AwarenessLevel[] awarenessLevels, Item[] items, NMSItemMapping mapping) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            Map<Item, List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            for (Item item : items) {
                List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
                listForItem.add(mapping);
            }
        }
    }

    @Override
    public void registerForAllItems(ClientView.AwarenessLevel awarenessLevel, NMSItemMapping mapping) {
        Map<Item, List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        ItemRegistry.get().stream().forEach(item -> {
            List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        });
    }

    @Override
    public void registerForAllItems(ClientView.AwarenessLevel[] awarenessLevels, NMSItemMapping mapping) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            Map<Item, List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            ItemRegistry.get().stream().forEach(item -> {
                List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item, $ -> new ArrayList<>(1));
                listForItem.add(mapping);
            });
        }
    }

    @Override
    public void changeRegistered(ClientView.AwarenessLevel awarenessLevel, Item item, Consumer<List<NMSItemMapping>> consumer) {
        Map<Item, List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        @Nullable List<NMSItemMapping> original = mapForAwarenessLevel.get(item);
        List<NMSItemMapping> passed = original != null ? original : new ArrayList<>(1);
        consumer.accept(passed);
        if (passed.isEmpty()) {
            if (original != null) {
                mapForAwarenessLevel.remove(item);
            }
        } else {
            mapForAwarenessLevel.put(item, passed);
        }
    }

    private Map<Item, List<NMSItemMapping>> getForAwarenessLevel(ClientView.AwarenessLevel awarenessLevel) {
        return this.mappings[awarenessLevel.ordinal()];
    }

}

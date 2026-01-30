package org.fiddlemc.fiddle.impl.packetmapping.item;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.item.Item;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMapping;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingPipelineComposeEvent;
import org.fiddlemc.fiddle.impl.moredatadriven.minecraft.ItemRegistry;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The implementation of {@link NMSItemMappingPipelineComposeEvent}.
 */
public final class ItemMappingPipelineComposeEventImpl implements PaperLifecycleEvent, NMSItemMappingPipelineComposeEvent {

    /**
     * The registered mappings.
     *
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in a map where {@link Item#indexInItemRegistry} is the key.
     * The array does not contain null values, but some maps may not contain every item as a key.
     * The maps do not contain empty lists.
     * </p>
     */
    final Int2ObjectMap<List<NMSItemMapping>>[] mappings;

    public ItemMappingPipelineComposeEventImpl() {
        this.mappings = new Int2ObjectMap[ClientView.AwarenessLevel.getAll().length];
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new Int2ObjectOpenHashMap<>();
        }
    }

    @Override
    public void register(ClientView.AwarenessLevel awarenessLevel, Item item, NMSItemMapping mapping) {
        Int2ObjectMap<List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item.indexInItemRegistry, $ -> new ArrayList<>(1));
        listForItem.add(mapping);
    }

    @Override
    public void register(ClientView.AwarenessLevel[] awarenessLevels, Item item, NMSItemMapping mapping) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            Int2ObjectMap<List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item.indexInItemRegistry, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        }
    }

    @Override
    public void register(ClientView.AwarenessLevel awarenessLevel, Item[] items, NMSItemMapping mapping) {
        Int2ObjectMap<List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        for (Item item : items) {
            List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item.indexInItemRegistry, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        }
    }

    @Override
    public void register(ClientView.AwarenessLevel[] awarenessLevels, Item[] items, NMSItemMapping mapping) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            Int2ObjectMap<List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            for (Item item : items) {
                List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item.indexInItemRegistry, $ -> new ArrayList<>(1));
                listForItem.add(mapping);
            }
        }
    }

    @Override
    public void registerForAllItems(ClientView.AwarenessLevel awarenessLevel, NMSItemMapping mapping) {
        Int2ObjectMap<List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        ItemRegistry.get().stream().forEach(item -> {
            List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item.indexInItemRegistry, $ -> new ArrayList<>(1));
            listForItem.add(mapping);
        });
    }

    @Override
    public void registerForAllItems(ClientView.AwarenessLevel[] awarenessLevels, NMSItemMapping mapping) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            Int2ObjectMap<List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            ItemRegistry.get().stream().forEach(item -> {
                List<NMSItemMapping> listForItem = mapForAwarenessLevel.computeIfAbsent(item.indexInItemRegistry, $ -> new ArrayList<>(1));
                listForItem.add(mapping);
            });
        }
    }

    @Override
    public void changeRegistered(ClientView.AwarenessLevel awarenessLevel, Item item, Consumer<List<NMSItemMapping>> consumer) {
        Int2ObjectMap<List<NMSItemMapping>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        @Nullable List<NMSItemMapping> original = mapForAwarenessLevel.get(item.indexInItemRegistry);
        List<NMSItemMapping> passed = original != null ? original : new ArrayList<>(1);
        consumer.accept(passed);
        if (passed.isEmpty()) {
            if (original != null) {
                mapForAwarenessLevel.remove(item.indexInItemRegistry);
            }
        } else {
            mapForAwarenessLevel.put(item.indexInItemRegistry, passed);
        }
    }

    private Int2ObjectMap<List<NMSItemMapping>> getForAwarenessLevel(ClientView.AwarenessLevel awarenessLevel) {
        return this.mappings[awarenessLevel.ordinal()];
    }

}

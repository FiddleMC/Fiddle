package org.fiddlemc.fiddle.impl.packetmapping.item;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingRegistrar;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMapping;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingHandle;
import org.fiddlemc.fiddle.impl.clientview.ClientViewImpl;
import org.fiddlemc.fiddle.impl.packetmapping.WithClientViewContextSingleStepMappingPipeline;
import org.fiddlemc.fiddle.impl.packetmapping.item.reverse.ItemMappingReverser;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.MappingPipelineImpl;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.WithContextSingleStepMappingPipeline;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pipeline of {@link NMSItemMapping}s.
 */
public final class ItemMappingPipelineImpl extends MappingPipelineImpl.Simple<ItemMappingRegistrar, ItemMappingRegistrarImpl> implements WithClientViewContextSingleStepMappingPipeline<ItemStack, ItemMappingContext, NMSItemMappingHandle, ItemMappingRegistrar>, ItemMappingPipeline {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<ItemMappingPipeline, ItemMappingPipelineImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(ItemMappingPipelineImpl.class);
        }

    }

    public static ItemMappingPipelineImpl get() {
        return (ItemMappingPipelineImpl) ItemMappingPipeline.get();
    }

    @Override
    protected String getEventTypeNamePrefix() {
        return "fiddle_item_mapping_pipeline";
    }

    /**
     * The registered mappings.
     *
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in a map where {@link Item#indexInItemRegistry} is the key.
     * The array does not contain null values, but some maps may not contain every item as a key.
     * The maps do not contain empty arrays.
     * </p>
     */
    private final Int2ObjectMap<NMSItemMapping[]>[] mappings;

    private ItemMappingPipelineImpl() {
        this.mappings = new Int2ObjectMap[ClientView.AwarenessLevel.values().length];
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new Int2ObjectOpenHashMap<>(16, 0.5f);
        }
    }

    @Override
    public NMSItemMapping @Nullable [] getMappingsThatMayApplyTo(NMSItemMappingHandle handle) {
        Int2ObjectMap<NMSItemMapping[]> mapForAwarenessLevel = this.mappings[handle.getContext().getClientView().getAwarenessLevel().ordinal()];
        return mapForAwarenessLevel.get(handle.getOriginal().getItem().indexInItemRegistry);
    }

    @Override
    public NMSItemMappingHandle createHandle(ItemStack data, ItemMappingContext context) {
        return new ItemMappingHandleImpl(data, context);
    }

    @Override
    public ItemMappingContextImpl createGenericContext(ClientView clientView) {
        return new ItemMappingContextImpl(clientView, false, false);
    }

    @Override
    public ItemStack apply(ItemStack data, ItemMappingContext context) {

        // Skip the mapping for empty item stacks
        if (data.isEmpty() || data.getItem() == null) {
            return data;
        }

        // Apply the pipeline
        ItemStack mapped = WithClientViewContextSingleStepMappingPipeline.super.apply(data, context);

        // If changes were made, make sure the mapping can be reversed
        if (!data.equals(mapped)) {
            @Nullable ItemMappingReverser itemMappingReverser = ((ClientViewImpl) context.getClientView()).getItemMappingReverser();
            if (itemMappingReverser != null) {
                mapped = itemMappingReverser.makeReversible(mapped, data);
            }
        }

        // Return the result
        return mapped;

    }

    /**
     * Similar to {@link #apply}, but for a whole item type.
     *
     * @return The mapped {@link Item}, on a best-attempt basis.
     */
    public Item apply(Item data, ItemMappingContext context) {
        return apply(new ItemStack(data), context).getItem();
    }

    /**
     * Convenience function for {@link #apply(Item, ItemMappingContext)},
     * analogous to {@link WithContextSingleStepMappingPipeline#applyGenerically}.
     */
    public Item applyGenerically(Item data) {
        return this.apply(data, this.createGenericContext());
    }

    /**
     * Convenience function to call {@link #apply(Item, ItemMappingContext)}
     * for all items in the given {@link HolderSet}.
     *
     * @return A {@link HolderSet} of mapped {@link Item}s.
     * This {@link HolderSet} may be the given {@code data}, and if not, the {@link Holder}s inside
     * may be those in {@code data}.
     */
    public HolderSet<Item> apply(HolderSet<Item> data, ItemMappingContext context) {
        Int2ObjectMap<Item> fromToMap = new Int2ObjectArrayMap<>();
        List<Holder<Item>> result = new ArrayList<>(data.size());
        boolean changed = false;
        for (int i = 0; i < data.size(); i++) {
            Holder<Item> holder = data.get(i);
            Item item = holder.value();
            int id = item.indexInItemRegistry;
            Item mapped = fromToMap.computeIfAbsent(id, $ -> this.apply(item, context));
            if (mapped != item) {
                changed = true;
            }
            result.add(changed ? Holder.direct(mapped) : holder);
        }
        return changed ? HolderSet.direct(result) : data;
    }

    /**
     * Convenience function for {@link #apply(HolderSet, ItemMappingContext)},
     * analogous to {@link WithContextSingleStepMappingPipeline#applyGenerically}.
     */
    public HolderSet<Item> applyGenerically(HolderSet<Item> data) {
        return this.apply(data, this.createGenericContext());
    }

    @Override
    protected ItemMappingRegistrarImpl createRegistrar() {
        return new ItemMappingRegistrarImpl();
    }

    @Override
    public void copyMappingsFrom(ItemMappingRegistrarImpl registrar) {
        Map<List<NMSItemMapping>, List<IntIntPair>> transposed = new HashMap<>();
        for (int awarenessLevelI = 0; awarenessLevelI < registrar.mappings.length; awarenessLevelI++) {
            for (Int2ObjectMap.Entry<List<NMSItemMapping>> entry : registrar.mappings[awarenessLevelI].int2ObjectEntrySet()) {
                transposed.computeIfAbsent(entry.getValue(), $ -> new ArrayList<>()).add(IntIntPair.of(awarenessLevelI, entry.getKey()));
            }
        }
        for (Map.Entry<List<NMSItemMapping>, List<IntIntPair>> entry : transposed.entrySet()) {
            for (IntIntPair target : entry.getValue()) {
                this.mappings[target.firstInt()].put(target.secondInt(), entry.getKey().toArray(NMSItemMapping[]::new));
            }
        }
    }

}

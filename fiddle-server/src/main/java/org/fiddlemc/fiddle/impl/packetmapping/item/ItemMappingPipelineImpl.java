package org.fiddlemc.fiddle.impl.packetmapping.item;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingFunctionContext;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappings;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingsComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMapping;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingHandle;
import org.fiddlemc.fiddle.impl.moredatadriven.minecraft.ItemRegistry;
import org.fiddlemc.fiddle.impl.packetmapping.WithClientViewContextSingleStepMappingPipeline;
import org.fiddlemc.fiddle.impl.packetmapping.item.builtin.BuiltInItemMapperImpl;
import org.fiddlemc.fiddle.impl.util.composable.ComposableImpl;
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
public final class ItemMappingPipelineImpl extends ComposableImpl<ItemMappingsComposeEvent, ItemMappingPipelineComposeEventImpl> implements WithClientViewContextSingleStepMappingPipeline<ItemStack, ItemMappingFunctionContext, NMSItemMappingHandle, ItemMappingsComposeEvent>, ItemMappings {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<ItemMappings, ItemMappingPipelineImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(ItemMappingPipelineImpl.class);
        }

    }

    public static ItemMappingPipelineImpl get() {
        return (ItemMappingPipelineImpl) ItemMappings.get();
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
     * is the index, and then in an array where {@link Item#indexInItemRegistry} is the index.
     * The lowest-level array may be null, but will never be empty.
     * </p>
     */
    private final NMSItemMapping[][][] mappings;

    private ItemMappingPipelineImpl() {
        this.mappings = new NMSItemMapping[ClientView.AwarenessLevel.getAll().length][][];
    }

    @Override
    public NMSItemMapping @Nullable [] getMappingsThatMayApplyTo(NMSItemMappingHandle handle) {
        return this.mappings[handle.getContext().getClientView().getAwarenessLevel().ordinal()][handle.getOriginal().getItem().indexInItemRegistry];
    }

    @Override
    public NMSItemMappingHandle createHandle(ItemStack data, ItemMappingFunctionContext context) {
        return new ItemMappingHandleImpl(data, context, false);
    }

    @Override
    public ItemMappingContextImpl createGenericContext(ClientView clientView) {
        return new ItemMappingContextImpl(clientView, false, false);
    }

    @Override
    public ItemStack apply(ItemStack data, ItemMappingFunctionContext context) {
        // Skip the mapping for empty item stacks
        if (data.isEmpty() || data.getItem() == null) {
            return data;
        }
        // Apply the pipeline
        return WithClientViewContextSingleStepMappingPipeline.super.apply(data, context);
    }

    /**
     * Similar to {@link #apply}, but for a whole item type.
     *
     * @return The mapped {@link Item}, on a best-attempt basis.
     */
    public Item apply(Item data, ItemMappingFunctionContext context) {
        return apply(new ItemStack(data), context).getItem();
    }

    /**
     * Convenience function for {@link #apply(Item, ItemMappingFunctionContext)},
     * analogous to {@link WithContextSingleStepMappingPipeline#applyGenerically}.
     */
    public Item applyGenerically(Item data) {
        return this.apply(data, this.createGenericContext());
    }

    /**
     * Convenience function to call {@link #apply(Item, ItemMappingFunctionContext)}
     * for all items in the given {@link HolderSet}.
     *
     * @return A {@link HolderSet} of mapped {@link Item}s.
     * This {@link HolderSet} may be the given {@code data}, and if not, the {@link Holder}s inside
     * may be those in {@code data}.
     */
    public HolderSet<Item> apply(HolderSet<Item> data, ItemMappingFunctionContext context) {
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
     * Convenience function for {@link #apply(HolderSet, ItemMappingFunctionContext)},
     * analogous to {@link WithContextSingleStepMappingPipeline#applyGenerically}.
     */
    public HolderSet<Item> applyGenerically(HolderSet<Item> data) {
        return this.apply(data, this.createGenericContext());
    }

    @Override
    protected ItemMappingPipelineComposeEventImpl createComposeEvent() {
        // Create the event
        ItemMappingPipelineComposeEventImpl event = new ItemMappingPipelineComposeEventImpl();
        // Register the built-in mappings
        BuiltInItemMapperImpl builtInItemMapper = BuiltInItemMapperImpl.get();
        builtInItemMapper.fireComposeEvent();
        builtInItemMapper.registerMappings(event);
        // Return the event
        return event;
    }

    @Override
    protected void copyInformationFromEvent(ItemMappingPipelineComposeEventImpl event) {

        // Initialize the mappings
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new NMSItemMapping[ItemRegistry.get().size()][];
        }

        // Invert the mapping from id -> lists of mappings to list of mappings -> ids, so that we only have one reference per unique list
        Map<List<NMSItemMapping>, List<IntIntPair>> invertedMappings = new HashMap<>();
        for (int awarenessLevelI = 0; awarenessLevelI < event.mappings.length; awarenessLevelI++) {
            for (Int2ObjectMap.Entry<List<NMSItemMapping>> entry : event.mappings[awarenessLevelI].int2ObjectEntrySet()) {
                invertedMappings.computeIfAbsent(entry.getValue(), $ -> new ArrayList<>()).add(IntIntPair.of(awarenessLevelI, entry.getIntKey()));
            }
        }
        // Invert back
        for (Map.Entry<List<NMSItemMapping>, List<IntIntPair>> entry : invertedMappings.entrySet()) {
            for (IntIntPair target : entry.getValue()) {
                this.mappings[target.firstInt()][target.secondInt()] = entry.getKey().toArray(NMSItemMapping[]::new);
            }
        }

    }

}

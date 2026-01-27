package org.fiddlemc.fiddle.impl.packetmapping.item;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMapping;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.fiddlemc.fiddle.impl.packetmapping.PacketDataMappingPipelineImpl;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pipeline of {@link NMSItemMapping}s.
 */
public final class ItemMappingPipelineImpl extends PacketDataMappingPipelineImpl<ItemStack, ItemMappingHandle<ItemStack>, ItemMappingContext, NMSItemMapping, ItemMappingRegistrarImpl> implements ItemMappingPipeline<ItemStack, ItemMappingHandle<ItemStack>, ItemMappingContext, NMSItemMapping, ItemMappingRegistrarImpl> {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<ItemMappingPipeline<ItemStack, ItemMappingHandle<ItemStack>, ItemMappingContext, NMSItemMapping, ItemMappingRegistrarImpl>, ItemMappingPipelineImpl> implements ServiceProvider<ItemStack, ItemMappingHandle<ItemStack>, ItemMappingContext, NMSItemMapping, ItemMappingRegistrarImpl> {

        public ServiceProviderImpl() {
            super(ItemMappingPipelineImpl.class);
        }

    }

    public static ItemMappingPipelineImpl get() {
        return (ItemMappingPipelineImpl) ItemMappingPipeline.get();
    }

    @Override
    protected String getEventTypeNamePrefix() {
        return "fiddle_item_mapping";
    }

    private final class ComposeEventImpl extends PacketDataMappingPipelineImpl<ItemStack, ItemMappingHandle<ItemStack>, ItemMappingContext, NMSItemMapping, ItemMappingRegistrarImpl>.ComposeEventImpl {

        public ComposeEventImpl(ItemMappingRegistrarImpl registrar) {
            super(registrar);
        }

        @Override
        public void invalidate() {

            // Bake the mappings
            Map<List<NMSItemMapping>, List<IntIntPair>> transposed = new HashMap<>();
            for (int awarenessLevelI = 0; awarenessLevelI < this.registrar.mappings.length; awarenessLevelI++) {
                for (Int2ObjectMap.Entry<List<NMSItemMapping>> entry : this.registrar.mappings[awarenessLevelI].int2ObjectEntrySet()) {
                    transposed.computeIfAbsent(entry.getValue(), $ -> new ArrayList<>()).add(IntIntPair.of(awarenessLevelI, entry.getKey()));
                }
            }
            for (Map.Entry<List<NMSItemMapping>, List<IntIntPair>> entry : transposed.entrySet()) {
                for (IntIntPair target : entry.getValue()) {
                    ItemMappingPipelineImpl.this.mappings[target.firstInt()].put(target.secondInt(), entry.getKey().toArray(NMSItemMapping[]::new));
                }
            }

            // Continue with invalidation
            super.invalidate();

        }

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
    public NMSItemMapping @Nullable [] getMappingsThatMayApplyTo(ItemStack data, ItemMappingContext context) {
        Int2ObjectMap<NMSItemMapping[]> mapForAwarenessLevel = this.mappings[context.getClientView().getAwarenessLevel().ordinal()];
        return mapForAwarenessLevel.get(data.getItem().indexInItemRegistry);
    }

    @Override
    public ItemMappingHandleImpl createHandle(ItemStack data) {
        return new ItemMappingHandleImpl(data);
    }

    @Override
    public ItemStack apply(ItemStack data, ItemMappingContext context) {

        // Skip the mapping for empty item stacks
        if (data.isEmpty() || data.getItem() == null) {
            return data;
        }

        // Apply the pipeline
        ItemStack mapped = super.apply(data, context);

        // If changes were made, make sure the mapping can be reversed
        if (!data.equals(mapped)) {
            org.fiddlemc.fiddle.impl.packetmapping.item.reverse.@Nullable ItemMappingReverser itemMappingReverser = ((org.fiddlemc.fiddle.impl.clientview.ClientViewImpl) context.getClientView()).getItemMappingReverser();
            if (itemMappingReverser != null) {
                mapped = itemMappingReverser.makeReversible(mapped, data);
            }
        }

        // Return the result
        return mapped;

    }

    @Override
    protected ItemMappingContextImpl createGenericContext(ClientView clientView) {
        return new ItemMappingContextImpl(clientView, false, false);
    }

    @Override
    protected ItemMappingRegistrarImpl createRegistrar() {
        return new ItemMappingRegistrarImpl();
    }

    @Override
    protected <CE extends ComposeEvent<ItemStack, ItemMappingRegistrarImpl> & PaperLifecycleEvent> CE createComposeEvent() {
        return (CE) new ComposeEventImpl(this.createRegistrar());
    }

}

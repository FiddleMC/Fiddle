package org.fiddlemc.fiddle.impl.packetmapping.item;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMapping;
import org.fiddlemc.fiddle.impl.clientview.lookup.packethandling.ClientViewLookupThreadLocal;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.fiddlemc.fiddle.impl.packetmapping.MutablePacketDataMappingHandleImpl;
import org.fiddlemc.fiddle.impl.packetmapping.PacketDataMappingPipelineImpl;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pipeline of {@link NMSItemMapping}s.
 */
public final class ItemMappingPipelineImpl extends PacketDataMappingPipelineImpl<ItemStack, MutablePacketDataMappingHandleImpl<ItemStack>, ItemMappingContext, NMSItemMapping, ItemMappingRegistrarImpl> implements ItemMappingPipeline<ItemStack, MutablePacketDataMappingHandleImpl<ItemStack>, ItemMappingContext, NMSItemMapping, ItemMappingRegistrarImpl> {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<ItemMappingPipeline<ItemStack, MutablePacketDataMappingHandleImpl<ItemStack>, ItemMappingContext, NMSItemMapping, ItemMappingRegistrarImpl>, ItemMappingPipelineImpl> implements ServiceProvider<ItemStack, MutablePacketDataMappingHandleImpl<ItemStack>, ItemMappingContext, NMSItemMapping, ItemMappingRegistrarImpl> {

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

    private final class ComposeEventImpl extends PacketDataMappingPipelineImpl<ItemStack, MutablePacketDataMappingHandleImpl<ItemStack>, ItemMappingContext, NMSItemMapping, ItemMappingRegistrarImpl>.ComposeEventImpl {

        public ComposeEventImpl(ItemMappingRegistrarImpl registrar) {
            super(registrar);
        }

        @Override
        public void invalidate() {

            // Bake the mappings
            Map<List<NMSItemMapping>, List<IntObjectPair<Item>>> transposed = new HashMap<>();
            for (int awarenessLevelI = 0; awarenessLevelI < this.registrar.mappings.length; awarenessLevelI++) {
                for (Map.Entry<Item, List<NMSItemMapping>> entry : this.registrar.mappings[awarenessLevelI].entrySet()) {
                    transposed.computeIfAbsent(entry.getValue(), $ -> new ArrayList<>()).add(IntObjectPair.of(awarenessLevelI, entry.getKey()));
                }
            }
            for (Map.Entry<List<NMSItemMapping>, List<IntObjectPair<Item>>> entry : transposed.entrySet()) {
                for (IntObjectPair<Item> target : entry.getValue()) {
                    ItemMappingPipelineImpl.this.mappings[target.firstInt()].put(target.second(), entry.getKey().toArray(NMSItemMapping[]::new));
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
     * is the index, and then in a map where {@link Item} is the key.
     * The array does not contain null values, but some maps may not contain every item as a key.
     * The maps do not contain empty arrays.
     * </p>
     */
    private final Map<Item, NMSItemMapping[]>[] mappings;

    private ItemMappingPipelineImpl() {
        this.mappings = new Map[ClientView.AwarenessLevel.values().length];
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new HashMap<>(16, 0.5f);
        }
    }

    @Override
    public NMSItemMapping @Nullable [] getMappingsThatMayApplyTo(ItemStack data, ItemMappingContext context) {
        Map<Item, NMSItemMapping[]> mapForAwarenessLevel = this.mappings[context.getClientView().getAwarenessLevel().ordinal()];
        return mapForAwarenessLevel.get(data.getItem());
    }

    @Override
    public ItemMappingHandleImpl createHandle(final ItemStack data) {
        return new ItemMappingHandleImpl(data);
    }

    /**
     * Convenience method for {@link #apply},
     * which can be called during generic packet handling.
     * It will create an {@link ItemMappingContextImpl} based on {@link ClientViewLookupThreadLocal}.
     */
    public ItemStack applyGenerically(ItemStack itemStack) {
        return this.apply(itemStack, new ItemMappingContextImpl(ClientViewLookupThreadLocal.getThreadLocalClientViewOrFallback(), false, false));
    }

    public void fireComposeEvent() {
        LifecycleEventRunner.INSTANCE.callEvent(composeEventType(), new ComposeEventImpl(new ItemMappingRegistrarImpl()));
    }

}

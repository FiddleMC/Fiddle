package org.fiddlemc.fiddle.impl.packetmapping.item.builtin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.builtin.BuiltInItemMapper;
import org.fiddlemc.fiddle.api.packetmapping.item.builtin.BuiltInItemMapperComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingUtilities;
import org.fiddlemc.fiddle.impl.packetmapping.component.ComponentMappingPipelineImpl;
import org.fiddlemc.fiddle.impl.packetmapping.item.ItemMappingPipelineComposeEventImpl;
import org.fiddlemc.fiddle.impl.util.composable.ComposableImpl;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.jspecify.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * The implementation of {@link BuiltInItemMapper}.
 */
public final class BuiltInItemMapperImpl extends ComposableImpl<BuiltInItemMapperComposeEvent, BuiltInItemMapperComposeEventImpl> implements BuiltInItemMapper {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<BuiltInItemMapper, BuiltInItemMapperImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(BuiltInItemMapperImpl.class);
        }

    }

    public static BuiltInItemMapperImpl get() {
        return (BuiltInItemMapperImpl) BuiltInItemMapper.get();
    }

    /**
     * The mapped items, or null after {@link #clear()} has been called.
     *
     * <p>
     * The items are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in a map where {@code from} is the key and {@code to} is the value.
     * The array does not contain null values, but some maps may not contain every item as a key.
     * </p>
     */
    public Map<Item, Item> @Nullable [] mappedItems;

    public BuiltInItemMapperImpl() {
        this.mappedItems = new Map[ClientView.AwarenessLevel.getAll().length];
        for (int i = 0; i < this.mappedItems.length; i++) {
            this.mappedItems[i] = new HashMap<>();
        }
    }

    @Override
    protected String getEventTypeNamePrefix() {
        return "fiddle_built_in_item_mapper";
    }

    @Override
    protected BuiltInItemMapperComposeEventImpl createComposeEvent() {
        return new BuiltInItemMapperComposeEventImpl(this);
    }

    public void registerMappings(ItemMappingPipelineComposeEventImpl itemMappingPipelineComposeEvent) {

        // Mappings for mapped items
        for (ClientView.AwarenessLevel awarenessLevel : ClientView.AwarenessLevel.getAll()) {
            for (Map.Entry<Item, Item> fromAndTo : this.mappedItems[awarenessLevel.ordinal()].entrySet()) {
                Item from = fromAndTo.getKey();
                Item to = fromAndTo.getValue();
                if (from != to) {
                    itemMappingPipelineComposeEvent.register(awarenessLevel, from, handle -> {
                        NMSItemMappingUtilities.get().setItemWhilePreservingRest(handle, to);
                    });
                }
            }
        }

        // Mappings to map default item name components
        itemMappingPipelineComposeEvent.registerForAllItems(ClientView.AwarenessLevel.getAll(), handle -> {
            Component itemName = handle.getImmutable().getItemName().copy();
            Component mappedItemName = ComponentMappingPipelineImpl.get().apply(itemName, handle.getContext());
            if (!mappedItemName.equals(itemName)) {
                handle.getMutable().set(DataComponents.ITEM_NAME, mappedItemName);
            }
        });

        // Clear to save memory
        this.clear();

    }

    /**
     * Clears all data in this mapper to reclaim memory.
     */
    private void clear() {
        this.mappedItems = null;
    }

}

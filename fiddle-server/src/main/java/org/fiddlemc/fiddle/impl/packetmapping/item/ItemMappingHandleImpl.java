package org.fiddlemc.fiddle.impl.packetmapping.item;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingHandle;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.WithContextMappingHandleImpl;

/**
 * The implementation of {@link NMSItemMappingHandle}.
 */
public class ItemMappingHandleImpl extends WithContextMappingHandleImpl<ItemStack, ItemStack, ItemMappingContext> implements NMSItemMappingHandle {

    public ItemMappingHandleImpl(final ItemStack data, final ItemMappingContext context, boolean isDataMutable) {
        super(data, context, isDataMutable);
    }

    @Override
    protected ItemStack cloneMutable(ItemStack data) {
        return data.copy();
    }

}

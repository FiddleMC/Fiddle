package org.fiddlemc.fiddle.impl.packetmapping.item;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingFunctionContext;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.ItemMappingHandleNMS;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.SimpleWithContextMappingFunctionHandleImpl;

/**
 * The implementation of {@link ItemMappingHandleNMS}.
 */
public class ItemMappingHandleImpl extends SimpleWithContextMappingFunctionHandleImpl<ItemStack, ItemStack, ItemMappingFunctionContext> implements ItemMappingHandleNMS {

    public ItemMappingHandleImpl(final ItemStack data, final ItemMappingFunctionContext context, boolean isDataMutable) {
        super(data, context, isDataMutable);
    }

    @Override
    protected ItemStack cloneMutable(ItemStack data) {
        return data.copy();
    }

}

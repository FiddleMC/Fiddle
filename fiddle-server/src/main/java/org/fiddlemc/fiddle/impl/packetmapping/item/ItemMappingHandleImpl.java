package org.fiddlemc.fiddle.impl.packetmapping.item;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMapping;
import org.fiddlemc.fiddle.impl.packetmapping.MutablePacketDataMappingHandleImpl;

/**
 * The {@link PacketDataMappingHandle} implementation for {@link NMSItemMapping}s.
 */
public class ItemMappingHandleImpl extends MutablePacketDataMappingHandleImpl<ItemStack, ItemStack> implements ItemMappingHandle<ItemStack> {

    public ItemMappingHandleImpl(ItemStack original) {
        super(original);
    }

    @Override
    protected ItemStack cloneMutable(ItemStack data) {
        return data.copy();
    }

}

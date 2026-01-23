package org.fiddlemc.fiddle.api.packetmapping.item.nms;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMapping;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingContext;
import org.fiddlemc.fiddle.impl.packetmapping.MutablePacketDataMappingHandleImpl;

/**
 * A {@link PacketDataMapping} for {@link ItemStack}s.
 */
@FunctionalInterface
public interface NMSItemMapping extends PacketDataMapping<ItemStack, MutablePacketDataMappingHandleImpl<ItemStack>, ItemMappingContext> {
}

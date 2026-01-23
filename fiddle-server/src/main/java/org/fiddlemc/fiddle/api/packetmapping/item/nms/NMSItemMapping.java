package org.fiddlemc.fiddle.api.packetmapping.item.nms;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMapping;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMapping;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingHandle;

/**
 * A {@link PacketDataMapping} for {@link ItemStack}s.
 */
@FunctionalInterface
public interface NMSItemMapping extends ItemMapping<ItemStack, ItemMappingHandle<ItemStack>> {
}

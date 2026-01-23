package org.fiddlemc.fiddle.api.packetmapping.item;

import org.fiddlemc.fiddle.api.packetmapping.PacketDataMapping;

/**
 * A {@link PacketDataMapping} for the {@link ItemMappingPipeline}.
 */
public interface ItemMapping<T, H extends ItemMappingHandle<T>> extends PacketDataMapping<T, H, ItemMappingContext> {
}

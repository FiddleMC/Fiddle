package org.fiddlemc.fiddle.api.packetmapping.component;

import org.fiddlemc.fiddle.api.packetmapping.PacketDataMapping;

/**
 * A {@link PacketDataMapping} for the {@link ComponentMappingPipeline}.
 */
public interface ComponentMapping<T, MT extends T, H extends ComponentMappingHandle<T, MT>> extends PacketDataMapping<T, H, ComponentMappingContext> {
}

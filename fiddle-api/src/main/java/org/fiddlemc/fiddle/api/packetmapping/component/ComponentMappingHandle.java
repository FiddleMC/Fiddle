package org.fiddlemc.fiddle.api.packetmapping.component;

import org.fiddlemc.fiddle.api.packetmapping.MutablePacketDataMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingHandle;

/**
 * A {@link PacketDataMappingHandle} for the {@link ComponentMappingPipeline}.
 */
public interface ComponentMappingHandle<T, MT extends T> extends MutablePacketDataMappingHandle<T, MT> {
}

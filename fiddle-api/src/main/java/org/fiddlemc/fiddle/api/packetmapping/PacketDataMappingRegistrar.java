package org.fiddlemc.fiddle.api.packetmapping;

import org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineRegistrar;

/**
 * Provides functionality to register {@linkplain PacketDataMapping}s to a pipeline.
 */
public interface PacketDataMappingRegistrar<T> extends MappingPipelineRegistrar {
}

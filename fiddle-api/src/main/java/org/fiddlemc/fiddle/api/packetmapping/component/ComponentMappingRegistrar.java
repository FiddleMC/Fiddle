package org.fiddlemc.fiddle.api.packetmapping.component;

import org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineRegistrar;

/**
 * Provides functionality to register {@linkplain ComponentMapping}s to the {@link ComponentMappingPipeline}.
 *
 * <p>
 * Currently, this must be cast to {@code NMSComponentMappingRegistrar} to be used.
 * </p>
 */
public interface ComponentMappingRegistrar extends MappingPipelineRegistrar {
}

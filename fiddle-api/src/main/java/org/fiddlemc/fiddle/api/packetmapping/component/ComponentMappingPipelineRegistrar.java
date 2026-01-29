package org.fiddlemc.fiddle.api.packetmapping.component;

import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineRegistrar;

/**
 * Provides functionality to register mappings to the {@link ComponentMappingPipeline}.
 *
 * <p>
 * Currently, this must be cast to {@code NMSComponentMappingRegistrar} to be used.
 * </p>
 */
public interface ComponentMappingPipelineRegistrar extends MappingPipelineRegistrar {
}

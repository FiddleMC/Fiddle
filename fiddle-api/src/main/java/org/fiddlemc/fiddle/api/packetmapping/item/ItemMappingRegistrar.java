package org.fiddlemc.fiddle.api.packetmapping.item;

import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineRegistrar;

/**
 * Provides functionality to register mappings to the {@link ItemMappingPipeline}.
 *
 * <p>
 * Currently, this must be cast to {@code NMSItemMappingRegistrar} to be used.
 * </p>
 */
public interface ItemMappingRegistrar extends MappingPipelineRegistrar {
}

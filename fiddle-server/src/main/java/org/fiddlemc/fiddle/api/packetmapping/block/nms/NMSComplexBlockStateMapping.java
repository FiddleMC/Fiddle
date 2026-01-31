package org.fiddlemc.fiddle.api.packetmapping.block.nms;

import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;

/**
 * A complex mapping that can be registered with the {@link NMSBlockMappingPipelineComposeEvent}.
 *
 * <p>
 * It represents a mapping that maps to the result of some {@linkplain #apply code}.
 * </p>
 */
@FunctionalInterface
public non-sealed interface NMSComplexBlockStateMapping extends NMSBlockStateMapping, SingleStepMapping<NMSBlockStateMappingHandle> {
}

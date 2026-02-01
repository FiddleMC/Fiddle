package org.fiddlemc.fiddle.api.packetmapping.block.nms;

import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;

/**
 * Common base for {@link NMSSimpleBlockStateMapping} and {@link NMSComplexBlockStateMapping}.
 */
public sealed interface NMSBlockStateMapping extends SingleStepMapping<NMSBlockStateMappingHandle> permits NMSSimpleBlockStateMapping, NMSComplexBlockStateMapping {
}

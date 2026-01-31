package org.fiddlemc.fiddle.api.packetmapping.block.nms;

/**
 * Common base for {@link NMSSimpleBlockStateMapping} and {@link NMSComplexBlockStateMapping}.
 */
public sealed interface NMSBlockStateMapping permits NMSSimpleBlockStateMapping, NMSComplexBlockStateMapping {
}

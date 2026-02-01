package org.fiddlemc.fiddle.api.packetmapping.block.nms;

/**
 * A complex mapping that can be registered with the {@link NMSBlockMappingPipelineComposeEvent}.
 *
 * <p>
 * It represents a mapping that maps to the result of some {@linkplain #apply code}.
 * </p>
 *
 * <p>
 * This mapping must be deterministic.
 * In other words, if it is called twice with the same arguments, it must return the same result.
 * </p>
 *
 * <p>
 * If this mapping relies on external factors (for example, the time, or the player
 * having a certain advancement), then when those factors change, any blocks influenced by it should be re-sent
 * to the player to avoid desynchronization.
 * </p>
 */
@FunctionalInterface
public non-sealed interface NMSComplexBlockStateMapping extends NMSBlockStateMapping {
}

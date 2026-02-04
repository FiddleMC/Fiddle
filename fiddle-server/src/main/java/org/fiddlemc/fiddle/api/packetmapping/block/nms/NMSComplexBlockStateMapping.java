package org.fiddlemc.fiddle.api.packetmapping.block.nms;

import org.fiddlemc.fiddle.api.packetmapping.block.BlockStateMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.block.ComplexBlockStateMapping;
import java.util.function.Consumer;

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
 *
 * @param apply               The code to run.
 * @param requiresCoordinates Whether this mapping requires the coordinates
 *                            ({@link BlockStateMappingContext#getPhysicalBlockX()} and so on).
 */
public record NMSComplexBlockStateMapping(
    Consumer<NMSBlockStateMappingHandle> apply,
    boolean requiresCoordinates
) implements ComplexBlockStateMapping, NMSBlockStateMapping {

    @Override
    public void apply(final NMSBlockStateMappingHandle handle) {
        this.apply.accept(handle);
    }

}

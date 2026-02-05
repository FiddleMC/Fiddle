package org.fiddlemc.fiddle.api.packetmapping.block.nms;

import net.minecraft.world.level.block.state.BlockState;

/**
 * A simple mapping that can be registered with the {@link NMSBlockMappingsComposeEvent}.
 *
 * <p>
 * It represents a mapping that always maps to the {@linkplain #getTo same} {@link BlockState}.
 * </p>
 */
public non-sealed interface NMSSimpleBlockStateMapping extends NMSBlockStateMapping {

    /**
     * @return {@link BlockState} that this mapping maps to.
     */
    BlockState getTo();

}

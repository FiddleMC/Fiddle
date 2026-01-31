package org.fiddlemc.fiddle.api.packetmapping.block;

import org.fiddlemc.fiddle.api.packetmapping.ClientViewMappingContext;

/**
 * A {@link ClientViewMappingContext} for the {@link BlockMappingPipeline}.
 */
public interface BlockStateMappingContext extends ClientViewMappingContext {

    /**
     * @return Whether the block state on which this mapping is being applied
     * is the block state of a physical block, i.e. a block at certain coordinates in a world.
     *
     * <p>
     * An example of where this method would return false is the block state of a block display entity.
     * </p>
     */
    boolean isStateOfPhysicalBlockInWorld();

}

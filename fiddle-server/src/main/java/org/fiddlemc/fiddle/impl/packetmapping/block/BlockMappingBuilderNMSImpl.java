package org.fiddlemc.fiddle.impl.packetmapping.block;

import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.BlockMappingBuilderNMS;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.BlockMappingHandleNMS;

/**
 * The implementation of {@link BlockMappingBuilderNMS}.
 */
public class BlockMappingBuilderNMSImpl extends AbstractBlockMappingBuilderImpl<BlockState, BlockMappingHandleNMS> implements BlockMappingBuilderNMS {

    @Override
    protected Iterable<BlockState> getStatesToRegisterFor() {
        return this.from;
    }

    @Override
    protected BlockMappingsStep createFunctionStep() {
        return new MinecraftFunctionBlockMappingsStep(this.function, this.functionRequiresCoordinates);
    }

    @Override
    protected BlockState getSimpleTo() {
        return this.to;
    }

}

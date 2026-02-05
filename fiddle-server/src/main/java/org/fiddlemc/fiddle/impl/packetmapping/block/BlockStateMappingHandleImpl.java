package org.fiddlemc.fiddle.impl.packetmapping.block;

import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockStateMappingFunctionContext;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSBlockStateMappingHandle;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.SimpleWithContextMappingFunctionHandleImpl;

/**
 * The implementation of {@link NMSBlockStateMappingHandle}.
 */
public class BlockStateMappingHandleImpl extends SimpleWithContextMappingFunctionHandleImpl<BlockState, BlockState, BlockStateMappingFunctionContext> implements NMSBlockStateMappingHandle {

    public BlockStateMappingHandleImpl(BlockState data, BlockStateMappingFunctionContext context, boolean isDataMutable) {
        super(data, context, isDataMutable);
    }

}

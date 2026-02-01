package org.fiddlemc.fiddle.impl.packetmapping.block;

import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockStateMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSBlockStateMappingHandle;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.WithContextMappingHandleImpl;

/**
 * The implementation of {@link NMSBlockStateMappingHandle}.
 */
public class BlockStateMappingHandleImpl extends WithContextMappingHandleImpl<BlockState, BlockState, BlockStateMappingContext> implements NMSBlockStateMappingHandle {

    public BlockStateMappingHandleImpl(BlockState data, BlockStateMappingContext context, boolean isDataMutable) {
        super(data, context, isDataMutable);
    }

}

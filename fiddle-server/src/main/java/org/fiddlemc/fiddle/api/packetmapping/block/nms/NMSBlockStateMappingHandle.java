package org.fiddlemc.fiddle.api.packetmapping.block.nms;

import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockStateMappingFunctionContext;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithContextMappingFunctionHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithOriginalMappingFunctionHandle;

/**
 * A handle provided to {@link NMSComplexBlockStateMapping}s.
 */
public interface NMSBlockStateMappingHandle extends WithContextMappingFunctionHandle<BlockState, BlockStateMappingFunctionContext>, WithOriginalMappingFunctionHandle<BlockState> {
}

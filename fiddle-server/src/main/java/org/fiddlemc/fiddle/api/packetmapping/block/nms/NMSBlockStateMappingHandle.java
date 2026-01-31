package org.fiddlemc.fiddle.api.packetmapping.block.nms;

import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockStateMappingContext;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithContextMappingHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithOriginalMappingHandle;

/**
 * A handle provided to {@link NMSComplexBlockStateMapping}s.
 */
public interface NMSBlockStateMappingHandle extends WithContextMappingHandle<BlockState, BlockStateMappingContext>, WithOriginalMappingHandle<BlockState> {
}

package org.fiddlemc.fiddle.impl.packetmapping.block;

import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSBlockStateMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSSimpleBlockStateMapping;

/**
 * The implementation of {@link NMSSimpleBlockStateMapping}.
 */
public final class SimpleBlockStateMappingImpl implements NMSSimpleBlockStateMapping {

    private final BlockState to;

    SimpleBlockStateMappingImpl(BlockState to) {
        this.to = to;
    }

    @Override
    public BlockState getTo() {
        return this.to;
    }

    @Override
    public void apply(NMSBlockStateMappingHandle handle) {
        handle.set(this.to);
    }

}

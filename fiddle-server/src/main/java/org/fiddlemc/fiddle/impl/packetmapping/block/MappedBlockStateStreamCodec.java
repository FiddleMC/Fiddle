package org.fiddlemc.fiddle.impl.packetmapping.block;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.impl.clientview.lookup.packethandling.ClientViewLookupThreadLocal;

public final class MappedBlockStateStreamCodec implements StreamCodec<ByteBuf, BlockState> {

    public static final MappedBlockStateStreamCodec GENERIC = new MappedBlockStateStreamCodec(false);
    public static final MappedBlockStateStreamCodec PHYSICAL = new MappedBlockStateStreamCodec(true);

    private final StreamCodec<ByteBuf, BlockState> internal;
    private final boolean isStateOfPhysicalBlockInWorld;

    private MappedBlockStateStreamCodec(boolean isStateOfPhysicalBlockInWorld) {
        this.internal = ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY);
        this.isStateOfPhysicalBlockInWorld = isStateOfPhysicalBlockInWorld;
    }

    @Override
    public BlockState decode(ByteBuf buffer) {
        return internal.decode(buffer);
    }

    @Override
    public void encode(ByteBuf buffer, BlockState value) {
        this.internal.encode(buffer, BlockMappingPipelineImpl.get().apply(value, new BlockStateMappingContextImpl(ClientViewLookupThreadLocal.getThreadLocalClientViewOrFallback(), this.isStateOfPhysicalBlockInWorld)));
    }

}

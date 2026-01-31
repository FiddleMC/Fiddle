package org.fiddlemc.fiddle.impl.packetmapping.block;

import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingPipelineComposeEvent;
import org.fiddlemc.fiddle.impl.util.composable.ComposableImpl;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;

/**
 * A pipeline of block mappings.
 */
public final class BlockMappingPipelineImpl extends ComposableImpl<BlockMappingPipelineComposeEvent, BlockMappingPipelineComposeEventImpl> implements BlockMappingPipeline {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<BlockMappingPipeline, BlockMappingPipelineImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(BlockMappingPipelineImpl.class);
        }

    }

    public static BlockMappingPipelineImpl get() {
        return (BlockMappingPipelineImpl) BlockMappingPipeline.get();
    }

    @Override
    protected String getEventTypeNamePrefix() {
        return "fiddle_block_mapping_pipeline";
    }

    @Override
    protected BlockMappingPipelineComposeEventImpl createComposeEvent() {
        // Create the event
        BlockMappingPipelineComposeEventImpl event = new BlockMappingPipelineComposeEventImpl();
        // Register the built-in mappings
        // TODO
        // Return the event
        return event;
    }

}

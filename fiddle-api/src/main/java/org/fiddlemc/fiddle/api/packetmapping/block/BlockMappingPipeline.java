package org.fiddlemc.fiddle.api.packetmapping.block;

import org.fiddlemc.fiddle.api.util.composable.Composable;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.GenericServiceProvider;
import java.util.ServiceLoader;

/**
 * A pipeline of block mappings.
 */
public interface BlockMappingPipeline extends Composable<BlockMappingPipelineComposeEvent> {

    /**
     * An internal interface to get the {@link BlockMappingPipeline} instance.
     */
    interface ServiceProvider extends GenericServiceProvider<BlockMappingPipeline> {
    }

    /**
     * @return The {@link BlockMappingPipeline} instance.
     */
    static BlockMappingPipeline get() {
        return ServiceLoader.load(BlockMappingPipeline.ServiceProvider.class, BlockMappingPipeline.ServiceProvider.class.getClassLoader()).findFirst().get().get();
    }

}

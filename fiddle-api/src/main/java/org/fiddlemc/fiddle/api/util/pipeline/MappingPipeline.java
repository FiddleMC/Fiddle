package org.fiddlemc.fiddle.api.util.pipeline;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;

/**
 * A generic pipeline of mappings.
 */
public interface MappingPipeline<R extends MappingPipelineRegistrar> {

    /**
     * @return The {@link LifecycleEventType} for this pipeline.
     */
    MappingPipelineComposeEventType<R> composeEventType();

}

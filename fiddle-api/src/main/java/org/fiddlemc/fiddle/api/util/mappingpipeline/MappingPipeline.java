package org.fiddlemc.fiddle.api.util.mappingpipeline;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;

/**
 * A generic pipeline of mappings.
 */
public interface MappingPipeline<R extends MappingPipelineRegistrar> {

    /**
     * @return The {@link LifecycleEventType} for this pipeline.
     */
    MappingPipelineComposeEventType<? extends R> composeEventType();

}

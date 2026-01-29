package org.fiddlemc.fiddle.api.util.pipeline;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;

/**
 * A generic pipeline of mappings.
 */
public interface MappingPipeline<R extends MappingPipelineRegistrar> {

    /**
     * @return The {@link LifecycleEventType} for this pipeline.
     */
    LifecycleEventType<BootstrapContext, MappingPipelineComposeEvent<R>, PrioritizedLifecycleEventHandlerConfiguration<BootstrapContext>> composeEventType();

}

package org.fiddlemc.fiddle.api.util.mappingpipeline;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;

/**
 * The {@link LifecycleEventType} for the {@link MappingPipelineComposeEvent}s of some {@link MappingPipeline}.
 */
public interface MappingPipelineComposeEventType<R extends MappingPipelineRegistrar> extends LifecycleEventType<BootstrapContext, MappingPipelineComposeEvent<R>, PrioritizedLifecycleEventHandlerConfiguration<BootstrapContext>> {
}

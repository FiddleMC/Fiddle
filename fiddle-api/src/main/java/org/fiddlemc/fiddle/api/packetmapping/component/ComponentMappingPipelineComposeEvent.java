package org.fiddlemc.fiddle.api.packetmapping.component;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;

/**
 * Provides functionality to register mappings to the {@link ComponentMappingPipeline}.
 *
 * <p>
 * Currently, this must be cast to {@code NMSComponentMappingPipelineComposeEvent} to be used.
 * </p>
 */
public interface ComponentMappingPipelineComposeEvent extends LifecycleEvent {
}

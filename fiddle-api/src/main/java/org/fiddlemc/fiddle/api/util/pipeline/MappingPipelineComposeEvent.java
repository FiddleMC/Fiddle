package org.fiddlemc.fiddle.api.util.pipeline;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;

/**
 * A {@link LifecycleEvent} that fires when a pipeline is composed.
 *
 * <p>
 * Mappings can be registered in handlers of this event,
 * by using the {@link #getRegistrar()}.
 * </p>
 */
public interface MappingPipelineComposeEvent<R extends MappingPipelineRegistrar> extends LifecycleEvent {

    /**
     * The registrar to register mappings with.
     */
    R getRegistrar();

}

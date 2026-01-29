package org.fiddlemc.fiddle.impl.pipeline;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineRegistrar;
import org.jspecify.annotations.Nullable;

/**
 * A base implementation of {@link MappingPipelineComposeEvent}.
 */
public class MappingPipelineComposeEventImpl<R extends MappingPipelineRegistrar> implements MappingPipelineComposeEvent<R>, PaperLifecycleEvent {

    /**
     * The pipeline,
     * or null if it has been cleared when this event has finished firing.
     */
    private @Nullable MappingPipelineImpl<R> pipeline;

    /**
     * The return value for {@link #getRegistrar},
     * or null if it has been cleared when this event has finished firing.
     */
    private @Nullable R registrar;

    public MappingPipelineComposeEventImpl(MappingPipelineImpl<R> pipeline, R registrar) {
        this.pipeline = pipeline;
        this.registrar = registrar;
    }

    @Override
    public R getRegistrar() {
        return this.registrar;
    }

    @Override
    public void invalidate() {

        // Copy the registered information to the pipeline
        this.pipeline.copyMappingsFrom(this.registrar);

        // Clear values to reclaim memory
        this.pipeline = null;
        this.registrar = null;

        // Continue with invalidation
        PaperLifecycleEvent.super.invalidate();

    }

}

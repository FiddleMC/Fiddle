package org.fiddlemc.fiddle.impl.util.mappingpipeline;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineRegistrar;
import org.jspecify.annotations.Nullable;

/**
 * A base implementation of {@link MappingPipelineComposeEvent}.
 */
public class MappingPipelineComposeEventImpl<R extends MappingPipelineRegistrar, RI extends R> implements MappingPipelineComposeEvent<RI>, PaperLifecycleEvent {

    /**
     * The pipeline,
     * or null if it has been cleared when this event has finished firing.
     */
    private @Nullable MappingPipelineImpl<R, RI, ?> pipeline;

    /**
     * The return value for {@link #getRegistrar},
     * or null if it has been cleared when this event has finished firing.
     */
    private @Nullable RI registrar;

    public MappingPipelineComposeEventImpl(MappingPipelineImpl<R, RI, ?> pipeline, RI registrar) {
        this.pipeline = pipeline;
        this.registrar = registrar;
    }

    @Override
    public RI getRegistrar() {
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

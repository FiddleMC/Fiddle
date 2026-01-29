package org.fiddlemc.fiddle.impl.pipeline;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import org.fiddlemc.fiddle.api.util.pipeline.MappingPipeline;
import org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineComposeEventType;
import org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineRegistrar;
import org.jspecify.annotations.Nullable;

/**
 * A base implementation of {@link MappingPipeline}.
 */
public abstract class MappingPipelineImpl<R extends MappingPipelineRegistrar> implements MappingPipeline<R> {

    /**
     * The cached return value of {@link #composeEventType()},
     * or null if not cached yet.
     */
    private @Nullable MappingPipelineComposeEventType<R> composeEventType;

    @Override
    public MappingPipelineComposeEventType<R> composeEventType() {
        if (this.composeEventType == null) {
            this.composeEventType = this.createComposeEventType();
        }
        return this.composeEventType;
    }

    /**
     * The prefix for the {@link LifecycleEventType#name()} of events for this pipeline.
     */
    protected abstract String getEventTypeNamePrefix();

    /**
     * The {@link LifecycleEventType#name()} of events for this pipeline.
     */
    protected String getEventTypeName() {
        return this.getEventTypeNamePrefix() + "/compose";
    }

    protected MappingPipelineComposeEventType<R> createComposeEventType() {
        return new MappingPipelineComposeEventTypeImpl<>(this.getEventTypeName());
    }

    /**
     * @return A new {@linkplain R registrar} instance.
     */
    protected abstract R createRegistrar();

    /**
     * @return A new {@link MappingPipelineComposeEvent}.
     */
    protected <CE extends MappingPipelineComposeEvent<R> & PaperLifecycleEvent> CE createComposeEvent() {
        return (CE) new MappingPipelineComposeEventImpl<>(this, this.createRegistrar());
    }

    /**
     * {@linkplain LifecycleEventRunner#callEvent Fires} a {@link MappingPipelineComposeEvent} for this pipeline.
     */
    public void fireComposeEvent() {
        LifecycleEventRunner.INSTANCE.callEvent(this.composeEventType(), this.createComposeEvent());
    }

    /**
     * Copies the mappings registered in the registrar to this pipeline.
     */
    public abstract void copyMappingsFrom(R registrar);

}

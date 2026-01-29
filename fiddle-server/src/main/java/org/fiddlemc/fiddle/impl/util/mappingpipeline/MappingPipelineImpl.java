package org.fiddlemc.fiddle.impl.util.mappingpipeline;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipeline;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineComposeEventType;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineRegistrar;
import org.jspecify.annotations.Nullable;

/**
 * A base implementation of {@link MappingPipeline}.
 */
public abstract class MappingPipelineImpl<R extends MappingPipelineRegistrar, RI extends R, CE extends MappingPipelineComposeEvent<RI> & PaperLifecycleEvent> implements MappingPipeline<R> {

    /**
     * The cached return value of {@link #composeEventType()},
     * or null if not cached yet.
     */
    private @Nullable MappingPipelineComposeEventType<RI> composeEventType;

    @Override
    public MappingPipelineComposeEventType<RI> composeEventType() {
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

    protected MappingPipelineComposeEventType<RI> createComposeEventType() {
        return new MappingPipelineComposeEventTypeImpl<>(this.getEventTypeName());
    }

    /**
     * @return A new {@linkplain RI registrar} instance.
     */
    protected abstract RI createRegistrar();

    /**
     * @return A new {@link MappingPipelineComposeEvent}.
     */
    protected abstract CE createComposeEvent();

    /**
     * {@linkplain LifecycleEventRunner#callEvent Fires} a {@link MappingPipelineComposeEvent} for this pipeline.
     */
    public void fireComposeEvent() {
        LifecycleEventRunner.INSTANCE.callEvent(this.composeEventType(), this.createComposeEvent());
    }

    /**
     * Copies the mappings registered in the registrar to this pipeline.
     */
    public abstract void copyMappingsFrom(RI registrar);

    /**
     * An implementation of {@link MappingPipelineImpl} using {@link MappingPipelineComposeEventImpl}.
     */
    public static abstract class Simple<R extends MappingPipelineRegistrar, RI extends R> extends MappingPipelineImpl<R, RI, MappingPipelineComposeEventImpl<R, RI>> {

        protected MappingPipelineComposeEventImpl<R, RI> createComposeEvent() {
            return new MappingPipelineComposeEventImpl<>(this, this.createRegistrar());
        }

    }

}

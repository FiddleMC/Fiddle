package org.fiddlemc.fiddle.impl.packetmapping;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.plugin.lifecycle.event.types.PrioritizableLifecycleEventType;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMapping;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingRegistrar;
import org.jspecify.annotations.Nullable;

/**
 * A base implementation of {@link PacketDataMappingPipeline}.
 */
public abstract class PacketDataMappingPipelineImpl<T, H extends PacketDataMappingHandle<T>, C extends PacketDataMappingContext, M extends PacketDataMapping<T, H, C>, R extends PacketDataMappingRegistrar<? extends T>> implements PacketDataMappingPipeline<T, H, C, M, R> {

    /**
     * The prefix for the {@link LifecycleEventType#name()} of events for this pipeline.
     */
    protected abstract String getEventTypeNamePrefix();

    public final class ComposeEventType extends PrioritizableLifecycleEventType.Simple<BootstrapContext, ComposeEvent<T, R>> {

        private ComposeEventType() {
            super(PacketDataMappingPipelineImpl.this.getEventTypeNamePrefix() + "/compose", BootstrapContext.class);
        }

    }

    /**
     * The cached return value of {@link #composeEventType()},
     * or null if not cached yet.
     */
    private @Nullable ComposeEventType composeEventType;

    @Override
    public ComposeEventType composeEventType() {
        if (this.composeEventType == null) {
            this.composeEventType = new ComposeEventType();
        }
        return this.composeEventType;
    }

    /**
     * A base implementation of {@link ComposeEvent}.
     */
    public class ComposeEventImpl implements ComposeEvent<T, R>, PaperLifecycleEvent {

        /**
         * The return value for {@link #getRegistrar},
         * or null if it has been cleared when this event has finished firing.
         */
        protected @Nullable R registrar;

        public ComposeEventImpl(R registrar) {
            this.registrar = registrar;
        }

        @Override
        public R getRegistrar() {
            return this.registrar;
        }

        @Override
        public void invalidate() {
            // Clear the registrar to reclaim memory
            this.registrar = null;
            // Continue with invalidation
            PaperLifecycleEvent.super.invalidate();
        }

    }

}

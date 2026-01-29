package org.fiddlemc.fiddle.impl.packetmapping;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.plugin.lifecycle.event.types.PrioritizableLifecycleEventType;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMapping;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingPipeline;
import org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineComposeEvent;
import org.fiddlemc.fiddle.impl.clientview.lookup.packethandling.ClientViewLookupThreadLocal;
import org.jspecify.annotations.Nullable;

/**
 * A base implementation of {@link PacketDataMappingPipeline}.
 */
public abstract class PacketDataMappingPipelineImpl<T, H extends PacketDataMappingHandle<T>, C extends PacketDataMappingContext, M extends PacketDataMapping<T, H, C>, R extends org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineRegistrar> implements PacketDataMappingPipeline<T, H, C, M, R> {

    /**
     * The prefix for the {@link LifecycleEventType#name()} of events for this pipeline.
     */
    protected abstract String getEventTypeNamePrefix();

    public final class ComposeEventType extends PrioritizableLifecycleEventType.Simple<BootstrapContext, MappingPipelineComposeEvent<R>> {

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
     * A base implementation of {@link MappingPipelineComposeEvent}.
     */
    public class ComposeEventImpl implements MappingPipelineComposeEvent<R>, PaperLifecycleEvent {

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

    /**
     * Creates a generic {@linkplain C context} for {@link #applyGenerically}.
     *
     * <p>
     * This method is allowed to make the resulting context as un-generic as possible,
     * e.g. by using thread-local variables to set fields correctly.
     * </p>
     *
     * @param clientView The {@link ClientView} the context will have.
     */
    protected abstract C createGenericContext(ClientView clientView);

    /**
     * Convenience method for {@link #apply},
     * which can be called during generic packet handling.
     * It will create an {@link C} based on {@link ClientViewLookupThreadLocal}.
     */
    public T applyGenerically(T itemStack) {
        return this.apply(itemStack, this.createGenericContext(ClientViewLookupThreadLocal.getThreadLocalClientViewOrFallback()));
    }

    /**
     * Creates a new {@linkplain R registrar} instance for {@link #createComposeEvent}.
     */
    protected abstract R createRegistrar();

    /**
     * Creates a new {@linkplain R registrar} instance for {@link #fireComposeEvent}.
     */
    protected <CE extends MappingPipelineComposeEvent<R> & PaperLifecycleEvent> CE createComposeEvent() {
        return (CE) new ComposeEventImpl(this.createRegistrar());
    }

    /**
     * {@linkplain LifecycleEventRunner#callEvent Fires} a {@link ComposeEventImpl} for this pipeline.
     */
    public void fireComposeEvent() {
        LifecycleEventRunner.INSTANCE.callEvent(this.composeEventType(), this.createComposeEvent());
    }

}

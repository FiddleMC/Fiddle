package org.fiddlemc.fiddle.impl.packetmapping;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventRunner;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMapping;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingPipeline;
import org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineComposeEvent;
import org.fiddlemc.fiddle.impl.clientview.lookup.packethandling.ClientViewLookupThreadLocal;
import org.fiddlemc.fiddle.impl.pipeline.MappingPipelineComposeEventImpl;
import org.fiddlemc.fiddle.impl.pipeline.MappingPipelineImpl;

/**
 * A base implementation of {@link PacketDataMappingPipeline}.
 */
public abstract class PacketDataMappingPipelineImpl<T, H extends PacketDataMappingHandle<T>, C extends PacketDataMappingContext, M extends PacketDataMapping<T, H, C>, R extends org.fiddlemc.fiddle.api.util.pipeline.MappingPipelineRegistrar> extends MappingPipelineImpl<R> implements PacketDataMappingPipeline<T, H, C, M, R> {

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

}

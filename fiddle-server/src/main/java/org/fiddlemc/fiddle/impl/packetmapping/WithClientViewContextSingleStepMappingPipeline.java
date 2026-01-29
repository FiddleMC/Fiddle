package org.fiddlemc.fiddle.impl.packetmapping;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.ClientViewMappingContext;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineRegistrar;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithContextMappingHandle;
import org.fiddlemc.fiddle.impl.clientview.lookup.packethandling.ClientViewLookupThreadLocal;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.WithContextSingleStepMappingPipeline;

/**
 * A {@link WithContextSingleStepMappingPipeline} for contexts of type {@link ClientViewMappingContext}.
 */
public interface WithClientViewContextSingleStepMappingPipeline<T, C extends ClientViewMappingContext, H extends WithContextMappingHandle<T, C>, R extends MappingPipelineRegistrar> extends WithContextSingleStepMappingPipeline<T, C, H, R> {

    /**
     * Creates a generic {@linkplain C context} for {@link #applyGenerically},
     * using the given {@link ClientView}.
     *
     * <p>
     * While this method is allowed to return a very generic context,
     * it also has the freedom to attempt to make the returned context as accurate as possible,
     * with the information available.
     * </p>
     */
    C createGenericContext(ClientView clientView);

    @Override
    default C createGenericContext() {
        return this.createGenericContext(ClientViewLookupThreadLocal.getThreadLocalClientViewOrFallback());
    }

    /**
     * A simple implementation of {@link WithClientViewContextSingleStepMappingPipeline}
     * where the context is no more complex than {@link ClientViewMappingContext}.
     */
    interface Simple<T, H extends WithContextMappingHandle<T, ClientViewMappingContext>, R extends MappingPipelineRegistrar> extends WithClientViewContextSingleStepMappingPipeline<T, ClientViewMappingContext, H, R> {

        @Override
        default ClientViewMappingContextImpl createGenericContext(ClientView clientView) {
            return new ClientViewMappingContextImpl(clientView);
        }

    }

}

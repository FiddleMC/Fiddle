package org.fiddlemc.fiddle.impl.util.mappingpipeline;

import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingContext;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithContextMappingHandle;

/**
 * A base implementation of {@link WithContextMappingHandle},
 * that builds upon {@link MappingHandleImpl}.
 */
public class WithContextMappingHandleImpl<T, MT extends T, C extends MappingContext> extends MappingHandleImpl<T, MT> implements WithContextMappingHandle<T, C> {

    /**
     * The context.
     */
    protected final C context;

    public WithContextMappingHandleImpl(T data, C context, boolean isDataMutable) {
        super(data, isDataMutable);
        this.context = context;
    }

    @Override
    public C getContext() {
        return this.context;
    }

}

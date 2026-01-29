package org.fiddlemc.fiddle.impl.util.mappingpipeline;

import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MutableMappingHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithOriginalMappingHandle;

/**
 * A base implementation of {@link MappingHandle},
 * that also implements {@link MutableMappingHandle} and {@link WithOriginalMappingHandle}.
 */
public class MappingHandleImpl<T, MT extends T> implements MutableMappingHandle<T, MT>, WithOriginalMappingHandle<T> {

    /**
     * The original data;
     */
    protected final T original;

    /**
     * The current data.
     */
    protected T data;

    /**
     * Whether the data is the original data.
     */
    protected boolean isDataOriginal = false;

    /**
     * Whether {@link #data} is mutable, and of type {@link MT}.
     */
    protected boolean isDataMutable = false;

    public MappingHandleImpl(T data) {
        this.original = data;
        this.data = data;
    }

    @Override
    public T getImmutable() {
        return this.data;
    }

    private void set(T data, boolean isDataMutableAfterwards) {
        this.data = data;
        this.isDataOriginal = false;
        this.isDataMutable = isDataMutableAfterwards;
    }

    @Override
    public void set(T data) {
        this.set(data, false);
    }

    @Override
    public MT getMutable() {
        if (!this.isDataMutable) {
            this.setMutable(this.cloneMutable(this.data));
        }
        return (MT) this.data;
    }

    @Override
    public void setMutable(MT data) {
        this.set(data, true);
    }

    /**
     * @param data A {@link T} instance.
     * @return A new {@link MT} instance with the same contents as the given instance.
     */
    protected MT cloneMutable(T data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T getOriginal() {
        return this.original;
    }

}

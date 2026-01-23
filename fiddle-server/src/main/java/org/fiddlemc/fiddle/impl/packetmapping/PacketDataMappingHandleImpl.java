package org.fiddlemc.fiddle.impl.packetmapping;

import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingHandle;
import org.jspecify.annotations.Nullable;

/**
 * A base implementation of {@link PacketDataMappingHandle}.
 */
public class PacketDataMappingHandleImpl<T> implements PacketDataMappingHandle<T> {

    protected final T original;

    /**
     * The current data,
     * or null if it has not been changed from the {@link #original}.
     */
    protected @Nullable T current;

    public PacketDataMappingHandleImpl(T original) {
        this.original = original;
    }

    @Override
    public T getOriginal() {
        return this.original;
    }

    @Override
    public T getImmutable() {
        return this.current != null ? this.current : this.original;
    }

    @Override
    public void set(T data) {
        this.current = data;
    }

}

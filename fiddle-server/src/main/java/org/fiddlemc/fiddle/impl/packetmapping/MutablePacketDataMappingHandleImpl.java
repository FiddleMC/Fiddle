package org.fiddlemc.fiddle.impl.packetmapping;

import org.fiddlemc.fiddle.api.packetmapping.MutablePacketDataMappingHandle;

/**
 * A base implementation of {@link MutablePacketDataMappingHandle}.
 */
public abstract class MutablePacketDataMappingHandleImpl<T> extends PacketDataMappingHandleImpl<T> implements MutablePacketDataMappingHandle<T> {

    public MutablePacketDataMappingHandleImpl(T original) {
        super(original);
    }

    @Override
    public T getMutable() {
        if (this.current == null) {
            this.current = this.clone(original);
        }
        return this.current;
    }

    /**
     * @param data A {@link T} instance.
     * @return A new {@link T} instance with the same contents as the given instance.
     */
    protected abstract T clone(T data);

}

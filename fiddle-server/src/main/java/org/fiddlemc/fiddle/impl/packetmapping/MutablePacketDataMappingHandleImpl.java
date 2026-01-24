package org.fiddlemc.fiddle.impl.packetmapping;

import org.fiddlemc.fiddle.api.packetmapping.MutablePacketDataMappingHandle;

/**
 * A base implementation of {@link MutablePacketDataMappingHandle}.
 */
public abstract class MutablePacketDataMappingHandleImpl<T, MT extends T> extends PacketDataMappingHandleImpl<T> implements MutablePacketDataMappingHandle<T, MT> {

    protected boolean isCurrentMutable = false;

    public MutablePacketDataMappingHandleImpl(T original) {
        super(original);
    }

    @Override
    public void set(T data) {
        this.isCurrentMutable = false;
        super.set(data);
    }

    /**
     * The same as {@link #set}, but with the explicit guarantee that the given instance
     * is allowed to be mutated by this or other mappings.
     */
    public void setMutable(MT data) {
        this.isCurrentMutable = true;
        super.set(data);
    }

    @Override
    public MT getMutable() {
        if (!this.isCurrentMutable) {
            this.setMutable(this.cloneMutable(this.current != null ? this.current : this.original));
        }
        return (MT) this.current;
    }

    /**
     * @param data A {@link T} instance.
     * @return A new {@link MT} instance with the same contents as the given instance.
     */
    protected abstract MT cloneMutable(T data);

}

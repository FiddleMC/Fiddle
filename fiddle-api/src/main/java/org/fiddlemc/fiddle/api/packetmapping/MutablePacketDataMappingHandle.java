package org.fiddlemc.fiddle.api.packetmapping;

/**
 * A {@link PacketDataMappingHandle} for a type {@link T} that can be mutated in-place.
 */
public interface MutablePacketDataMappingHandle<T, MT extends T> extends PacketDataMappingHandle<T> {

    /**
     * @return The current data, which may be mutated in-place.
     * For performance reasons, only call this method once you are sure the mapping will make changes.
     */
    MT getMutable();

}

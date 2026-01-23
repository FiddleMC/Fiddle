package org.fiddlemc.fiddle.api.packetmapping;

/**
 * A mapping that is applied to some elements of type {@link T} when they are being sent in a packet.
 */
@FunctionalInterface
public interface PacketDataMapping<T, H extends PacketDataMappingHandle<T>, C extends PacketDataMappingContext> {

    /**
     * Applies this mapping to the data.
     *
     * @param handle  The handle that holds the item stack being mapped.
     * @param context Additional context of this mapping.
     */
    void apply(H handle, C context);

}

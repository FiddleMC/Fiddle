package org.fiddlemc.fiddle.api.packetmapping;

/**
 * A handle for the data being mapped in a {@link PacketDataMapping}.
 */
public interface PacketDataMappingHandle<T> {

    /**
     * @return The original data, before any mappings were applied.
     * The returned instance must not be modified.
     */
    T getOriginal();

    /**
     * @return The current data, after potential previous mappings were applied.
     * The returned instance must not be modified.
     * You can use this to decide whether you wish to mutate it.
     */
    T getImmutable();

    /**
     * Replaces the data with the given one.
     *
     * @param data The data that results from this mapping.
     */
    void set(T data);

}

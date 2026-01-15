package org.fiddlemc.fiddle.paper.registry.data;

import net.minecraft.resources.Identifier;

/**
 * A registry entry that is aware of the {@linkplain Identifier key}
 * with which it is being registered to a registry.
 */
public interface KeyAwareRegistryEntry {

    /**
     * The key with which this entry is being registered,
     * or null if not set yet.
     * <p>
     * We assume it will always be set with {@link #setKey}.
     * </p>
     */
    Identifier getKey();

    void setKey(Identifier key);

}

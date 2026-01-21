package org.fiddlemc.fiddle.api.paper.registry.data;

import net.minecraft.resources.Identifier;

/**
 * A registry entry that is aware of the {@linkplain Identifier key}
 * with which it is being registered to a registry.
 */
public interface KeyAwareRegistryEntry {

    /**
     * The key with which this entry is being registered,
     * or null if not set yet.
     *
     * <p>
     * Plugins can assume it will always be set.
     * </p>
     */
    Identifier getKey();

}

package org.fiddlemc.fiddle.api.moredatadriven.paper.nms;

import net.minecraft.resources.Identifier;

/**
 * A registry entry that is aware of the {@linkplain Identifier key}
 * with which it is being registered to a registry.
 */
public interface NMSKeyAwareRegistryEntry {

    /**
     * The key with w hich this entry is being registered,
     * or null if not set yet.
     *
     * <p>
     * Plugins can assume it will always be set.
     * </p>
     */
    Identifier getKey();

}

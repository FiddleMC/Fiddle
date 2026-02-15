package org.fiddlemc.fiddle.api.moredatadriven.paper.registry.nms;

import net.minecraft.resources.Identifier;
import org.fiddlemc.fiddle.api.moredatadriven.paper.registry.KeyAwareRegistryEntry;

/**
 * Extension of {@link KeyAwareRegistryEntry} for Minecraft internals.
 */
public interface KeyAwareRegistryEntryNMS extends KeyAwareRegistryEntry {

    /**
     * @see #getKey
     */
    Identifier getKeyNMS();

}

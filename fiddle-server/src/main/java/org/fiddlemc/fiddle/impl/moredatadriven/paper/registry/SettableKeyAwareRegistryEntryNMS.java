package org.fiddlemc.fiddle.impl.moredatadriven.paper.registry;

import net.minecraft.resources.Identifier;
import org.fiddlemc.fiddle.api.moredatadriven.paper.nms.KeyAwareRegistryEntryNMS;

/**
 * Allows for the setting of the {@link #getKey} property of a {@link KeyAwareRegistryEntryNMS}.
 */
public interface SettableKeyAwareRegistryEntryNMS extends KeyAwareRegistryEntryNMS {

    /**
     * Sets the {@link #getKey} of this registry entry.
     */
    void setKey(Identifier key);

}

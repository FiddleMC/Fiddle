package org.fiddlemc.fiddle.impl.moredatadriven.paper;

import net.minecraft.resources.Identifier;
import org.fiddlemc.fiddle.api.moredatadriven.paper.nms.NMSKeyAwareRegistryEntry;

/**
 * Allows for the setting of the {@link #getKey} property of a {@link NMSKeyAwareRegistryEntry}.
 */
public interface SettableNMSKeyAwareRegistryEntry extends NMSKeyAwareRegistryEntry {

    /**
     * Sets the {@link #getKey} of this registry entry.
     */
    void setKey(Identifier key);

}

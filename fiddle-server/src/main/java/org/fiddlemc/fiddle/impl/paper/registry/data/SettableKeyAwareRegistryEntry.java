package org.fiddlemc.fiddle.impl.paper.registry.data;

import net.minecraft.resources.Identifier;
import org.fiddlemc.fiddle.api.paper.registry.data.KeyAwareRegistryEntry;

/**
 * Allows for the setting of the {@link #getKey} property of a {@link KeyAwareRegistryEntry}.
 */
public interface SettableKeyAwareRegistryEntry extends KeyAwareRegistryEntry {

    /**
     * Sets the {@link #getKey} of this registry entry.
     */
    void setKey(Identifier key);

}

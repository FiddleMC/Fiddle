// Fiddle - Define namespace

package org.fiddlemc.fiddle;

import org.bukkit.NamespacedKey;

/**
 * Holds the {@link #FIDDLE} namespace.
 */
public final class FiddleNamespace {
    private FiddleNamespace() {
        throw new UnsupportedOperationException();
    }

    /**
      * The namespace for Fiddle {@link NamespacedKey}s.
     * <br>
      * This is for {@link NamespacedKey}s that are defined by and belong to Fiddle itself,
      * not those of packs that are loaded by Fiddle (content in those packs uses its own namespaces).
      */
    public static final String FIDDLE = "fiddle";
}

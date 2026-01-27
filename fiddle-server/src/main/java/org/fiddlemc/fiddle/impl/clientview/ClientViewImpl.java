package org.fiddlemc.fiddle.impl.clientview;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.clientview.nms.NMSClientView;
import org.fiddlemc.fiddle.impl.packetmapping.item.reverse.ItemMappingReverser;
import org.jspecify.annotations.Nullable;

/**
 * The base implementation of {@link ClientView}.
 *
 * <p>
 * Every instance of {@link ClientView} is also an instance of {@link ClientViewImpl}.
 * </p>
 */
public abstract class ClientViewImpl implements NMSClientView {

    /**
     * @return The {@link ItemMappingReverser} of this client,
     * or null if not available.
     *
     * <p>
     * The reverser (if present) instance stays the same during the entire connection session of a client.
     * </p>
     */
    public abstract @Nullable ItemMappingReverser getItemMappingReverser();

}

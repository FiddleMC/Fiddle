package org.fiddlemc.fiddle.api.packetmapping;

import org.fiddlemc.fiddle.api.clientview.ClientView;

/**
 * The context of a {@link PacketDataMapping} being applied.
 */
public interface PacketDataMappingContext {

    /**
     * @return The {@link ClientView} of the client that this mapping is being done for.
     */
    ClientView getClientView();

}

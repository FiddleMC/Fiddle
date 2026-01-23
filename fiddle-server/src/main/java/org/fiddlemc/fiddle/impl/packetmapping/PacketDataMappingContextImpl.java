package org.fiddlemc.fiddle.impl.packetmapping;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingContext;

/**
 * A base implementation of {@link PacketDataMappingContext}.
 */
public class PacketDataMappingContextImpl implements PacketDataMappingContext {

    private final ClientView clientView;

    public PacketDataMappingContextImpl(ClientView clientView) {
        this.clientView = clientView;
    }

    @Override
    public ClientView getClientView() {
        return this.clientView;
    }

}

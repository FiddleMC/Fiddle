package org.fiddlemc.fiddle.impl.packetmapping;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.ClientViewMappingContext;

/**
 * A base implementation of {@link ClientViewMappingContext}.
 */
public class ClientViewMappingContextImpl implements ClientViewMappingContext {

    private final ClientView clientView;

    public ClientViewMappingContextImpl(ClientView clientView) {
        this.clientView = clientView;
    }

    @Override
    public ClientView getClientView() {
        return this.clientView;
    }

}

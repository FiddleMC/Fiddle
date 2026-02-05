package org.fiddlemc.fiddle.impl.packetmapping;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.WithClientViewMappingFunctionContext;

/**
 * A base implementation of {@link WithClientViewMappingFunctionContext}.
 */
public class ClientViewMappingContextImpl implements WithClientViewMappingFunctionContext {

    private final ClientView clientView;

    public ClientViewMappingContextImpl(ClientView clientView) {
        this.clientView = clientView;
    }

    @Override
    public ClientView getClientView() {
        return this.clientView;
    }

}

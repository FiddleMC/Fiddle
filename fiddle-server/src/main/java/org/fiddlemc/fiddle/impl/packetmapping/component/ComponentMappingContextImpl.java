package org.fiddlemc.fiddle.impl.packetmapping.component;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingContext;
import org.fiddlemc.fiddle.impl.packetmapping.PacketDataMappingContextImpl;

/**
 * The implementation of {@link ComponentMappingContext}.
 */
public class ComponentMappingContextImpl extends PacketDataMappingContextImpl implements ComponentMappingContext {

    public ComponentMappingContextImpl(ClientView clientView) {
        super(clientView);
    }

}

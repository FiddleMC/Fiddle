package org.fiddlemc.fiddle.impl.packetmapping.block;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockStateMappingContext;
import org.fiddlemc.fiddle.impl.packetmapping.ClientViewMappingContextImpl;

/**
 * The implementation of {@link BlockStateMappingContext}.
 */
public class BlockStateMappingContextImpl extends ClientViewMappingContextImpl implements BlockStateMappingContext {

    private final boolean isStateOfPhysicalBlockInWorld;

    public BlockStateMappingContextImpl(ClientView clientView, boolean isStateOfPhysicalBlockInWorld) {
        super(clientView);
        this.isStateOfPhysicalBlockInWorld = isStateOfPhysicalBlockInWorld;
    }

    @Override
    public boolean isStateOfPhysicalBlockInWorld() {
        return this.isStateOfPhysicalBlockInWorld;
    }

}

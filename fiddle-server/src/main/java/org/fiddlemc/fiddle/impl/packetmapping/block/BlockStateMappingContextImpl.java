package org.fiddlemc.fiddle.impl.packetmapping.block;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockStateMappingContext;
import org.fiddlemc.fiddle.impl.packetmapping.ClientViewMappingContextImpl;

/**
 * The implementation of {@link BlockStateMappingContext}.
 */
public class BlockStateMappingContextImpl extends ClientViewMappingContextImpl implements BlockStateMappingContext {

    private final boolean isStateOfPhysicalBlockInWorld;
    private final int physicalBlockX;
    private final int physicalBlockY;
    private final int physicalBlockZ;

    public BlockStateMappingContextImpl(ClientView clientView, boolean isStateOfPhysicalBlockInWorld, int physicalBlockX, int physicalBlockY, int physicalBlockZ) {
        super(clientView);
        this.isStateOfPhysicalBlockInWorld = isStateOfPhysicalBlockInWorld;
        this.physicalBlockX = physicalBlockX;
        this.physicalBlockY = physicalBlockY;
        this.physicalBlockZ = physicalBlockZ;
    }

    public BlockStateMappingContextImpl(ClientView clientView) {
        this(clientView, false, 0, 0, 0);
    }

    public BlockStateMappingContextImpl(ClientView clientView, int physicalBlockX, int physicalBlockY, int physicalBlockZ) {
        this(clientView, true, physicalBlockX, physicalBlockY, physicalBlockZ);
    }

    @Override
    public boolean isStateOfPhysicalBlockInWorld() {
        return this.isStateOfPhysicalBlockInWorld;
    }

    @Override
    public int getPhysicalBlockX() {
        return this.physicalBlockX;
    }

    @Override
    public int getPhysicalBlockY() {
        return this.physicalBlockY;
    }

    @Override
    public int getPhysicalBlockZ() {
        return this.physicalBlockZ;
    }

}

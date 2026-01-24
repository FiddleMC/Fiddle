package org.fiddlemc.fiddle.impl.packetmapping.component;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMapping;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMapping;
import org.fiddlemc.fiddle.impl.packetmapping.MutablePacketDataMappingHandleImpl;

/**
 * The {@link PacketDataMappingHandle} implementation for {@link NMSComponentMapping}s.
 */
public class ComponentMappingHandleImpl extends MutablePacketDataMappingHandleImpl<Component, MutableComponent> implements ComponentMappingHandle<Component, MutableComponent> {

    public ComponentMappingHandleImpl(Component original) {
        super(original);
    }

    @Override
    protected MutableComponent cloneMutable(Component data) {
        return data.copy();
    }

}

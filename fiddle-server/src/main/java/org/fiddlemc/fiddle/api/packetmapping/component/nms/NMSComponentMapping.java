package org.fiddlemc.fiddle.api.packetmapping.component.nms;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMapping;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMapping;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingHandle;

/**
 * A {@link PacketDataMapping} for {@link Component}s.
 */
@FunctionalInterface
public interface NMSComponentMapping extends ComponentMapping<Component, MutableComponent, ComponentMappingHandle<Component, MutableComponent>> {
}

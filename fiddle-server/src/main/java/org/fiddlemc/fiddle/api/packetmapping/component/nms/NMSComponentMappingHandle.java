package org.fiddlemc.fiddle.api.packetmapping.component.nms;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.fiddlemc.fiddle.api.packetmapping.ClientViewMappingContext;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MutableMappingHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithContextMappingHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithOriginalMappingHandle;

/**
 * A handle provided to {@link NMSComponentMapping}s.
 */
public interface NMSComponentMappingHandle extends WithContextMappingHandle<Component, ClientViewMappingContext>, WithOriginalMappingHandle<Component>, MutableMappingHandle<Component, MutableComponent> {
}

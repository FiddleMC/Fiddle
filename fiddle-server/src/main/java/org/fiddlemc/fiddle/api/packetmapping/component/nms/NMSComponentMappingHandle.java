package org.fiddlemc.fiddle.api.packetmapping.component.nms;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.fiddlemc.fiddle.api.packetmapping.WithClientViewMappingFunctionContext;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MutableMappingFunctionHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithContextMappingFunctionHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithOriginalMappingFunctionHandle;

/**
 * A handle provided to {@link NMSComponentMapping}s.
 */
public interface NMSComponentMappingHandle extends WithContextMappingFunctionHandle<Component, WithClientViewMappingFunctionContext>, WithOriginalMappingFunctionHandle<Component>, MutableMappingFunctionHandle<Component, MutableComponent> {
}

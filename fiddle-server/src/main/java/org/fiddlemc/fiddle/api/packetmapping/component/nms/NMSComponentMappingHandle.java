package org.fiddlemc.fiddle.api.packetmapping.component.nms;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.fiddlemc.fiddle.api.packetmapping.WithClientViewMappingFunctionContext;
import org.fiddlemc.fiddle.api.util.mapping.MutableMappingFunctionHandle;
import org.fiddlemc.fiddle.api.util.mapping.WithContextMappingFunctionHandle;
import org.fiddlemc.fiddle.api.util.mapping.WithOriginalMappingFunctionHandle;

/**
 * A handle provided to {@link NMSComponentMapping}s.
 */
public interface NMSComponentMappingHandle extends WithContextMappingFunctionHandle<Component, WithClientViewMappingFunctionContext>, WithOriginalMappingFunctionHandle<Component>, MutableMappingFunctionHandle<Component, MutableComponent> {
}

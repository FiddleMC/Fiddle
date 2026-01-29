package org.fiddlemc.fiddle.impl.packetmapping.component;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.fiddlemc.fiddle.api.packetmapping.ClientViewMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMappingHandle;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.WithContextMappingHandleImpl;

/**
 * The implementation of {@link NMSComponentMappingHandle}.
 */
public class ComponentMappingHandleImpl extends WithContextMappingHandleImpl<Component, MutableComponent, ClientViewMappingContext> implements NMSComponentMappingHandle {

    public ComponentMappingHandleImpl(Component data, ClientViewMappingContext context) {
        super(data, context);
    }

    @Override
    protected MutableComponent cloneMutable(Component data) {
        return data.copy();
    }

}

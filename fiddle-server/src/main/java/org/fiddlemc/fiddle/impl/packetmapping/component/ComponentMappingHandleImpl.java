package org.fiddlemc.fiddle.impl.packetmapping.component;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.fiddlemc.fiddle.api.packetmapping.WithClientViewMappingFunctionContext;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMappingHandle;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.SimpleWithContextMappingFunctionHandleImpl;

/**
 * The implementation of {@link NMSComponentMappingHandle}.
 */
public class ComponentMappingHandleImpl extends SimpleWithContextMappingFunctionHandleImpl<Component, MutableComponent, WithClientViewMappingFunctionContext> implements NMSComponentMappingHandle {

    public ComponentMappingHandleImpl(Component data, WithClientViewMappingFunctionContext context, boolean isDataMutable) {
        super(data, context, isDataMutable);
    }

    @Override
    protected MutableComponent cloneMutable(Component data) {
        return data.copy();
    }

}

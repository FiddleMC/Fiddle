package org.fiddlemc.fiddle.api.packetmapping.component;

import java.util.ServiceLoader;
import org.fiddlemc.fiddle.api.packetmapping.MutablePacketDataMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMapping;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingRegistrar;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.GenericServiceProvider;

/**
 * A pipeline of component mappings.
 */
public interface ComponentMappingPipeline<T, MT extends T, H extends MutablePacketDataMappingHandle<T, MT>, C extends ComponentMappingContext, M extends PacketDataMapping<T, H, C>, R extends PacketDataMappingRegistrar<? extends T>> extends PacketDataMappingPipeline<T, H, C, M, R> {

    /**
     * An internal interface to get the {@link ComponentMappingPipeline} instance.
     */
    interface ServiceProvider<T, MT extends T, H extends MutablePacketDataMappingHandle<T, MT>, C extends ComponentMappingContext, M extends PacketDataMapping<T, H, C>, R extends PacketDataMappingRegistrar<? extends T>> extends GenericServiceProvider<ComponentMappingPipeline<T, MT, H, C, M, R>> {
    }

    /**
     * @return The {@link ComponentMappingPipeline} instance.
     */
    static ComponentMappingPipeline<?, ?, ?, ?, ?, ?> get() {
        return (ComponentMappingPipeline<?, ?, ?, ?, ?, ?>) ServiceLoader.load(ComponentMappingPipeline.ServiceProvider.class, ComponentMappingPipeline.ServiceProvider.class.getClassLoader()).findFirst().get().get();
    }

}

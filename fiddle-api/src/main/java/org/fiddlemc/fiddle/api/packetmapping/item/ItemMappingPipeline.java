package org.fiddlemc.fiddle.api.packetmapping.item;

import org.fiddlemc.fiddle.api.packetmapping.MutablePacketDataMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMapping;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.PacketDataMappingRegistrar;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.GenericServiceProvider;
import java.util.ServiceLoader;

/**
 * A pipeline of item mappings.
 */
public interface ItemMappingPipeline<T, H extends MutablePacketDataMappingHandle<T>, C extends ItemMappingContext, M extends PacketDataMapping<T, H, C>, R extends PacketDataMappingRegistrar<? extends T>> extends PacketDataMappingPipeline<T, H, C, M, R> {

    /**
     * An internal interface to get the {@link ItemMappingPipeline} instance.
     */
    interface ServiceProvider<T, H extends MutablePacketDataMappingHandle<T>, C extends ItemMappingContext, M extends PacketDataMapping<T, H, C>, R extends PacketDataMappingRegistrar<? extends T>> extends GenericServiceProvider<ItemMappingPipeline<T, H, C, M, R>> {
    }

    /**
     * @return The {@link ItemMappingPipeline} instance.
     */
    static ItemMappingPipeline<?, ?, ?, ?, ?> get() {
        return (ItemMappingPipeline<?, ?, ?, ?, ?>) ServiceLoader.load(ItemMappingPipeline.ServiceProvider.class, ItemMappingPipeline.ServiceProvider.class.getClassLoader()).findFirst().get().get();
    }

}

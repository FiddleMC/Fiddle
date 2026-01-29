package org.fiddlemc.fiddle.api.packetmapping.item;

import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipeline;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.GenericServiceProvider;
import java.util.ServiceLoader;

/**
 * A pipeline of item mappings.
 */
public interface ItemMappingPipeline extends MappingPipeline<ItemMappingPipelineRegistrar> {

    /**
     * An internal interface to get the {@link ItemMappingPipeline} instance.
     */
    interface ServiceProvider extends GenericServiceProvider<ItemMappingPipeline> {
    }

    /**
     * @return The {@link ItemMappingPipeline} instance.
     */
    static ItemMappingPipeline get() {
        return ServiceLoader.load(ItemMappingPipeline.ServiceProvider.class, ItemMappingPipeline.ServiceProvider.class.getClassLoader()).findFirst().get().get();
    }

}

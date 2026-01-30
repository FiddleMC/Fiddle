package org.fiddlemc.fiddle.api.packetmapping.component;

import java.util.ServiceLoader;

import org.fiddlemc.fiddle.api.util.composable.Composable;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.GenericServiceProvider;

/**
 * A pipeline of component mappings.
 */
public interface ComponentMappingPipeline extends Composable<ComponentMappingPipelineComposeEvent> {

    /**
     * An internal interface to get the {@link ComponentMappingPipeline} instance.
     */
    interface ServiceProvider extends GenericServiceProvider<ComponentMappingPipeline> {
    }

    /**
     * @return The {@link ComponentMappingPipeline} instance.
     */
    static ComponentMappingPipeline get() {
        return ServiceLoader.load(ComponentMappingPipeline.ServiceProvider.class, ComponentMappingPipeline.ServiceProvider.class.getClassLoader()).findFirst().get().get();
    }

}

package org.fiddlemc.fiddle.api.packetmapping.item;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.GenericServiceProvider;
import java.util.ServiceLoader;

/**
 * A pipeline of item mappings.
 */
public interface ItemMappingPipeline {

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

    /**
     * @return The {@link LifecycleEventType} for the {@link ComposeEvent}.
     */
    LifecycleEventType<BootstrapContext, ComposeEvent, PrioritizedLifecycleEventHandlerConfiguration<BootstrapContext>> composeEventType();

    /**
     * A {@link LifecycleEvent} that fires when this pipeline is composed.
     *
     * <p>
     * Item mappings can be registered in handlers of this event.
     * </p>
     */
    interface ComposeEvent extends LifecycleEvent {

        /**
         * The registrar to register mappings with.
         */
        ItemMappingRegistrar getRegistrar();

    }

}

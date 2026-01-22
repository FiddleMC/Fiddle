package org.fiddlemc.fiddle.api.packetmapping.item;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import java.util.ServiceLoader;

/**
 * A pipeline of item mappings.
 */
public interface ItemMappingPipeline {

    /**
     * The backing field for {@link #get()}.
     */
    ItemMappingPipeline INSTANCE = ServiceLoader.load(ItemMappingPipeline.class, ItemMappingPipeline.class.getClassLoader()).findFirst().get();

    /**
     * @return The pipeline instance.
     */
    static ItemMappingPipeline get() {
        return INSTANCE;
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

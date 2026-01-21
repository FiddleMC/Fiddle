package org.fiddlemc.fiddle.api.minecraft.packet.mapping.item;

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
     * @return The pipeline instance.
     */
    static ItemMappingPipeline get() {
        return ServiceLoader.load(ItemMappingPipeline.class).findFirst().get();
    }

    /**
     * @return The {@link LifecycleEventType} for the {@link ComposeEvent}.
     */
    LifecycleEventType<BootstrapContext, ComposeEvent, PrioritizedLifecycleEventHandlerConfiguration<BootstrapContext>> compose();

    /**
     * A {@link LifecycleEvent} that fires when this pipeline is composed.
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

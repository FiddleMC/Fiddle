package org.fiddlemc.fiddle.api.packetmapping;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import org.jspecify.annotations.Nullable;

/**
 * A pipeline that applies {@linkplain PacketDataMapping mappings} to packet data of type {@link T}.
 */
public interface PacketDataMappingPipeline<T, H extends PacketDataMappingHandle<T>, C extends PacketDataMappingContext, M extends PacketDataMapping<T, H, C>, R extends PacketDataMappingRegistrar<? extends T>> {

    /**
     * @return The {@link LifecycleEventType} for the {@link ComposeEvent}.
     */
    LifecycleEventType<BootstrapContext, ComposeEvent<T, R>, PrioritizedLifecycleEventHandlerConfiguration<BootstrapContext>> composeEventType();

    /**
     * A {@link LifecycleEvent} that fires when this pipeline is composed.
     *
     * <p>
     * Mappings can be registered in handlers of this event,
     * by using the {@link #getRegistrar()}.
     * </p>
     */
    interface ComposeEvent<T, R extends PacketDataMappingRegistrar<? extends T>> extends LifecycleEvent {

        /**
         * The registrar to register mappings with.
         */
        R getRegistrar();

    }

    /**
     * @return The smallest possible list containing all mappings in this pipeline
     * that may apply to the given data and context.
     * It may be null to indicate that there are none.
     */
    @Nullable M @Nullable [] getMappingsThatMayApplyTo(T data, C context);

    /**
     * @return A new {@linkplain H handle} for the given data.
     */
    H createHandle(T data);

    /**
     * {@linkplain PacketDataMapping#apply Applies} all applicable mappings to the data.
     *
     * <p>
     * This method will not mutate the given {@code itemStack}.
     * </p>
     *
     * @param context The context of this mapping.
     * @return The resulting data, which may be the given instance if no changes were made.
     */
    default T apply(T data, C context) {
        M @Nullable [] mappings = this.getMappingsThatMayApplyTo(data, context);
        if (mappings == null || mappings.length == 0) {
            return data;
        }
        H handle = this.createHandle(data);
        for (M mapping : mappings) {
            mapping.apply(handle, context);
        }
        return handle.getImmutable();
    }

}

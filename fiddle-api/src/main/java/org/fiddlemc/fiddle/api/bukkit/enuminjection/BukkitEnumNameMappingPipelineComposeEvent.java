package org.fiddlemc.fiddle.api.bukkit.enuminjection;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;
import java.util.List;
import java.util.function.Consumer;

/**
 * Provides functionality to register mappings to a {@link BukkitEnumNameMappingPipeline}.
 */
public interface BukkitEnumNameMappingPipelineComposeEvent<S> extends LifecycleEvent {

    /**
     * Registers a mapping.
     *
     * @param mapping The mapping to register.
     */
    void register(SingleStepMapping<BukkitEnumNameMappingHandle<S>> mapping);

    /**
     * Changes the list of registered mappings.
     *
     * <p>
     * This list is freely mutable.
     * This can be used to remove existing mappings or insert a mapping at the desired index.
     * </p>
     *
     * @param consumer The consumer that applies the desired changes to the list of mappings.
     */
    void changeRegistered(Consumer<List<SingleStepMapping<BukkitEnumNameMappingHandle<S>>>> consumer);

}

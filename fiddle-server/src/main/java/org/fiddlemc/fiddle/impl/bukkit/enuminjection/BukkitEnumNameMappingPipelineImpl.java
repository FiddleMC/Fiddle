package org.fiddlemc.fiddle.impl.bukkit.enuminjection;

import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNameMappingHandle;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNameMappingPipeline;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNameMappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;
import org.fiddlemc.fiddle.impl.util.composable.ComposableImpl;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.SingleStepMappingPipeline;
import org.jspecify.annotations.Nullable;

/**
 * Base implementation of {@link BukkitEnumNameMappingPipeline}
 */
public abstract class BukkitEnumNameMappingPipelineImpl<S> extends ComposableImpl<BukkitEnumNameMappingPipelineComposeEvent<S>, BukkitEnumNameMappingPipelineComposeEventImpl<S>> implements SingleStepMappingPipeline<String, BukkitEnumNameMappingHandle<S>, BukkitEnumNameMappingPipelineComposeEvent<S>>, BukkitEnumNameMappingPipeline<S> {

    /**
     * The registered mappings,
     * or null if there are no registered mappings.
     */
    protected SingleStepMapping<BukkitEnumNameMappingHandle<S>> @Nullable [] mappings;

    @Override
    protected BukkitEnumNameMappingPipelineComposeEventImpl<S> createComposeEvent() {
        return new BukkitEnumNameMappingPipelineComposeEventImpl<>();
    }

    @Override
    protected void copyInformationFromEvent(BukkitEnumNameMappingPipelineComposeEventImpl<S> event) {
        if (event.mappings != null) {
            this.mappings = event.mappings.toArray(SingleStepMapping[]::new);
        }
    }

    @Override
    public @Nullable SingleStepMapping<BukkitEnumNameMappingHandle<S>> @Nullable [] getMappingsThatMayApplyTo(BukkitEnumNameMappingHandle<S> handle) {
        return this.mappings;
    }

}

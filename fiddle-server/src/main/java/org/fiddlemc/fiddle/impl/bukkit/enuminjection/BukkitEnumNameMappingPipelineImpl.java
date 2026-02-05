package org.fiddlemc.fiddle.impl.bukkit.enuminjection;

import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNamePickHandle;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNames;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNamesComposeEvent;
import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;
import org.fiddlemc.fiddle.impl.util.composable.ComposableImpl;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.SingleStepMappingPipeline;
import org.jspecify.annotations.Nullable;

/**
 * Base implementation of {@link BukkitEnumNames}
 */
public abstract class BukkitEnumNameMappingPipelineImpl<S> extends ComposableImpl<BukkitEnumNamesComposeEvent<S>, BukkitEnumNameMappingPipelineComposeEventImpl<S>> implements SingleStepMappingPipeline<String, BukkitEnumNamePickHandle<S>, BukkitEnumNamesComposeEvent<S>>, BukkitEnumNames<S> {

    /**
     * The registered mappings,
     * or null if there are no registered mappings.
     */
    protected SingleStepMapping<BukkitEnumNamePickHandle<S>> @Nullable [] mappings;

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
    public @Nullable SingleStepMapping<BukkitEnumNamePickHandle<S>> @Nullable [] getMappingsThatMayApplyTo(BukkitEnumNamePickHandle<S> handle) {
        return this.mappings;
    }

}

package org.fiddlemc.fiddle.impl.bukkit.enuminjection;

import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNameMappingHandle;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNameMappingPipeline;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNameMappingPipelineRegistrar;
import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.MappingPipelineImpl;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.SingleStepMappingPipeline;
import org.jspecify.annotations.Nullable;

/**
 * Base implementation of {@link BukkitEnumNameMappingPipeline}
 */
public abstract class BukkitEnumNameMappingPipelineImpl<S> extends MappingPipelineImpl.Simple<BukkitEnumNameMappingPipelineRegistrar<S>, BukkitEnumNameMappingPipelineRegistrarImpl<S>> implements SingleStepMappingPipeline<String, BukkitEnumNameMappingHandle<S>, BukkitEnumNameMappingPipelineRegistrar<S>>, BukkitEnumNameMappingPipeline<S> {

    /**
     * The registered mappings,
     * or null if there are no registered mappings.
     */
    protected SingleStepMapping<BukkitEnumNameMappingHandle<S>> @Nullable [] mappings;

    @Override
    public void copyMappingsFrom(BukkitEnumNameMappingPipelineRegistrarImpl<S> registrar) {
        if (registrar.mappings != null) {
            this.mappings = registrar.mappings.toArray(SingleStepMapping[]::new);
        }
    }

    @Override
    protected BukkitEnumNameMappingPipelineRegistrarImpl<S> createRegistrar() {
        return new BukkitEnumNameMappingPipelineRegistrarImpl<>();
    }

    @Override
    public @Nullable SingleStepMapping<BukkitEnumNameMappingHandle<S>> @Nullable [] getMappingsThatMayApplyTo(BukkitEnumNameMappingHandle<S> handle) {
        return this.mappings;
    }

}

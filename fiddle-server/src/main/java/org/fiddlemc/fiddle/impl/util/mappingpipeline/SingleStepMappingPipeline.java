package org.fiddlemc.fiddle.impl.util.mappingpipeline;

import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipeline;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineRegistrar;
import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;
import org.jspecify.annotations.Nullable;

/**
 * A base implementation of a {@link MappingPipeline} for {@link SingleStepMapping}s.
 */
public interface SingleStepMappingPipeline<T, H extends MappingHandle<T>, R extends MappingPipelineRegistrar> extends MappingPipeline<R> {

    /**
     * @return The smallest possible list containing all mappings in this pipeline
     * that may apply to the given handle.
     * It may be null to indicate that there are none.
     */
    @Nullable SingleStepMapping<H> @Nullable [] getMappingsThatMayApplyTo(H handle);

    /**
     * {@linkplain SingleStepMapping#apply Applies} all applicable mappings to the input.
     *
     * @param handle The handle to map. The data in it may be mutated.
     * @return The resulting data, which may be the given instance if no changes were made.
     */
    default T apply(H handle) {
        SingleStepMapping<H> @Nullable [] mappings = this.getMappingsThatMayApplyTo(handle);
        if (mappings != null && mappings.length != 0) {
            for (SingleStepMapping<H> mapping : mappings) {
                mapping.apply(handle);
            }
        }
        return handle.getImmutable();
    }

}

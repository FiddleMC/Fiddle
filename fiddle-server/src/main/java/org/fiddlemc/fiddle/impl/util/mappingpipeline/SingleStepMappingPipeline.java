package org.fiddlemc.fiddle.impl.util.mappingpipeline;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import org.fiddlemc.fiddle.api.util.composable.Composable;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingFunctionHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;
import org.jspecify.annotations.Nullable;

/**
 * A base implementation of a mapping pipeline for {@link SingleStepMapping}s.
 */
public interface SingleStepMappingPipeline<T, H extends MappingFunctionHandle<T>, E extends LifecycleEvent> extends Composable<E> {

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

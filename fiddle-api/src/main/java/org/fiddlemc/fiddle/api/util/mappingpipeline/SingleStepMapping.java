package org.fiddlemc.fiddle.api.util.mappingpipeline;

/**
 * A mapping that is applied as part of a pipeline,
 * defined as a single operation.
 */
public interface SingleStepMapping<H extends MappingFunctionHandle<?>> {

    /**
     * Applies this mapping.
     *
     * @param handle The handle being mapped.
     */
    void apply(H handle);

}

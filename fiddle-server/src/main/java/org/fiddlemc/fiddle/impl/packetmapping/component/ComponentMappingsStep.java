package org.fiddlemc.fiddle.impl.packetmapping.component;

import org.fiddlemc.fiddle.impl.util.mappingpipeline.MappingPipelineStep;

/**
 * A mapping that is stored in {@link ComponentMappingsImpl}.
 */
public sealed interface ComponentMappingsStep extends MappingPipelineStep<ComponentMappingHandleNMSImpl> permits MinecraftFunctionComponentMappingsStep, AdventureFunctionComponentMappingsStep {
}

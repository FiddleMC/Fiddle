package org.fiddlemc.fiddle.api.packetmapping.component.nms;

import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;

/**
 * A mapping that can be registered with the {@link NMSComponentMappingRegistrar}.
 */
@FunctionalInterface
public interface NMSComponentMapping extends SingleStepMapping<NMSComponentMappingHandle> {
}

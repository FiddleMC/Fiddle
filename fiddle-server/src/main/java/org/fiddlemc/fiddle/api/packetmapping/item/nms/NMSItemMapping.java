package org.fiddlemc.fiddle.api.packetmapping.item.nms;

import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;

/**
 * A mapping that can be registered with the {@link NMSItemMappingRegistrar}.
 */
@FunctionalInterface
public interface NMSItemMapping extends SingleStepMapping<NMSItemMappingHandle> {
}

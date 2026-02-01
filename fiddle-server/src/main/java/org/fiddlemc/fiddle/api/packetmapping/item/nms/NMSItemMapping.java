package org.fiddlemc.fiddle.api.packetmapping.item.nms;

import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;

/**
 * A mapping that can be registered with the {@link NMSItemMappingPipelineComposeEvent}.
 *
 * <p>
 * If this mapping relies on external factors (for example, the time, or the player
 * having a certain advancement), then when those factors change, any items influenced by it should be re-sent
 * to the player to avoid desynchronization.
 * </p>
 */
@FunctionalInterface
public interface NMSItemMapping extends SingleStepMapping<NMSItemMappingHandle> {
}

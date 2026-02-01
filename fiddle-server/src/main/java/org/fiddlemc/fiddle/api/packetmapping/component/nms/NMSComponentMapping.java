package org.fiddlemc.fiddle.api.packetmapping.component.nms;

import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;

/**
 * A mapping that can be registered with the {@link NMSComponentMappingPipelineComposeEvent}.
 *
 * <p>
 * If this mapping relies on external factors (for example, the client locale),
 * then when those factors change, any components influenced by it should be re-sent
 * to the player to avoid desynchronization.
 * </p>
 */
@FunctionalInterface
public interface NMSComponentMapping extends SingleStepMapping<NMSComponentMappingHandle> {
}

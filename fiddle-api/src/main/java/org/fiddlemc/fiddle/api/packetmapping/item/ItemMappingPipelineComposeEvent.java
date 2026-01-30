package org.fiddlemc.fiddle.api.packetmapping.item;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;

/**
 * Provides functionality to register mappings to the {@link ItemMappingPipeline}.
 *
 * <p>
 * Currently, this must be cast to {@code NMSItemMappingPipelineComposeEvent} to be used.
 * </p>
 */
public interface ItemMappingPipelineComposeEvent extends LifecycleEvent {
}

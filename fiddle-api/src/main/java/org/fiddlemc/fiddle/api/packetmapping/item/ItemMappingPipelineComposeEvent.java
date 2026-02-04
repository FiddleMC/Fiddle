package org.fiddlemc.fiddle.api.packetmapping.item;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Provides functionality to register mappings to the {@link ItemMappingPipeline}.
 *
 * <p>
 * Currently, this must be cast to {@code NMSItemMappingPipelineComposeEvent} to be used.
 * </p>
 *
 * <p>
 * Mapping from {@linkplain ItemStack#isEmpty empty} to non-empty, or the other way around, will lead to glitches.
 * </p>
 */
public interface ItemMappingPipelineComposeEvent extends LifecycleEvent {
}

package org.fiddlemc.fiddle.api.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;

/**
 * A data-centric version-specific registry entry for the {@link ItemType} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemRegistryEntry {

    /**
     * A mutable builder for the {@link ItemRegistryEntry} plugins may change in applicable registry events.
     * <p>
     * Currently, this must be cast to {@code NMSItemRegistryEntryBuilder} to be used.
     * </p>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends ItemRegistryEntry, RegistryBuilder<ItemType> {
    }

}

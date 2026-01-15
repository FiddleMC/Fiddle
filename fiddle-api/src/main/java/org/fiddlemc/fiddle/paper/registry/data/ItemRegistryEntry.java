package org.fiddlemc.fiddle.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilder;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * A data-centric version-specific registry entry for the {@link ItemType} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemRegistryEntry {

    /**
     * A mutable builder for the {@link ItemRegistryEntry} plugins may change in applicable registry events.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends ItemRegistryEntry, RegistryBuilder<ItemType> {
    }

}

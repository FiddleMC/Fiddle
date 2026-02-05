package org.fiddlemc.fiddle.api.moredatadriven.paper.registry;

import io.papermc.paper.registry.RegistryBuilder;
import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;

/**
 * A data-centric version-specific registry entry for the {@link BlockType} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface BlockRegistryEntry {

    /**
     * A mutable builder for the {@link BlockRegistryEntry} plugins may change in applicable registry events.
     *
     * <p>
     * Currently, this must be cast to {@code NMSBlockRegistryEntryBuilder} to be used.
     * </p>
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends BlockRegistryEntry, RegistryBuilder<BlockType> {
    }

}

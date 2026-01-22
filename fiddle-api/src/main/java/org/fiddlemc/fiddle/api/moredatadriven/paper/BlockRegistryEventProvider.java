package org.fiddlemc.fiddle.api.moredatadriven.paper;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.RegistryEventProvider;
import io.papermc.paper.registry.event.RegistryEventProviderImpl;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;

/**
 * Holder for {@link #BLOCK}.
 */
public final class BlockRegistryEventProvider {

    private BlockRegistryEventProvider() {
        throw new UnsupportedOperationException();
    }

    /**
     * The {@link RegistryEventProvider} for events of the {@link Registry#BLOCK} registry.
     */
    public static final RegistryEventProvider<BlockType, BlockRegistryEntry.Builder> BLOCK = RegistryEventProviderImpl.create(RegistryKey.BLOCK);

}

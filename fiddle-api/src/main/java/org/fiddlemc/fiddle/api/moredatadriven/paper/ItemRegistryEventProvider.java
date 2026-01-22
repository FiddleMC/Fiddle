package org.fiddlemc.fiddle.api.moredatadriven.paper;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.event.RegistryEventProvider;
import io.papermc.paper.registry.event.RegistryEventProviderImpl;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemType;

/**
 * Holder for {@link #ITEM}.
 */
public final class ItemRegistryEventProvider {

    private ItemRegistryEventProvider() {
        throw new UnsupportedOperationException();
    }

    /**
     * The {@link RegistryEventProvider} for events of the {@link Registry#ITEM} registry.
     */
    public static final RegistryEventProvider<ItemType, ItemRegistryEntry.Builder> ITEM = RegistryEventProviderImpl.create(RegistryKey.ITEM);

}

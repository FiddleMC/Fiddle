package org.fiddlemc.fiddle.api.packetmapping.item.nms;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.impl.java.serviceloader.GenericServiceProvider;
import java.util.ServiceLoader;

/**
 * Some utilities for the mapping of items.
 */
public interface NMSItemMappingUtilities {

    /**
     * An internal interface to get the {@link NMSItemMappingUtilities} instance.
     */
    interface ServiceProvider extends GenericServiceProvider<NMSItemMappingUtilities> {
    }

    /**
     * @return The {@link NMSItemMappingUtilities} instance.
     */
    static NMSItemMappingUtilities get() {
        return ServiceLoader.load(NMSItemMappingUtilities.ServiceProvider.class, NMSItemMappingUtilities.ServiceProvider.class.getClassLoader()).findFirst().get().get();
    }

    /**
     * Changes the {@link Item} of the given handle's {@link ItemStack},
     * while attempting to keep the client-side appearance the same in most ways.
     *
     * @param handle  The handle being mapped.
     * @param newItem The new {@link Item} for the item stack.
     * @return Whether any changes were made.
     */
    boolean setItemWhilePreservingRest(NMSItemMappingHandle handle, Item newItem);

}

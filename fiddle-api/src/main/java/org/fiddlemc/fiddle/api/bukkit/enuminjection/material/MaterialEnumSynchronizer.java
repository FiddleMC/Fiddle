package org.fiddlemc.fiddle.api.bukkit.enuminjection.material;

import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumSynchronizer;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.KeyedSourceBukkitEnumSynchronizer;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.GenericServiceProvider;
import org.jspecify.annotations.Nullable;
import java.util.ServiceLoader;

/**
 * A {@link BukkitEnumSynchronizer} for {@link Material}.
 */
public interface MaterialEnumSynchronizer extends KeyedSourceBukkitEnumSynchronizer<Material, Triple<NamespacedKey, @Nullable BlockType, @Nullable ItemType>> {

    /**
     * An internal interface to get the {@link MaterialEnumSynchronizer} instance.
     */
    interface ServiceProvider extends GenericServiceProvider<MaterialEnumSynchronizer> {
    }

    /**
     * @return The {@link MaterialEnumSynchronizer} instance.
     */
    static MaterialEnumSynchronizer get() {
        return ServiceLoader.load(MaterialEnumSynchronizer.ServiceProvider.class, MaterialEnumSynchronizer.ServiceProvider.class.getClassLoader()).findFirst().get().get();
    }

}

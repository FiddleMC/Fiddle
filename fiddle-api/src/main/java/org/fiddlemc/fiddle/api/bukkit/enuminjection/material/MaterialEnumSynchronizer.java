package org.fiddlemc.fiddle.api.bukkit.enuminjection.material;

import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumSynchronizer;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.KeyedSourceBukkitEnumSynchronizer;
import org.jspecify.annotations.Nullable;
import java.util.ServiceLoader;

/**
 * A {@link BukkitEnumSynchronizer} for {@link Material}.
 */
public interface MaterialEnumSynchronizer extends KeyedSourceBukkitEnumSynchronizer<Material, Triple<NamespacedKey, @Nullable BlockType, @Nullable ItemType>> {

    /**
     * The backing field for {@link #get()}.
     */
    MaterialEnumSynchronizer INSTANCE = ServiceLoader.load(MaterialEnumSynchronizer.class, MaterialEnumSynchronizer.class.getClassLoader()).findFirst().get();

    /**
     * @return The synchronizer instance.
     */
    static MaterialEnumSynchronizer get() {
        return INSTANCE;
    }

}

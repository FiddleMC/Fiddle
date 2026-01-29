package org.fiddlemc.fiddle.api.bukkit.enuminjection.material;

import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNameMappingPipeline;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.GenericServiceProvider;
import org.jspecify.annotations.Nullable;
import java.util.ServiceLoader;

/**
 * The {@link BukkitEnumNameMappingPipeline} for {@link Material}.
 */
public interface MaterialEnumNameMappingPipeline extends BukkitEnumNameMappingPipeline<Triple<NamespacedKey, @Nullable BlockType, @Nullable ItemType>> {

    /**
     * An internal interface to get the {@link MaterialEnumNameMappingPipeline} instance.
     */
    interface ServiceProvider extends GenericServiceProvider<MaterialEnumNameMappingPipeline> {
    }

    /**
     * @return The {@link MaterialEnumNameMappingPipeline} instance.
     */
    static MaterialEnumNameMappingPipeline get() {
        return ServiceLoader.load(MaterialEnumNameMappingPipeline.ServiceProvider.class, MaterialEnumNameMappingPipeline.ServiceProvider.class.getClassLoader()).findFirst().get().get();
    }

}

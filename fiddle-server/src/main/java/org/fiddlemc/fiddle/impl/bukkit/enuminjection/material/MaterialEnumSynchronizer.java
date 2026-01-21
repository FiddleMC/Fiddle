package org.fiddlemc.fiddle.impl.bukkit.enuminjection.material;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.server.dedicated.DedicatedServer;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.impl.bukkit.enuminjection.BukkitEnumSynchronizer;
import org.fiddlemc.fiddle.impl.bukkit.enuminjection.KeyedSourceBukkitEnumSynchronizer;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link BukkitEnumSynchronizer} for {@link Material}.
 */
public final class MaterialEnumSynchronizer extends KeyedSourceBukkitEnumSynchronizer<Material, Triple<NamespacedKey, @Nullable BlockType, @Nullable ItemType>, MaterialEnumInjector> {

    private static final Logger LOGGER = LogUtils.getLogger();

    public MaterialEnumSynchronizer() throws Exception {
        super(new MaterialEnumInjector());
    }

    @Override
    protected List<Triple<NamespacedKey, @Nullable BlockType, @Nullable ItemType>> getSourceValues() {

        Map<NamespacedKey, Pair<@Nullable BlockType, @Nullable ItemType>> map = new LinkedHashMap<>();

        // Add all non-vanilla blocks
        Registry.BLOCK.stream().forEach(blockType -> {
            NamespacedKey key = blockType.getKey();
            if (key.namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE)) {
                return;
            }
            map.put(key, Pair.of(blockType, null));
        });

        // Add all non-vanilla items
        Registry.ITEM.stream().forEach(itemType -> {
            NamespacedKey key = itemType.getKey();
            if (key.namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE)) {
                return;
            }
            map.compute(key, ($, existingValue) -> {
                if (existingValue == null) {
                    return Pair.of(null, itemType);
                }
                return Pair.of(existingValue.first(), itemType);
            });
        });

        // Turn into a list
        List<Triple<NamespacedKey, @Nullable BlockType, @Nullable ItemType>> list = new ArrayList<>();
        for (var entry : map.entrySet()) {
            list.add(Triple.of(entry.getKey(), entry.getValue().left(), entry.getValue().right()));
        }
        return list;

    }

    @Override
    protected void stage(String enumName, Triple<NamespacedKey, @Nullable BlockType, @Nullable ItemType> sourceValue) {
        this.injector.stage(enumName, sourceValue.getLeft(), sourceValue.getMiddle(), sourceValue.getRight());
    }

    /**
     * @return The {@link NamespacedKey} for the given source value.
     */
    @Override
    protected NamespacedKey getKey(Triple<NamespacedKey, @Nullable BlockType, @Nullable ItemType> sourceValue) {
        return sourceValue.getLeft();
    }

    /**
     * A convenience method to {@link #run} this synchronizer from {@link DedicatedServer#initServer}.
     *
     * @return True if {@link #run} was successful, false otherwise.
     * @throws IllegalStateException If running was unsuccessful for an unexpected reason.
     */
    public static boolean runFromDedicatedServerInit() throws IllegalStateException {
        try {
            new MaterialEnumSynchronizer().run();
            // Success
            return true;
        } catch (EnumNameMappingException e) {
            LOGGER.warn("Failed to extend Bukkit Material enum: {}", e.getMessage());
            // Don't start the server with an incomplete Material enum
            return false;
        } catch (Exception e) {
            // Don't start the server with an incomplete Material enum
            throw new IllegalStateException("Failed to extend Bukkit Material enum", e);
        }
    }

}

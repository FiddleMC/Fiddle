package org.fiddlemc.fiddle.bukkit.enums;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Provides {@link #run}.
 */
public final class MaterialEnumSynchronizer {

    private MaterialEnumSynchronizer() {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates the {@link Material} enum based on the
     * {@linkplain Registry#BLOCK block} and {@linkplain Registry#ITEM item} registries.
     *
     * @return A user error message if something was wrong with the user input, or null otherwise.
     * @throws Exception If something goes wrong.
     */
    public static @Nullable String run() throws Exception {

        // Discover the non-vanilla block and item types
        Map<NamespacedKey, Pair<@Nullable BlockType, @Nullable ItemType>> nonVanilla = new LinkedHashMap<>();
        Registry.BLOCK.stream().forEach(blockType -> {
            NamespacedKey key = blockType.getKey();
            if (key.namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE)) {
                return;
            }
            nonVanilla.put(key, Pair.of(blockType, null));
        });
        Registry.ITEM.stream().forEach(itemType -> {
            NamespacedKey key = itemType.getKey();
            if (key.namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE)) {
                return;
            }
            nonVanilla.compute(key, ($, existingValue) -> {
                if (existingValue == null) {
                    return Pair.of(null, itemType);
                }
                return Pair.of(existingValue.first(), itemType);
            });
        });

        // Skip if there is nothing to synchronize
        if (nonVanilla.isEmpty()) {
            return null;
        }

        // Inject the new values
        MaterialEnumInjector injector = new MaterialEnumInjector();
        for (Map.Entry<NamespacedKey, Pair<@Nullable BlockType, @Nullable ItemType>> entry : nonVanilla.entrySet()) {

            // Get the values to inject
            NamespacedKey key = entry.getKey();
            @Nullable BlockType blockType = entry.getValue().first();
            @Nullable ItemType itemType = entry.getValue().second();

            // Check the key and map it to an enum name
            @Nullable String errorMessage = checkAcceptableNamespacedKeyPart(key);
            if (errorMessage != null) {
                return errorMessage;
            }
            String enumName = "FIDDLE_" + mapNamespacedKeyPartToEnumPart(key.namespace()) + "_" + mapNamespacedKeyPartToEnumPart(key.getKey());

            // Stage the new value
            injector.stage(key, enumName, blockType, itemType);

        }
        injector.commit();

        // Synchronization finished successfully
        return null;

    }

    /**
     * @return An error message if the given {@link NamespacedKey} is not acceptable
     * as the key of a mapped {@link BlockType} or {@link ItemType},
     * or null otherwise.
     */
    private static @Nullable String checkAcceptableNamespacedKeyPart(NamespacedKey key) {
        @Nullable String errorMessage = checkAcceptableNamespacedKeyPart(key, key.namespace());
        if (errorMessage == null) {
            errorMessage = checkAcceptableNamespacedKeyPart(key, key.getKey());
        }
        return errorMessage;
    }

    /**
     * @return An error message if the given string is not acceptable
     * as a namespace or path of the key of a mapped {@link BlockType} or {@link ItemType},
     * or null otherwise.
     */
    private static @Nullable String checkAcceptableNamespacedKeyPart(NamespacedKey key, String part) {
        for (char c : part.toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
                // Alphanumeric characters are allowed
                continue;
            }
            if (c == '_') {
                // Underscores are allowed
                continue;
            }
            if (c == '.' || c == '-' || c == '/') {
                return "A block or item key (" + key + ") contains a " + c + " character, which is technically allowed by Minecraft, but not by Fiddle";
            }
            return "A block or item key (" + key + ") contains a " + c + " character, which is not allowed by Fiddle";
        }
        // The part is acceptable
        return null;
    }

    /**
     * Maps a {@link NamespacedKey} namespaced or path to a fitting enum name part.
     * <p>
     * This method assumes that the part is {@linkplain #checkAcceptableNamespacedKeyPart acceptable}.
     * </p>
     *
     * @return The enum name part.
     */
    private static String mapNamespacedKeyPartToEnumPart(String part) {
        return part.toUpperCase(Locale.ROOT);
    }

}

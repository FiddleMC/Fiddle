package org.fiddlemc.testplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.fiddlemc.testplugin.data.PluginItemTypes;
import java.util.Arrays;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class TestPlugin extends JavaPlugin {

    private Logger logger;

    @Override
    public void onEnable() {

        // Get the logger
        this.logger = this.getLogger();

        // Print non-vanilla block types
        this.logger.info("All non-vanilla block types:");
        Registry.BLOCK.stream()
            .filter(type -> !type.getKey().namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            .forEach(type -> this.logger.info("* " + type.getKey()));

        // Print non-vanilla item types
        this.logger.info("All non-vanilla item types:");
        Registry.ITEM.stream()
            .filter(type -> !type.getKey().namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            .forEach(type -> this.logger.info("* " + type.getKey()));

        // Print non-vanilla Material instances
        this.logger.info("All non-vanilla Material instances:");
        Arrays.stream(Material.values())
            .filter(type -> !type.isLegacy() && !type.getKey().namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            .forEach(type -> this.logger.info("* " + type.name() + " = " + type.getKey()));

        // Register crafting recipes
        this.registerCraftingRecipes();

    }

    private void registerCraftingRecipes() {
        // 9 x Ash -> Ash block
        Bukkit.addRecipe(new ShapelessRecipe(NamespacedKey.fromString("example:ash_block"), PluginItemTypes.ASH_BLOCK.get().createItemStack()).addIngredient(9, PluginItemTypes.ASH.get().createItemStack()));
        // Ash block -> 9x Ash
        Bukkit.addRecipe(new ShapelessRecipe(NamespacedKey.fromString("example:ash_from_block"), PluginItemTypes.ASH.get().createItemStack(9)).addIngredient(PluginItemTypes.ASH_BLOCK.get().createItemStack()));
    }

}

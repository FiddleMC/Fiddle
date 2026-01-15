package org.fiddlemc.testplugin;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.fiddlemc.testplugin.data.PluginItemTypes;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class TestPlugin extends JavaPlugin {

    private Logger logger;

    @Override
    public void onEnable() {

        // Get the logger
        this.logger = this.getLogger();

        // Print non-vanilla block types
        logger.info("All non-vanilla block types:");
        Registry.BLOCK.stream()
            .filter(type -> !type.getKey().namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            .forEach(type -> logger.info("* " + type.getKey()));

        // Print non-vanilla item types
        logger.info("All non-vanilla item types:");
        Registry.ITEM.stream()
            .filter(type -> !type.getKey().namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            .forEach(type -> logger.info("* " + type.getKey()));

        // Register crafting recipes
        this.registerCraftingRecipes();

    }

    private void registerCraftingRecipes() {
        // 9 x Ash -> Ash block
        Bukkit.addRecipe(new ShapelessRecipe(NamespacedKey.fromString("example:ash_block"), PluginItemTypes.ASH_BLOCK.createItemStack()).addIngredient(9, PluginItemTypes.ASH.createItemStack()));
        // Ash block -> 9x Ash
        Bukkit.addRecipe(new ShapelessRecipe(NamespacedKey.fromString("example:ash_from_block"), PluginItemTypes.ASH.createItemStack(9)).addIngredient(PluginItemTypes.ASH_BLOCK.createItemStack()));
    }

}

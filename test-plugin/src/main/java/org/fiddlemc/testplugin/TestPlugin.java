package org.fiddlemc.testplugin;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.plugin.java.JavaPlugin;
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
            .filter(blockType -> !blockType.getKey().namespace().equals(NamespacedKey.MINECRAFT_NAMESPACE))
            .forEach(blockType -> logger.info("* " + blockType.getKey()));

    }

}

package org.fiddlemc.testplugin.data;

import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;

/**
 * Don't use this class before the blocks have been registered.
 */
public class PluginBlockTypes {
    public static BlockType ASH_BLOCK = Registry.BLOCK.get(Key.key("example:ash_block"));
}

package org.fiddlemc.testplugin.data;

import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemType;

/**
 * Don't use this class before the items have been registered.
 */
public final class PluginItemTypes {
    public static ItemType ASH = Registry.ITEM.get(Key.key("example:ash"));
    public static ItemType ASH_BLOCK = Registry.ITEM.get(Key.key("example:ash_block"));
}

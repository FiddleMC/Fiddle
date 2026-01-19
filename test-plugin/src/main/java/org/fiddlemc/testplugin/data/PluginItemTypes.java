package org.fiddlemc.testplugin.data;

import com.google.common.base.Suppliers;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemType;
import java.util.function.Supplier;

/**
 * Don't call a method of this class before its item type has been registered.
 */
public final class PluginItemTypes {
    public static Supplier<ItemType> ASH = itemType("example:ash");
    public static Supplier<ItemType> ASH_BLOCK = itemType("example:ash_block");

    private static Supplier<ItemType> itemType(String key) {
        return Suppliers.memoize(() -> Registry.ITEM.get(Key.key(key)));
    }
}

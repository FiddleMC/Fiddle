package org.fiddlemc.testplugin.data;

import com.google.common.base.Suppliers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import java.util.function.Supplier;

/**
 * Don't call a method of this class before its item has been registered.
 */
public final class PluginItems {
    public static Supplier<Item> ASH = item("example:ash");
    public static Supplier<Item> ASH_BLOCK = item("example:ash_block");

    private static Supplier<Item> item(String key) {
        return Suppliers.memoize(() -> BuiltInRegistries.ITEM.getValue(Identifier.parse(key)));
    }
}

package org.fiddlemc.testplugin.data;

import com.google.common.base.Suppliers;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import java.util.function.Supplier;

/**
 * Don't call a method of this class before its block type has been registered.
 */
public final class PluginBlockTypes {
    public static Supplier<BlockType> ASH_BLOCK = blockType("example:ash_block");

    private static Supplier<BlockType> blockType(String key) {
        return Suppliers.memoize(() -> Registry.BLOCK.get(Key.key(key)));
    }
}

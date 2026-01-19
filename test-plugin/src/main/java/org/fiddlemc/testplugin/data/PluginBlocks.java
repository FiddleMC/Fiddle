package org.fiddlemc.testplugin.data;

import com.google.common.base.Suppliers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import java.util.function.Supplier;

/**
 * Don't call a method of this class before its block has been registered.
 */
public final class PluginBlocks {
    public static Supplier<Block> ASH_BLOCK = block("example:ash_block");

    private static Supplier<Block> block(String key) {
        return Suppliers.memoize(() -> BuiltInRegistries.BLOCK.getValue(Identifier.parse(key)));
    }
}

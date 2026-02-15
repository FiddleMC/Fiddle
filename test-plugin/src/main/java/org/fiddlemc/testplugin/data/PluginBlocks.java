// This file is commented out, it is only an example of how you could keep a single list of custom blocks when using the dev bundle

// package org.fiddlemc.testplugin.data;
//
// import com.google.common.base.Suppliers;
// import net.minecraft.core.registries.BuiltInRegistries;
// import net.minecraft.resources.Identifier;
// import net.minecraft.world.level.block.Block;
// import java.util.function.Supplier;
//
// /**
//  * Don't call {@link Supplier#get} on a field of this class before its block has been registered.
//  */
// public final class PluginBlocks {
//     public static Supplier<Block> ASH_BLOCK = block("example:ash_block");
//     public static Supplier<Block> ASH_STAIRS = block("example:ash_stairs");
//
//     private static Supplier<Block> block(String key) {
//         return Suppliers.memoize(() -> BuiltInRegistries.BLOCK.getValue(Identifier.parse(key)));
//     }
// }

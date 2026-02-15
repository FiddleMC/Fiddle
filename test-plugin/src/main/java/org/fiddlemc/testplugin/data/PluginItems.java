// This file is commented out, it is only an example of how you could keep a single list of custom items when using the dev bundle

// package org.fiddlemc.testplugin.data;
//
// import com.google.common.base.Suppliers;
// import net.minecraft.core.registries.BuiltInRegistries;
// import net.minecraft.resources.Identifier;
// import net.minecraft.world.item.Item;
// import java.util.function.Supplier;
//
// /**
//  * Don't call {@link Supplier#get} on a field of this class before its item has been registered.
//  */
// public final class PluginItems {
//     public static Supplier<Item> ASH = item("example:ash");
//     public static Supplier<Item> ASH_BLOCK = item("example:ash_block");
//     public static Supplier<Item> ASH_STAIRS = item("example:ash_stairs");
//
//     private static Supplier<Item> item(String key) {
//         return Suppliers.memoize(() -> BuiltInRegistries.ITEM.getValue(Identifier.parse(key)));
//     }
// }

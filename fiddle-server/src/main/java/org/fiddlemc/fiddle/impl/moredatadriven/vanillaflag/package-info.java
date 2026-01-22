/**
 * <h1>Module: More data-driven - Vanilla flag</h1>
 *
 * <p>
 * Adds a flag to blocks and items, indicating whether they are vanilla or not.
 * </p>
 *
 * <p>
 * <h3>Paper changes</h3>
 * <ul>
 *     <li>
 *         {@link org.bukkit.craftbukkit.block.CraftBlockType},
 *         {@link org.bukkit.craftbukkit.block.data.CraftBlockData} and
 *         {@link org.bukkit.craftbukkit.block.CraftBlockType} - To add a method for getting the flag.
 *     </li>
 * </ul>
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>
 *         {@link net.minecraft.world.level.block.Block}
 *         {@link net.minecraft.world.level.block.state.BlockState} and
 *         {@link net.minecraft.world.item.Item} - To add the flag as a field.
 *     </li>
 *     <li>
 *         {@link net.minecraft.world.level.block.Blocks} and
 *         {@link net.minecraft.world.item.Items} - To set the flag.
 *     </li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.moredatadriven.vanillaflag;

import org.jspecify.annotations.NullMarked;

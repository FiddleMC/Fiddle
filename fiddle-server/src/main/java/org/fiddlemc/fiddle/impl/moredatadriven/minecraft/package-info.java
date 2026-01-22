/**
 * <h1>Module: More data-driven - Minecraft registries</h1>
 *
 * <p>
 * Allows the vanilla block and item registries to be extended.
 * </p>
 *
 * <p>
 * Special classes ({@link org.fiddlemc.fiddle.impl.moredatadriven.minecraft.BlockRegistry},
 * {@link org.fiddlemc.fiddle.impl.moredatadriven.minecraft.BlockStateRegistry} and
 * {@link org.fiddlemc.fiddle.impl.moredatadriven.minecraft.ItemRegistry}) replace the vanilla
 * implementations of these registries.
 * The contents of the vanilla registries are preserved in respective {@code VanillaOnly}-prefixed registries.
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>
 *         {@link net.minecraft.core.registries.BuiltInRegistries} -
 *         To use the custom registry classes for blocks and items instead of the vanilla ones.
 *     </li>
 *     <li>
 *         {@link net.minecraft.world.level.block.Blocks} - To remove registration and caching of block states
 *         (it is done in {@link org.fiddlemc.fiddle.impl.moredatadriven.minecraft.BlockRegistry} instead).
 *     </li>
 *     <li>
 *         {@link net.minecraft.world.item.Items} - To remove block registration for block items
 *         (it is done in {@link org.fiddlemc.fiddle.impl.moredatadriven.minecraft.ItemRegistry} instead).
 *     </li>
 *     <li>
 *         {@link net.minecraft.world.level.block.Block},
 *         {@link net.minecraft.world.level.block.state.BlockState} and
 *         {@link net.minecraft.world.item.Item} -
 *         To add cached values of the registry keys and indexes, for performance.
 *     </li>
 *     <li>{@link net.minecraft.world.level.block.Block} - To use the custom registry for block states.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.moredatadriven.minecraft;

import org.jspecify.annotations.NullMarked;

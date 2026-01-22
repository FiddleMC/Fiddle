/**
 * <h1>Module: Item mapping - Detect stonecutter result</h1>
 *
 * <p>
 * Detects whether an item being mapped is a stonecutter recipe result,
 * for use in the item mapping pipeline.
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>
 *         {@link net.minecraft.world.item.crafting.RecipeSerializer} -
 *         To mark the stonecutter recipe serializer.
 *     </li>
 *     <li>
 *         {@link net.minecraft.world.item.crafting.SingleItemRecipe} -
 *         To use the custom codec for the stonecutter recipe serializer.
 *     </li>
 *     <li>{@link net.minecraft.network.FriendlyByteBuf} - To store the detection result.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.packetmapping.item.detectstonecutterresult;

import org.jspecify.annotations.NullMarked;

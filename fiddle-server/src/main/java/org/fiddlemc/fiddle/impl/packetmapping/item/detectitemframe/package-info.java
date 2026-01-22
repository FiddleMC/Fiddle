/**
 * <h1>Module: Item mapping - Detect item frame</h1>
 *
 * <p>
 * Detects whether an item being mapped is an item frame item,
 * for use in the item mapping pipeline.
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>
 *         {@link net.minecraft.world.entity.decoration.ItemFrame} -
 *         To use the custom serializer for the item frame's item.
 *     </li>
 *     <li>{@link net.minecraft.network.FriendlyByteBuf} - To store the detection result.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.packetmapping.item.detectitemframe;

import org.jspecify.annotations.NullMarked;

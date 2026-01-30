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
 *         {@link net.minecraft.network.syncher.SynchedEntityData} -
 *         To detect serialized item frames and set the detection result.
 *     </li>
 *     <li>{@link net.minecraft.network.FriendlyByteBuf} - To hold the detection result.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.packetmapping.item.detectitemframe;

import org.jspecify.annotations.NullMarked;

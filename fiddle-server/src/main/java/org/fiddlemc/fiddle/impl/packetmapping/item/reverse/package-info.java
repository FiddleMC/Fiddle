/**
 * <h1>Module: Item mapping - Reverse</h1>
 *
 * <p>
 * Allows for the original server-side item to be retrieved from the item stacks sent to the client,
 * and applies this reverse mapping to received item stacks.
 * </p>
 *
 * <p>
 * This prevents desync issues, because the client includes the item stack in some packets sent to the server,
 * which would be the item stack that we sent to the client instead of the server-side item stack.
 * For example, the client of creative mode players simply tells the server which item stack (including
 * all data attached to it) should be in a certain slot, so if a creative mode player moves an item in their
 * inventory, the server will replace the server-side item stack with the received client-side version.
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>{@link net.minecraft.world.item.ItemStack} - To reverse-map the item stack in received packets.</li>
 *     <li>
 *         {@link net.minecraft.network.Connection} - To store a
 *         {@link org.fiddlemc.fiddle.impl.packetmapping.item.reverse.ItemMappingReverser instance}.
 *     </li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.packetmapping.item.reverse;

import org.jspecify.annotations.NullMarked;

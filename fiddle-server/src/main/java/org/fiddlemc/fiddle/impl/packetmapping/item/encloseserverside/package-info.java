/**
 * <h1>Module: Item mapping - Enclose server-side</h1>
 *
 * <p>
 * Encloses the server-side item stack in the data of item stacks sent to the client,
 * and extracts it again when received.
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
 *     <li>{@link net.minecraft.world.item.ItemStack} - To extract the enclosed item stack in received packets.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.packetmapping.item.encloseserverside;

import org.jspecify.annotations.NullMarked;

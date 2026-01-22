/**
 * <h1>Module: Item mapping - Implementation</h1>
 *
 * <p>
 * Implements the mapping of items in packets.
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>{@link net.minecraft.server.dedicated.DedicatedServer} - To fire the pipeline compose event.</li>
 *     <li>{@link net.minecraft.world.item.ItemStack} - To call the mapping pipeline during encoding.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.packetmapping.item;

import org.jspecify.annotations.NullMarked;

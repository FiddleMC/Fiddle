/**
 * <h1>Module: Block mapping - Implementation</h1>
 *
 * <p>
 * Implements the mapping of blocks in packets.
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>{@link net.minecraft.server.dedicated.DedicatedServer} - To fire the pipeline compose event.</li>
 *     <li>
 *         {@link net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket},
 *         {@link net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket} - To apply mappings.
 *     </li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.packetmapping.block;

import org.jspecify.annotations.NullMarked;

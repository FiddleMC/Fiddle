/**
 * <h1>Module: Block mapping - Chunk packets</h1>
 *
 * <p>
 * Applies the block mappings to chunk packets.
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>{@link net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket} - To apply mappings.</li>
 *     <li>{@link net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData} - To expose internals.</li>
 *     <li>
 *         {@link io.papermc.paper.FeatureHooks},
 *         {@link net.minecraft.server.players.PlayerList} and
 *         {@link net.minecraft.server.network.PlayerChunkSender} -
 *         To pass the player to which chunk packets are sent.
 *     </li>
 *     <li>
 *         {@link io.papermc.paper.antixray.ChunkPacketInfo} and
 *         {@link net.minecraft.world.level.chunk.LevelChunkSection} -
 *         To remember the end index in the byte buffer of each section.
 *     </li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.packetmapping.block.chunk;

import org.jspecify.annotations.NullMarked;

/**
 * <h1>Module: Component mapping - Implementation</h1>
 *
 * <p>
 * Implements the mapping of components in packets.
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>{@link net.minecraft.server.dedicated.DedicatedServer} - To fire the pipeline compose event.</li>
 *     <li>
 *         {@link net.minecraft.network.chat.ComponentSerialization} - To call the mapping pipeline during encoding.
 *     </li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.packetmapping.component;

import org.jspecify.annotations.NullMarked;

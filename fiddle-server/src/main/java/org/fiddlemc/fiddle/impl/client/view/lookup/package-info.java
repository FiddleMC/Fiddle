/**
 * <h1>Module: Client view lookup</h1>
 *
 * Provides the {@link org.fiddlemc.fiddle.impl.client.view.lookup.ClientViewLookup}
 * interface for any class that can look up its associated client view,
 * and implements that interface for:
 * <ul>
 *     <li>{@link net.minecraft.network.Connection}</li>
 *     <li>{@link net.minecraft.server.network.ServerLoginPacketListenerImpl}</li>
 *     <li>{@link net.minecraft.server.network.ServerCommonPacketListenerImpl}</li>
 *     <li>{@link net.minecraft.server.level.ServerPlayer}</li>
 * </ul>
 *
 * <p>
 * Minecraft/Paper changes:
 * <ul>
 *     <li>{@link net.minecraft.network.Connection} - To implement the interface.</li>
 *     <li>
 *         {@link net.minecraft.network.PacketListener} - To define the implements for all its subclasses,
 *         which include {@link net.minecraft.server.network.ServerLoginPacketListenerImpl} and
 *         {@link net.minecraft.server.network.ServerCommonPacketListenerImpl}.
 *     </li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.client.view.lookup;

import org.jspecify.annotations.NullMarked;

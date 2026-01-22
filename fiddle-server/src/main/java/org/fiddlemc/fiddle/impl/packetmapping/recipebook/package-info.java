/**
 * <h1>Module: Recipe packet filter</h1>
 *
 * <p>
 * Filters recipes from recipe packets if clients can't process them.
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>{@link net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket} - To add the filter.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.packetmapping.recipebook;

import org.jspecify.annotations.NullMarked;

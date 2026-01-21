/**
 * <h1>Module: Material injection</h1>
 *
 * Synchronizes the Bukkit {@link org.bukkit.Material} enum
 * with the {@link org.bukkit.block.BlockType} and {@link org.bukkit.inventory.ItemType} values.
 *
 * <p>
 * Minecraft/Paper changes:
 * <ul>
 *     <li>{@link net.minecraft.server.dedicated.DedicatedServer} - To run the synchronization.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.bukkit.enuminjection.material;

import org.jspecify.annotations.NullMarked;

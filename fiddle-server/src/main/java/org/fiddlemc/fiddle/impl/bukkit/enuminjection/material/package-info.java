/**
 * <h1>Module: Material injection</h1>
 *
 * <p>
 * Synchronizes the Bukkit {@link org.bukkit.Material} enum
 * with the {@link org.bukkit.block.BlockType} and {@link org.bukkit.inventory.ItemType} values.
 * </p>
 *
 * <p>
 * <h3>Paper changes</h3>
 * <ul>
 *     <li>{@link org.bukkit.craftbukkit.util.CraftMagicNumbers} - To make some maps that must be updated public.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>{@link net.minecraft.server.dedicated.DedicatedServer} - To run the synchronization.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.bukkit.enuminjection.material;

import org.jspecify.annotations.NullMarked;

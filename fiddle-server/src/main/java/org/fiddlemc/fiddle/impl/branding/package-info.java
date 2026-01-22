/**
 * <h1>Module: Branding (server)</h1>
 *
 * <p>
 * Changes the branding of the server to Fiddle.
 * </p>
 *
 * <p>
 * <h3>Paper changes</h3>
 * <ul>
 *     <li>{@link com.destroystokyo.paper.PaperVersionFetcher}</li>
 *     <li>{@link com.destroystokyo.paper.console.PaperConsole}</li>
 *     <li>{@link io.papermc.paper.ServerBuildInfoImpl}</li>
 *     <li>{@link org.bukkit.craftbukkit.scheduler.CraftScheduler}</li>
 *     <li>{@link org.spigotmc.WatchdogThread}</li>
 * </ul>
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>{@link net.minecraft.world.level.chunk.storage.RegionFileStorage}</li>
 *     <li>{@link net.minecraft.server.MinecraftServer} - To remove the onboarding message</li>
 *     <li>{@code LICENCE.txt} - To replace the license by GPL-3.0</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.branding;

import org.jspecify.annotations.NullMarked;

/**
 * <h1>Module: Configuration</h1>
 *
 * <p>
 * Defines the Fiddle configuration files.
 * </p>
 *
 * <p>
 * <h3>Paper changes</h3>
 * <ul>
 *     <li>
 *         {@link io.papermc.paper.configuration.PaperConfigurations},
 *         {@link io.papermc.paper.configuration.Configurations},
 *         {@link io.papermc.paper.configuration.mapping.InnerClassFieldDiscoverer},
 *         {@link net.minecraft.server.MinecraftServer} and
 *         {@link net.minecraft.server.Services} - To use
 *         {@link io.papermc.paper.configuration.PaperConfigurations} as an abstract basis for
 *         {@link org.fiddlemc.fiddle.impl.configuration.FiddleConfigurations}.
 *     </li>
 *     <li>{@link org.bukkit.craftbukkit.CraftServer} - To reload the configuration on server reload.</li>
 * </ul>
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>
 *         {@link net.minecraft.server.Services} and
 *         {@link net.minecraft.server.Main} - To load the global configuration before bootstrap.
 *     </li>
 *     <li>
 *         {@link net.minecraft.world.level.Level} and
 *         {@link net.minecraft.server.level.ServerLevel} - To create and store the configuration per world.
 *     </li>
 *     <li>{@link net.minecraft.server.dedicated.DedicatedServer} - To load the world-defaults configuration.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.configuration;

import org.jspecify.annotations.NullMarked;

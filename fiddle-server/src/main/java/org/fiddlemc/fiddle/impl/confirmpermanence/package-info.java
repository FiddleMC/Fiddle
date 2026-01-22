/**
 * <h1>Module: Confirm permanence</h1>
 *
 * <p>
 * Makes the server not started unless a configuration setting,
 * that specifically makes sure the user of the software understands that modding a server
 * has a permanent effect on its data,
 * has been set to true.
 * </p>
 *
 * <p>
 * <h3>Minecraft changes</h3>
 * <ul>
 *     <li>{@link net.minecraft.server.Main} - To check the configuration setting and exit if necessary.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.confirmpermanence;

import org.jspecify.annotations.NullMarked;

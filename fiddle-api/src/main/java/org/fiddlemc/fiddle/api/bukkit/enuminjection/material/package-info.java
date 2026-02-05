/**
 * <h1>Module: Material injection - API</h1>
 *
 * <p>
 * Provides API for hooking into the injection of values into {@link org.bukkit.Material},
 * in particular for changing the determination of enum names.
 * </p>
 *
 * <p>
 * Also, modifies {@link org.bukkit.Material#matchMaterial}
 * to be more flexible towards plugins that make outdated assumptions.
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.api.bukkit.enuminjection.material;

import org.jspecify.annotations.NullMarked;

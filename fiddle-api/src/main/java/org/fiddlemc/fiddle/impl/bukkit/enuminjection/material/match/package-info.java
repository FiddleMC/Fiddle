/**
 * <h1>Material injection - Flexible matchMaterial - API part</h1>
 *
 * <p>
 * Makes the {@link org.bukkit.Material#matchMaterial} more flexible
 * to allow for plugins relying on enum names matching namespaced keys in specific ways.
 * </p>
 *
 * <p>
 * <h3>Paper changes</h3>
 * <ul>
 *     <li>{@link org.bukkit.Material} - To replace the {@code matchMaterial} implementation.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.bukkit.enuminjection.material.match;

import org.jspecify.annotations.NullMarked;

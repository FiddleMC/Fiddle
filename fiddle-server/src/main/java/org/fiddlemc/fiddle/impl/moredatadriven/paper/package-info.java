/**
 * <h1>Module: More data-driven - Registry API - Implementation</h1>
 *
 * <p>
 * Implementation of extending the Paper registry API to include the block and item registries.
 * </p>
 *
 * <p>
 * <h3>Paper changes</h3>
 * <ul>
 *     <li>
 *         {@link io.papermc.paper.registry.entry.RegistryEntryMeta} - To make the Bukkit registry
 *         returned for the block and item registries an instance of
 *         {@link org.fiddlemc.fiddle.impl.moredatadriven.paper.KeyAwareWritableCraftRegistry}.
 *     </li>
 *     <li>{@link io.papermc.paper.registry.PaperRegistries} - To make the registries mutable.</li>
 *     <li>{@code io.papermc.paper.registry.RegistryBuilderTest} - To make the test ignore modified registries.</li>
 * </ul>
 * </p>
 */
@NullMarked
package org.fiddlemc.fiddle.impl.moredatadriven.paper;

import org.jspecify.annotations.NullMarked;

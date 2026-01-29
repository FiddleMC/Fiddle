package org.fiddlemc.fiddle.api.bukkit.enuminjection;

import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipeline;

/**
 * A pipeline for mappings to map a source value into a {@link Enum#name()} for a value of a Bukkit enum.
 *
 * <p>
 * Don't use this unless you know what you are doing.
 * The default name for enums is {@code FIDDLE_<namespace>_<key>}, for example
 * {@code willow_trees:willow_log} will become {@code FIDDLE_WILLOW_TREES_WILLOW_LOG}.
 * With this naming style, it is very unlikely that any issues will come up now or in the future.
 * Other naming styles may lead to collisions or other plugins parsing strings incorrectly.
 * </p>
 */
public interface BukkitEnumNameMappingPipeline<S> extends MappingPipeline<BukkitEnumNameMappingPipelineRegistrar<S>> {
}

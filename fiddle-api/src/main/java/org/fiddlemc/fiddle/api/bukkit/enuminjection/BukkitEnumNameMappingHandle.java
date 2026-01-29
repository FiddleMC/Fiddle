package org.fiddlemc.fiddle.api.bukkit.enuminjection;

import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithOriginalMappingHandle;

/**
 * A {@link MappingHandle} for {@link BukkitEnumNameMappingPipeline}s.
 */
public interface BukkitEnumNameMappingHandle<S> extends WithOriginalMappingHandle<String> {

    /**
     * @return The source value for which the enum name is being determined.
     */
    S getSourceValue();

}

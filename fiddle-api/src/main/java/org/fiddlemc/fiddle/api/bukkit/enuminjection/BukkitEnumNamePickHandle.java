package org.fiddlemc.fiddle.api.bukkit.enuminjection;

import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithOriginalMappingHandle;

/**
 * A {@link MappingHandle} for {@link BukkitEnumNames}s.
 */
public interface BukkitEnumNamePickHandle<S> extends WithOriginalMappingHandle<String> {

    /**
     * @return The source value for which the enum name is being picked.
     */
    S getSourceValue();

}

package org.fiddlemc.fiddle.api.bukkit.enuminjection;

import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingFunctionHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithOriginalMappingFunctionHandle;

/**
 * A {@link MappingFunctionHandle} for {@link BukkitEnumNames}s.
 */
public interface BukkitEnumNamePickFunctionHandle<S> extends WithOriginalMappingFunctionHandle<String> {

    /**
     * @return The source value for which the enum name is being picked.
     */
    S getSourceValue();

}

package org.fiddlemc.fiddle.impl.bukkit.enuminjection;

import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNameMappingHandle;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.MappingHandleImpl;

/**
 * The implementation of {@link BukkitEnumNameMappingHandle}.
 */
public final class BukkitEnumNameMappingHandleImpl<S> extends MappingHandleImpl<String, String> implements BukkitEnumNameMappingHandle<S> {

    private final S sourceValue;

    public BukkitEnumNameMappingHandleImpl(String data, S sourceValue) {
        super(data, false);
        this.sourceValue = sourceValue;
    }

    @Override
    public S getSourceValue() {
        return this.sourceValue;
    }

}

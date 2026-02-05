package org.fiddlemc.fiddle.impl.bukkit.enuminjection;

import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNamePickHandle;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.MappingHandleImpl;

/**
 * The implementation of {@link BukkitEnumNamePickHandle}.
 */
public final class BukkitEnumNameMappingHandleImpl<S> extends MappingHandleImpl<String, String> implements BukkitEnumNamePickHandle<S> {

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

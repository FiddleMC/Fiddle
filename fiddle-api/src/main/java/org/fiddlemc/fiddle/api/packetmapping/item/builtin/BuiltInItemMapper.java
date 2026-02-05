package org.fiddlemc.fiddle.api.packetmapping.item.builtin;

import org.fiddlemc.fiddle.api.util.composable.Composable;
import org.fiddlemc.fiddle.impl.java.serviceloader.GenericServiceProvider;
import java.util.ServiceLoader;

/**
 * Provides functionality to customize built-in item mappings.
 */
public interface BuiltInItemMapper extends Composable<BuiltInItemMapperComposeEvent> {

    /**
     * An internal interface to get the {@link BuiltInItemMapper} instance.
     */
    interface ServiceProvider extends GenericServiceProvider<BuiltInItemMapper> {
    }

    /**
     * @return The {@link BuiltInItemMapper} instance.
     */
    static BuiltInItemMapper get() {
        return ServiceLoader.load(BuiltInItemMapper.ServiceProvider.class, BuiltInItemMapper.ServiceProvider.class.getClassLoader()).findFirst().get().get();
    }

}

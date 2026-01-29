package org.fiddlemc.fiddle.impl.util.java.serviceloader;

import org.fiddlemc.fiddle.impl.java.util.serviceloader.GenericServiceProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link GenericServiceProvider}, where the singleton instance is defined by {@link #create}.
 */
public abstract class GenericServiceProviderImpl<S, I extends S> implements GenericServiceProvider<S> {

    /**
     * The backing cache for {@link #get()}.
     */
    private static final Map<Class<?>, Object> INSTANCES = new HashMap<>();

    /**
     * The service implementation class.
     */
    protected final Class<I> clazz;

    protected GenericServiceProviderImpl(Class<I> clazz) {
        this.clazz = clazz;
    }

    @Override
    public S get() {
        return (I) INSTANCES.computeIfAbsent(this.clazz, $ -> this.create());
    }

    /**
     * @return A new implementation of the service.
     */
    protected abstract I create();

}

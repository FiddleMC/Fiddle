package org.fiddlemc.fiddle.util.function;

import java.util.function.Consumer;

/**
 * Equivalent to {@link Consumer}, accept {@link #accept} can throw exceptions.
 */
@FunctionalInterface
public interface ConsumerThrowsException<T, E extends Throwable> {

    void accept(T t) throws E;

}

package org.fiddlemc.fiddle.client;

import org.jspecify.annotations.Nullable;
import java.lang.ref.WeakReference;

/**
 * Holder for {@link #THREAD_LOCAL}.
 */
public final class ClientViewProviderThreadLocal {

    private ClientViewProviderThreadLocal() {
        throw new UnsupportedOperationException();
    }

    /**
     * A {@link ThreadLocal} holding the {@link ClientViewProvider}
     * for which packets are being encoded on the current thread.
     */
    public static final ThreadLocal<@Nullable WeakReference<ClientViewProvider>> THREAD_LOCAL = new ThreadLocal<>();

}

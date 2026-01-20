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

    /**
     * Utility function to get the {@link ClientView} from the {@link ClientViewProvider} in {@link #THREAD_LOCAL},
     * or null if it doesn't exist.
     */
    public static @Nullable ClientView getThreadLocalClientView() {
        @Nullable WeakReference<ClientViewProvider> clientViewProviderReference = THREAD_LOCAL.get();
        @Nullable ClientViewProvider clientViewProvider = clientViewProviderReference == null ? null : clientViewProviderReference.get();
        return clientViewProvider == null ? null : clientViewProvider.getClientView();
    }

    /**
     * Utility function, the same as {@link #getThreadLocalClientView}, but will return
     * {@link ClientView#createDefault} instead of null.
     */
    public static ClientView getThreadLocalClientViewOrDefault() {
        @Nullable ClientView clientView = getThreadLocalClientView();
        return clientView != null ? clientView : ClientView.createDefault();
    }

}

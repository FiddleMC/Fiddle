package org.fiddlemc.fiddle.client;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import java.lang.ref.WeakReference;

/**
 * Holder for {@link #ATTRIBUTE_KEY}.
 */
public final class NettyClientViewProviderAttribute {

    private NettyClientViewProviderAttribute() {
        throw new UnsupportedOperationException();
    }

    /**
     * An {@link AttributeKey} that is used to {@linkplain Channel#attr store} the {@link ClientViewProvider}
     * in its {@link Channel}.
     */
    public static final AttributeKey<WeakReference<ClientViewProvider>> ATTRIBUTE_KEY = AttributeKey.valueOf("fiddle:client_view_provider");

}

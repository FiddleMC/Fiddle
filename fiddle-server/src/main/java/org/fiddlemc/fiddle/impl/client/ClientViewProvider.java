package org.fiddlemc.fiddle.impl.client;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.fiddlemc.fiddle.api.client.ClientView;
import org.jspecify.annotations.Nullable;

/**
 * An interface for classes that can provide a {@link ClientView} for a client that they represent.
 */
public interface ClientViewProvider {

    /**
     * @return The {@link ClientView} of the client, or null if none is determined.
     */
    default @Nullable ClientView getClientView() {
        if (this instanceof ServerLoginPacketListenerImpl serverLoginPacketListener) {
            return serverLoginPacketListener.connection.getClientView();
        } else if (this instanceof ServerCommonPacketListenerImpl serverCommonPacketListener) {
            return serverCommonPacketListener.connection.getClientView();
        } else if (this instanceof ServerPlayer serverPlayer) {
            return serverPlayer.connection.connection.getClientView();
        }
        throw new UnsupportedOperationException(this.getClass().getName() + " does not support getClientView");
    }

    /**
     * @return The same as {@link ClientView},
     * except null will be replaced by {@link DefaultClientView#createDefault}.
     */
    default ClientView getClientViewOrDefault() {
        @Nullable ClientView clientView = this.getClientView();
        return clientView != null ? clientView : DefaultClientView.createDefault();
    }

}

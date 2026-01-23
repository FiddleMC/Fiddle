package org.fiddlemc.fiddle.impl.clientview;

import net.minecraft.network.Connection;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.clientview.nms.NMSClientView;
import org.jspecify.annotations.Nullable;
import java.lang.ref.WeakReference;

/**
 * An abstract implementation of {@link ClientView}
 * for clients that represent a {@link Connection}.
 */
public abstract class ConnectionClientViewImpl implements NMSClientView {

    /**
     * The {@link Connection} tied to this client.
     */
    private final WeakReference<Connection> connection;

    protected ConnectionClientViewImpl(Connection connection) {
        this.connection = new WeakReference<>(connection);
    }

    @Override
    public @Nullable Connection getConnection() {
        return this.connection.get();
    }

}

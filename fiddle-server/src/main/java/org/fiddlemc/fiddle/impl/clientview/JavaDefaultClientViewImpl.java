package org.fiddlemc.fiddle.impl.clientview;

import net.minecraft.network.Connection;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.jspecify.annotations.Nullable;

/**
 * A simple implementation of {@link ClientView}
 * for {@link ClientView.AwarenessLevel#JAVA_DEFAULT} clients.
 */
public class JavaDefaultClientViewImpl extends ConnectionClientViewImpl {

    public JavaDefaultClientViewImpl(Connection connection) {
        super(connection);
    }

    @Override
    public AwarenessLevel getAwarenessLevel() {
        return AwarenessLevel.JAVA_DEFAULT;
    }

}

package org.fiddlemc.fiddle.impl.client.view;

import org.fiddlemc.fiddle.api.client.view.ClientView;

/**
 * A simple implementation of {@link ClientView}
 * for {@link ClientView.AwarenessLevel#JAVA_DEFAULT} clients.
 */
public class JavaDefaultClientViewImpl implements ClientView { // TODO use

    @Override
    public AwarenessLevel getAwarenessLevel() {
        return AwarenessLevel.JAVA_DEFAULT;
    }

}

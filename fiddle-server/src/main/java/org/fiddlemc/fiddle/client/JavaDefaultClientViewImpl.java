package org.fiddlemc.fiddle.client;

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

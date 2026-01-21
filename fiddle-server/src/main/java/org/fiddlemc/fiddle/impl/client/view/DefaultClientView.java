package org.fiddlemc.fiddle.impl.client.view;

import org.fiddlemc.fiddle.api.client.view.ClientView;

/**
 * Holder for {@link #createDefault()}.
 */
public final class DefaultClientView {

    private DefaultClientView() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return A default {@link ClientView} that is used for mappings when no view is known.
     */
    static ClientView createDefault() {
        return new JavaDefaultClientViewImpl();
    }

}

package org.fiddlemc.fiddle.impl.clientview;

import org.fiddlemc.fiddle.api.clientview.ClientView;

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
    public static ClientView createDefault() {
        return new JavaDefaultClientViewImpl();
    }

}

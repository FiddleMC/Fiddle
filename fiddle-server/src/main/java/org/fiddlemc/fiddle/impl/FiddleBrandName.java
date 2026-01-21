package org.fiddlemc.fiddle.impl;

/**
 * Holder for {@link #BRAND_FIDDLE_NAME}.
 */
public final class FiddleBrandName {

    private FiddleBrandName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Replacement for {@link io.papermc.paper.ServerBuildInfoImpl#BRAND_PAPER_NAME}.
     */
    public static final String BRAND_FIDDLE_NAME = "Fiddle";

}

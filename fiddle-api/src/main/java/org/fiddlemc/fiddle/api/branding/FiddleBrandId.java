package org.fiddlemc.fiddle.api.branding;

import net.kyori.adventure.key.Key;

/**
 * Holder for {@link #BRAND_FIDDLE_ID}.
 */
public final class FiddleBrandId {

    private FiddleBrandId() {
        throw new UnsupportedOperationException();
    }

    /**
     * Replacement for {@link io.papermc.paper.ServerBuildInfo#BRAND_PAPER_ID}.
     */
    public static final Key BRAND_FIDDLE_ID = Key.key("fiddlemc", "fiddle");

}

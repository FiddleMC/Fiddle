package org.fiddlemc.fiddle.impl.packetmapping.component;

import net.minecraft.network.chat.Component;
import org.fiddlemc.fiddle.api.clientview.ClientView;

/**
 * A mapping that is applied to components when they are being sent in a packet.
 */
public interface ComponentMapping {

    interface Context {

        /**
         * @return The original component, before any mappings were applied.
         * This component must not be modified.
         */
        Component getOriginal();

        /**
         * @return The {@link ClientView} of the client that this mapping is being done for.
         */
        ClientView getClientView();

    }

}

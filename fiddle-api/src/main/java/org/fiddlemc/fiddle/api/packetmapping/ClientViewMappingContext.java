package org.fiddlemc.fiddle.api.packetmapping;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingContext;

/**
 * A {@link MappingContext} for mappings that happen in the context of some {@link ClientView}.
 */
public interface ClientViewMappingContext extends MappingContext {

    /**
     * @return The {@link ClientView} of the client that this mapping is being done for.
     */
    ClientView getClientView();

}

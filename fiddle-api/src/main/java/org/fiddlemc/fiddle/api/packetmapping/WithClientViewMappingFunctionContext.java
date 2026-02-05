package org.fiddlemc.fiddle.api.packetmapping;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingFunctionContext;

/**
 * A {@link MappingFunctionContext} for mappings that happen in the context of some {@link ClientView}.
 */
public interface WithClientViewMappingFunctionContext extends MappingFunctionContext {

    /**
     * @return The {@link ClientView} of the client that this mapping is being done for.
     */
    ClientView getClientView();

}

package org.fiddlemc.fiddle.api.packetmapping.item;

import org.fiddlemc.fiddle.api.packetmapping.ClientViewMappingContext;

/**
 * A {@link ClientViewMappingContext} for the {@link ItemMappingPipeline}.
 */
public interface ItemMappingContext extends ClientViewMappingContext {

    /**
     * @return Whether the item stack on which this mapping is being applied
     * is an item stack in an item frame.
     */
    boolean isItemStackInItemFrame();

    /**
     * @return Whether the item stack on which this mapping is being applied
     * is the result of a stonecutter recipe.
     */
    boolean isStonecutterRecipeResult();

}

package org.fiddlemc.fiddle.impl.packetmapping.item;

import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingContext;
import org.fiddlemc.fiddle.impl.packetmapping.ClientViewMappingContextImpl;

/**
 * The implementation of {@link ItemMappingContext}.
 */
public class ItemMappingContextImpl extends ClientViewMappingContextImpl implements ItemMappingContext {

    private final boolean isItemStackInItemFrame;
    private final boolean isStonecutterRecipeResult;

    public ItemMappingContextImpl(ClientView clientView, boolean isItemStackInItemFrame, boolean isStonecutterRecipeResult) {
        super(clientView);
        this.isItemStackInItemFrame = isItemStackInItemFrame;
        this.isStonecutterRecipeResult = isStonecutterRecipeResult;
    }

    @Override
    public boolean isItemStackInItemFrame() {
        return this.isItemStackInItemFrame;
    }

    @Override
    public boolean isStonecutterRecipeResult() {
        return this.isStonecutterRecipeResult;
    }

}

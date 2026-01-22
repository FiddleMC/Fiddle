package org.fiddlemc.fiddle.impl.packetmapping.item;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.clientview.ClientView;

/**
 * A simple implementation of {@link ItemMapping.Context}.
 */
public class ItemMappingContextImpl implements ItemMapping.Context {

    private final ItemStack original;
    private final ClientView clientView;
    private final boolean isItemStackInItemFrame;
    private final boolean isStonecutterRecipeResult;

    public ItemMappingContextImpl(ItemStack original, ClientView clientView, boolean isItemStackInItemFrame, boolean isStonecutterRecipeResult) {
        this.original = original;
        this.clientView = clientView;
        this.isItemStackInItemFrame = isItemStackInItemFrame;
        this.isStonecutterRecipeResult = isStonecutterRecipeResult;
    }

    @Override
    public ItemStack getOriginal() {
        return this.original;
    }

    @Override
    public ClientView getClientView() {
        return this.clientView;
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

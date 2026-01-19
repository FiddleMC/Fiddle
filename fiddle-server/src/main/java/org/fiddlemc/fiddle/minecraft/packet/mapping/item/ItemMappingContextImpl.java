package org.fiddlemc.fiddle.minecraft.packet.mapping.item;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.client.ClientProfile;

/**
 * A simple implementation of {@link ItemMapping.Context}.
 */
public class ItemMappingContextImpl implements ItemMapping.Context {

    private final ItemStack original;
    private final ClientProfile clientProfile;
    private final boolean isItemStackInItemFrame;
    private final boolean isStonecutterRecipeResult;

    public ItemMappingContextImpl(ItemStack original, ClientProfile clientProfile, boolean isItemStackInItemFrame, boolean isStonecutterRecipeResult) {
        this.original = original;
        this.clientProfile = clientProfile;
        this.isItemStackInItemFrame = isItemStackInItemFrame;
        this.isStonecutterRecipeResult = isStonecutterRecipeResult;
    }

    @Override
    public ItemStack getOriginal() {
        return this.original;
    }

    @Override
    public ClientProfile getClientProfile() {
        return this.clientProfile;
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

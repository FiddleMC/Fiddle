package org.fiddlemc.fiddle.impl.packetmapping.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingUtilities;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.jspecify.annotations.Nullable;
import java.util.Objects;

/**
 * The implementation for {@link NMSItemMappingUtilities}.
 */
public final class ItemMappingUtilitiesImpl implements NMSItemMappingUtilities {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<NMSItemMappingUtilities, ItemMappingUtilitiesImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(ItemMappingUtilitiesImpl.class);
        }

    }

    public static ItemMappingUtilitiesImpl get() {
        return (ItemMappingUtilitiesImpl) NMSItemMappingUtilities.get();
    }

    @Override
    public boolean setItemWhilePreservingRest(final NMSItemMappingHandle handle, final Item newItem) {

        // Don't make changes if the item is already present
        ItemStack immutable = handle.getImmutable();
        Item originalItem = immutable.getItem();
        if (originalItem == newItem) {
            return false;
        }

        // Save the original item name
        Component originalItemName = immutable.getItemName();
        if (originalItemName.equals(CommonComponents.EMPTY)) {
            originalItemName = null;
        }

        // Save the original rarity
        Rarity originalRarity = immutable.getRarity();

        // Change the item
        ItemStack mutable = handle.getMutable();
        mutable.setItem(newItem);

        // Restore the item name
        Component newItemName = mutable.getItemName();
        if (newItemName.equals(CommonComponents.EMPTY)) {
            newItemName = null;
        }
        if (newItemName == null && originalItemName == null) {
            if (!newItem.getDescriptionId().equals(originalItem.getDescriptionId())) {
                mutable.set(DataComponents.ITEM_NAME, Component.translatable(originalItem.getDescriptionId()));
            }
        } else if (!Objects.equals(newItemName, originalItemName)) {
            mutable.set(DataComponents.ITEM_NAME, originalItemName != null ? originalItemName : Component.translatable(originalItem.getDescriptionId()));
        }

        // Restore the rarity
        Rarity newRarity = mutable.getRarity();
        if (!newRarity.equals(originalRarity)) {
            Rarity newRarityComponentValue;
            if (mutable.isEnchanted()) {
                // Set the rarity component to one lower
                newRarityComponentValue = switch (originalRarity) {
                    case EPIC -> Rarity.RARE;
                    case RARE -> Rarity.UNCOMMON;
                    default -> Rarity.COMMON;
                };
            } else {
                newRarityComponentValue = originalRarity;
            }
            @Nullable Rarity existingRarityComponentValue = mutable.get(DataComponents.RARITY);
            if (existingRarityComponentValue == null || !existingRarityComponentValue.equals(newRarityComponentValue)) {
                mutable.set(DataComponents.RARITY, newRarityComponentValue);
            }
        }

        // We made changes
        return true;

    }


}

package org.fiddlemc.fiddle.minecraft.packet.mapping.item;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.client.ClientView;
import org.jspecify.annotations.Nullable;

/**
 * A mapping that is applied to items when they are being sent in a packet.
 */
public interface ItemMapping {

    interface Context {

        /**
         * @return The original item stack, before any mappings were applied.
         * This item stack must not be modified.
         */
        ItemStack getOriginal();

        /**
         * @return The {@link ClientView} of the client that this mapping is being done for.
         */
        ClientView getClientView();

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

    /**
     * Applies this mapping to the item stack.
     *
     * @param itemStack      The item stack to apply the mapping to.
     *                       This item stack can be modified in-place.
     * @param mappingContext Additional context of this mapping.
     * @return The item stack after the mappings have been applied.
     * This is often the given {@code itemStack} with changes applied.
     * It can also be null to indicate this mapping makes no changes to the given {@code itemStack}.
     * Returning null is strongly preferred if this mapping makes no changes,
     * since the underlying mechanism can then skip re-writing the packet.
     */
    @Nullable ItemStack apply(ItemStack itemStack, Context mappingContext);

}

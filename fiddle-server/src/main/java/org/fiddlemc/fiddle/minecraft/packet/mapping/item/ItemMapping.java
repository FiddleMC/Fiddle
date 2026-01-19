package org.fiddlemc.fiddle.minecraft.packet.mapping.item;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.client.ClientProfile;
import org.jspecify.annotations.Nullable;

/**
 * A mapping that is applied to items when they are being sent in a packet.
 */
public interface ItemMapping {

    /**
     * Applies this mapping to the item stack.
     *
     * @param itemStack     The item stack to apply the mapping to.
     *                      This item stack can be modified in-place.
     * @param original      The original item stack, before any mappings were applied.
     *                      This item stack must not be modified.
     * @param clientProfile The {@link ClientProfile} of the client that this mapping is being done for.
     * @return The item stack after the mappings have been applied.
     * This is often the given {@code itemStack} with changes applied.
     * It can also be null to indicate this mapping makes no changes to the given {@code itemStack}.
     * Returning null is strongly preferred if this mapping makes no changes,
     * since the underlying mechanism can then skip re-writing the packet.
     */
    @Nullable ItemStack apply(ItemStack itemStack, ItemStack original, ClientProfile clientProfile);

}

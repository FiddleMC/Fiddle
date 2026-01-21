package org.fiddlemc.fiddle.impl.minecraft.packet.mapping.item.hoverevent;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.impl.client.view.ClientViewProviderThreadLocal;
import org.fiddlemc.fiddle.impl.minecraft.packet.mapping.item.ItemMappingContextImpl;
import org.fiddlemc.fiddle.impl.minecraft.packet.mapping.item.ItemMappingPipelineImpl;

import java.util.function.Function;

/**
 * Holder for {@link #CLIENT_VIEW_MAPPED_CODEC}.
 */
public final class ClientViewMappedItemStackMapCodec {

    private ClientViewMappedItemStackMapCodec() {
        throw new UnsupportedOperationException();
    }

    /**
     * A modified version of {@link ItemStack#MAP_CODEC}, which maps the item according to the
     * {@link ClientViewProviderThreadLocal#getThreadLocalClientViewOrDefault}.
     * <p>
     * The usage of this instance in {@link HoverEvent.ShowItem} makes it so that
     * item stacks in hover events are mapped.
     * </p>
     */
    public static final MapCodec<ItemStack> CLIENT_VIEW_MAPPED_CODEC = ItemStack.MAP_CODEC.xmap(
        Function.identity(), // Used by io.papermc.paper.adventure.WrapperAwareSerializer#deserialize
        itemStack -> ItemMappingPipelineImpl.get().apply(itemStack, new ItemMappingContextImpl(null, ClientViewProviderThreadLocal.getThreadLocalClientViewOrDefault(), false, false))
    );

}

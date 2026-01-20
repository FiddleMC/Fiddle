package org.fiddlemc.fiddle.minecraft.packet.mapping.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.client.ClientViewProviderThreadLocal;
import java.util.function.Function;

/**
 * Holder for {@link #CLIENT_VIEW_MAPPED_MAP_CODEC}.
 */
public final class ClientViewMappedItemStackMapCodec {

    private ClientViewMappedItemStackMapCodec() {
        throw new UnsupportedOperationException();
    }

    /**
     * A modified version of {@link ItemStack#MAP_CODEC}, which maps the item according to the
     * {@link ClientViewProviderThreadLocal#getThreadLocalClientViewOrDefault}.
     */
    public static final MapCodec<ItemStack> CLIENT_VIEW_MAPPED_MAP_CODEC = ItemStack.MAP_CODEC.xmap(Function.identity(), itemStack -> ItemMappingPipeline.get().apply(itemStack, new ItemMappingContextImpl(null, ClientViewProviderThreadLocal.getThreadLocalClientViewOrDefault(), false, false)));

}

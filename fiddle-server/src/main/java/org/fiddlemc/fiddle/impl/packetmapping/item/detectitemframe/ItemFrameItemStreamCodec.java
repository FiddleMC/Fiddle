package org.fiddlemc.fiddle.impl.packetmapping.item.detectitemframe;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;

/**
 * Holder for {@link #STREAM_CODEC}.
 */
public final class ItemFrameItemStreamCodec {

    private ItemFrameItemStreamCodec() {
        throw new UnsupportedOperationException();
    }

    /**
     * A {@link StreamCodec} based on the {@link EntityDataSerializers#ITEM_STACK} codec that
     * sets {@link FriendlyByteBuf#isEncodingItemFrameItem} to true while encoding.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> STREAM_CODEC = new StreamCodec<>() {

        private final StreamCodec<? super RegistryFriendlyByteBuf, ItemStack> internalCodec = EntityDataSerializers.ITEM_STACK.codec();

        @Override
        public ItemStack decode(RegistryFriendlyByteBuf buffer) {
            throw new UnsupportedOperationException("This codec must only be used to encode");
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, ItemStack value) {
            buffer.isEncodingItemFrameItem = true;
            internalCodec.encode(buffer, value);
            buffer.isEncodingItemFrameItem = false;
        }

    };

}

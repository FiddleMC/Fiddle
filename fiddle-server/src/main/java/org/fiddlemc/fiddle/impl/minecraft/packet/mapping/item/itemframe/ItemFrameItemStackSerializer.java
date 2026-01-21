package org.fiddlemc.fiddle.impl.minecraft.packet.mapping.item.itemframe;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.item.ItemStack;

/**
 * Holder for {@link #SERIALIZER}.
 */
public final class ItemFrameItemStackSerializer {

    private ItemFrameItemStackSerializer() {
        throw new UnsupportedOperationException();
    }

    /**
     * A {@link EntityDataSerializer} based on {@link EntityDataSerializers#ITEM_STACK} that
     * uses {@link ItemFrameItemStreamCodec#STREAM_CODEC} as its codec.
     */
    public static final EntityDataSerializer<ItemStack> SERIALIZER = new EntityDataSerializer<>() {

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, ItemStack> codec() {
            return ItemFrameItemStreamCodec.STREAM_CODEC;
        }

        @Override
        public ItemStack copy(ItemStack value) {
            return value.copy();
        }

    };

}

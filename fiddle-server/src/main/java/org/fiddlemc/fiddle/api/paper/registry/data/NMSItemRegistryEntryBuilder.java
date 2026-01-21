package org.fiddlemc.fiddle.api.paper.registry.data;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * An {@link ItemRegistryEntry.Builder} that allows building an {@link Item} using Minecraft internals.
 */
public interface NMSItemRegistryEntryBuilder extends ItemRegistryEntry.Builder, KeyAwareRegistryEntry {

    /**
     * Sets the factory to use, and marks this builder as using NMS.
     */
    void nmsFactory(Function<Item.Properties, Item> factory) ;

    /**
     * Sets the factory to use for an item for a block with the {@linkplain #getKey same} {@link Identifier},
     * and marks this builder as using NMS.
     */
    void nmsFactoryForBlock();

    /**
     * Sets the factory to use for an item for a block with the given {@link Identifier},
     * and marks this builder as using NMS.
     *
     * @param blockIdentifier The identifier of the block.
     */
    void nmsFactoryForBlock(Identifier blockIdentifier);

    /**
     * Sets the factory to use for an item for a block with the {@linkplain #getKey same} {@link Identifier},
     * and marks this builder as using NMS.
     */
    void nmsFactoryForBlock(BiFunction<Block, Item.Properties, BlockItem> factory);

    /**
     * Sets the factory to use for an item for a block with the given {@link Identifier},
     * and marks this builder as using NMS.
     *
     * @param blockIdentifier The identifier of the block.
     */
    void nmsFactoryForBlock(Identifier blockIdentifier, BiFunction<Block, Item.Properties, BlockItem> factory);

    /**
     * Replaces the NMS properties for the item.
     */
    void nmsProperties(Item.Properties properties);

    /**
     * Modifies the NMS properties for the item.
     */
    void nmsProperties(Consumer<Item.Properties> properties);

}

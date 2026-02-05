package org.fiddlemc.fiddle.api.moredatadriven.paper.nms;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.fiddlemc.fiddle.api.moredatadriven.paper.registry.ItemRegistryEntry;

/**
 * An {@link ItemRegistryEntry.Builder} that allows building an {@link Item} using Minecraft internals.
 */
public interface ItemRegistryEntryBuilderNMS extends ItemRegistryEntry.Builder, KeyAwareRegistryEntryNMS {

    /**
     * Sets the factory to use, and marks this builder as using NMS.
     */
    void factoryNMS(Function<Item.Properties, Item> factory) ;

    /**
     * Sets the factory to use for an item for a block with the {@linkplain #getKey same} {@link Identifier},
     * calls {@link Item.Properties#useBlockDescriptionPrefix()},
     * and marks this builder as using NMS.
     */
    void factoryForBlockNMS();

    /**
     * Sets the factory to use for an item for a block with the given {@link Identifier},,
     * calls {@link Item.Properties#useBlockDescriptionPrefix()},
     * and marks this builder as using NMS.
     *
     * @param blockIdentifier The identifier of the block.
     */
    void factoryForBlockNMS(Identifier blockIdentifier);

    /**
     * Sets the factory to use for an item for a block with the {@linkplain #getKey same} {@link Identifier},,
     * calls {@link Item.Properties#useBlockDescriptionPrefix()},
     * and marks this builder as using NMS.
     */
    void factoryForBlockNMS(BiFunction<Block, Item.Properties, BlockItem> factory);

    /**
     * Sets the factory to use for an item for a block with the given {@link Identifier},,
     * calls {@link Item.Properties#useBlockDescriptionPrefix()},
     * and marks this builder as using NMS.
     *
     * @param blockIdentifier The identifier of the block.
     */
    void factoryForBlockNMS(Identifier blockIdentifier, BiFunction<Block, Item.Properties, BlockItem> factory);

    /**
     * Replaces the NMS properties for the item.
     */
    void propertiesNMS(Item.Properties properties);

    /**
     * Modifies the NMS properties for the item.
     */
    void propertiesNMS(Consumer<Item.Properties> properties);

}

package org.fiddlemc.fiddle.api.paper.registry.data;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A {@link BlockRegistryEntry.Builder} that allows building a {@link Block} using Minecraft internals.
 */
public interface NMSBlockRegistryEntryBuilder extends BlockRegistryEntry.Builder, KeyAwareRegistryEntry {

    /**
     * Sets the factory to use, and marks this builder as using NMS.
     */
    void nmsFactory(Function<BlockBehaviour.Properties, Block> factory);

    /**
     * Replaces the NMS properties for the block.
     */
    void nmsProperties(BlockBehaviour.Properties properties);

    /**
     * Modifies the NMS properties for the block.
     */
    void nmsProperties(Consumer<BlockBehaviour.Properties> properties);

}

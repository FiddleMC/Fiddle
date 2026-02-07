package org.fiddlemc.fiddle.api.packetmapping.block;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.fiddlemc.fiddle.api.packetmapping.AwarenessLevelMappingBuilder;
import org.fiddlemc.fiddle.api.util.composable.FromBuilder;
import org.fiddlemc.fiddle.api.util.composable.FunctionBuilder;
import org.fiddlemc.fiddle.api.util.composable.ToBuilder;

/**
 * A builder to define a block mapping.
 */
public interface BlockMappingBuilder extends AwarenessLevelMappingBuilder, FromBuilder<BlockData>, ToBuilder<BlockData> {

    /**
     * Sets the target {@link BlockType} to which this mapping will be applied.
     * It will be applied to every block state of that type.
     *
     * <p>
     * This replaces any previous value set with {@link #from} or {@link #fromBlockType}.
     * </p>
     */
    default void fromBlockType(BlockType from) {
        this.fromBlockType(List.of(from));
    }

    /**
     * @see #fromBlockType(BlockType)
     */
    default void fromBlockType(BlockType[] from) {
        this.fromBlockType(Arrays.asList(from));
    }

    /**
     * @see #fromBlockType(BlockType)
     */
    default void fromBlockType(Collection<BlockType> from) {
        this.from(from.stream().flatMap(value -> value.createBlockDataStates().stream().map(state -> (BlockData) state)).toList());
    }

    /**
     * Adds a {@link BlockType} to which this mapping will be applied.
     *
     * @see #fromBlockType(BlockType)
     */
    default void addFromBlockType(BlockType from) {
        for (BlockData value : from.createBlockDataStates()) {
            this.addFrom(value);
        }
    }

    /**
     * @see #addFromBlockType(BlockType)
     */
    default void addFromBlockType(BlockType[] from) {
        for (BlockType value : from) {
            this.addFromBlockType(value);
        }
    }

    /**
     * @see #addFromBlockType(BlockType)
     */
    default void addFromBlockType(Collection<BlockType> from) {
        for (BlockType value : from) {
            this.addFromBlockType(value);
        }
    }

    /**
     * Calls {@link #to} with the {@linkplain BlockType#createBlockData() default block state}
     * of the given {@link BlockType}.
     */
    default void toDefaultStateOf(BlockType to) {
        this.to(to.createBlockData());
    }

    /**
     * @param requiresCoordinates Whether this mapping requires the coordinates
     *                            ({@link BlockMappingFunctionContext#getPhysicalBlockX()} and so on).
     * @see FunctionBuilder#function
     */
    void function(Consumer<BlockMappingHandle> function, boolean requiresCoordinates);

}

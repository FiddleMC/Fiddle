package org.fiddlemc.fiddle.api.packetmapping.block;

import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithContextMappingFunctionHandle;
import java.util.function.Consumer;

/**
 * A builder to create a {@link BlockMapping}.
 */
public interface BlockMappingBuilder {

    /**
     * @return A {@link BlockMapping} for the current values of this builder.
     */
    BlockMapping build();

    /**
     * Sets the {@link BlockData} that this mapping will map to.
     *
     * <p>
     * This replaces any previous value set with {@link #toBlockData} or {@link #toBlockTypeDefaultState}.
     * </p>
     */
    void toBlockData(BlockData blockData);

    /**
     * Sets the {@link BlockData} that this mapping will map to
     * as the {@linkplain BlockType#createBlockData() default block state} of the given {@link BlockType}.
     *
     * <p>
     * This replaces any previous value set with {@link #toBlockData} or {@link #toBlockTypeDefaultState}.
     * </p>
     */
    default void toBlockTypeDefaultState(BlockType blockType) {
        blockType.createBlockData();
    }

    /**
     * Sets the function that is applied for this mapping.
     *
     * <p>
     * If set, the mapping will ignore any direct {@link #toBlockData} set.
     * </p>
     *
     * @param function The function to apply.
     * @param requiresCoordinates Whether this mapping requires the coordinates
     *                            ({@link BlockStateMappingFunctionContext#getPhysicalBlockX()} and so on).
     */
    void function(Consumer<WithContextMappingFunctionHandle<BlockData, BlockStateMappingFunctionContext>> function, boolean requiresCoordinates);

}

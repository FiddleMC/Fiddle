package org.fiddlemc.fiddle.api.packetmapping.block;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithContextMappingFunctionHandle;

/**
 * A builder to register a {@link BlockMapping}.
 */
public interface BlockMappingRegistrationBuilder extends BlockMappingBuilder {

    /**
     * Sets the {@link ClientView.AwarenessLevel} to which this mapping will be applied.
     *
     * <p>
     * This replaces any previous value set with {@link #awarenessLevel}.
     * </p>
     */
    default void awarenessLevel(ClientView.AwarenessLevel awarenessLevel) {
        this.awarenessLevel(List.of(awarenessLevel));
    }

    /**
     * @see #awarenessLevel(ClientView.AwarenessLevel)
     */
    default void awarenessLevel(ClientView.AwarenessLevel[] awarenessLevel) {
        this.awarenessLevel(Arrays.asList(awarenessLevel));
    }

    /**
     * @see #awarenessLevel(ClientView.AwarenessLevel)
     */
    void awarenessLevel(Iterable<ClientView.AwarenessLevel> awarenessLevel);

    /**
     * Adds a {@link ClientView.AwarenessLevel} to which this mapping will be applied.
     */
    void addAwarenessLevel(ClientView.AwarenessLevel awarenessLevel);

    /**
     * @see #addAwarenessLevel(ClientView.AwarenessLevel)
     */
    default void addAwarenessLevel(ClientView.AwarenessLevel[] awarenessLevel) {
        for (ClientView.AwarenessLevel value : awarenessLevel) {
            this.addAwarenessLevel(value);
        }
    }

    /**
     * @see #addAwarenessLevel(ClientView.AwarenessLevel)
     */
    default void addAwarenessLevel(Iterable<ClientView.AwarenessLevel> awarenessLevel) {
        for (ClientView.AwarenessLevel value : awarenessLevel) {
            this.addAwarenessLevel(value);
        }
    }

    /**
     * Sets the {@link BlockData} to which this mapping will be applied.
     *
     * <p>
     * This replaces any previous value set with {@link #fromBlockData} or {@link #fromBlockType}.
     * </p>
     */
    default void fromBlockData(BlockData blockData) {
        this.fromBlockData(List.of(blockData));
    }

    /**
     * @see #fromBlockData(BlockData)
     */
    default void fromBlockData(BlockData[] blockData) {
        this.fromBlockData(Arrays.asList(blockData));
    }

    /**
     * @see #fromBlockData(BlockData)
     */
    void fromBlockData(Iterable<BlockData> blockData);

    /**
     * Adds a {@link BlockData} to which this mapping will be applied.
     */
    void addFromBlockData(BlockData blockData);

    /**
     * @see #addFromBlockData(BlockData)
     */
    default void addFromBlockData(BlockData[] blockData) {
        for (BlockData value : blockData) {
            this.addFromBlockData(value);
        }
    }

    /**
     * @see #addFromBlockData(BlockData)
     */
    default void addFromBlockData(Iterable<BlockData> blockData) {
        for (BlockData value : blockData) {
            this.addFromBlockData(value);
        }
    }

    /**
     * Sets the target {@link BlockType} to which this mapping will be applied.
     * It will be applied to every block state of that type.
     *
     * <p>
     * This replaces any previous value set with {@link #fromBlockData} or {@link #fromBlockType}.
     * </p>
     */
    default void fromBlockType(BlockType blockType) {
        this.fromBlockType(List.of(blockType));
    }

    /**
     * @see #fromBlockType(BlockType)
     */
    default void fromBlockType(BlockType[] blockType) {
        this.fromBlockType(Arrays.asList(blockType));
    }

    /**
     * @see #fromBlockType(BlockType)
     */
    void fromBlockType(Iterable<BlockType> blockType);

    /**
     * Adds a {@link BlockType} to which this mapping will be applied.
     */
    void addFromBlockType(BlockType blockType);

    /**
     * @see #addFromBlockType(BlockType)
     */
    default void addFromBlockType(BlockType[] blockType) {
        for (BlockType value : blockType) {
            this.addFromBlockType(value);
        }
    }

    /**
     * @see #addFromBlockType(BlockType)
     */
    default void addFromBlockType(Iterable<BlockType> blockType) {
        for (BlockType value : blockType) {
            this.addFromBlockType(value);
        }
    }

}

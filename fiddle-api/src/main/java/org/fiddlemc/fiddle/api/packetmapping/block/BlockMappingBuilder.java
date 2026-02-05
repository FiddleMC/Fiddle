package org.fiddlemc.fiddle.api.packetmapping.block;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.fiddlemc.fiddle.api.clientview.ClientView;

/**
 * A builder to define a block mapping.
 */
public interface BlockMappingBuilder {

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
    default void awarenessLevel(ClientView.AwarenessLevel[] awarenessLevels) {
        this.awarenessLevel(Arrays.asList(awarenessLevels));
    }

    /**
     * @see #awarenessLevel(ClientView.AwarenessLevel)
     */
    void awarenessLevel(Collection<ClientView.AwarenessLevel> awarenessLevels);

    /**
     * Adds a {@link ClientView.AwarenessLevel} to which this mapping will be applied.
     */
    void addAwarenessLevel(ClientView.AwarenessLevel awarenessLevel);

    /**
     * @see #addAwarenessLevel(ClientView.AwarenessLevel)
     */
    default void addAwarenessLevel(ClientView.AwarenessLevel[] awarenessLevels) {
        for (ClientView.AwarenessLevel value : awarenessLevels) {
            this.addAwarenessLevel(value);
        }
    }

    /**
     * @see #addAwarenessLevel(ClientView.AwarenessLevel)
     */
    default void addAwarenessLevel(Collection<ClientView.AwarenessLevel> awarenessLevels) {
        for (ClientView.AwarenessLevel value : awarenessLevels) {
            this.addAwarenessLevel(value);
        }
    }

    /**
     * Sets the {@link BlockData} to which this mapping will be applied.
     *
     * <p>
     * This replaces any previous value set with {@link #from} or {@link #fromBlockType}.
     * </p>
     */
    default void from(BlockData from) {
        this.from(List.of(from));
    }

    /**
     * @see #from(BlockData)
     */
    default void from(BlockData[] from) {
        this.from(Arrays.asList(from));
    }

    /**
     * @see #from(BlockData)
     */
    void from(Collection<BlockData> from);

    /**
     * Adds a {@link BlockData} to which this mapping will be applied.
     */
    void addFrom(BlockData from);

    /**
     * @see #addFrom(BlockData)
     */
    default void addFrom(BlockData[] from) {
        for (BlockData value : from) {
            this.addFrom(value);
        }
    }

    /**
     * @see #addFrom(BlockData)
     */
    default void addFrom(Collection<BlockData> from) {
        for (BlockData value : from) {
            this.addFrom(value);
        }
    }

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
     * Sets the {@link BlockData} that this mapping will map to.
     *
     * <p>
     * This replaces any previous value set.
     * </p>
     */
    void to(BlockData to);

    /**
     * Calls {@link #to} with the {@linkplain BlockType#createBlockData() default block state}
     * of the given {@link BlockType}.
     */
    default void toDefaultStateOf(BlockType to) {
        this.to(to.createBlockData());
    }

    /**
     * Sets the function that is applied for this mapping.
     *
     * <p>
     * If set, the mapping will ignore any direct {@link #to} set.
     * </p>
     *
     * <p>
     * Function mappings are much less efficient than simple mappings.
     * Please use simple mappings ({@link #to}) whenever you can.
     * </p>
     *
     * <p>
     * The given function is preferred to be deterministic.
     * In other words, if it is called twice with the same arguments and environment,
     * it should return the same result. Otherwise, you may get unstable visuals.
     * </p>
     *
     * <p>
     * If this mapping relies on external factors (for example, the time, or the player
     * having a certain advancement), then when those factors change, any blocks influenced by it should be re-sent
     * to the player to avoid desynchronization.
     * </p>
     *
     * <p>
     * The given function may or may not be run on the main thread.
     * Making sure the code is thread-safe is your own responsibility.
     * </p>
     *
     * @param function            The function to apply.
     * @param requiresCoordinates Whether this mapping requires the coordinates
     *                            ({@link BlockMappingFunctionContext#getPhysicalBlockX()} and so on).
     */
    void function(Consumer<BlockMappingHandle> function, boolean requiresCoordinates);

}

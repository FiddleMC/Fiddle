package org.fiddlemc.fiddle.api.packetmapping.block.nms;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingBuilder;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingFunctionContext;

/**
 * An alternative to {@link BlockMappingBuilder} that uses Minecraft internals.
 */
public interface BlockMappingBuilderNMS {

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
     * Sets the {@link BlockState} to which this mapping will be applied.
     *
     * <p>
     * This replaces any previous value set with {@link #from} or {@link #fromBlock}.
     * </p>
     */
    default void from(BlockState from) {
        this.from(List.of(from));
    }

    /**
     * @see #from(BlockState)
     */
    default void from(BlockState[] from) {
        this.from(Arrays.asList(from));
    }

    /**
     * @see #from(BlockState)
     */
    void from(Collection<BlockState> from);

    /**
     * Adds a {@link BlockState} to which this mapping will be applied.
     */
    void addFrom(BlockState from);

    /**
     * @see #addFrom(BlockState)
     */
    default void addFrom(BlockState[] from) {
        for (BlockState value : from) {
            this.addFrom(value);
        }
    }

    /**
     * @see #addFrom(BlockState)
     */
    default void addFrom(Collection<BlockState> from) {
        for (BlockState value : from) {
            this.addFrom(value);
        }
    }

    /**
     * Sets the target {@link BlockState} to which this mapping will be applied.
     * It will be applied to every block state of that type.
     *
     * <p>
     * This replaces any previous value set with {@link #from} or {@link #fromBlock}.
     * </p>
     */
    default void fromBlock(Block from) {
        this.fromBlock(List.of(from));
    }

    /**
     * @see #fromBlock(Block)
     */
    default void fromBlock(Block[] from) {
        this.fromBlock(Arrays.asList(from));
    }

    /**
     * @see #fromBlock(Block)
     */
    default void fromBlock(Collection<Block> from) {
        this.from(from.stream().flatMap(value -> value.getStateDefinition().getPossibleStates().stream()).toList());
    }

    /**
     * Adds a {@link Block} to which this mapping will be applied.
     *
     * @see #fromBlock(Block)
     */
    default void addFromBlock(Block from) {
        for (BlockState value : from.getStateDefinition().getPossibleStates()) {
            this.addFrom(value);
        }
    }

    /**
     * @see #addFromBlock(Block)
     */
    default void addFromBlock(Block[] from) {
        for (Block value : from) {
            this.addFromBlock(value);
        }
    }

    /**
     * @see #addFromBlock(Block)
     */
    default void addFromBlock(Collection<Block> from) {
        for (Block value : from) {
            this.addFromBlock(value);
        }
    }

    /**
     * Sets the {@link BlockState} that this mapping will map to.
     *
     * <p>
     * This replaces any previous value set.
     * </p>
     */
    void to(BlockState to);

    /**
     * Calls {@link #to} with the {@linkplain Block#defaultBlockState() default block state}
     * of the given {@link Block}.
     */
    default void toDefaultStateOf(Block to) {
        this.to(to.defaultBlockState());
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
     * @param function            The function to apply.
     * @param requiresCoordinates Whether this mapping requires the coordinates
     *                            ({@link BlockMappingFunctionContext#getPhysicalBlockX()} and so on).
     */
    void function(Consumer<BlockMappingHandleNMS> function, boolean requiresCoordinates);

}

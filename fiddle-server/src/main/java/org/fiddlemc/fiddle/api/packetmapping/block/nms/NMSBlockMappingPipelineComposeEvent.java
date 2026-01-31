package org.fiddlemc.fiddle.api.packetmapping.block.nms;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingPipelineComposeEvent;
import java.util.List;
import java.util.function.Consumer;

/**
 * Provides Minecraft internals functionality that extends {@link BlockMappingPipelineComposeEvent}.
 *
 * <p>
 * For performance reasons, input should be as specific as possible.
 * This means arrays should be as small as possible.
 * </p>
 */
public interface NMSBlockMappingPipelineComposeEvent extends BlockMappingPipelineComposeEvent {

    /**
     * Register a simple mapping.
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param from           The {@link BlockState} to which the mapping applies.
     * @param to             The {@link BlockState} to map to.
     */
    void registerSimple(ClientView.AwarenessLevel awarenessLevel, BlockState from, BlockState to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockState, BlockState)
     */
    void registerSimple(ClientView.AwarenessLevel[] awarenessLevels, BlockState from, BlockState to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockState, BlockState)
     */
    void registerSimple(ClientView.AwarenessLevel awarenessLevel, BlockState[] from, BlockState to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockState, BlockState)
     */
    void registerSimple(ClientView.AwarenessLevel[] awarenessLevels, BlockState[] from, BlockState to);

    /**
     * The same as {@link #registerSimple(ClientView.AwarenessLevel, BlockState, BlockState)},
     * but for each {@link BlockState} of the given {@code from}.
     */
    void registerSimple(ClientView.AwarenessLevel awarenessLevel, Block from, BlockState to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, Block, BlockState)
     */
    void registerSimple(ClientView.AwarenessLevel[] awarenessLevels, Block from, BlockState to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, Block, BlockState)
     */
    void registerSimple(ClientView.AwarenessLevel awarenessLevel, Block[] from, BlockState to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, Block, BlockState)
     */
    void registerSimple(ClientView.AwarenessLevel[] awarenessLevels, Block[] from, BlockState to);

    /**
     * Calls {@link #registerSimple(ClientView.AwarenessLevel, BlockState, BlockState)}
     * for each matching {@link BlockState} of the {@code from} and {@code to}.
     *
     * <p>
     * This requires that the blocks share all their states.
     * </p>
     */
    void registerStateToState(ClientView.AwarenessLevel awarenessLevel, Block from, Block to);

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, Block, Block)
     */
    void registerStateToState(ClientView.AwarenessLevel[] awarenessLevels, Block from, Block to);

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, Block, Block)
     */
    void registerStateToState(ClientView.AwarenessLevel awarenessLevel, Block[] from, Block to);

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, Block, Block)
     */
    void registerStateToState(ClientView.AwarenessLevel[] awarenessLevels, Block[] from, Block to);

    /**
     * Registers a complex mapping.
     *
     * <p>
     * Complex mappings are much less efficient than simple mappings.
     * Please use simple mappings whenever you can.
     * </p>
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param blockState     The {@link BlockState} to which the mapping applies.
     * @param mapping        The {@link NMSBlockStateMapping} to register.
     */
    void registerComplex(ClientView.AwarenessLevel awarenessLevel, BlockState blockState, NMSBlockStateMapping mapping);

    /**
     * @see #registerComplex(ClientView.AwarenessLevel, BlockState, NMSBlockStateMapping)
     */
    void registerComplex(ClientView.AwarenessLevel[] awarenessLevels, BlockState blockState, NMSBlockStateMapping mapping);

    /**
     * @see #registerComplex(ClientView.AwarenessLevel, BlockState, NMSBlockStateMapping)
     */
    void registerComplex(ClientView.AwarenessLevel awarenessLevel, BlockState[] blockStates, NMSBlockStateMapping mapping);

    /**
     * @see #registerComplex(ClientView.AwarenessLevel, BlockState, NMSBlockStateMapping)
     */
    void registerComplex(ClientView.AwarenessLevel[] awarenessLevels, BlockState[] blockStates, NMSBlockStateMapping mapping);

    /**
     * The same as {@link #registerComplex(ClientView.AwarenessLevel, BlockState, NMSBlockStateMapping)},
     * but for each {@link BlockState} of the given {@code block}.
     */
    void registerComplex(ClientView.AwarenessLevel awarenessLevel, Block block, NMSBlockStateMapping mapping);

    /**
     * @see #registerComplex(ClientView.AwarenessLevel, Block, NMSBlockStateMapping)
     */
    void registerComplex(ClientView.AwarenessLevel[] awarenessLevels, Block block, NMSBlockStateMapping mapping);

    /**
     * @see #registerComplex(ClientView.AwarenessLevel, Block, NMSBlockStateMapping)
     */
    void registerComplex(ClientView.AwarenessLevel awarenessLevel, Block[] blocks, NMSBlockStateMapping mapping);

    /**
     * @see #registerComplex(ClientView.AwarenessLevel, Block, NMSBlockStateMapping)
     */
    void registerComplex(ClientView.AwarenessLevel[] awarenessLevels, Block[] blocks, NMSBlockStateMapping mapping);

    /**
     * Changes the list of registered mappings for the given awareness level and block state.
     *
     * <p>
     * This list is freely mutable.
     * This can be used to remove existing mappings or insert a mapping at the desired index.
     * </p>
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel}.
     * @param blockState     The {@link BlockState}.
     * @param consumer       The consumer that applies the desired changes to the list of mappings.
     */
    void changeRegistered(ClientView.AwarenessLevel awarenessLevel, BlockState blockState, Consumer<List<NMSBlockStateMapping>> consumer);

}

package org.fiddlemc.fiddle.api.packetmapping.block.nms;

import io.papermc.paper.registry.PaperRegistries;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMapping;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingsComposeEvent;
import org.fiddlemc.fiddle.api.util.composable.GetRegisteredComposeEvent;
import org.fiddlemc.fiddle.impl.moredatadriven.minecraft.BlockRegistry;
import java.util.List;
import java.util.function.Consumer;

/**
 * Provides Minecraft internals functionality that extends {@link BlockMappingsComposeEvent}.
 *
 * <p>
 * For performance reasons, input should be as specific as possible.
 * This means arrays should be as small as possible.
 * </p>
 */
public interface NMSBlockMappingsComposeEvent extends BlockMappingsComposeEvent {

    /**
     * @see #getRegistered(Object)
     */
    default List<BlockMapping> getRegisteredNMS(Pair<ClientView.AwarenessLevel, BlockState> key) {
        return this.getRegisteredNMS(key.left(), key.right());
    }

    /**
     * @see #getRegistered(ClientView.AwarenessLevel, BlockData)
     */
    default List<BlockMapping> getRegisteredNMS(ClientView.AwarenessLevel awarenessLevel, BlockState from) {
        return this.getRegistered(awarenessLevel, from.createCraftBlockData());
    }

    /**
     * @see #changeRegistered(Object, Consumer)
     */
    default void changeRegisteredNMS(Pair<ClientView.AwarenessLevel, BlockState> key, Consumer<List<BlockMapping>> listConsumer) {
        this.changeRegisteredNMS(key.left(), key.right(), listConsumer);
    }

    /**
     * @see #changeRegistered(ClientView.AwarenessLevel, BlockData, Consumer)
     */
    default void changeRegisteredNMS(ClientView.AwarenessLevel awarenessLevel, BlockState from, Consumer<List<BlockMapping>> listConsumer) {
        this.changeRegistered(awarenessLevel, from.createCraftBlockData(), listConsumer);
    }

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockData, BlockData)
     */
    default void registerSimpleNMS(ClientView.AwarenessLevel awarenessLevel, BlockState from, BlockState to) {
        this.registerSimple(awarenessLevel, from.createCraftBlockData(), to.createCraftBlockData());
    }

    /**
     * @see #registerSimple(ClientView.AwarenessLevel[], BlockData, BlockData)
     */
    default void registerSimpleNMS(ClientView.AwarenessLevel[] awarenessLevel, BlockState from, BlockState to) {
        this.registerSimple(awarenessLevel, from.createCraftBlockData(), to.createCraftBlockData());
    }

    /**
     * @see #registerSimple(Iterable, BlockData, BlockData)
     */
    default void registerSimpleNMS(Iterable<ClientView.AwarenessLevel> awarenessLevel, BlockState from, BlockState to) {
        this.registerSimple(awarenessLevel, from.createCraftBlockData(), to.createCraftBlockData());
    }

    /**
     * Register a simple mapping to the {@linkplain BlockType#createBlockData() default block state} of the given {@link BlockType}.
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param from           The {@link BlockData} to which the mapping applies.
     * @param to             The {@link BlockType} of which the default block state to map to.
     */
    default void registerSimpleToDefaultState(ClientView.AwarenessLevel awarenessLevel, BlockState from, Block to) {
        this.registerSimpleToDefaultState(List.of(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimpleToDefaultState(ClientView.AwarenessLevel, BlockState, Block)
     */
    default void registerSimpleToDefaultState(ClientView.AwarenessLevel[] awarenessLevel, BlockState from, Block to) {
        this.registerSimpleToDefaultState(Arrays.asList(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimpleToDefaultState(ClientView.AwarenessLevel, BlockState, Block)
     */
    default void registerSimpleToDefaultState(Iterable<ClientView.AwarenessLevel> awarenessLevel, BlockState from, Block to) {
        this.register(builder -> {
            builder.awarenessLevel(awarenessLevel);
            builder.fromBlockData(from);
            builder.toBlockTypeDefaultState(to);
        });
    }

    /**
     * The same as {@link #registerSimple(ClientView.AwarenessLevel, BlockState, BlockState)},
     * but for each {@link BlockData} of the given {@code from}.
     */
    default void registerSimple(ClientView.AwarenessLevel awarenessLevel, Block from, BlockState to) {
        this.registerSimple(List.of(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, Block, BlockState)
     */
    default void registerSimple(ClientView.AwarenessLevel[] awarenessLevel, Block from, BlockState to) {
        this.registerSimple(Arrays.asList(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, Block, BlockState)
     */
    default void registerSimple(Iterable<ClientView.AwarenessLevel> awarenessLevel, Block from, BlockState to) {
        this.register(builder -> {
            builder.awarenessLevel(awarenessLevel);
            builder.fromBlockType(from);
            builder.toBlockData(to);
        });
    }

    /**
     * The same as {@link #registerSimpleToDefaultState(ClientView.AwarenessLevel, BlockState, Block)},
     * but for each {@link BlockData} of the given {@code from}.
     */
    default void registerSimpleToDefaultState(ClientView.AwarenessLevel awarenessLevel, Block from, Block to) {
        this.registerSimpleToDefaultState(List.of(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimpleToDefaultState(ClientView.AwarenessLevel, Block, Block)
     */
    default void registerSimpleToDefaultState(ClientView.AwarenessLevel[] awarenessLevel, Block from, Block to) {
        this.registerSimpleToDefaultState(Arrays.asList(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimpleToDefaultState(ClientView.AwarenessLevel, Block, Block)
     */
    default void registerSimpleToDefaultState(Iterable<ClientView.AwarenessLevel> awarenessLevel, Block from, Block to) {
        this.register(builder -> {
            builder.awarenessLevel(awarenessLevel);
            builder.fromBlockType(from);
            builder.toBlockTypeDefaultState(to);
        });
    }

    /**
     * Calls {@link #registerSimple(ClientView.AwarenessLevel, BlockData, BlockData)}
     * for each matching {@link BlockData} of the {@code from} and {@code to}.
     *
     * <p>
     * This requires that the blocks share all their states.
     * </p>
     */
    default void registerStateToState(ClientView.AwarenessLevel awarenessLevel, Block from, Block to) {
        this.registerStateToState(List.of(awarenessLevel), from, to);
    }

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, Block, Block)
     */
    default void registerStateToState(ClientView.AwarenessLevel[] awarenessLevel, Block from, Block to) {
        this.registerStateToState(Arrays.asList(awarenessLevel), from, to);
    }

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, BlockType, BlockType)
     */
    default void registerStateToStateNMS(Iterable<ClientView.AwarenessLevel> awarenessLevel, Block from, Block to) {
        this.registerStateToState(awarenessLevel, PaperRegistries.
    }

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

}

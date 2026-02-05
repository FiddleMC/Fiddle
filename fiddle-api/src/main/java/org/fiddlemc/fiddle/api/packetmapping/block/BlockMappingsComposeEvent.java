package org.fiddlemc.fiddle.api.packetmapping.block;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.util.composable.BuilderComposeEvent;
import org.fiddlemc.fiddle.api.util.composable.ChangeRegisteredComposeEvent;
import org.fiddlemc.fiddle.api.util.composable.GetRegisteredComposeEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Provides functionality to register mappings to the {@link BlockMappings}.
 *
 * <p>
 * Extended functionality is available by casting to {@code NMSBlockMappingPipelineComposeEvent}.
 * </p>
 *
 * <p>
 * For performance reasons, input should be as specific as possible.
 * This means arrays should be as small as possible.
 * </p>
 */
public interface BlockMappingsComposeEvent extends BuilderComposeEvent<BlockMappingRegistrationBuilder>, GetRegisteredComposeEvent<Pair<ClientView.AwarenessLevel, BlockData>, BlockMapping>, ChangeRegisteredComposeEvent<Pair<ClientView.AwarenessLevel, BlockData>, BlockMapping> {

    /**
     * @see #getRegistered(Object)
     */
    default List<BlockMapping> getRegistered(ClientView.AwarenessLevel awarenessLevel, BlockData from) {
        return this.getRegistered(Pair.of(awarenessLevel, from));
    }

    /**
     * @see #changeRegistered(Object, Consumer)
     */
    default void changeRegistered(ClientView.AwarenessLevel awarenessLevel, BlockData from, Consumer<List<BlockMapping>> listConsumer) {
        this.changeRegistered(Pair.of(awarenessLevel, from), listConsumer);
    }

    /**
     * Register a simple mapping.
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param from           The {@link BlockData} to which the mapping applies.
     * @param to             The {@link BlockData} to map to.
     */
    default void registerSimple(ClientView.AwarenessLevel awarenessLevel, BlockData from, BlockData to) {
        this.registerSimple(List.of(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockData, BlockData)
     */
    default void registerSimple(ClientView.AwarenessLevel[] awarenessLevel, BlockData from, BlockData to) {
        this.registerSimple(Arrays.asList(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockData, BlockData)
     */
    default void registerSimple(Iterable<ClientView.AwarenessLevel> awarenessLevel, BlockData from, BlockData to) {
        this.register(builder -> {
            builder.awarenessLevel(awarenessLevel);
            builder.fromBlockData(from);
            builder.toBlockData(to);
        });
    }

    /**
     * Register a simple mapping to the {@linkplain BlockType#createBlockData() default block state} of the given {@link BlockType}.
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param from           The {@link BlockData} to which the mapping applies.
     * @param to             The {@link BlockType} of which the default block state to map to.
     */
    default void registerSimpleToDefaultState(ClientView.AwarenessLevel awarenessLevel, BlockData from, BlockType to) {
        this.registerSimpleToDefaultState(List.of(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimpleToDefaultState(ClientView.AwarenessLevel, BlockData, BlockType)
     */
    default void registerSimpleToDefaultState(ClientView.AwarenessLevel[] awarenessLevel, BlockData from, BlockType to) {
        this.registerSimpleToDefaultState(Arrays.asList(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimpleToDefaultState(ClientView.AwarenessLevel, BlockData, BlockType)
     */
    default void registerSimpleToDefaultState(Iterable<ClientView.AwarenessLevel> awarenessLevel, BlockData from, BlockType to) {
        this.register(builder -> {
            builder.awarenessLevel(awarenessLevel);
            builder.fromBlockData(from);
            builder.toBlockTypeDefaultState(to);
        });
    }

    /**
     * The same as {@link #registerSimple(ClientView.AwarenessLevel, BlockData, BlockData)},
     * but for each {@link BlockData} of the given {@code from}.
     */
    default void registerSimple(ClientView.AwarenessLevel awarenessLevel, BlockType from, BlockData to) {
        this.registerSimple(List.of(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockType, BlockData)
     */
    default void registerSimple(ClientView.AwarenessLevel[] awarenessLevel, BlockType from, BlockData to) {
        this.registerSimple(Arrays.asList(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockType, BlockData)
     */
    default void registerSimple(Iterable<ClientView.AwarenessLevel> awarenessLevel, BlockType from, BlockData to) {
        this.register(builder -> {
            builder.awarenessLevel(awarenessLevel);
            builder.fromBlockType(from);
            builder.toBlockData(to);
        });
    }

    /**
     * The same as {@link #registerSimpleToDefaultState(ClientView.AwarenessLevel, BlockData, BlockType)},
     * but for each {@link BlockData} of the given {@code from}.
     */
    default void registerSimpleToDefaultState(ClientView.AwarenessLevel awarenessLevel, BlockType from, BlockType to) {
        this.registerSimpleToDefaultState(List.of(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimpleToDefaultState(ClientView.AwarenessLevel, BlockType, BlockType)
     */
    default void registerSimpleToDefaultState(ClientView.AwarenessLevel[] awarenessLevel, BlockType from, BlockType to) {
        this.registerSimpleToDefaultState(Arrays.asList(awarenessLevel), from, to);
    }

    /**
     * @see #registerSimpleToDefaultState(ClientView.AwarenessLevel, BlockType, BlockType)
     */
    default void registerSimpleToDefaultState(Iterable<ClientView.AwarenessLevel> awarenessLevel, BlockType from, BlockType to) {
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
    default void registerStateToState(ClientView.AwarenessLevel awarenessLevel, BlockType from, BlockType to) {
        this.registerStateToState(List.of(awarenessLevel), from, to);
    }

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, BlockType, BlockType)
     */
    default void registerStateToState(ClientView.AwarenessLevel[] awarenessLevel, BlockType from, BlockType to) {
        this.registerStateToState(Arrays.asList(awarenessLevel), from, to);
    }

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, BlockType, BlockType)
     */
    default void registerStateToState(Iterable<ClientView.AwarenessLevel> awarenessLevel, BlockType from, BlockType to) {
        for (BlockData fromState : from.createBlockDataStates()) {
            BlockData toState = to.createBlockData();
            fromState.copyTo(toState);
            this.registerSimple(awarenessLevel, fromState, toState);
        }
    }

}

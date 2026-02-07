package org.fiddlemc.fiddle.api.packetmapping.block.nms;

import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.key.Key;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingsComposeEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * An extension to {@link BlockMappingsComposeEvent} using Minecraft internals.
 */
public interface BlockMappingsComposeEventNMS<M> extends BlockMappingsComposeEvent<M> {

    /**
     * @see #register(Consumer)
     */
    void registerNMS(Consumer<BlockMappingBuilderNMS> builderConsumer);

    /**
     * @see #getRegistered(Object)
     */
    default List<M> getRegisteredNMS(Pair<ClientView.AwarenessLevel, BlockState> key) {
        return this.getRegisteredNMS(key.left(), key.right());
    }

    /**
     * @see #getRegistered(ClientView.AwarenessLevel, BlockData)
     */
    default List<M> getRegisteredNMS(ClientView.AwarenessLevel awarenessLevel, BlockState from) {
        return this.getRegistered(awarenessLevel, from.createCraftBlockData());
    }

    /**
     * @see #changeRegistered(Object, Consumer)
     */
    default void changeRegisteredNMS(Pair<ClientView.AwarenessLevel, BlockState> key, Consumer<List<M>> listConsumer) {
        this.changeRegisteredNMS(key.left(), key.right(), listConsumer);
    }

    /**
     * @see #changeRegistered(ClientView.AwarenessLevel, BlockData, Consumer)
     */
    default void changeRegisteredNMS(ClientView.AwarenessLevel awarenessLevel, BlockState from, Consumer<List<M>> listConsumer) {
        this.changeRegistered(awarenessLevel, from.createCraftBlockData(), listConsumer);
    }

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, BlockType, BlockType) 
     */
    default void registerStateToStateNMS(ClientView.AwarenessLevel awarenessLevel, Block from, Block to) {
        this.registerStateToStateNMS(List.of(awarenessLevel), from, to);
    }

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel[], BlockType, BlockType)
     */
    default void registerStateToStateNMS(ClientView.AwarenessLevel[] awarenessLevel, Block from, Block to) {
        this.registerStateToStateNMS(Arrays.asList(awarenessLevel), from, to);
    }

    /**
     * @see #registerStateToState(Collection, BlockType, BlockType)
     */
    default void registerStateToStateNMS(Collection<ClientView.AwarenessLevel> awarenessLevel, Block from, Block to) {
        this.registerStateToState(awarenessLevel, Registry.BLOCK.getOrThrow(Key.key(from.keyInBlockRegistry.getNamespace(), from.keyInBlockRegistry.getPath())), Registry.BLOCK.getOrThrow(Key.key(to.keyInBlockRegistry.getNamespace(), to.keyInBlockRegistry.getPath())));
    }

}

package org.fiddlemc.fiddle.impl.packetmapping.block;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSBlockMappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSBlockStateMapping;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The implementation of {@link NMSBlockMappingPipelineComposeEvent}.
 */
public final class BlockMappingPipelineComposeEventImpl implements PaperLifecycleEvent, NMSBlockMappingPipelineComposeEvent {

    /**
     * The registered mappings.
     *
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in a map where {@link BlockState#indexInBlockStateRegistry} is the key.
     * The array does not contain null values, but some maps may not contain every item as a key.
     * The maps do not contain empty lists.
     * </p>
     */
    final Int2ObjectMap<List<NMSBlockStateMapping>>[] mappings;

    public BlockMappingPipelineComposeEventImpl() {
        this.mappings = new Int2ObjectMap[ClientView.AwarenessLevel.getAll().length];
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new Int2ObjectOpenHashMap<>();
        }
    }

    private void register(final Int2ObjectMap<List<NMSBlockStateMapping>> mapForAwarenessLevel, final BlockState from, final NMSBlockStateMapping mapping) {
        mapForAwarenessLevel.computeIfAbsent(from.indexInBlockStateRegistry, $ -> new ArrayList<>(1)).add(mapping);
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel awarenessLevel, final BlockState from, final BlockState to) {
        if (from.equals(to)) {
            return;
        }
        this.register(this.getForAwarenessLevel(awarenessLevel), from, new SimpleBlockStateMappingImpl(to));
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel[] awarenessLevels, final BlockState from, final BlockState to) {
        if (from.equals(to)) {
            return;
        }
        var mapping = new SimpleBlockStateMappingImpl(to);
        for (var awarenessLevel : awarenessLevels) {
            this.register(this.getForAwarenessLevel(awarenessLevel), from, mapping);
        }
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel awarenessLevel, final BlockState[] from, final BlockState to) {
        var mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        var mapping = new SimpleBlockStateMappingImpl(to);
        for (var fromElement : from) {
            if (fromElement.equals(to)) {
                continue;
            }
            this.register(mapForAwarenessLevel, fromElement, mapping);
        }
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel[] awarenessLevels, final BlockState[] from, final BlockState to) {
        var mapping = new SimpleBlockStateMappingImpl(to);
        for (var awarenessLevel : awarenessLevels) {
            var mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            for (var fromElement : from) {
                if (fromElement.equals(to)) {
                    continue;
                }
                this.register(mapForAwarenessLevel, fromElement, mapping);
            }
        }
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel awarenessLevel, final Block from, final BlockState to) {
        this.registerSimple(awarenessLevel, getBlockStateArray(from), to);
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel[] awarenessLevels, final Block from, final BlockState to) {
        this.registerSimple(awarenessLevels, getBlockStateArray(from), to);
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel awarenessLevel, final Block[] from, final BlockState to) {
        this.registerSimple(awarenessLevel, getBlockStateArray(from), to);
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel[] awarenessLevels, final Block[] from, final BlockState to) {
        this.registerSimple(awarenessLevels, getBlockStateArray(from), to);
    }

    @Override
    public void registerStateToState(final ClientView.AwarenessLevel awarenessLevel, final Block from, final Block to) {
        var mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        for (var toState : to.getStateDefinition().getPossibleStates()) {
            var fromState = copyProperties(toState, from);
            if (fromState.equals(toState)) {
                continue;
            }
            this.register(mapForAwarenessLevel, fromState, new SimpleBlockStateMappingImpl(toState));
        }
    }

    @Override
    public void registerStateToState(final ClientView.AwarenessLevel[] awarenessLevels, final Block from, final Block to) {
        var mapsForAwarenessLevels = this.getForAwarenessLevels(awarenessLevels);
        for (var toState : to.getStateDefinition().getPossibleStates()) {
            var fromState = copyProperties(toState, from);
            if (fromState.equals(toState)) {
                continue;
            }
            var mapping = new SimpleBlockStateMappingImpl(toState);
            for (var mapForAwarenessLevel : mapsForAwarenessLevels) {
                this.register(mapForAwarenessLevel, fromState, mapping);
            }
        }
    }

    @Override
    public void registerStateToState(final ClientView.AwarenessLevel awarenessLevel, final Block[] from, final Block to) {
        var mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        for (var toState : to.getStateDefinition().getPossibleStates()) {
            var mapping = new SimpleBlockStateMappingImpl(toState);
            for (var fromElement : from) {
                var fromState = copyProperties(toState, fromElement);
                if (fromState.equals(toState)) {
                    continue;
                }
                this.register(mapForAwarenessLevel, fromState, mapping);
            }
        }
    }

    @Override
    public void registerStateToState(final ClientView.AwarenessLevel[] awarenessLevels, final Block[] from, final Block to) {
        var mapsForAwarenessLevels = this.getForAwarenessLevels(awarenessLevels);
        for (var toState : to.getStateDefinition().getPossibleStates()) {
            var mapping = new SimpleBlockStateMappingImpl(toState);
            for (var fromElement : from) {
                var fromState = copyProperties(toState, fromElement);
                if (fromState.equals(toState)) {
                    continue;
                }
                for (var mapForAwarenessLevel : mapsForAwarenessLevels) {
                    this.register(mapForAwarenessLevel, fromState, mapping);
                }
            }
        }
    }

    @Override
    public void registerComplex(final ClientView.AwarenessLevel awarenessLevel, final BlockState blockState, final NMSBlockStateMapping mapping) {
        this.register(this.getForAwarenessLevel(awarenessLevel), blockState, mapping);
    }

    @Override
    public void registerComplex(final ClientView.AwarenessLevel[] awarenessLevels, final BlockState blockState, final NMSBlockStateMapping mapping) {
        for (var awarenessLevel : awarenessLevels) {
            this.register(this.getForAwarenessLevel(awarenessLevel), blockState, mapping);
        }
    }

    @Override
    public void registerComplex(final ClientView.AwarenessLevel awarenessLevel, final BlockState[] blockStates, final NMSBlockStateMapping mapping) {
        var mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        for (var blockState : blockStates) {
            this.register(mapForAwarenessLevel, blockState, mapping);
        }
    }

    @Override
    public void registerComplex(final ClientView.AwarenessLevel[] awarenessLevels, final BlockState[] blockStates, final NMSBlockStateMapping mapping) {
        for (var awarenessLevel : awarenessLevels) {
            var mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            for (var blockState : blockStates) {
                this.register(mapForAwarenessLevel, blockState, mapping);
            }
        }
    }

    @Override
    public void registerComplex(final ClientView.AwarenessLevel awarenessLevel, final Block block, final NMSBlockStateMapping mapping) {
        this.registerComplex(awarenessLevel, getBlockStateArray(block), mapping);
    }

    @Override
    public void registerComplex(final ClientView.AwarenessLevel[] awarenessLevels, final Block block, final NMSBlockStateMapping mapping) {
        this.registerComplex(awarenessLevels, getBlockStateArray(block), mapping);
    }

    @Override
    public void registerComplex(final ClientView.AwarenessLevel awarenessLevel, final Block[] blocks, final NMSBlockStateMapping mapping) {
        this.registerComplex(awarenessLevel, getBlockStateArray(blocks), mapping);
    }

    @Override
    public void registerComplex(final ClientView.AwarenessLevel[] awarenessLevels, final Block[] blocks, final NMSBlockStateMapping mapping) {
        this.registerComplex(awarenessLevels, getBlockStateArray(blocks), mapping);
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel awarenessLevel, final BlockData from, final BlockData to) {
        this.registerSimple(awarenessLevel, ((CraftBlockData) from).getState(), ((CraftBlockData) to).getState());
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel[] awarenessLevels, final BlockData from, final BlockData to) {
        this.registerSimple(awarenessLevels, ((CraftBlockType<?>) from).getHandle(), ((CraftBlockData) to).getState());
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel awarenessLevel, final BlockData[] from, final BlockData to) {
        this.registerSimple(awarenessLevel, toNMS(from), ((CraftBlockData) to).getState());
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel[] awarenessLevels, final BlockData[] from, final BlockData to) {
        this.registerSimple(awarenessLevels, toNMS(from), ((CraftBlockData) to).getState());
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel awarenessLevel, final BlockType from, final BlockData to) {
        this.registerSimple(awarenessLevel, ((CraftBlockType<?>) from).getHandle(), ((CraftBlockData) to).getState());
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel[] awarenessLevels, final BlockType from, final BlockData to) {
        this.registerSimple(awarenessLevels, ((CraftBlockType<?>) from).getHandle(), ((CraftBlockData) to).getState());
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel awarenessLevel, final BlockType[] from, final BlockData to) {
        this.registerSimple(awarenessLevel, toNMS(from), ((CraftBlockData) to).getState());
    }

    @Override
    public void registerSimple(final ClientView.AwarenessLevel[] awarenessLevels, final BlockType[] from, final BlockData to) {
        this.registerSimple(awarenessLevels, toNMS(from), ((CraftBlockData) to).getState());
    }

    @Override
    public void registerStateToState(final ClientView.AwarenessLevel awarenessLevel, final BlockType from, final BlockType to) {
        this.registerStateToState(awarenessLevel, ((CraftBlockType<?>) from).getHandle(), ((CraftBlockType<?>) to).getHandle());
    }

    @Override
    public void registerStateToState(final ClientView.AwarenessLevel[] awarenessLevels, final BlockType from, final BlockType to) {
        this.registerStateToState(awarenessLevels, ((CraftBlockType<?>) from).getHandle(), ((CraftBlockType<?>) to).getHandle());
    }

    @Override
    public void registerStateToState(final ClientView.AwarenessLevel awarenessLevel, final BlockType[] from, final BlockType to) {
        this.registerStateToState(awarenessLevel, toNMS(from), ((CraftBlockType<?>) to).getHandle());
    }

    @Override
    public void registerStateToState(final ClientView.AwarenessLevel[] awarenessLevels, final BlockType[] from, final BlockType to) {
        this.registerStateToState(awarenessLevels, toNMS(from), ((CraftBlockType<?>) to).getHandle());
    }

    @Override
    public void changeRegistered(ClientView.AwarenessLevel awarenessLevel, BlockState blockState, Consumer<List<NMSBlockStateMapping>> consumer) {
        var mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        @Nullable List<NMSBlockStateMapping> original = mapForAwarenessLevel.get(blockState.indexInBlockStateRegistry);
        List<NMSBlockStateMapping> passed = original != null ? original : new ArrayList<>(1);
        consumer.accept(passed);
        if (passed.isEmpty()) {
            if (original != null) {
                mapForAwarenessLevel.remove(blockState.indexInBlockStateRegistry);
            }
        } else {
            if (original == null) {
                mapForAwarenessLevel.put(blockState.indexInBlockStateRegistry, passed);
            }
        }
    }

    private Int2ObjectMap<List<NMSBlockStateMapping>> getForAwarenessLevel(ClientView.AwarenessLevel awarenessLevel) {
        return this.mappings[awarenessLevel.ordinal()];
    }

    private Int2ObjectMap<List<NMSBlockStateMapping>>[] getForAwarenessLevels(ClientView.AwarenessLevel[] awarenessLevels) {
        Int2ObjectMap<List<NMSBlockStateMapping>>[] maps = new Int2ObjectMap[awarenessLevels.length];
        for (int i = 0; i < awarenessLevels.length; i++) {
            maps[i] = this.getForAwarenessLevel(awarenessLevels[i]);
        }
        return maps;
    }

    private static BlockState[] getBlockStateArray(Block block) {
        return block.getStateDefinition().getPossibleStates().toArray(BlockState[]::new);
    }

    private static BlockState[] getBlockStateArray(Block[] blocks) {
        int count = 0;
        for (var block : blocks) {
            count += block.getStateDefinition().getPossibleStates().size();
        }
        BlockState[] states = new BlockState[count];
        int i = 0;
        for (var block : blocks) {
            for (var state : block.getStateDefinition().getPossibleStates()) {
                states[i++] = state;
            }
        }
        return states;
    }

    private static BlockState copyProperties(BlockState from, Block to) {
        BlockState state = to.defaultBlockState();
        for (Property<?> property : to.getStateDefinition().getProperties()) {
            state = copyPropertyValue(property, from, state);
        }
        return state;
    }

    private static <V extends Comparable<V>> BlockState copyPropertyValue(Property<V> property, BlockState from, BlockState to) {
        return to.setValue(property, from.getValue(property));
    }

    private static BlockState[] toNMS(BlockData[] data) {
        BlockState[] states = new BlockState[data.length];
        for (int i = 0; i < data.length; i++) {
            states[i] = ((CraftBlockData) data[i]).getState();
        }
        return states;
    }

    private static Block[] toNMS(BlockType[] blockTypes) {
        Block[] blocks = new Block[blockTypes.length];
        for (int i = 0; i < blockTypes.length; i++) {
            blocks[i] = ((CraftBlockType<?>) blockTypes[i]).getHandle();
        }
        return blocks;
    }

}

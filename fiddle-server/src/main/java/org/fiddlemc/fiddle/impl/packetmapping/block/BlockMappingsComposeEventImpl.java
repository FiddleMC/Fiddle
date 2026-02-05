package org.fiddlemc.fiddle.impl.packetmapping.block;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingBuilder;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingsComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.BlockMappingBuilderNMS;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.BlockMappingsComposeEventNMS;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * The implementation of {@link BlockMappingsComposeEvent}.
 */
public final class BlockMappingsComposeEventImpl implements PaperLifecycleEvent, BlockMappingsComposeEventNMS<BlockMappingsStep> {

    /**
     * The registered steps.
     *
     * <p>
     * The steps are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in a map where {@link BlockState#indexInBlockStateRegistry} is the key.
     * The array does not contain null values, but some maps may not contain every item as a key.
     * The maps do not contain empty lists.
     * </p>
     */
    final Int2ObjectMap<List<BlockMappingsStep>>[] mappings;

    public BlockMappingsComposeEventImpl() {
        this.mappings = new Int2ObjectMap[ClientView.AwarenessLevel.getAll().length];
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new Int2ObjectOpenHashMap<>();
        }
    }

    @Override
    public void register(Consumer<BlockMappingBuilder> builderConsumer) {
        BlockMappingBuilderImpl builder = new BlockMappingBuilderImpl();
        builderConsumer.accept(builder);
        builder.registerWith(this);
    }

    @Override
    public List<BlockMappingsStep> getRegistered(Pair<ClientView.AwarenessLevel, BlockData> key) {
        ClientView.AwarenessLevel awarenessLevel = key.left();
        int indexInBlockStateRegistry = ((CraftBlockData) key).getState().indexInBlockStateRegistry;
        @Nullable List<BlockMappingsStep> original = this.getForAwarenessLevel(awarenessLevel).get(indexInBlockStateRegistry);
        return original != null ? Collections.unmodifiableList(original) : Collections.emptyList();
    }

    @Override
    public void changeRegistered(Pair<ClientView.AwarenessLevel, BlockData> key, Consumer<List<BlockMappingsStep>> listConsumer) {
        ClientView.AwarenessLevel awarenessLevel = key.left();
        int indexInBlockStateRegistry = ((CraftBlockData) key).getState().indexInBlockStateRegistry;
        Int2ObjectMap<List<BlockMappingsStep>> mapForAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
        @Nullable List<BlockMappingsStep> original = mapForAwarenessLevel.get(indexInBlockStateRegistry);
        List<BlockMappingsStep> passed = original != null ? original : new ArrayList<>(1);
        listConsumer.accept(passed);
        if (passed.isEmpty()) {
            if (original != null) {
                mapForAwarenessLevel.remove(indexInBlockStateRegistry);
            }
        } else {
            if (original == null) {
                mapForAwarenessLevel.put(indexInBlockStateRegistry, passed);
            }
        }
    }

    @Override
    public void registerNMS(Consumer<BlockMappingBuilderNMS> builderConsumer) {
        BlockMappingBuilderNMSImpl builder = new BlockMappingBuilderNMSImpl();
        builderConsumer.accept(builder);
        builder.registerWith(this);
    }

    public void register(Iterable<ClientView.AwarenessLevel> awarenessLevels, Iterable<BlockState> states, BlockMappingsStep step) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            Int2ObjectMap<List<BlockMappingsStep>> forAwarenessLevel = this.getForAwarenessLevel(awarenessLevel);
            for (BlockState state : states) {
                forAwarenessLevel.computeIfAbsent(state.indexInBlockStateRegistry, $ -> new ArrayList<>(1)).add(step);
            }
        }
    }

    private Int2ObjectMap<List<BlockMappingsStep>> getForAwarenessLevel(ClientView.AwarenessLevel awarenessLevel) {
        return this.mappings[awarenessLevel.ordinal()];
    }

}

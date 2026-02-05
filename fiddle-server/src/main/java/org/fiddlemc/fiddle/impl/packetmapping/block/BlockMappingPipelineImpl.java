package org.fiddlemc.fiddle.impl.packetmapping.block;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappings;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingsComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockStateMappingFunctionContext;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSBlockStateMapping;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSComplexBlockStateMapping;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSSimpleBlockStateMapping;
import org.fiddlemc.fiddle.impl.moredatadriven.minecraft.BlockStateRegistry;
import org.fiddlemc.fiddle.impl.util.composable.ComposableImpl;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pipeline of block mappings.
 */
public final class BlockMappingPipelineImpl extends ComposableImpl<BlockMappingsComposeEvent, BlockMappingPipelineComposeEventImpl> implements BlockMappings {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<BlockMappings, BlockMappingPipelineImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(BlockMappingPipelineImpl.class);
        }

    }

    public static BlockMappingPipelineImpl get() {
        return (BlockMappingPipelineImpl) BlockMappings.get();
    }

    @Override
    protected String getEventTypeNamePrefix() {
        return "fiddle_block_mapping_pipeline";
    }

    /**
     * The registered mappings that must be performed in chains:
     * this includes only the mappings for {@link BlockState}s for which there was
     * at least 1 {@linkplain NMSComplexBlockStateMapping complex} mapping.
     *
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in an array where {@link BlockState#indexInBlockStateRegistry} is the index.
     * The lowest-level array may be null, but will never be empty.
     * </p>
     */
    private final NMSBlockStateMapping[][][] chainMappings;

    /**
     * The registered mappings that can be applied directly:
     * this includes only the mappings for {@link BlockState}s for which there were only
     * {@linkplain NMSSimpleBlockStateMapping simple} mappings,
     * meaning the mapping always returns the same value.
     *
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in an array where {@link BlockState#indexInBlockStateRegistry} is the index.
     * The lowest-level value may be null.
     * </p>
     */
    private final @Nullable BlockState[][] directMappings;

    /**
     * The same as {@link #directMappings}, but contains the {@link BlockState#indexInBlockStateRegistry},
     * or -1 if the value in {@link #directMappings} would be null.
     */
    private final int[][] directMappingsAsIds;

    private BlockMappingPipelineImpl() {
        this.chainMappings = new NMSBlockStateMapping[ClientView.AwarenessLevel.getAll().length][][];
        this.directMappings = new BlockState[ClientView.AwarenessLevel.getAll().length][];
        this.directMappingsAsIds = new int[this.directMappings.length][];
    }

    public BlockState apply(BlockState data, BlockStateMappingFunctionContext context) {
        int awarenessLevelI = context.getClientView().getAwarenessLevel().ordinal();
        // If there is a direct mapping, apply it
        @Nullable BlockState directMapping = this.directMappings[awarenessLevelI][data.indexInBlockStateRegistry];
        if (directMapping != null) {
            return directMapping;
        }
        // If there is a mapping chain, apply it
        NMSBlockStateMapping @Nullable [] chain = this.chainMappings[awarenessLevelI][data.indexInBlockStateRegistry];
        if (chain != null) {
            return applyChain(data, context, chain);
        }
        // No mappings need to be applied
        return data;
    }

    public static BlockState applyChain(BlockState blockState, BlockStateMappingFunctionContext context, NMSBlockStateMapping[] chain) {
        BlockStateMappingHandleImpl handle = new BlockStateMappingHandleImpl(blockState, context, false);
        for (NMSBlockStateMapping mapping : chain) {
            mapping.apply(handle);
        }
        return handle.getImmutable();
    }

    public static int applyChain(int blockStateId, BlockStateMappingFunctionContext context, NMSBlockStateMapping[] chain) {
        return applyChain(BlockStateRegistry.get().byId(blockStateId), context, chain).indexInBlockStateRegistry;
    }

    /**
     * @param awarenessLevelI A {@link ClientView.AwarenessLevel#ordinal()}.
     * @param blockStateId A {@link BlockState#indexInBlockStateRegistry}.
     * @return The {@link BlockState#indexInBlockStateRegistry} for the {@link BlockState}
     * to which the {@link BlockState} with the given {@code blockStateId} is mapped,
     * or -1 if there is no direct mapping.
     */
    public int getDirectMapping(int awarenessLevelI, int blockStateId) {
        return this.directMappingsAsIds[awarenessLevelI][blockStateId];
    }

    /**
     * @param awarenessLevelI A {@link ClientView.AwarenessLevel#ordinal()}.
     * @param blockStateId A {@link BlockState#indexInBlockStateRegistry}.
     * @return The chain mapping for the {@link BlockState} with the given {@code blockStateId},
     * or null if there is no chain mapping.
     */
    public NMSBlockStateMapping @Nullable [] getChainMapping(int awarenessLevelI, int blockStateId) {
        return this.chainMappings[awarenessLevelI][blockStateId];
    }

    public boolean hasAnyMapping(int awarenessLevelI, int blockStateId) {
        return this.directMappingsAsIds[awarenessLevelI][blockStateId] != -1 || this.chainMappings[awarenessLevelI][blockStateId] != null;
    }

    public boolean hasAnyMapping(int awarenessLevelI, BlockState blockState) {
        return this.hasAnyMapping(awarenessLevelI, blockState.indexInBlockStateRegistry);
    }

    @Override
    protected BlockMappingPipelineComposeEventImpl createComposeEvent() {
        // Create the event
        BlockMappingPipelineComposeEventImpl event = new BlockMappingPipelineComposeEventImpl();
        // Register the built-in mappings
        // TODO
        // Return the event
        return event;
    }

    @Override
    protected void copyInformationFromEvent(final BlockMappingPipelineComposeEventImpl event) {

        // Initialize the mappings
        for (int i = 0; i < this.chainMappings.length; i++) {
            int registrySize = BlockStateRegistry.get().size();
            this.chainMappings[i] = new NMSBlockStateMapping[registrySize][];
            this.directMappings[i] = new BlockState[registrySize];
            this.directMappingsAsIds[i] = new int[registrySize];
            Arrays.fill(this.directMappingsAsIds[i], -1);
        }

        // Invert the mapping from id -> lists of mappings to list of mappings -> ids, so that we only have one reference per unique list
        Map<List<NMSBlockStateMapping>, List<IntIntPair>> invertedChainMappings = new HashMap<>();
        for (int awarenessLevelI = 0; awarenessLevelI < event.mappings.length; awarenessLevelI++) {
            for (Int2ObjectMap.Entry<List<NMSBlockStateMapping>> entry : event.mappings[awarenessLevelI].int2ObjectEntrySet()) {
                List<NMSBlockStateMapping> mappings = entry.getValue();
                // Do a check for if the list contains any complex mapping
                boolean containsComplexMapping = false;
                for (NMSBlockStateMapping mapping : mappings) {
                    if (mapping instanceof NMSComplexBlockStateMapping) {
                        containsComplexMapping = true;
                        break;
                    }
                }
                int fromId = entry.getIntKey();
                if (containsComplexMapping) {
                    // If there is a complex mapping, add the list of mappings as a chain
                    // But first we can simplify it a bit by keeping only the last simple mapping in any contiguous subsequence of simple mappings
                    List<NMSBlockStateMapping> optimizedMappings = new ArrayList<>(mappings.size());
                    for (int i = 0; i < mappings.size(); i++) {
                        NMSBlockStateMapping mapping = mappings.get(i);
                        if (i == mappings.size() - 1 || mapping instanceof NMSComplexBlockStateMapping || mappings.get(i + 1) instanceof NMSComplexBlockStateMapping) {
                            optimizedMappings.add(mapping);
                        }
                    }
                    invertedChainMappings.computeIfAbsent(optimizedMappings, $ -> new ArrayList<>()).add(IntIntPair.of(awarenessLevelI, entry.getIntKey()));
                } else {
                    // If there is no complex mapping, add this to the direct mappings
                    BlockState to = ((NMSSimpleBlockStateMapping) mappings.get(mappings.size() - 1)).getTo();;
                    this.directMappings[awarenessLevelI][fromId] = to;
                    this.directMappingsAsIds[awarenessLevelI][fromId] = to.indexInBlockStateRegistry;
                }
            }
        }
        // Invert back
        for (Map.Entry<List<NMSBlockStateMapping>, List<IntIntPair>> entry : invertedChainMappings.entrySet()) {
            for (IntIntPair target : entry.getValue()) {
                this.chainMappings[target.firstInt()][target.secondInt()] = entry.getKey().toArray(NMSBlockStateMapping[]::new);
            }
        }

    }

}

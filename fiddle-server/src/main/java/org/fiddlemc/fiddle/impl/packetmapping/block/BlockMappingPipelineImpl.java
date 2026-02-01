package org.fiddlemc.fiddle.impl.packetmapping.block;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import net.minecraft.world.level.block.state.BlockState;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockStateMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSBlockStateMapping;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSBlockStateMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSComplexBlockStateMapping;
import org.fiddlemc.fiddle.api.packetmapping.block.nms.NMSSimpleBlockStateMapping;
import org.fiddlemc.fiddle.impl.moredatadriven.minecraft.BlockStateRegistry;
import org.fiddlemc.fiddle.impl.util.composable.ComposableImpl;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pipeline of block mappings.
 */
public final class BlockMappingPipelineImpl extends ComposableImpl<BlockMappingPipelineComposeEvent, BlockMappingPipelineComposeEventImpl> implements BlockMappingPipeline {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<BlockMappingPipeline, BlockMappingPipelineImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(BlockMappingPipelineImpl.class);
        }

    }

    public static BlockMappingPipelineImpl get() {
        return (BlockMappingPipelineImpl) BlockMappingPipeline.get();
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

    private BlockMappingPipelineImpl() {
        this.chainMappings = new NMSBlockStateMapping[ClientView.AwarenessLevel.getAll().length][][];
        this.directMappings = new BlockState[ClientView.AwarenessLevel.getAll().length][];
    }

    public BlockState apply(BlockState data, BlockStateMappingContext context) {
        int awarenessLevelI = context.getClientView().getAwarenessLevel().ordinal();
        // If there is a direct mapping, apply it
        @Nullable BlockState directMapping = this.directMappings[awarenessLevelI][data.indexInBlockStateRegistry];
        if (directMapping != null) {
            return directMapping;
        }
        // If there is a mapping chain, apply it
        NMSBlockStateMapping @Nullable [] chain = this.chainMappings[awarenessLevelI][data.indexInBlockStateRegistry];
        if (chain != null) {
            BlockStateMappingHandleImpl handle = new BlockStateMappingHandleImpl(data, context, false);
            for (NMSBlockStateMapping mapping : chain) {
                mapping.apply(handle);
            }
            return handle.getImmutable();
        }
        // No mappings need to be applied
        return data;
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
            this.chainMappings[i] = new NMSBlockStateMapping[BlockStateRegistry.get().size()][];
            this.directMappings[i] = new BlockState[BlockStateRegistry.get().size()];
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
                    this.directMappings[awarenessLevelI][entry.getIntKey()] = ((NMSSimpleBlockStateMapping) mappings.get(mappings.size() - 1)).getTo();
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

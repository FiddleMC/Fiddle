package org.fiddlemc.fiddle.api.packetmapping.block;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.fiddlemc.fiddle.api.clientview.ClientView;

/**
 * Provides functionality to register mappings to the {@link BlockMappingPipeline}.
 *
 * <p>
 * Extended functionality is available by casting to {@code NMSBlockMappingPipelineComposeEvent}.
 * </p>
 *
 * <p>
 * For performance reasons, input should be as specific as possible.
 * This means arrays should be as small as possible.
 * </p>
 *
 * <p>
 * Mapping from {@linkplain BlockType#isAir air} to non-air, or the other way around, will lead to glitches.
 * </p>
 */
public interface BlockMappingPipelineComposeEvent extends LifecycleEvent {

    /**
     * Register a simple mapping.
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param from           The {@link BlockData} to which the mapping applies.
     * @param to             The {@link BlockData} to map to.
     */
    void registerSimple(ClientView.AwarenessLevel awarenessLevel, BlockData from, BlockData to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockData, BlockData)
     */
    void registerSimple(ClientView.AwarenessLevel[] awarenessLevels, BlockData from, BlockData to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockData, BlockData)
     */
    void registerSimple(ClientView.AwarenessLevel awarenessLevel, BlockData[] from, BlockData to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockData, BlockData)
     */
    void registerSimple(ClientView.AwarenessLevel[] awarenessLevels, BlockData[] from, BlockData to);

    /**
     * The same as {@link #registerSimple(ClientView.AwarenessLevel, BlockData, BlockData)},
     * but for each {@link BlockData} of the given {@code from}.
     */
    void registerSimple(ClientView.AwarenessLevel awarenessLevel, BlockType from, BlockData to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockType, BlockData)
     */
    void registerSimple(ClientView.AwarenessLevel[] awarenessLevels, BlockType from, BlockData to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockType, BlockData)
     */
    void registerSimple(ClientView.AwarenessLevel awarenessLevel, BlockType[] from, BlockData to);

    /**
     * @see #registerSimple(ClientView.AwarenessLevel, BlockType, BlockData)
     */
    void registerSimple(ClientView.AwarenessLevel[] awarenessLevels, BlockType[] from, BlockData to);

    /**
     * Calls {@link #registerSimple(ClientView.AwarenessLevel, BlockData, BlockData)}
     * for each matching {@link BlockData} of the {@code from} and {@code to}.
     *
     * <p>
     * This requires that the blocks share all their states.
     * </p>
     */
    void registerStateToState(ClientView.AwarenessLevel awarenessLevel, BlockType from, BlockType to);

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, BlockType, BlockType)
     */
    void registerStateToState(ClientView.AwarenessLevel[] awarenessLevels, BlockType from, BlockType to);

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, BlockType, BlockType)
     */
    void registerStateToState(ClientView.AwarenessLevel awarenessLevel, BlockType[] from, BlockType to);

    /**
     * @see #registerStateToState(ClientView.AwarenessLevel, BlockType, BlockType)
     */
    void registerStateToState(ClientView.AwarenessLevel[] awarenessLevels, BlockType[] from, BlockType to);

}

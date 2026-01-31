package org.fiddlemc.fiddle.api.packetmapping.item.nms;

import net.minecraft.world.item.Item;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingPipelineComposeEvent;

import java.util.List;
import java.util.function.Consumer;

/**
 * Provides functionality to register {@link NMSItemMapping}s
 * to the {@link ItemMappingPipeline}.
 *
 * <p>
 * For performance reasons, input should be as specific as possible.
 * This means arrays should be as small as possible.
 * </p>
 */
public interface NMSItemMappingPipelineComposeEvent extends ItemMappingPipelineComposeEvent {

    /**
     * Registers a mapping.
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param item           The {@link Item} to which the mapping applies.
     * @param mapping        The {@link NMSItemMapping} to register.
     */
    void register(ClientView.AwarenessLevel awarenessLevel, Item item, NMSItemMapping mapping);

    /**
     * @see #register(ClientView.AwarenessLevel, Item, NMSItemMapping)
     */
    void register(ClientView.AwarenessLevel[] awarenessLevels, Item item, NMSItemMapping mapping);

    /**
     * @see #register(ClientView.AwarenessLevel, Item, NMSItemMapping)
     */
    void register(ClientView.AwarenessLevel awarenessLevel, Item[] items, NMSItemMapping mapping);

    /**
     * @see #register(ClientView.AwarenessLevel, Item, NMSItemMapping)
     */
    void register(ClientView.AwarenessLevel[] awarenessLevels, Item[] items, NMSItemMapping mapping);

    /**
     * The same as {@link #register(ClientView.AwarenessLevel, Item, NMSItemMapping)},
     * but for every {@link Item}.
     *
     * <p>
     * This negatively affects performance: try to use
     * {@link #register(ClientView.AwarenessLevel, Item[], NMSItemMapping)} instead.
     * </p>
     */
    void registerForAllItems(ClientView.AwarenessLevel awarenessLevel, NMSItemMapping mapping);

    /**
     * @see #registerForAllItems(ClientView.AwarenessLevel, NMSItemMapping)
     */
    void registerForAllItems(ClientView.AwarenessLevel[] awarenessLevels, NMSItemMapping mapping);

    /**
     * Changes the list of registered mappings for the given awareness level and item.
     *
     * <p>
     * This list is freely mutable.
     * This can be used to remove existing mappings or insert a mapping at the desired index.
     * </p>
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel}.
     * @param item           The {@link Item}.
     * @param consumer       The consumer that applies the desired changes to the list of mappings.
     */
    void changeRegistered(ClientView.AwarenessLevel awarenessLevel, Item item, Consumer<List<NMSItemMapping>> consumer);

}

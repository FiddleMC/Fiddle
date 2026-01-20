package org.fiddlemc.fiddle.minecraft.packet.mapping.item;

import net.minecraft.world.item.Item;
import org.fiddlemc.fiddle.client.ClientView;
import java.util.List;
import java.util.function.Consumer;

/**
 * Provides functionality to register {@link ItemMapping}s.
 */
public interface ItemMappingRegistrar {

    /**
     * Registers a mapping.
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param item           The {@link Item} to which the mapping applies.
     * @param mapping        The {@link ItemMapping} to register.
     */
    void register(ClientView.AwarenessLevel awarenessLevel, Item item, ItemMapping mapping);

    /**
     * Registers a mapping.
     *
     * @param awarenessLevels The {@link ClientView.AwarenessLevel}s to which the mapping applies
     *                        (without duplicates).
     *                        For performance reasons, this array should be made as small as possible.
     * @param item            The {@link Item} to which the mapping applies.
     * @param mapping         The {@link ItemMapping} to register.
     */
    void register(ClientView.AwarenessLevel[] awarenessLevels, Item item, ItemMapping mapping);

    /**
     * Registers a mapping.
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param items          The {@link Item}s to which the mapping applies
     *                       (without duplicates).
     *                       For performance reasons, this array should be made as small as possible.
     * @param mapping        The {@link ItemMapping} to register.
     */
    void register(ClientView.AwarenessLevel awarenessLevel, Item[] items, ItemMapping mapping);

    /**
     * Registers a mapping.
     *
     * @param awarenessLevels The {@link ClientView.AwarenessLevel}s to which the mapping applies
     *                        (without duplicates).
     *                        For performance reasons, this array should be made as small as possible.
     * @param items           The {@link Item}s to which the mapping applies
     *                        (without duplicates).
     *                        For performance reasons, this array should be made as small as possible.
     * @param mapping         The {@link ItemMapping} to register.
     */
    void register(ClientView.AwarenessLevel[] awarenessLevels, Item[] items, ItemMapping mapping);

    /**
     * Registers a mapping that applies to all {@link Item}s.
     * <p>
     * This negatively affects performance: try to use
     * {@link #register(ClientView.AwarenessLevel, Item[], ItemMapping)} instead.
     * </p>
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param mapping        The {@link ItemMapping} to register.
     */
    void registerForAllItems(ClientView.AwarenessLevel awarenessLevel, ItemMapping mapping);

    /**
     * Registers a mapping that applies to all {@link Item}s.
     * <p>
     * This negatively affects performance: try to use
     * {@link #register(ClientView.AwarenessLevel[], Item[], ItemMapping)} instead.
     * </p>
     *
     * @param awarenessLevels The {@link ClientView.AwarenessLevel}s to which the mapping applies.
     *                        For performance reasons, this array should be made as small as possible.
     * @param mapping         The {@link ItemMapping} to register.
     */
    void registerForAllItems(ClientView.AwarenessLevel[] awarenessLevels, ItemMapping mapping);

    /**
     * Changes the list of registered mappings for the given awareness level and item.
     * <p>
     * This list is freely mutable.
     * This can be used to remove existing mappings or insert a mapping at the desired index.
     * </p>
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel}.
     * @param item           The {@link Item}.
     * @param consumer       The consumer that applies the desired changes to the list of mappings.
     */
    void changeRegistered(ClientView.AwarenessLevel awarenessLevel, Item item, Consumer<List<ItemMapping>> consumer);

}

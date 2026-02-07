package org.fiddlemc.fiddle.api.packetmapping.item;

import org.bukkit.Registry;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.packetmapping.AwarenessLevelMappingBuilder;
import org.fiddlemc.fiddle.api.util.composable.FromBuilder;
import org.fiddlemc.fiddle.api.util.composable.FunctionBuilder;
import org.fiddlemc.fiddle.api.util.composable.ToBuilder;

/**
 * A builder to define an item mapping.
 */
public interface ItemMappingBuilder extends AwarenessLevelMappingBuilder, FromBuilder<ItemType>, ToBuilder<ItemType>, FunctionBuilder<ItemMappingHandle> {

    /**
     * Sets this builder to target all items.
     *
     * <p>
     * This negatively affects performance: try to target specific items instead.
     * </p>
     */
    default void fromAllItems() {
        this.from(Registry.ITEM.stream().toList());
    }

}

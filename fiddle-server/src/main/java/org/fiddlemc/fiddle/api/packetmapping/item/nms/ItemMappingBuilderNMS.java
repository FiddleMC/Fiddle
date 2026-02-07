package org.fiddlemc.fiddle.api.packetmapping.item.nms;

import net.minecraft.world.item.Item;
import org.fiddlemc.fiddle.api.packetmapping.AwarenessLevelMappingBuilder;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingBuilder;
import org.fiddlemc.fiddle.api.util.composable.FromBuilder;
import org.fiddlemc.fiddle.api.util.composable.FunctionBuilder;
import org.fiddlemc.fiddle.api.util.composable.ToBuilder;
import org.fiddlemc.fiddle.impl.moredatadriven.minecraft.ItemRegistry;

/**
 * An alternative to {@link ItemMappingBuilder} that uses Minecraft internals.
 */
public interface ItemMappingBuilderNMS extends AwarenessLevelMappingBuilder, FromBuilder<Item>, ToBuilder<Item>, FunctionBuilder<ItemMappingHandleNMS> {

    /**
     * Sets this builder to target all items.
     *
     * <p>
     * This negatively affects performance: try to target specific items instead.
     * </p>
     */
    default void fromAllItems() {
        this.from(ItemRegistry.get().stream().toList());
    }

}

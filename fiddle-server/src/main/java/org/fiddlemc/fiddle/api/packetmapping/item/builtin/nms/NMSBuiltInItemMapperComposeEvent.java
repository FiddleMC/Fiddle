package org.fiddlemc.fiddle.api.packetmapping.item.builtin.nms;

import net.minecraft.world.item.Item;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.builtin.BuiltInItemMapperComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.ItemMappingUtilitiesNMS;

/**
 * Extends {@link BuiltInItemMapperComposeEvent} with NMS support.
 *
 * <p>
 * For performance reasons, input should be as specific as possible.
 * This means arrays should be as small as possible.
 * </p>
 */
public interface NMSBuiltInItemMapperComposeEvent extends BuiltInItemMapperComposeEvent {

    /**
     * Adds a mapping that maps the given {@code from} {@link Item} to the given {@code to} {@link Item},
     * using {@link ItemMappingUtilitiesNMS#setItemWhilePreservingRest}.
     */
    void mapItem(ClientView.AwarenessLevel awarenessLevel, Item from, Item to);

    /**
     * @see #mapItem(ClientView.AwarenessLevel, ItemType, ItemType)
     */
    default void mapItem(ClientView.AwarenessLevel[] awarenessLevels, Item from, Item to) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            this.mapItem(awarenessLevel, from, to);
        }
    }

    /**
     * The same as {@link #mapItem(ClientView.AwarenessLevel, Item, Item)},
     * but for {@link ClientView.AwarenessLevel#getThatDoNotAlwaysUnderstandsAllServerSideItems()}.
     */
    default void mapItem(Item from, Item to) {
        this.mapItem(ClientView.AwarenessLevel.getThatDoNotAlwaysUnderstandsAllServerSideItems(), from, to);
    }

    @Override
    default void mapItem(ClientView.AwarenessLevel awarenessLevel, ItemType from, ItemType to) {
        this.mapItem(awarenessLevel, CraftItemType.bukkitToMinecraftNew(from), CraftItemType.bukkitToMinecraftNew(to));
    }

}

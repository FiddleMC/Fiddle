package org.fiddlemc.fiddle.api.packetmapping.item.builtin;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.clientview.ClientView;

/**
 * Provides functionality to customize mappings performed by the {@link BuiltInItemMapper}.
 *
 * <p>
 * Extended functionality is available by casting to {@code NMSBuiltInItemMapperComposeEvent}.
 * </p>
 *
 * <p>
 * For performance reasons, input should be as specific as possible.
 * This means arrays should be as small as possible.
 * </p>
 */
public interface BuiltInItemMapperComposeEvent extends LifecycleEvent {

    /**
     * Adds a mapping that maps the given {@code from} {@link ItemType} to the given {@code to} {@link ItemType}.
     *
     * @see <code>NMSBuiltInItemMapperComposeEvent#mapItem</code>
     */
    void mapItem(ClientView.AwarenessLevel awarenessLevel, ItemType from, ItemType to);

    /**
     * @see #mapItem(ClientView.AwarenessLevel, ItemType, ItemType)
     */
    default void mapItem(ClientView.AwarenessLevel[] awarenessLevels, ItemType from, ItemType to) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            this.mapItem(awarenessLevel, from, to);
        }
    }

    /**
     * The same as {@link #mapItem(ClientView.AwarenessLevel, ItemType, ItemType)},
     * but for {@link ClientView.AwarenessLevel#getThatDoNotAlwaysUnderstandsAllServerSideItems()}.
     */
    default void mapItem(ItemType from, ItemType to) {
        this.mapItem(ClientView.AwarenessLevel.getThatDoNotAlwaysUnderstandsAllServerSideItems(), from, to);
    }

}

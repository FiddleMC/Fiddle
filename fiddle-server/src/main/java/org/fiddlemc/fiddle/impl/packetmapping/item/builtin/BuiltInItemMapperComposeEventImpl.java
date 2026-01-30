package org.fiddlemc.fiddle.impl.packetmapping.item.builtin;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import net.minecraft.world.item.Item;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.item.builtin.BuiltInItemMapperComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.item.builtin.nms.NMSBuiltInItemMapperComposeEvent;

/**
 * The implementation of {@link BuiltInItemMapperComposeEvent}
 */
public final class BuiltInItemMapperComposeEventImpl implements PaperLifecycleEvent, NMSBuiltInItemMapperComposeEvent {

    private final BuiltInItemMapperImpl mapper;

    BuiltInItemMapperComposeEventImpl(BuiltInItemMapperImpl mapper) {
        this.mapper = mapper;
    }

    @Override
    public void mapItem(final ClientView.AwarenessLevel awarenessLevel, final Item from, final Item to) {
        this.mapper.mappedItems[awarenessLevel.ordinal()].put(from, to);
    }

}

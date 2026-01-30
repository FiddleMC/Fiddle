package org.fiddlemc.fiddle.api.packetmapping.item.builtin;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;

/**
 * Provides functionality to customize mappings performed by the {@link BuiltInItemMapper}.
 *
 * <p>
 * Currently, this must be cast to {@code NMSBuiltInItemMapperComposeEvent} to be used.
 * </p>
 */
public interface BuiltInItemMapperComposeEvent extends LifecycleEvent {
}

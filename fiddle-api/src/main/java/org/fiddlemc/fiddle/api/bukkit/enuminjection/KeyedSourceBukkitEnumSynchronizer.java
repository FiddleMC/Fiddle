package org.fiddlemc.fiddle.api.bukkit.enuminjection;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.handler.configuration.PrioritizedLifecycleEventHandlerConfiguration;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import org.bukkit.NamespacedKey;

/**
 * A {@link BukkitEnumSynchronizer} for which the source value has a {@link NamespacedKey},
 * and which bases the enum name on that key.
 */
public interface KeyedSourceBukkitEnumSynchronizer<E extends Enum<E>, T> extends BukkitEnumSynchronizer<E, T> {

    /**
     * @return The {@link LifecycleEventType} for the {@link DetermineEnumNameEvent}.
     */
    LifecycleEventType<BootstrapContext, DetermineEnumNameEvent<T>, PrioritizedLifecycleEventHandlerConfiguration<BootstrapContext>> determineEnumNameEventType();

    /**
     * A {@link LifecycleEvent} that fires when an enum name must be determined for a newly injected enum value.
     *
     * <p>
     * The resulting enum name can be changed by handlers of this event.
     * </p>
     */
    interface DetermineEnumNameEvent<T> extends LifecycleEvent {

        /**
         * @return The source value for which a new enum value is being injected.
         */
        T getSourceValue();

        /**
         * @return The originally determined enum name, before any other handlers of this event
         * made any changes.
         */
        String getOriginallyDeterminedEnumName();

        /**
         * @return The determined enum name, which may have been changed by other handlers of this event.
         */
        String getDeterminedEnumName();

        /**
         * Changes the enum name that will be used.
         *
         * <p>
         * Don't use this unless you know what you are doing.
         * The default name for enums is {@code FIDDLE_<namespace>_<key>}, for example
         * {@code willow_trees:willow_log} will become {@code FIDDLE_WILLOW_TREES_WILLOW_LOG}.
         * With this naming style, it is very unlikely that any issues will come up now or in the future.
         * Other naming styles may lead to collisions or other plugins parsing strings incorrectly.
         * </p>
         *
         * @param enumName The enum name to use.
         */
        void setDeterminedEnumName(String enumName);

    }

}

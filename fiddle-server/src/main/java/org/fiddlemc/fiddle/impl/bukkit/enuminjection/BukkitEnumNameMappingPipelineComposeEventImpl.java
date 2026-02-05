package org.fiddlemc.fiddle.impl.bukkit.enuminjection;

import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNamePickHandle;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNamesComposeEvent;
import org.fiddlemc.fiddle.api.util.mappingpipeline.SingleStepMapping;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * The implementation of {@link BukkitEnumNamesComposeEvent}.
 */
public final class BukkitEnumNameMappingPipelineComposeEventImpl<S> implements PaperLifecycleEvent, BukkitEnumNamesComposeEvent<S> {

    /**
     * The registered mappings,
     * or null if no mappings are registered.
     */
    @Nullable List<SingleStepMapping<BukkitEnumNamePickHandle<S>>> mappings;

    @Override
    public void register(final SingleStepMapping<BukkitEnumNamePickHandle<S>> mapping) {
        if (this.mappings == null) {
            this.mappings = new ArrayList<>(1);
        }
        this.mappings.add(mapping);
    }

    @Override
    public void changeRegistered(final Consumer<List<SingleStepMapping<BukkitEnumNamePickHandle<S>>>> consumer) {
        List<SingleStepMapping<BukkitEnumNamePickHandle<S>>> passed = this.mappings != null ? this.mappings : new ArrayList<>(1);
        consumer.accept(passed);
        if (passed.isEmpty() && this.mappings != null) {
            this.mappings = null;
        } else if (!passed.isEmpty() && this.mappings == null) {
            this.mappings = passed;
        }
    }

}

package org.fiddlemc.fiddle.impl.util.mappingpipeline;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.PrioritizableLifecycleEventType;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineComposeEventType;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MappingPipelineRegistrar;

/**
 * A base implementation of {@link MappingPipelineComposeEventType}.
 */
public class MappingPipelineComposeEventTypeImpl<R extends MappingPipelineRegistrar> extends PrioritizableLifecycleEventType.Simple<BootstrapContext, MappingPipelineComposeEvent<R>> implements MappingPipelineComposeEventType<R> {

    public MappingPipelineComposeEventTypeImpl(final String name) {
        super(name, BootstrapContext.class);
    }

}

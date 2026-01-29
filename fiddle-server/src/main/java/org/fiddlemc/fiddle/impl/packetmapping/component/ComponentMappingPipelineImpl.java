package org.fiddlemc.fiddle.impl.packetmapping.component;

import io.papermc.paper.adventure.AdventureComponent;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.chat.Component;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.ClientViewMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingRegistrar;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMapping;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMappingHandle;
import org.fiddlemc.fiddle.impl.packetmapping.WithClientViewContextSingleStepMappingPipeline;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.MappingPipelineImpl;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.jspecify.annotations.Nullable;

/**
 * A pipeline of {@link NMSComponentMapping}s.
 */
public final class ComponentMappingPipelineImpl extends MappingPipelineImpl.Simple<ComponentMappingRegistrar, ComponentMappingRegistrarImpl> implements WithClientViewContextSingleStepMappingPipeline.Simple<Component, NMSComponentMappingHandle, ComponentMappingRegistrar>, ComponentMappingPipeline {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<ComponentMappingPipeline, ComponentMappingPipelineImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(ComponentMappingPipelineImpl.class);
        }

    }

    public static ComponentMappingPipelineImpl get() {
        return (ComponentMappingPipelineImpl) ComponentMappingPipeline.get();
    }

    @Override
    protected String getEventTypeNamePrefix() {
        return "fiddle_component_mapping_pipeline";
    }

    /**
     * The registered mappings.
     *
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()} is the index.
     * </p>
     */
    private final NMSComponentMapping[][] mappings;

    private ComponentMappingPipelineImpl() {
        this.mappings = new NMSComponentMapping[ClientView.AwarenessLevel.values().length][];
    }

    @Override
    public NMSComponentMapping @Nullable [] getMappingsThatMayApplyTo(NMSComponentMappingHandle handle) {
        return this.mappings[handle.getContext().getClientView().getAwarenessLevel().ordinal()];
    }

    @Override
    public NMSComponentMappingHandle createHandle(Component data, ClientViewMappingContext context) {
        return new ComponentMappingHandleImpl(data, context);
    }

    @Override
    public Component apply(Component data, ClientViewMappingContext context) {
        return WithClientViewContextSingleStepMappingPipeline.Simple.super.apply(data instanceof AdventureComponent adventureComponent ? adventureComponent.deepConverted() : data, context);
    }

    @Override
    protected ComponentMappingRegistrarImpl createRegistrar() {
        return new ComponentMappingRegistrarImpl();
    }

    @Override
    public void copyMappingsFrom(ComponentMappingRegistrarImpl registrar) {
        Map<List<NMSComponentMapping>, IntList> transposed = new HashMap<>();
        for (int awarenessLevelI = 0; awarenessLevelI < registrar.mappings.length; awarenessLevelI++) {
            transposed.computeIfAbsent(registrar.mappings[awarenessLevelI], $ -> new IntArrayList()).add(awarenessLevelI);
        }
        for (Map.Entry<List<NMSComponentMapping>, IntList> entry : transposed.entrySet()) {
            for (int target : entry.getValue()) {
                this.mappings[target] = entry.getKey().toArray(NMSComponentMapping[]::new);
            }
        }
    }

}

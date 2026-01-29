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
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingPipelineRegistrar;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMapping;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMappingHandle;
import org.fiddlemc.fiddle.impl.packetmapping.WithClientViewContextSingleStepMappingPipeline;
import org.fiddlemc.fiddle.impl.packetmapping.component.translatable.ServerSideTranslationComponentMapping;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.MappingPipelineImpl;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.jspecify.annotations.Nullable;

/**
 * A pipeline of {@link NMSComponentMapping}s.
 */
public final class ComponentMappingPipelineImpl extends MappingPipelineImpl.Simple<ComponentMappingPipelineRegistrar, ComponentMappingPipelineRegistrarImpl> implements WithClientViewContextSingleStepMappingPipeline.Simple<Component, NMSComponentMappingHandle, ComponentMappingPipelineRegistrar>, ComponentMappingPipeline {

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
        return new ComponentMappingHandleImpl(data, context, false);
    }

    @Override
    public Component apply(Component data, ClientViewMappingContext context) {
        return WithClientViewContextSingleStepMappingPipeline.Simple.super.apply(data instanceof AdventureComponent adventureComponent ? adventureComponent.deepConverted() : data, context);
    }

    @Override
    protected ComponentMappingPipelineRegistrarImpl createRegistrar() {

        // Create the registrar
        ComponentMappingPipelineRegistrarImpl registrar = new ComponentMappingPipelineRegistrarImpl();

        // Register the server-side translation mapping
        registrar.register(ClientView.AwarenessLevel.getThatDoNotAlwaysUnderstandsAllServerSideTranslatables(), new ServerSideTranslationComponentMapping());

        // Return the registrar
        return registrar;

    }

    @Override
    public void copyMappingsFrom(ComponentMappingPipelineRegistrarImpl registrar) {
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

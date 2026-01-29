package org.fiddlemc.fiddle.impl.packetmapping.component;

import io.papermc.paper.adventure.AdventureComponent;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingContext;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingHandle;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMapping;
import org.fiddlemc.fiddle.impl.java.util.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.fiddlemc.fiddle.impl.packetmapping.PacketDataMappingPipelineImpl;
import org.jspecify.annotations.Nullable;

/**
 * A pipeline of {@link NMSComponentMapping}s.
 */
public final class ComponentMappingPipelineImpl extends PacketDataMappingPipelineImpl<Component, ComponentMappingHandle<Component, MutableComponent>, ComponentMappingContext, NMSComponentMapping, ComponentMappingRegistrarImpl> implements ComponentMappingPipeline<Component, MutableComponent, ComponentMappingHandle<Component, MutableComponent>, ComponentMappingContext, NMSComponentMapping, ComponentMappingRegistrarImpl> {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<ComponentMappingPipeline<Component, MutableComponent, ComponentMappingHandle<Component, MutableComponent>, ComponentMappingContext, NMSComponentMapping, ComponentMappingRegistrarImpl>, ComponentMappingPipelineImpl> implements ServiceProvider<Component, MutableComponent, ComponentMappingHandle<Component, MutableComponent>, ComponentMappingContext, NMSComponentMapping, ComponentMappingRegistrarImpl> {

        public ServiceProviderImpl() {
            super(ComponentMappingPipelineImpl.class);
        }

    }

    public static ComponentMappingPipelineImpl get() {
        return (ComponentMappingPipelineImpl) ComponentMappingPipeline.get();
    }

    @Override
    protected String getEventTypeNamePrefix() {
        return "fiddle_component_mapping";
    }

    private final class ComposeEventImpl extends PacketDataMappingPipelineImpl<Component, ComponentMappingHandle<Component, MutableComponent>, ComponentMappingContext, NMSComponentMapping, ComponentMappingRegistrarImpl>.ComposeEventImpl {

        public ComposeEventImpl(ComponentMappingRegistrarImpl registrar) {
            super(registrar);
        }

        @Override
        public void invalidate() {

            // Bake the mappings
            // TODO
            Map<List<NMSComponentMapping>, IntList> transposed = new HashMap<>();
            for (int awarenessLevelI = 0; awarenessLevelI < this.registrar.mappings.length; awarenessLevelI++) {
                transposed.computeIfAbsent(this.registrar.mappings[awarenessLevelI], $ -> new IntArrayList()).add(awarenessLevelI);
            }
            for (Map.Entry<List<NMSComponentMapping>, IntList> entry : transposed.entrySet()) {
                for (int target : entry.getValue()) {
                    ComponentMappingPipelineImpl.this.mappings[target] = entry.getKey().toArray(NMSComponentMapping[]::new);
                }
            }

            // Continue with invalidation
            super.invalidate();

        }

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
    public NMSComponentMapping @Nullable [] getMappingsThatMayApplyTo(Component data, ComponentMappingContext context) {
        return this.mappings[context.getClientView().getAwarenessLevel().ordinal()];
    }

    @Override
    public ComponentMappingHandleImpl createHandle(Component data) {
        return new ComponentMappingHandleImpl(data);
    }

    @Override
    public Component apply(final Component data, final ComponentMappingContext context) {
        return super.apply(data instanceof AdventureComponent adventureComponent ? adventureComponent.deepConverted() : data, context);
    }

    @Override
    protected ComponentMappingContextImpl createGenericContext(ClientView clientView) {
        return new ComponentMappingContextImpl(clientView);
    }

    @Override
    protected ComponentMappingRegistrarImpl createRegistrar() {
        return new ComponentMappingRegistrarImpl();
    }

    @Override
    protected <CE extends ComposeEvent<Component, ComponentMappingRegistrarImpl> & PaperLifecycleEvent> CE createComposeEvent() {
        return (CE) new ComposeEventImpl(this.createRegistrar());
    }

}

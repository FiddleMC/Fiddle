package org.fiddlemc.fiddle.impl.packetmapping.component;

import io.papermc.paper.adventure.AdventureComponent;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.chat.Component;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.WithClientViewMappingFunctionContext;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappings;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingsComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentTarget;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.ComponentMappingHandleNMS;
import org.fiddlemc.fiddle.impl.packetmapping.WithClientViewContextSingleStepMappingPipeline;
import org.fiddlemc.fiddle.impl.packetmapping.component.translatable.ServerSideTranslationComponentMapping;
import org.fiddlemc.fiddle.impl.util.composable.ComposableImpl;
import org.fiddlemc.fiddle.impl.util.java.serviceloader.NoArgsConstructorServiceProviderImpl;
import org.jspecify.annotations.Nullable;

/**
 * A pipeline of component mappings.
 */
public final class ComponentMappingsImpl extends ComposableImpl<ComponentMappingsComposeEvent<ComponentMappingsStep>, ComponentMappingsComposeEventImpl> implements WithClientViewContextSingleStepMappingPipeline.Simple<Component, ComponentMappingHandleNMS>, ComponentMappings<ComponentMappingsStep> {

    public static final class ServiceProviderImpl extends NoArgsConstructorServiceProviderImpl<ComponentMappings<?>, ComponentMappingsImpl> implements ServiceProvider {

        public ServiceProviderImpl() {
            super(ComponentMappingsImpl.class);
        }

    }

    public static ComponentMappingsImpl get() {
        return (ComponentMappingsImpl) ComponentMappings.get();
    }

    @Override
    protected String getEventTypeNamePrefix() {
        return "fiddle_component_mappings";
    }

    /**
     * The registered steps.
     *
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in an array where {@link ComponentTarget#ordinal()} is the index.
     * The lowest-level array may be null, but will never be empty.
     * </p>
     */
    private final ComponentMappingsStep[][][] mappings;

    private ComponentMappingsImpl() {
        this.mappings = new ComponentMappingsStep[ClientView.AwarenessLevel.getAll().length][][];
    }

    @Override
    public ComponentMappingsStep @Nullable [] getStepsThatMayApplyTo(ComponentMappingHandleNMS handle) {
        return this.mappings[handle.getContext().getClientView().getAwarenessLevel().ordinal()][ComponentTargetUtil.getMostSpecificTarget(handle.getOriginal())];
    }

    @Override
    public ComponentMappingHandleNMS createHandle(Component data, WithClientViewMappingFunctionContext context) {
        return new ComponentMappingHandleNMSImpl(data, context, false);
    }

    @Override
    public Component apply(Component data, WithClientViewMappingFunctionContext context) {
        return WithClientViewContextSingleStepMappingPipeline.Simple.super.apply(data instanceof AdventureComponent adventureComponent ? adventureComponent.deepConverted() : data, context);
    }

    @Override
    protected ComponentMappingsComposeEventImpl createComposeEvent() {
        // Create the event
        ComponentMappingsComposeEventImpl event = new ComponentMappingsComposeEventImpl();
        // Register the server-side translation mapping
        event.register(ClientView.AwarenessLevel.getThatDoNotAlwaysUnderstandsAllServerSideTranslatables(), new ServerSideTranslationComponentMapping());
        // Return the event
        return event;
    }

    @Override
    protected void copyInformationFromEvent(ComponentMappingsComposeEventImpl event) {
        Map<List<NMSComponentMapping>, IntList> transposed = new HashMap<>();
        for (int awarenessLevelI = 0; awarenessLevelI < event.mappings.length; awarenessLevelI++) {
            transposed.computeIfAbsent(event.mappings[awarenessLevelI], $ -> new IntArrayList()).add(awarenessLevelI);
        }
        for (Map.Entry<List<NMSComponentMapping>, IntList> entry : transposed.entrySet()) {
            for (int target : entry.getValue()) {
                this.mappings[target] = entry.getKey().toArray(NMSComponentMapping[]::new);
            }
        }
    }

}

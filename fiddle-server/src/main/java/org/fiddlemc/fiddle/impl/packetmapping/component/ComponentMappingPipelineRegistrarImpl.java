package org.fiddlemc.fiddle.impl.packetmapping.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMapping;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMappingPipelineRegistrar;

/**
 * The implementation of {@link NMSComponentMappingPipelineRegistrar}.
 */
public final class ComponentMappingPipelineRegistrarImpl implements NMSComponentMappingPipelineRegistrar {

    /**
     * The registered mappings.
     *
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()} is the index.
     * </p>
     */
    final List<NMSComponentMapping>[] mappings;

    public ComponentMappingPipelineRegistrarImpl() {
        this.mappings = new List[ClientView.AwarenessLevel.values().length];
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new ArrayList<>(1);
        }
    }

    @Override
    public void register(ClientView.AwarenessLevel awarenessLevel, NMSComponentMapping mapping) {
        this.getForAwarenessLevel(awarenessLevel).add(mapping);
    }

    @Override
    public void register(ClientView.AwarenessLevel[] awarenessLevels, NMSComponentMapping mapping) {
        for (ClientView.AwarenessLevel awarenessLevel : awarenessLevels) {
            this.register(awarenessLevel, mapping);
        }
    }

    @Override
    public void changeRegistered(ClientView.AwarenessLevel awarenessLevel, Consumer<List<NMSComponentMapping>> consumer) {
        consumer.accept(this.getForAwarenessLevel(awarenessLevel));
    }

    private List<NMSComponentMapping> getForAwarenessLevel(ClientView.AwarenessLevel awarenessLevel) {
        return this.mappings[awarenessLevel.ordinal()];
    }

}

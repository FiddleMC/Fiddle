package org.fiddlemc.fiddle.api.packetmapping.component.nms;

import java.util.List;
import java.util.function.Consumer;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingRegistrar;

/**
 * Provides functionality to register {@link NMSComponentMapping}s
 * to the {@link ComponentMappingPipeline}.
 */
public interface NMSComponentMappingRegistrar extends ComponentMappingRegistrar {

    /**
     * Registers a mapping.
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel} to which the mapping applies.
     * @param mapping        The {@link NMSComponentMapping} to register.
     */
    void register(ClientView.AwarenessLevel awarenessLevel, NMSComponentMapping mapping);

    /**
     * Registers a mapping.
     *
     * @param awarenessLevels The {@link ClientView.AwarenessLevel}s to which the mapping applies
     *                        (without duplicates).
     *                        For performance reasons, this array should be made as small as possible.
     * @param mapping         The {@link NMSComponentMapping} to register.
     */
    void register(ClientView.AwarenessLevel[] awarenessLevels, NMSComponentMapping mapping);

    /**
     * Changes the list of registered mappings for the given awareness level.
     *
     * <p>
     * This list is freely mutable.
     * This can be used to remove existing mappings or insert a mapping at the desired index.
     * </p>
     *
     * @param awarenessLevel The {@link ClientView.AwarenessLevel}.
     * @param consumer       The consumer that applies the desired changes to the list of mappings.
     */
    void changeRegistered(ClientView.AwarenessLevel awarenessLevel, Consumer<List<NMSComponentMapping>> consumer);

}

package org.fiddlemc.fiddle.api.packetmapping.block;

/**
 * An abstract mapping for block states, where the mapping is applied by running code.
 */
public interface ComplexBlockStateMapping {

    /**
     * @return Whether this mapping requires the coordinates
     * ({@link BlockStateMappingFunctionContext#getPhysicalBlockX()} and so on).
     */
    boolean requiresCoordinates();

}

package org.fiddlemc.fiddle.api.packetmapping.block;

/**
 * An abstract mapping for block states, where the mapping is applied by running code.
 */
public interface ComplexBlockStateMapping {

    /**
     * @return Whether this mapping requires the coordinates
     * ({@link BlockStateMappingContext#getPhysicalBlockX()} and so on).
     */
    boolean requiresCoordinates();

}

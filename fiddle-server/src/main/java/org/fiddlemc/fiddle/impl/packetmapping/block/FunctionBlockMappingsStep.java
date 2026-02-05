package org.fiddlemc.fiddle.impl.packetmapping.block;

/**
 * A {@link BlockMappingsStep} that is defined by a function.
 */
public sealed interface FunctionBlockMappingsStep extends BlockMappingsStep permits MinecraftFunctionBlockMappingsStep, BukkitFunctionBlockMappingsStep {

    boolean requiresCoordinates();

}

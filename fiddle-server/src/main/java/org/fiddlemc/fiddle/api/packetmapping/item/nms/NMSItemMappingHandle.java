package org.fiddlemc.fiddle.api.packetmapping.item.nms;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingFunctionContext;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MutableMappingFunctionHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithContextMappingFunctionHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithOriginalMappingFunctionHandle;

/**
 * A handle provided to {@link NMSItemMapping}s.
 */
public interface NMSItemMappingHandle extends WithContextMappingFunctionHandle<ItemStack, ItemMappingFunctionContext>, WithOriginalMappingFunctionHandle<ItemStack>, MutableMappingFunctionHandle<ItemStack, ItemStack> {
}

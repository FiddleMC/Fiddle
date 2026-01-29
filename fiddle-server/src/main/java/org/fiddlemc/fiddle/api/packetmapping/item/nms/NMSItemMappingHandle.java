package org.fiddlemc.fiddle.api.packetmapping.item.nms;

import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingContext;
import org.fiddlemc.fiddle.api.util.mappingpipeline.MutableMappingHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithContextMappingHandle;
import org.fiddlemc.fiddle.api.util.mappingpipeline.WithOriginalMappingHandle;

/**
 * A handle provided to {@link NMSItemMapping}s.
 */
public interface NMSItemMappingHandle extends WithContextMappingHandle<ItemStack, ItemMappingContext>, WithOriginalMappingHandle<ItemStack>, MutableMappingHandle<ItemStack, ItemStack> {
}

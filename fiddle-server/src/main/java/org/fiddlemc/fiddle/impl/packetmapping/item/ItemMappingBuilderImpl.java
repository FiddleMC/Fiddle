package org.fiddlemc.fiddle.impl.packetmapping.item;

import java.util.Collection;
import net.minecraft.world.item.Item;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingBuilder;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingHandle;

/**
 * The implementation of {@link ItemMappingBuilder}.
 */
public class ItemMappingBuilderImpl extends AbstractItemMappingBuilderImpl<ItemType, ItemMappingHandle> implements ItemMappingBuilder {

    @Override
    protected Collection<ItemType> getItemsToRegisterFor() {
        return this.from;
    }

    @Override
    protected ItemMappingsStep createFunctionStep() {
        return new BukkitFunctionItemMappingsStep(this.function);
    }

    @Override
    protected Item getSimpleTo() {
        return ((CraftItemType<?>) this.to).getHandle();
    }

}

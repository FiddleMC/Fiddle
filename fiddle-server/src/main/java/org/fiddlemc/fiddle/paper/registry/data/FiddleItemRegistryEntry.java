package org.fiddlemc.fiddle.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of {@link ItemRegistryEntry}.
 */
public class FiddleItemRegistryEntry implements ItemRegistryEntry {

    public FiddleItemRegistryEntry(
        final Conversions conversions,
        final @Nullable Item internal
    ) {
        if (internal == null) {
            return;
        }

        //TODO
    }

    public static final class FiddleBuilder extends FiddleItemRegistryEntry implements Builder, PaperRegistryBuilder<Item, ItemType> {

        public FiddleBuilder(final Conversions conversions, final @Nullable Item internal) {
            super(conversions, internal);
        }

        @Override
        public Item build() {
            return new Item(null); // TODO
        }

    }

}

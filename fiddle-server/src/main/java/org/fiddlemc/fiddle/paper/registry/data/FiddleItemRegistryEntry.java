package org.fiddlemc.fiddle.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of {@link ItemRegistryEntry}.
 */
public class FiddleItemRegistryEntry  implements ItemRegistryEntry, KeyAwareRegistryEntry {

    private @Nullable Identifier key;

    public FiddleItemRegistryEntry(
        final Conversions conversions,
        final @Nullable Item internal
    ) {
        if (internal == null) {
            return;
        }
    }

    @Override
    public Identifier getKey()  {
        return this.key;
    }

    @Override
    public void setKey(Identifier key) {
        this.key = key;
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

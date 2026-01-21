package org.fiddlemc.fiddle.impl.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.paper.registry.data.ItemRegistryEntry;
import org.fiddlemc.fiddle.api.paper.registry.data.NMSItemRegistryEntryBuilder;
import org.fiddlemc.fiddle.impl.minecraft.registries.BlockRegistry;
import org.jspecify.annotations.Nullable;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The implementation of {@link ItemRegistryEntry}.
 */
public abstract class FiddleItemRegistryEntry implements ItemRegistryEntry, SettableKeyAwareRegistryEntry {

    protected @Nullable Identifier key;

    public FiddleItemRegistryEntry(
        final Conversions conversions,
        final @Nullable Item internal
    ) {
        if (internal == null) {
            return;
        }
    }

    @Override
    public Identifier getKey() {
        return this.key;
    }

    public static final class FiddleBuilder extends FiddleItemRegistryEntry implements NMSItemRegistryEntryBuilder, PaperRegistryBuilder<Item, ItemType> {

        private @Nullable Function<Item.Properties, Item> nmsFactory;
        private Item.@Nullable Properties nmsProperties;

        public FiddleBuilder(final Conversions conversions, final @Nullable Item internal) {
            super(conversions, internal);
        }

        @Override
        public void setKey(Identifier key) {
            this.key = key;
        }

        @Override
        public void nmsFactory(Function<Item.Properties, Item> factory) {
            this.nmsFactory = factory;
        }

        @Override
        public void nmsFactoryForBlock() {
            this.nmsFactoryForBlock(BlockItem::new);
        }

        @Override
        public void nmsFactoryForBlock(Identifier blockIdentifier) {
            this.nmsFactoryForBlock(blockIdentifier);
        }

        @Override
        public void nmsFactoryForBlock(BiFunction<Block, Item.Properties, BlockItem> factory) {
            this.nmsFactoryForBlock(this.key, factory);
        }

        @Override
        public void nmsFactoryForBlock(Identifier blockIdentifier, BiFunction<Block, Item.Properties, BlockItem> factory) {
            this.nmsFactory = properties -> factory.apply(BlockRegistry.get().getValue(blockIdentifier), properties);
        }

        @Override
        public void nmsProperties(Item.Properties properties) {
            this.nmsProperties = properties;
        }

        @Override
        public void nmsProperties(Consumer<Item.Properties> properties) {
            if (this.nmsProperties == null) {
                this.nmsProperties = new Item.Properties();
            }
            properties.accept(this.nmsProperties);
        }

        @Override
        public Item build() {
            Function<Item.Properties, Item> factory = this.nmsFactory != null ? this.nmsFactory : Item::new;
            Item.Properties properties = this.nmsProperties != null ? this.nmsProperties : new Item.Properties();
            properties.setId(ResourceKey.create(Registries.ITEM, this.key));
            return factory.apply(properties);
        }

    }

}

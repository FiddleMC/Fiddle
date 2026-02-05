package org.fiddlemc.fiddle.impl.moredatadriven.paper.registry;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.moredatadriven.paper.registry.ItemRegistryEntry;
import org.fiddlemc.fiddle.api.moredatadriven.paper.nms.ItemRegistryEntryBuilderNMS;
import org.fiddlemc.fiddle.impl.moredatadriven.minecraft.BlockRegistry;
import org.jspecify.annotations.Nullable;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The implementation of {@link ItemRegistryEntry}.
 */
public abstract class ItemRegistryEntryImpl implements ItemRegistryEntry, SettableKeyAwareRegistryEntryNMS {

    protected @Nullable Identifier key;

    public ItemRegistryEntryImpl(
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

    public static final class BuilderImpl extends ItemRegistryEntryImpl implements ItemRegistryEntryBuilderNMS, PaperRegistryBuilder<Item, ItemType> {

        private @Nullable Function<Item.Properties, Item> nmsFactory;
        private Item.@Nullable Properties nmsProperties;

        public BuilderImpl(final Conversions conversions, final @Nullable Item internal) {
            super(conversions, internal);
        }

        @Override
        public void setKey(Identifier key) {
            this.key = key;
        }

        @Override
        public void factoryNMS(Function<Item.Properties, Item> factory) {
            this.nmsFactory = factory;
        }

        @Override
        public void factoryForBlockNMS() {
            this.factoryForBlockNMS(BlockItem::new);
        }

        @Override
        public void factoryForBlockNMS(Identifier blockIdentifier) {
            this.factoryForBlockNMS(blockIdentifier);
        }

        @Override
        public void factoryForBlockNMS(BiFunction<Block, Item.Properties, BlockItem> factory) {
            this.factoryForBlockNMS(this.key, factory);
        }

        @Override
        public void factoryForBlockNMS(Identifier blockIdentifier, BiFunction<Block, Item.Properties, BlockItem> factory) {
            this.factoryNMS(properties -> factory.apply(BlockRegistry.get().getValue(blockIdentifier), properties));
            this.propertiesNMS(Item.Properties::useBlockDescriptionPrefix);
        }

        @Override
        public void propertiesNMS(Item.Properties properties) {
            this.nmsProperties = properties;
        }

        @Override
        public void propertiesNMS(Consumer<Item.Properties> properties) {
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

package org.fiddlemc.fiddle.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.bukkit.block.BlockType;
import org.jspecify.annotations.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The implementation of {@link BlockRegistryEntry}.
 */
public abstract class FiddleBlockRegistryEntry implements BlockRegistryEntry, KeyAwareRegistryEntry {

    protected @Nullable Identifier key;

    public FiddleBlockRegistryEntry(
        final Conversions conversions,
        final @Nullable Block internal
    ) {
        if (internal == null) {
            return;
        }
    }

    @Override
    public Identifier getKey() {
        return this.key;
    }

    public static final class FiddleBuilder extends FiddleBlockRegistryEntry implements Builder, PaperRegistryBuilder<Block, BlockType> {

        private @Nullable Function<Block.Properties, Block> nmsFactory;
        private BlockBehaviour.@Nullable Properties nmsProperties;

        public FiddleBuilder(final Conversions conversions, final @Nullable Block internal) {
            super(conversions, internal);
        }

        @Override
        public void setKey(Identifier key) {
            this.key = key;
        }

        /**
         * Sets the factory to use, and marks this builder as using NMS.
         */
        public void nmsFactory(Function<BlockBehaviour.Properties, Block> factory) {
            this.nmsFactory = factory;
        }

        /**
         * Replaces the NMS properties for the block.
         */
        public void nmsProperties(BlockBehaviour.Properties properties) {
            this.nmsProperties = properties;
        }

        /**
         * Modifies the NMS properties for the block.
         */
        public void nmsProperties(Consumer<BlockBehaviour.Properties> properties) {
            if (this.nmsProperties == null) {
                this.nmsProperties = BlockBehaviour.Properties.of();
            }
            properties.accept(this.nmsProperties);
        }

        @Override
        public Block build() {
            Function<BlockBehaviour.Properties, Block> factory = this.nmsFactory != null ? this.nmsFactory : Block::new;
            BlockBehaviour.Properties properties = this.nmsProperties != null ? this.nmsProperties : BlockBehaviour.Properties.of();
            properties.setId(ResourceKey.create(Registries.BLOCK, this.key));
            return factory.apply(properties);
        }

    }

}

package org.fiddlemc.fiddle.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.bukkit.block.BlockType;
import org.jspecify.annotations.Nullable;

/**
 * The implementation of {@link BlockRegistryEntry}.
 */
public class FiddleBlockRegistryEntry implements BlockRegistryEntry, KeyAwareRegistryEntry {

    private @Nullable Identifier key;

    public FiddleBlockRegistryEntry(
        final Conversions conversions,
        final @Nullable Block internal
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

    public static final class FiddleBuilder extends FiddleBlockRegistryEntry implements Builder, PaperRegistryBuilder<Block, BlockType> {

        public FiddleBuilder(final Conversions conversions, final @Nullable Block internal) {
            super(conversions, internal);
        }

        @Override
        public Block build() {
            return new Block(null); // TODO
        }

    }

}

package org.fiddlemc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import net.kyori.adventure.key.Key;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import org.fiddlemc.fiddle.paper.registry.data.FiddleBlockRegistryEntry;
import org.fiddlemc.fiddle.paper.registry.data.FiddleItemRegistryEntry;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // Register custom blocks
        context.getLifecycleManager().registerEventHandler(RegistryEvents.BLOCK.compose(), event -> {
            // Register an ash block
            event.registry().register(TypedKey.create(RegistryKey.BLOCK, Key.key("fiddle:ash")), builder -> {
                FiddleBlockRegistryEntry.FiddleBuilder internalBuilder = (FiddleBlockRegistryEntry.FiddleBuilder) builder;
                // It's a full block
                internalBuilder.nmsFactory(Block::new);
                internalBuilder.nmsProperties(properties -> {
                    // It breaks when pushed by a piston
                    properties.pushReaction(PushReaction.DESTROY);
                });
            });
        });
        // Register a custom block item
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ITEM.compose(), event -> {
            //TODO
        });
    }

}

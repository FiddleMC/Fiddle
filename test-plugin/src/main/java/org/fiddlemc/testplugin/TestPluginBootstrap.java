package org.fiddlemc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import net.kyori.adventure.key.Key;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        // Register a custom block
        context.getLifecycleManager().registerEventHandler(RegistryEvents.BLOCK.compose(), event -> {
            //TODO
            event.registry().register(TypedKey.create(RegistryKey.BLOCK, Key.key("test:block_1")), b -> b
                .nmsBlock(new TestBlockImpl((BlockBehaviour.Properties) b.constructBlockProperties()))
            );
        });
        // Register a custom block item
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ITEM.compose(), event -> {
            //TODO
        });
    }

}

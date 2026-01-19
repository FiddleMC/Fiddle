package org.fiddlemc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.event.RegistryEvents;
import net.kyori.adventure.key.Key;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.fiddlemc.fiddle.client.ClientProfile;
import org.fiddlemc.fiddle.minecraft.packet.mapping.item.ItemMappingPipeline;
import org.fiddlemc.fiddle.paper.registry.data.FiddleBlockRegistryEntry;
import org.fiddlemc.fiddle.paper.registry.data.FiddleItemRegistryEntry;
import org.fiddlemc.testplugin.data.PluginItems;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        // Register custom blocks
        context.getLifecycleManager().registerEventHandler(RegistryEvents.BLOCK.compose(), event -> {
            context.getLogger().info("Registering custom blocks...");
            // Register an ash block
            event.registry().register(TypedKey.create(RegistryKey.BLOCK, Key.key("example:ash_block")), builder -> {
                FiddleBlockRegistryEntry.FiddleBuilder internalBuilder = (FiddleBlockRegistryEntry.FiddleBuilder) builder;
                internalBuilder.nmsProperties(properties -> {
                    // It breaks when pushed by a piston
                    properties.pushReaction(PushReaction.DESTROY);
                    // It drops nothing unless broken with the right tool
                    properties.requiresCorrectToolForDrops();
                    // It shows up light gray on maps
                    properties.mapColor(MapColor.COLOR_LIGHT_GRAY);
                });
            });
        });

        // Register custom items
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ITEM.compose(), event -> {
            context.getLogger().info("Registering custom items...");
            // Register ash
            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("example:ash")), builder -> {
                FiddleItemRegistryEntry.FiddleBuilder internalBuilder = (FiddleItemRegistryEntry.FiddleBuilder) builder;
                internalBuilder.nmsProperties(properties -> {
                    // It stacks to 32
                    properties.stacksTo(32);
                    // It is resistant to fire
                    properties.fireResistant();
                });
            });
            // Register the item for ash blocks
            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("example:ash_block")), builder -> {
                FiddleItemRegistryEntry.FiddleBuilder internalBuilder = (FiddleItemRegistryEntry.FiddleBuilder) builder;
                // It's a block item
                internalBuilder.nmsFactoryForBlock();
                internalBuilder.nmsProperties(properties -> {
                    // It leaves ash when used in a crafting recipe
                    properties.craftRemainder(PluginItems.ASH.get());
                });
            });
        });

        // Register item mappings
        context.getLifecycleManager().registerEventHandler(ItemMappingPipeline.compose(), event -> {
            context.getLogger().info("Registering item mappings...");
            // Map ash to gunpowder
            event.getRegistrar().register(ClientProfile.AwarenessLevel.JAVA_DEFAULT, PluginItems.ASH.get(), (itemStack, mappingContext) -> {
                // Change the type
                itemStack.setItem(Items.GUNPOWDER);
                // Set the desired item name
                itemStack.set(DataComponents.ITEM_NAME, Component.literal("Ash"));
                return itemStack;
            });
            // Map ash blocks to light gray concrete powder
            event.getRegistrar().register(ClientProfile.AwarenessLevel.JAVA_DEFAULT, PluginItems.ASH_BLOCK.get(), (itemStack, mappingContext) -> {
                // Change the type
                itemStack.setItem(Items.LIGHT_GRAY_CONCRETE_POWDER);
                // Set the desired item name
                itemStack.set(DataComponents.ITEM_NAME, Component.literal("Ash block"));
                return itemStack;
            });
        });

    }

}

package org.fiddlemc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.bukkit.NamespacedKey;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.material.MaterialEnumSynchronizer;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.moredatadriven.paper.BlockRegistryEventProvider;
import org.fiddlemc.fiddle.api.moredatadriven.paper.ItemRegistryEventProvider;
import org.fiddlemc.fiddle.api.moredatadriven.paper.nms.NMSBlockRegistryEntryBuilder;
import org.fiddlemc.fiddle.api.moredatadriven.paper.nms.NMSItemRegistryEntryBuilder;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.NMSComponentMappingRegistrar;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingRegistrar;
import org.fiddlemc.testplugin.data.PluginBlocks;
import org.fiddlemc.testplugin.data.PluginItems;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;

@SuppressWarnings("unused")
public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {

        // Make sure the included data pack is loaded
        // It contains crafting recipes for the custom items we add
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY.newHandler(
            event -> {
                try {
                    event.registrar().discoverPack(this.getClass().getResource("/data_pack").toURI(), "provided");
                } catch (URISyntaxException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        ));

        // Register custom blocks
        context.getLifecycleManager().registerEventHandler(BlockRegistryEventProvider.BLOCK.compose(), event -> {
            context.getLogger().info("Registering custom blocks...");
            // Register an ash block
            event.registry().register(TypedKey.create(RegistryKey.BLOCK, Key.key("example:ash_block")), builder -> {
                NMSBlockRegistryEntryBuilder nmsBuilder = (NMSBlockRegistryEntryBuilder) builder;
                nmsBuilder.nmsProperties(properties -> {
                    // It breaks when pushed by a piston
                    properties.pushReaction(PushReaction.DESTROY);
                    // It drops nothing unless broken with the right tool
                    properties.requiresCorrectToolForDrops();
                    // It shows up light gray on maps
                    properties.mapColor(MapColor.COLOR_LIGHT_GRAY);
                });
            });
            // Register ash stairs
            event.registry().register(TypedKey.create(RegistryKey.BLOCK, Key.key("example:ash_stairs")), builder -> {
                NMSBlockRegistryEntryBuilder nmsBuilder = (NMSBlockRegistryEntryBuilder) builder;
                // Use a factory that returns StairBlock to add new stairs
                nmsBuilder.nmsFactory(properties -> new StairBlock(PluginBlocks.ASH_BLOCK.get().defaultBlockState(), properties) {});
            });
        });

        // Register custom items
        context.getLifecycleManager().registerEventHandler(ItemRegistryEventProvider.ITEM.compose(), event -> {
            context.getLogger().info("Registering custom items...");
            // Register ash
            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("example:ash")), builder -> {
                NMSItemRegistryEntryBuilder nmsBuilder = (NMSItemRegistryEntryBuilder) builder;
                nmsBuilder.nmsProperties(properties -> {
                    // It stacks to 32
                    properties.stacksTo(32);
                    // It is resistant to fire
                    properties.fireResistant();
                });
            });
            // Register the item for ash blocks
            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("example:ash_block")), builder -> {
                NMSItemRegistryEntryBuilder nmsBuilder = (NMSItemRegistryEntryBuilder) builder;
                // It's a block item
                nmsBuilder.nmsFactoryForBlock();
                nmsBuilder.nmsProperties(properties -> {
                    // It leaves ash when used in a crafting recipe
                    properties.craftRemainder(PluginItems.ASH.get());
                });
            });
            // Register the item for ash stairs
            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("example:ash_stairs")), builder -> {
                NMSItemRegistryEntryBuilder nmsBuilder = (NMSItemRegistryEntryBuilder) builder;
                // It's a block item
                nmsBuilder.nmsFactoryForBlock();
            });
        });

        // Use a custom enum name, just to show that we can (but please don't do this yourself unless you really know what you are doing)
        context.getLifecycleManager().registerEventHandler(MaterialEnumSynchronizer.get().determineEnumNameEventType(), event -> {
            NamespacedKey key = event.getSourceValue().getLeft();
            if (key.equals(NamespacedKey.fromString("example:ash"))) {
                event.setDeterminedEnumName("ASHES_TO_DUST");
                context.getLogger().info("Changed enum name for " + key + " to " + event.getDeterminedEnumName());
            }
        });

        // Register item mappings
        context.getLifecycleManager().registerEventHandler(ItemMappingPipeline.get().composeEventType(), event -> {
            context.getLogger().info("Registering item mappings...");
            NMSItemMappingRegistrar registrar = (NMSItemMappingRegistrar) event.getRegistrar();
            // Map ash to gunpowder
            registrar.register(ClientView.AwarenessLevel.JAVA_DEFAULT, PluginItems.ASH.get(), handle -> {
                ItemStack itemStack = handle.getMutable();
                // Change the type
                itemStack.setItem(Items.GUNPOWDER);
                // Set the desired item name
                itemStack.set(DataComponents.ITEM_NAME, Component.translatable("item.example.ash"));
            });
            // Map ash blocks to light gray concrete powder
            registrar.register(ClientView.AwarenessLevel.getThatDoNotAlwaysUnderstandsAllServerSideItems(), PluginItems.ASH_BLOCK.get(), handle -> {
                if (handle.getContext().getClientView().understandsAllServerSideItems()) return;
                ItemStack itemStack = handle.getMutable();
                // Change the type
                itemStack.setItem(Items.LIGHT_GRAY_CONCRETE_POWDER);
                // Set the desired item name
                itemStack.set(DataComponents.ITEM_NAME, Component.translatable("block.example.ash_block"));
            });
            // Map ash stairs to andesite stairs
            registrar.register(ClientView.AwarenessLevel.getThatDoNotAlwaysUnderstandsAllServerSideItems(), PluginItems.ASH_STAIRS.get(), handle -> {
                if (handle.getContext().getClientView().understandsAllServerSideItems()) return;
                ItemStack itemStack = handle.getMutable();
                // Change the type
                itemStack.setItem(Items.ANDESITE_STAIRS);
                // Set the desired item name
                itemStack.set(DataComponents.ITEM_NAME, Component.translatable(PluginItems.ASH_STAIRS.get().getDescriptionId()));
            });
        });

        // Register component mappings
        context.getLifecycleManager().registerEventHandler(ComponentMappingPipeline.get().composeEventType(), event -> {
            context.getLogger().info("Registering component mappings...");
            NMSComponentMappingRegistrar registrar = (NMSComponentMappingRegistrar) event.getRegistrar();
            // Map the translatables
            registrar.register(ClientView.AwarenessLevel.getThatDoNotAlwaysUnderstandsAllServerSideTranslatables(), handle -> {
                if (handle.getContext().getClientView().understandsAllServerSideTranslatables()) return;
                ComponentContents contents = handle.getImmutable().getContents();
                if (contents instanceof TranslatableContents translatableContents) {
                    String key = translatableContents.getKey();
                    if (key.equals(PluginItems.ASH.get().getDescriptionId())) {
                        handle.setMutable(Component.translatableWithFallback(key, "Ash"));
                    } else if (key.equals(PluginItems.ASH_BLOCK.get().getDescriptionId())) {
                        handle.setMutable(Component.translatableWithFallback(key, "Ash block"));
                    } else if (key.equals(PluginItems.ASH_STAIRS.get().getDescriptionId())) {
                        handle.setMutable(Component.translatableWithFallback(key, "Ash stairs"));
                    }
                }
            });
        });

    }

}

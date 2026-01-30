package org.fiddlemc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.material.MaterialEnumNameMappingPipeline;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.moredatadriven.paper.BlockRegistryEventProvider;
import org.fiddlemc.fiddle.api.moredatadriven.paper.ItemRegistryEventProvider;
import org.fiddlemc.fiddle.api.moredatadriven.paper.nms.NMSBlockRegistryEntryBuilder;
import org.fiddlemc.fiddle.api.moredatadriven.paper.nms.NMSItemRegistryEntryBuilder;
import org.fiddlemc.fiddle.api.packetmapping.component.translatable.ServerSideTranslationRegistrar;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.item.builtin.BuiltInItemMapper;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingPipelineComposeEvent;
import org.fiddlemc.testplugin.data.PluginBlocks;
import org.fiddlemc.testplugin.data.PluginItemTypes;
import org.fiddlemc.testplugin.data.PluginItems;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

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
                nmsBuilder.nmsFactory(properties -> new StairBlock(PluginBlocks.ASH_BLOCK.get().defaultBlockState(), properties) {
                });
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
        context.getLifecycleManager().registerEventHandler(MaterialEnumNameMappingPipeline.get().compose(), event -> {
            event.register(handle -> {
                NamespacedKey key = handle.getSourceValue().getLeft();
                if (key.equals(NamespacedKey.fromString("example:ash"))) {
                    handle.set("ASHES_TO_DUST");
                    context.getLogger().info("Changed enum name for " + key + " to " + handle.getImmutable());
                }
            });
        });

        // Configure basic item mappings
        context.getLifecycleManager().registerEventHandler(BuiltInItemMapper.get().compose(), event -> {
            context.getLogger().info("Configuring built-in item mapper...");
            event.mapItem(ClientView.AwarenessLevel.JAVA_DEFAULT, PluginItemTypes.ASH.get(), ItemType.GUNPOWDER);
            event.mapItem(ClientView.AwarenessLevel.JAVA_DEFAULT, PluginItemTypes.ASH_BLOCK.get(), ItemType.LIGHT_GRAY_CONCRETE_POWDER);
            event.mapItem(ClientView.AwarenessLevel.JAVA_DEFAULT, PluginItemTypes.ASH_STAIRS.get(), ItemType.ANDESITE_STAIRS);
        });

        // Register more custom item mapping code
        context.getLifecycleManager().registerEventHandler(ItemMappingPipeline.get().compose(), event -> {
            context.getLogger().info("Registering custom item mappings...");
            NMSItemMappingPipelineComposeEvent nmsEvent = (NMSItemMappingPipelineComposeEvent) event;

            // Screw around with ash stairs a bit, to demonstrate that we can do whatever we want to the client-side view
            nmsEvent.register(ClientView.AwarenessLevel.JAVA_DEFAULT, PluginItems.ASH_STAIRS.get(), handle -> {
                ItemStack immutable = handle.getImmutable();
                // Make the item name bold
                Component itemName = immutable.getItemName();
                if (!itemName.getStyle().isBold()) {
                    handle.getMutable().set(DataComponents.ITEM_NAME, itemName.copy().setStyle(itemName.getStyle().withBold(true)));
                }
                // Give it an enchantment glint
                if (!Boolean.TRUE.equals(immutable.get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE))) {
                    handle.getMutable().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
                }
                // Add some lore
                @Nullable ItemLore lore = immutable.get(DataComponents.LORE);
                Component toAdd = Component.literal("You can craft these stairs from ").append(Component.translatable(PluginItems.ASH_BLOCK.get().getDescriptionId()));
                if (lore == null) {
                    lore = new ItemLore(List.of(toAdd));
                } else {
                    lore = lore.withLineAdded(toAdd);
                }
                handle.getMutable().set(DataComponents.LORE, lore);
            });

            // Add helpful lore to crafting tables without affecting the server-side data
            nmsEvent.register(ClientView.AwarenessLevel.getAll(), Items.CRAFTING_TABLE, handle -> {
                @Nullable ItemLore lore = handle.getImmutable().get(DataComponents.LORE);
                Component toAdd = Component.literal("This is a very important block for beginners");
                if (lore == null) {
                    lore = new ItemLore(List.of(toAdd));
                } else {
                    lore = lore.withLineAdded(toAdd);
                }
                handle.getMutable().set(DataComponents.LORE, lore);
            });

        });

        // Register translations
        context.getLifecycleManager().registerEventHandler(ServerSideTranslationRegistrar.get().compose(), event -> {
            // For the custom blocks and items
            event.register(PluginItems.ASH.get().getDescriptionId(), "Ash");
            event.register(PluginItems.ASH.get().getDescriptionId(), "灰", "ja_jp", ServerSideTranslationRegistrar.FallbackScope.LANGUAGE_GROUP, true);
            event.register(PluginItems.ASH_BLOCK.get().getDescriptionId(), "Ash block");
            event.register(PluginItems.ASH_STAIRS.get().getDescriptionId(), "Ash stairs");
            // Override the vanilla bookshelf name just to show that we can
            event.register(Blocks.BOOKSHELF.getDescriptionId(), "Booky Bookshelf");
        });

    }

}

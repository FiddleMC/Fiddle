package org.fiddlemc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.material.MaterialEnumNameMappingPipeline;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.moredatadriven.paper.BlockRegistryEventProvider;
import org.fiddlemc.fiddle.api.moredatadriven.paper.ItemRegistryEventProvider;
import org.fiddlemc.fiddle.api.moredatadriven.paper.nms.NMSBlockRegistryEntryBuilder;
import org.fiddlemc.fiddle.api.moredatadriven.paper.nms.NMSItemRegistryEntryBuilder;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.component.translatable.ServerSideTranslationRegistrar;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.item.builtin.BuiltInItemMapper;
import org.fiddlemc.fiddle.api.packetmapping.item.nms.NMSItemMappingPipelineComposeEvent;
import org.fiddlemc.testplugin.data.PluginBlockTypes;
import org.fiddlemc.testplugin.data.PluginBlocks;
import org.fiddlemc.testplugin.data.PluginItemTypes;
import org.fiddlemc.testplugin.data.PluginItems;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class TestPluginBootstrap implements PluginBootstrap {

    /**
     * Makes sure the included data pack is loaded.
     * It contains crafting recipes for the custom items we add.
     */
    private void loadIncludedDataPack(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY, event -> {
            try {
                event.registrar().discoverPack(this.getClass().getResource("/data_pack").toURI(), "provided");
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Adds our custom blocks.
     *
     * <p>
     * We register the following blocks:
     * <ul>
     *     <li><code>example:ash_block</code></li>
     *     <li><code>example:ash_stairs</code></li>
     * </ul>
     * We add some server-side properties to <code>example:ash_block</code> as a demo.
     * </p>
     */
    private void addCustomBlocks(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(BlockRegistryEventProvider.BLOCK.compose(), event -> {

            event.registry().register(TypedKey.create(RegistryKey.BLOCK, Key.key("example:ash_block")), builder -> {
                var nmsBuilder = (NMSBlockRegistryEntryBuilder) builder;
                nmsBuilder.nmsProperties(properties -> {
                    properties.pushReaction(PushReaction.DESTROY); // It breaks when pushed by a piston
                    properties.requiresCorrectToolForDrops(); // It drops nothing unless broken with the right tool (a shovel, as defined in the included data pack)
                    properties.mapColor(MapColor.COLOR_LIGHT_GRAY); // It shows up light gray on maps
                });
            });

            event.registry().register(TypedKey.create(RegistryKey.BLOCK, Key.key("example:ash_stairs")), builder -> {
                var nmsBuilder = (NMSBlockRegistryEntryBuilder) builder;
                nmsBuilder.nmsFactory(properties -> new StairBlock(PluginBlocks.ASH_BLOCK.get().defaultBlockState(), properties) {
                }); // Use a factory that returns StairBlock to add new stairs
            });

        });
    }

    /**
     * Adds our custom items.
     *
     * <p>
     * We register the following items:
     * <ul>
     *     <li><code>example:ash</code></li>
     *     <li><code>example:ash_block</code></li>
     *     <li><code>example:ash_stairs</code></li>
     * </ul>
     * We add some server-side properties to <code>example:ash_block</code> as a demo.
     * </p>
     */
    private void addCustomItems(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(ItemRegistryEventProvider.ITEM.compose(), event -> {

            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("example:ash")), builder -> {
                var nmsBuilder = (NMSItemRegistryEntryBuilder) builder;
                nmsBuilder.nmsProperties(properties -> {
                });
            });

            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("example:ash_block")), builder -> {
                var nmsBuilder = (NMSItemRegistryEntryBuilder) builder;
                nmsBuilder.nmsFactoryForBlock(); // It's a block item
                nmsBuilder.nmsProperties(properties -> {
                    properties.stacksTo(32); // It stacks to 32
                    properties.fireResistant(); // It is resistant to fire
                    properties.craftRemainder(PluginItems.ASH.get()); // It leaves ash when used in a crafting recipe
                });
            });

            event.registry().register(TypedKey.create(RegistryKey.ITEM, Key.key("example:ash_stairs")), builder -> {
                var nmsBuilder = (NMSItemRegistryEntryBuilder) builder;
                nmsBuilder.nmsFactoryForBlock(); // It's a block item
            });

        });
    }

    /**
     * Sets a custom enum name <code>ASHES_TO_DUST</code> for the <code>example:ash</code> item,
     * purely as a demo.
     *
     * <p>
     * This is just intended as a demo that this is possible.
     * Please don't do this yourself unless you really know what you are doing!
     * </p>
     */
    private void customizeEnumNameForAnItem(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(MaterialEnumNameMappingPipeline.get().compose(), event -> {
            event.register(handle -> {
                var key = handle.getSourceValue().getLeft();
                if (key.equals(NamespacedKey.fromString("example:ash"))) {
                    handle.set("ASHES_TO_DUST");
                }
            });
        });
    }

    /**
     * Configures the basic server-to-client mappings for blocks,
     * with a simple syntax.
     *
     * <p>
     * This maps our custom blocks,
     * and also maps the vanilla {@code minecraft:birch_leaves} to {@code minecraft:orange_wool},
     * to show that you can also map vanilla blocks.
     * </p>
     */
    private void setBasicBlockMappings(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(BlockMappingPipeline.get().compose(), event -> {

            event.registerSimple(ClientView.AwarenessLevel.JAVA_DEFAULT, PluginBlockTypes.ASH_BLOCK.get(), BlockType.LIGHT_GRAY_CONCRETE_POWDER.createBlockData());
            event.registerStateToState(ClientView.AwarenessLevel.JAVA_DEFAULT, PluginBlockTypes.ASH_STAIRS.get(), BlockType.ANDESITE_STAIRS);

            event.registerSimple(ClientView.AwarenessLevel.JAVA_DEFAULT, BlockType.BIRCH_LEAVES, BlockType.ORANGE_WOOL.createBlockData());

        });
    }

    /**
     * Configures the basic server-to-client mappings for items,
     * with a simple syntax.
     *
     * <p>
     * This works by using {@link BuiltInItemMapper}, which does the heavy lifting for us.
     * </p>
     *
     * <p>
     * This maps our custom items,
     * and also maps the vanilla {@code minecraft:iron_axe} to {@code minecraft:echo_shard},
     * to show that you can also map vanilla items.
     * </p>
     */
    private void setBasicItemMappings(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(BuiltInItemMapper.get().compose(), event -> {

            event.mapItem(PluginItemTypes.ASH.get(), ItemType.GUNPOWDER);
            event.mapItem(PluginItemTypes.ASH_BLOCK.get(), ItemType.LIGHT_GRAY_CONCRETE_POWDER);
            event.mapItem(PluginItemTypes.ASH_STAIRS.get(), ItemType.ANDESITE_STAIRS);

            event.mapItem(ItemType.IRON_AXE, ItemType.ECHO_SHARD);

        });
    }

    /**
     * Configures more customized server-to-client mappings for items,
     * as a demo that this is possible when needed.
     *
     * <p>
     * We add the following mappings:
     * <ul>
     *     <li>We add extra lore to a vanilla item, <code>example:crafting_table</code></li>
     *     <li>We also change some random stuff of one of our custom items, <code>example:ash_stairs</code></li>
     * </ul>
     * </p>
     */
    private void setComplexItemMappings(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(ItemMappingPipeline.get().compose(), event -> {
            var nmsEvent = (NMSItemMappingPipelineComposeEvent) event;

            nmsEvent.register(ClientView.AwarenessLevel.getAll(), Items.CRAFTING_TABLE, handle -> {
                var newLines = Stream.of(
                    Component.literal("This is a very important block for beginners!"),
                    Component.literal("For example, it can be used to craft ").append(Component.translatable(PluginItems.ASH_BLOCK.get().getDescriptionId()))
                ).map(line -> (Component) line.withStyle(Style.EMPTY.withItalic(false).withColor(5526612))).toList();
                var existingLore = handle.getImmutable().get(DataComponents.LORE);
                handle.getMutable().set(DataComponents.LORE, existingLore == null ? new ItemLore(newLines) : existingLore.withLineAdded(newLines.get(0)).withLineAdded(newLines.get(1)));

            });

            nmsEvent.register(ClientView.AwarenessLevel.JAVA_DEFAULT, PluginItems.ASH_STAIRS.get(), handle -> {
                var immutable = handle.getImmutable();
                handle.getMutable().set(DataComponents.ITEM_NAME, immutable.getItemName().copy().withStyle(ChatFormatting.BOLD)); // Make the item name bold
                handle.getMutable().set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true); // Give it an enchantment glint
            });

        });
    }

    /**
     * Configures the translations of names of blocks and items.
     *
     * <p>
     * We set a generic name for our custom blocks and items,
     * as well as a Japanese name for <code>example:ash</code> that will be visible to clients
     * that use Japanese as their display language.
     * </p>
     *
     * <p>
     * We can also use this to change existing vanilla texts.
     * To demonstrate this, we change the name of <code>minecraft:bookshelf</code>.
     * </p>
     */
    private void setTranslations(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(ServerSideTranslationRegistrar.get().compose(), event -> {

            event.register(PluginItems.ASH.get().getDescriptionId(), "Ash");
            event.register(PluginItems.ASH_BLOCK.get().getDescriptionId(), "Ash block");
            event.register(PluginItems.ASH_STAIRS.get().getDescriptionId(), "Ash stairs");

            event.register(PluginItems.ASH.get().getDescriptionId(), "灰", "ja_jp", ServerSideTranslationRegistrar.FallbackScope.LANGUAGE_GROUP);

            event.register(Blocks.BOOKSHELF.getDescriptionId(), "Booky Bookshelf");

        });
    }

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        loadIncludedDataPack(context);
        addCustomBlocks(context);
        addCustomItems(context);
        customizeEnumNameForAnItem(context);
        setBasicBlockMappings(context);
        setBasicItemMappings(context);
        setComplexItemMappings(context);
        setTranslations(context);
    }

}

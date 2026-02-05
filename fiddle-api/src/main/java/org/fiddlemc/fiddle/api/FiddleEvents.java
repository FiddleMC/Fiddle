package org.fiddlemc.fiddle.api;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.event.RegistryComposeEvent;
import io.papermc.paper.registry.event.RegistryEvents;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNamesComposeEvent;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.material.MaterialEnumNames;
import org.fiddlemc.fiddle.api.moredatadriven.paper.BlockRegistryEntry;
import org.fiddlemc.fiddle.api.moredatadriven.paper.ItemRegistryEntry;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappings;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingsComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappings;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingsComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.component.translatable.ServerSideTranslations;
import org.fiddlemc.fiddle.api.packetmapping.component.translatable.ServerSideTranslationsComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappings;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingsComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.item.builtin.BuiltInItemMapper;
import org.fiddlemc.fiddle.api.packetmapping.item.builtin.BuiltInItemMapperComposeEvent;
import org.fiddlemc.fiddle.api.util.composable.ComposableEventType;
import org.jspecify.annotations.Nullable;

/**
 * A convenience class providing links to the different Fiddle {@link LifecycleEventType}s.
 */
public final class FiddleEvents {

    private FiddleEvents() {
        throw new UnsupportedOperationException();
    }

    public static final LifecycleEventType.Prioritizable<BootstrapContext, RegistryComposeEvent<BlockType, BlockRegistryEntry.Builder>> BLOCK_REGISTRY_COMPOSE = RegistryEvents.BLOCK.compose();
    public static final LifecycleEventType.Prioritizable<BootstrapContext, RegistryComposeEvent<ItemType, ItemRegistryEntry.Builder>> ITEM_REGISTRY_COMPOSE = RegistryEvents.ITEM.compose();
    public static final ComposableEventType<BlockMappingsComposeEvent> BLOCK_MAPPINGS = BlockMappings.get().compose();
    public static final ComposableEventType<ItemMappingsComposeEvent> ITEM_MAPPINGS = ItemMappings.get().compose();
    public static final ComposableEventType<BuiltInItemMapperComposeEvent> BUILT_IN_ITEM_MAPPER_COMPOSE = BuiltInItemMapper.get().compose();
    public static final ComposableEventType<ComponentMappingsComposeEvent> COMPONENT_MAPPINGS = ComponentMappings.get().compose();
    public static final ComposableEventType<ServerSideTranslationsComposeEvent> SERVER_SIDE_TRANSLATIONS = ServerSideTranslations.get().compose();
    public static final ComposableEventType<BukkitEnumNamesComposeEvent<Triple<NamespacedKey, @Nullable BlockType, @Nullable ItemType>>> MATERIAL_ENUM_NAMES = MaterialEnumNames.get().compose();

}

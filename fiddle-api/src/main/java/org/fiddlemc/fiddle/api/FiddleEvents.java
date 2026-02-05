package org.fiddlemc.fiddle.api;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.registry.event.RegistryComposeEvent;
import io.papermc.paper.registry.event.RegistryEvents;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemType;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.BukkitEnumNameMappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.bukkit.enuminjection.material.MaterialEnumNameMappingPipeline;
import org.fiddlemc.fiddle.api.moredatadriven.paper.BlockRegistryEntry;
import org.fiddlemc.fiddle.api.moredatadriven.paper.ItemRegistryEntry;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.block.BlockMappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentMappingPipelineComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.component.translatable.ServerSideTranslationRegistrar;
import org.fiddlemc.fiddle.api.packetmapping.component.translatable.ServerSideTranslationRegistrarComposeEvent;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingPipeline;
import org.fiddlemc.fiddle.api.packetmapping.item.ItemMappingPipelineComposeEvent;
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
    public static final ComposableEventType<BlockMappingPipelineComposeEvent> BLOCK_MAPPING_PIPELINE_COMPOSE = BlockMappingPipeline.get().compose();
    public static final ComposableEventType<ItemMappingPipelineComposeEvent> ITEM_MAPPING_PIPELINE_COMPOSE = ItemMappingPipeline.get().compose();
    public static final ComposableEventType<BuiltInItemMapperComposeEvent> BUILT_IN_ITEM_MAPPER_COMPOSE = BuiltInItemMapper.get().compose();
    public static final ComposableEventType<ComponentMappingPipelineComposeEvent> COMPONENT_MAPPING_PIPELINE_COMPOSE = ComponentMappingPipeline.get().compose();
    public static final ComposableEventType<ServerSideTranslationRegistrarComposeEvent> SERVER_SIDE_TRANSLATION_REGISTRAR_COMPOSE = ServerSideTranslationRegistrar.get().compose();
    public static final ComposableEventType<BukkitEnumNameMappingPipelineComposeEvent<Triple<NamespacedKey, @Nullable BlockType, @Nullable ItemType>>> MATERIAL_ENUM_MAPPING_PIPELINE_COMPOSE = MaterialEnumNameMappingPipeline.get().compose();

}

package org.fiddlemc.fiddle.configuration;

import com.google.common.base.Suppliers;
import com.google.common.collect.Table;
import com.mojang.logging.LogUtils;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.configuration.ConfigurationPart;
import io.papermc.paper.configuration.Configurations;
import io.papermc.paper.configuration.NestedSetting;
import io.papermc.paper.configuration.PaperConfigurations;
import io.papermc.paper.configuration.legacy.RequiresSpigotInitialization;
import io.papermc.paper.configuration.mapping.Definition;
import io.papermc.paper.configuration.mapping.FieldProcessor;
import io.papermc.paper.configuration.mapping.InnerClassFieldDiscoverer;
import io.papermc.paper.configuration.mapping.MergeMap;
import io.papermc.paper.configuration.serializer.ServerboundPacketClassSerializer;
import io.papermc.paper.configuration.serializer.StringRepresentableSerializer;
import io.papermc.paper.configuration.serializer.collection.TableSerializer;
import io.papermc.paper.configuration.serializer.collection.map.FastutilMapSerializer;
import io.papermc.paper.configuration.serializer.registry.RegistryHolderSerializer;
import io.papermc.paper.configuration.serializer.registry.RegistryValueSerializer;
import io.papermc.paper.configuration.transformation.Transformations;
import io.papermc.paper.configuration.type.DespawnRange;
import io.papermc.paper.configuration.type.EngineMode;
import io.papermc.paper.configuration.type.fallback.FallbackValueSerializer;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import joptsimple.OptionSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.VisibleForTesting;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spigotmc.SpigotWorldConfig;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import static io.leangen.geantyref.GenericTypeReflector.erase;

/**
 * The configurations for Fiddle. The instance can be set up before bootstrap,
 * so that the global configuration can be loaded before bootstrap.
 */
@SuppressWarnings("Convert2Diamond")
public final class FiddleConfigurations extends Configurations<FiddleGlobalConfiguration, FiddleWorldConfiguration> {

    private static FiddleConfigurations INSTANCE;

    /**
     * @param optionSet The {@link OptionSet}, which must be non-null if the {@link #INSTANCE} needs to be created.
     */
    public static FiddleConfigurations get(@Nullable OptionSet optionSet) {
        if (INSTANCE == null) {
            if (optionSet == null) {
                throw new IllegalStateException();
            }
            final Path configDirPath = Services.getConfigDirPath(optionSet);
            try {
                INSTANCE = setup(configDirPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return INSTANCE;
    }

    private static final Logger LOGGER = LogUtils.getClassLogger();
    static final String GLOBAL_CONFIG_FILE_NAME = "fiddle-global.yml";
    static final String WORLD_DEFAULTS_CONFIG_FILE_NAME = "fiddle-world-defaults.yml";
    static final String WORLD_CONFIG_FILE_NAME = "fiddle-world.yml";

    private static final String GLOBAL_HEADER = String.format("""
            This is the global configuration file for Fiddle.

            If you need help with the configuration or have any questions related to Fiddle,
            join us in our Discord, or check the GitHub Wiki pages.

            The world configuration options are inside
            their respective world folder. The files are named %s

            Wiki: https://github.com/FiddleMC/Fiddle/wiki
            Discord: https://discord.gg/EduvcVmKS7""", WORLD_CONFIG_FILE_NAME);

    private static final String WORLD_DEFAULTS_HEADER = """
            This is the world defaults configuration file for Fiddle.

            If you need help with the configuration or have any questions related to Fiddle,
            join us in our Discord, or check the GitHub Wiki pages.

            Configuration options here apply to all worlds, unless you specify overrides inside
            the world-specific config file inside each world folder.

            Wiki: https://github.com/FiddleMC/Fiddle/wiki
            Discord: https://discord.gg/EduvcVmKS7""";

    private static final Function<ContextMap, String> WORLD_HEADER = map -> String.format("""
        This is a world configuration file for Fiddle.
        This file may start empty but can be filled with settings to override ones in the %s/%s
        
        World: %s (%s)""",
        PaperConfigurations.CONFIG_DIR,
        FiddleConfigurations.WORLD_DEFAULTS_CONFIG_FILE_NAME,
        map.require(WORLD_NAME),
        map.require(WORLD_KEY)
    );

    private FiddleConfigurations(final Path globalFolder) {
        super(globalFolder, FiddleGlobalConfiguration.class, FiddleWorldConfiguration.class, GLOBAL_CONFIG_FILE_NAME, WORLD_DEFAULTS_CONFIG_FILE_NAME, WORLD_CONFIG_FILE_NAME);
    }

    @Override
    protected int globalConfigVersion() {
        return FiddleGlobalConfiguration.CURRENT_VERSION;
    }

    @Override
    protected int worldConfigVersion() {
        return FiddleWorldConfiguration.CURRENT_VERSION;
    }

    @Override
    protected YamlConfigurationLoader.Builder createLoaderBuilder() {
        return super.createLoaderBuilder()
            .defaultOptions(FiddleConfigurations::defaultOptions);
    }

    private static ConfigurationOptions defaultOptions(ConfigurationOptions options) {
        return PaperConfigurations.defaultOptions(options);
    }

    @Override
    protected ObjectMapper.Factory.Builder createGlobalObjectMapperFactoryBuilder() {
        return defaultGlobalFactoryBuilder(super.createGlobalObjectMapperFactoryBuilder());
    }

    private static ObjectMapper.Factory.Builder defaultGlobalFactoryBuilder(ObjectMapper.Factory.Builder builder) {
        return builder.addDiscoverer(InnerClassFieldDiscoverer.globalConfig(defaultFieldProcessors()));
    }

    @Override
    protected YamlConfigurationLoader.Builder createGlobalLoaderBuilder(RegistryAccess registryAccess) {
        return super.createGlobalLoaderBuilder(registryAccess)
            .defaultOptions((options) -> defaultGlobalOptions(registryAccess, options));
    }

    private static ConfigurationOptions defaultGlobalOptions(RegistryAccess registryAccess, ConfigurationOptions options) {
        return options
            .header(GLOBAL_HEADER)
            .serializers(builder -> builder
                .register(new ServerboundPacketClassSerializer())
                .register(new RegistryValueSerializer<>(new TypeToken<DataComponentType<?>>() {}, registryAccess, Registries.DATA_COMPONENT_TYPE, false))
            );
    }

    @Override
    public FiddleGlobalConfiguration initializeGlobalConfiguration(final RegistryAccess registryAccess) throws ConfigurateException {
        LOGGER.info("Initializing Fiddle global configuration...");
        FiddleGlobalConfiguration configuration = super.initializeGlobalConfiguration(registryAccess);
        FiddleGlobalConfiguration.set(configuration);
        return configuration;
    }

    @Override
    protected ContextMap.Builder createDefaultContextMap(final RegistryAccess registryAccess) {
        return super.createDefaultContextMap(registryAccess)
            .put(PaperConfigurations.SPIGOT_WORLD_CONFIG_CONTEXT_KEY, PaperConfigurations.SPIGOT_WORLD_DEFAULTS);
    }

    @Override
    protected ObjectMapper.Factory.Builder createWorldObjectMapperFactoryBuilder(final ContextMap contextMap) {
        return super.createWorldObjectMapperFactoryBuilder(contextMap)
            .addNodeResolver(new RequiresSpigotInitialization.Factory(contextMap.require(PaperConfigurations.SPIGOT_WORLD_CONFIG_CONTEXT_KEY).get()))
            .addNodeResolver(new NestedSetting.Factory())
            .addDiscoverer(InnerClassFieldDiscoverer.create(Map.of(FiddleWorldConfiguration.class, createWorldConfigInstance(contextMap)), defaultFieldProcessors())); // Based on InnerClassFieldDiscoverer#worldConfig
    }

    private static FiddleWorldConfiguration createWorldConfigInstance(ContextMap contextMap) {
        return new FiddleWorldConfiguration(
            contextMap.require(PaperConfigurations.SPIGOT_WORLD_CONFIG_CONTEXT_KEY).get(),
            contextMap.require(Configurations.WORLD_KEY)
        );
    }

    @Override
    protected YamlConfigurationLoader.Builder createWorldConfigLoaderBuilder(final ContextMap contextMap) {
        final RegistryAccess access = contextMap.require(REGISTRY_ACCESS);
        return super.createWorldConfigLoaderBuilder(contextMap)
            .defaultOptions(options -> options
                .header(contextMap.require(WORLD_NAME).equals(WORLD_DEFAULTS) ? WORLD_DEFAULTS_HEADER : WORLD_HEADER.apply(contextMap))
                .serializers(serializers -> serializers
                    .register(new TypeToken<Reference2IntMap<?>>() {}, new FastutilMapSerializer.SomethingToPrimitive<Reference2IntMap<?>>(Reference2IntOpenHashMap::new, Integer.TYPE))
                    .register(new TypeToken<Reference2LongMap<?>>() {}, new FastutilMapSerializer.SomethingToPrimitive<Reference2LongMap<?>>(Reference2LongOpenHashMap::new, Long.TYPE))
                    .register(new TypeToken<Reference2ObjectMap<?, ?>>() {}, new FastutilMapSerializer.SomethingToSomething<Reference2ObjectMap<?, ?>>(Reference2ObjectOpenHashMap::new))
                    .register(new TypeToken<Table<?, ?, ?>>() {}, new TableSerializer())
                    .register(DespawnRange.class, DespawnRange.SERIALIZER)
                    .register(StringRepresentableSerializer::isValidFor, new StringRepresentableSerializer())
                    .register(EngineMode.SERIALIZER)
                    .register(FallbackValueSerializer.create(contextMap.require(PaperConfigurations.SPIGOT_WORLD_CONFIG_CONTEXT_KEY).get(), MinecraftServer::getServer))
                    .register(new RegistryValueSerializer<>(new TypeToken<EntityType<?>>() {}, access, Registries.ENTITY_TYPE, true))
                    .register(new RegistryValueSerializer<>(Item.class, access, Registries.ITEM, true))
                    .register(new RegistryValueSerializer<>(Block.class, access, Registries.BLOCK, true))
                    .register(new RegistryHolderSerializer<>(new TypeToken<ConfiguredFeature<?, ?>>() {}, access, Registries.CONFIGURED_FEATURE, false))
                )
            );
    }

    @Override
    protected void applyWorldConfigTransformations(final ContextMap contextMap, final ConfigurationNode node, final @Nullable ConfigurationNode defaultsNode) throws ConfigurateException {
        // Not needed now
        // When needed in future due to backwards-incompatible configuration changes,
        // use PaperConfigurations#applyWorldConfigTransformations as template
    }

    @Override
    protected void applyGlobalConfigTransformations(ConfigurationNode node) throws ConfigurateException {
        // Not needed now
        // When needed in future due to backwards-incompatible configuration changes,
        // use PaperConfigurations#applyGlobalConfigTransformations as template
    }

    private static final List<Transformations.DefaultsAware> DEFAULT_AWARE_TRANSFORMATIONS = List.of(
    );

    @Override
    protected void applyDefaultsAwareWorldConfigTransformations(final ContextMap contextMap, final ConfigurationNode worldNode, final ConfigurationNode defaultsNode) throws ConfigurateException {
        final ConfigurationTransformation.Builder builder = ConfigurationTransformation.builder();
        // ADD FUTURE TRANSFORMS HERE (these transforms run after the defaults have been merged into the node)
        DEFAULT_AWARE_TRANSFORMATIONS.forEach(transform -> transform.apply(builder, contextMap, defaultsNode));
        builder.build().apply(worldNode);
    }

    @Override
    public FiddleWorldConfiguration createWorldConfig(final ContextMap contextMap) {
        final String levelName = contextMap.require(WORLD_NAME);
        try {
            return super.createWorldConfig(contextMap);
        } catch (IOException exception) {
            throw new RuntimeException("Could not create world config for " + levelName, exception);
        }
    }

    @Override
    protected boolean isConfigType(final Type type) {
        return ConfigurationPart.class.isAssignableFrom(erase(type));
    }

    public void reloadConfigs(MinecraftServer server) {
        try {
            this.initializeGlobalConfiguration(server.registryAccess(), reloader(this.globalConfigClass, FiddleGlobalConfiguration.get()));
            this.initializeWorldDefaultsConfiguration(server.registryAccess());
            for (ServerLevel level : server.getAllLevels()) {
                this.createWorldConfig(PaperConfigurations.createWorldContextMap(level), reloader(this.worldConfigClass, level.fiddleConfig()));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not reload Fiddle configuration files", ex);
        }
    }

    private static List<Definition<? extends Annotation, ?, ? extends FieldProcessor.Factory<?, ?>>> defaultFieldProcessors() {
        return PaperConfigurations.defaultFieldProcessors();
    }

    public static FiddleConfigurations setup(final Path configDir) throws Exception {
        try {
            createDirectoriesSymlinkAware(configDir);
            return new FiddleConfigurations(configDir);
        } catch (final IOException ex) {
            throw new RuntimeException("Could not setup FiddleConfigurations", ex);
        }
    }

    @VisibleForTesting
    static ConfigurationNode createForTesting(RegistryAccess registryAccess) {
        ObjectMapper.Factory factory = defaultGlobalFactoryBuilder(ObjectMapper.factoryBuilder()).build();
        ConfigurationOptions options = defaultGlobalOptions(registryAccess, defaultOptions(ConfigurationOptions.defaults()))
            .serializers(builder -> builder.register(type -> ConfigurationPart.class.isAssignableFrom(erase(type)), factory.asTypeSerializer()));
        return BasicConfigurationNode.root(options);
    }

    // Symlinks are not correctly checked in createDirectories
    static void createDirectoriesSymlinkAware(Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            Files.createDirectories(path);
        }
    }
}

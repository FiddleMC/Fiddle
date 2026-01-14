package org.fiddlemc.fiddle.configuration;

import com.mojang.logging.LogUtils;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.configuration.Configurations;
import io.papermc.paper.configuration.PaperConfigurations;
import io.papermc.paper.configuration.serializer.ServerboundPacketClassSerializer;
import io.papermc.paper.configuration.serializer.registry.RegistryValueSerializer;
import io.papermc.paper.configuration.transformation.Transformations;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import joptsimple.OptionSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.Services;
import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

/**
 * The configurations for Fiddle. The instance can be set up before bootstrap,
 * so that the global configuration can be loaded before bootstrap.
 */
@SuppressWarnings("Convert2Diamond")
public final class FiddleConfigurations extends PaperConfigurations<FiddleGlobalConfiguration, FiddleWorldConfiguration> {

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

    private static final String FIDDLE_WORLD_CONFIG_FILE_NAME = "fiddle-world.yml";

    private static final String FIDDLE_GLOBAL_HEADER = String.format("""
        This is the global configuration file for Fiddle.
        
        If you need help with the configuration or have any questions related to Fiddle,
        join us in our Discord, or check the GitHub Wiki pages.
        
        The world configuration options are inside
        their respective world folder. The files are named %s
        
        Wiki: https://github.com/FiddleMC/Fiddle/wiki
        Discord: https://discord.gg/EduvcVmKS7""", FIDDLE_WORLD_CONFIG_FILE_NAME);

    @Override
    protected String getWorldDefaultsHeader() {
        return """
            This is the world defaults configuration file for Fiddle.
            
            If you need help with the configuration or have any questions related to Fiddle,
            join us in our Discord, or check the GitHub Wiki pages.
            
            Configuration options here apply to all worlds, unless you specify overrides inside
            the world-specific config file inside each world folder.
            
            Wiki: https://github.com/FiddleMC/Fiddle/wiki
            Discord: https://discord.gg/EduvcVmKS7""";
    }

    @Override
    protected String getWorldHeader(ContextMap map) {
        return String.format("""
                This is a world configuration file for Fiddle.
                This file may start empty but can be filled with settings to override ones in the %s/%s
                
                World: %s (%s)""",
            PaperConfigurations.CONFIG_DIR,
            this.defaultWorldConfigFileName,
            map.require(WORLD_NAME),
            map.require(WORLD_KEY)
        );
    }

    @Override
    protected FiddleGlobalConfiguration getGlobalConfiguration() {
        return FiddleGlobalConfiguration.get();
    }

    @Override
    protected FiddleWorldConfiguration getWorldConfiguration(ServerLevel level) {
        return level.fiddleConfig();
    }

    private FiddleConfigurations(final Path globalFolder) {
        super(globalFolder, FiddleGlobalConfiguration.class, FiddleWorldConfiguration.class, "fiddle");
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
    protected YamlConfigurationLoader.Builder createGlobalLoaderBuilder(RegistryAccess registryAccess) {
        return super.createGlobalLoaderBuilder(registryAccess)
            .defaultOptions((options) -> defaultGlobalOptions(registryAccess, options));
    }

    private static ConfigurationOptions defaultGlobalOptions(RegistryAccess registryAccess, ConfigurationOptions options) {
        return options
            .header(FIDDLE_GLOBAL_HEADER)
            .serializers(builder -> builder
                .register(new ServerboundPacketClassSerializer())
                .register(new RegistryValueSerializer<>(new TypeToken<DataComponentType<?>>() {}, registryAccess, Registries.DATA_COMPONENT_TYPE, false))
            );
    }

    @Override
    public FiddleGlobalConfiguration initializeGlobalConfiguration(final @Nullable RegistryAccess registryAccess) throws ConfigurateException {
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
    protected FiddleWorldConfiguration createWorldConfigInstance(ContextMap contextMap) {
        return new FiddleWorldConfiguration(
            contextMap.require(PaperConfigurations.SPIGOT_WORLD_CONFIG_CONTEXT_KEY).get(),
            contextMap.require(Configurations.WORLD_KEY)
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

    public static FiddleConfigurations setup(final Path configDir) throws Exception {
        try {
            PaperConfigurations.createDirectoriesSymlinkAware(configDir);
            return new FiddleConfigurations(configDir);
        } catch (final IOException ex) {
            throw new RuntimeException("Could not setup FiddleConfigurations", ex);
        }
    }
}

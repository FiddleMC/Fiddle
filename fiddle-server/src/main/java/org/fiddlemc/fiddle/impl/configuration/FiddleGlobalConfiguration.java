package org.fiddlemc.fiddle.impl.configuration;

import com.mojang.logging.LogUtils;
import io.papermc.paper.configuration.Configuration;
import io.papermc.paper.configuration.ConfigurationPart;
import io.papermc.paper.configuration.GlobalConfiguration;
import org.slf4j.Logger;
import org.spongepowered.configurate.objectmapping.meta.Setting;

/**
 * The global configuration for Fiddle.
 *
 * <p>
 * Analogous to the Paper {@link GlobalConfiguration}.
 * </p>
 */
@SuppressWarnings({"CanBeFinal", "FieldCanBeLocal", "FieldMayBeFinal", "NotNullFieldNotInitialized", "InnerClassMayBeStatic"})
public class FiddleGlobalConfiguration extends ConfigurationPart {
    private static final Logger LOGGER = LogUtils.getClassLogger();
    static final int CURRENT_VERSION = 1; // (when you change the version, change the comment, so it conflicts on rebases): initial version
    private static FiddleGlobalConfiguration instance;
    public static FiddleGlobalConfiguration get() {
        return instance;
    }
    static void set(final FiddleGlobalConfiguration instance) {
        FiddleGlobalConfiguration.instance = instance;
    }

    @Setting(Configuration.VERSION_FIELD)
    public int version = CURRENT_VERSION;

    public boolean _enabled = false;
}

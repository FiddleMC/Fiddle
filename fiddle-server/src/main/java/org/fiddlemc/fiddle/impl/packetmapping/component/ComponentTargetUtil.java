package org.fiddlemc.fiddle.impl.packetmapping.component;

import io.papermc.paper.adventure.AdventureComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.KeybindContents;
import net.minecraft.network.chat.contents.NbtContents;
import net.minecraft.network.chat.contents.ObjectContents;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.ScoreContents;
import net.minecraft.network.chat.contents.SelectorContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.chat.contents.data.BlockDataSource;
import net.minecraft.network.chat.contents.data.DataSource;
import net.minecraft.network.chat.contents.data.EntityDataSource;
import net.minecraft.network.chat.contents.data.StorageDataSource;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
import net.minecraft.network.chat.contents.objects.ObjectInfo;
import net.minecraft.network.chat.contents.objects.PlayerSprite;
import org.fiddlemc.fiddle.api.packetmapping.component.ComponentTarget;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for getting {@link ComponentTarget}.
 */
public final class ComponentTargetUtil {

    private ComponentTargetUtil() {
        throw new UnsupportedOperationException();
    }

    private static final ComponentTarget[] VALUES = ComponentTarget.values();

    private static final ComponentTarget[] GENERIC_TARGETS = {ComponentTarget.ALL};
    private static final ComponentTarget[] TEXT_TARGETS = {ComponentTarget.ALL, ComponentTarget.TEXT};
    private static final ComponentTarget[] TRANSLATABLE_TARGETS = {ComponentTarget.ALL, ComponentTarget.TRANSLATABLE};
    private static final ComponentTarget[] TRANSLATABLE_WITHOUT_FALLBACK_TARGETS = {ComponentTarget.ALL, ComponentTarget.TRANSLATABLE, ComponentTarget.TRANSLATABLE_WITHOUT_FALLBACK};
    private static final ComponentTarget[] SCORE_TARGETS = {ComponentTarget.ALL, ComponentTarget.SCORE};
    private static final ComponentTarget[] SELECTOR_TARGETS = {ComponentTarget.ALL, ComponentTarget.SELECTOR};
    private static final ComponentTarget[] KEYBIND_TARGETS = {ComponentTarget.ALL, ComponentTarget.KEYBIND};
    private static final ComponentTarget[] NBT_TARGETS = {ComponentTarget.ALL, ComponentTarget.NBT};
    private static final ComponentTarget[] NBT_BLOCK_TARGETS = {ComponentTarget.ALL, ComponentTarget.NBT, ComponentTarget.NBT_BLOCK};
    private static final ComponentTarget[] NBT_ENTITY_TARGETS = {ComponentTarget.ALL, ComponentTarget.NBT, ComponentTarget.NBT_ENTITY};
    private static final ComponentTarget[] NBT_STORAGE_TARGETS = {ComponentTarget.ALL, ComponentTarget.NBT, ComponentTarget.NBT_STORAGE};
    private static final ComponentTarget[] OBJECT_TARGETS = {ComponentTarget.ALL, ComponentTarget.OBJECT};
    private static final ComponentTarget[] OBJECT_ATLAS_TARGETS = {ComponentTarget.ALL, ComponentTarget.OBJECT, ComponentTarget.OBJECT_ATLAS};
    private static final ComponentTarget[] OBJECT_PLAYER_TARGETS = {ComponentTarget.ALL, ComponentTarget.OBJECT, ComponentTarget.OBJECT_PLAYER};

    public static ComponentTarget[] getTargetsThatApply(Component component) {
        if (component instanceof MutableComponent mutableComponent) {
            ComponentContents contents = mutableComponent.getContents();
            if (contents instanceof PlainTextContents) {
                return TEXT_TARGETS;
            } else if (contents instanceof TranslatableContents translatableContents) {
                if (translatableContents.getFallback() == null) {
                    return TRANSLATABLE_WITHOUT_FALLBACK_TARGETS;
                }
                return TRANSLATABLE_TARGETS;
            } else if (contents instanceof ScoreContents) {
                return SCORE_TARGETS;
            } else if (contents instanceof SelectorContents) {
                return SELECTOR_TARGETS;
            } else if (contents instanceof KeybindContents) {
                return KEYBIND_TARGETS;
            } else if (contents instanceof NbtContents nbtContents) {
                DataSource dataSource = nbtContents.getDataSource();
                if (dataSource instanceof BlockDataSource) {
                    return NBT_BLOCK_TARGETS;
                } else if (dataSource instanceof EntityDataSource) {
                    return NBT_ENTITY_TARGETS;
                } else if (dataSource instanceof StorageDataSource) {
                    return NBT_STORAGE_TARGETS;
                }
                return NBT_TARGETS;
            } else if (contents instanceof ObjectContents(ObjectInfo objectInfo)) {
                if (objectInfo instanceof AtlasSprite) {
                    return OBJECT_ATLAS_TARGETS;
                } else if (objectInfo instanceof PlayerSprite) {
                    return OBJECT_PLAYER_TARGETS;
                }
                return OBJECT_TARGETS;
            }
        } else if (component instanceof AdventureComponent adventureComponentHolder) {
            net.kyori.adventure.text.Component adventureComponent = adventureComponentHolder.adventure$component();
            if (adventureComponent instanceof net.kyori.adventure.text.TextComponent) {
                return TEXT_TARGETS;
            } else if (adventureComponent instanceof net.kyori.adventure.text.TranslatableComponent translatableComponent) {
                if (translatableComponent.fallback() == null) {
                    return TRANSLATABLE_WITHOUT_FALLBACK_TARGETS;
                }
                return TRANSLATABLE_TARGETS;
            } else if (adventureComponent instanceof net.kyori.adventure.text.ScoreComponent) {
                return SCORE_TARGETS;
            } else if (adventureComponent instanceof net.kyori.adventure.text.SelectorComponent) {
                return SELECTOR_TARGETS;
            } else if (adventureComponent instanceof net.kyori.adventure.text.KeybindComponent) {
                return KEYBIND_TARGETS;
            } else if (adventureComponent instanceof net.kyori.adventure.text.NBTComponent<?, ?>) {
                if (adventureComponent instanceof net.kyori.adventure.text.BlockNBTComponent) {
                    return NBT_BLOCK_TARGETS;
                } else if (adventureComponent instanceof net.kyori.adventure.text.EntityNBTComponent) {
                    return NBT_ENTITY_TARGETS;
                } else if (adventureComponent instanceof net.kyori.adventure.text.StorageNBTComponent) {
                    return NBT_STORAGE_TARGETS;
                }
                return NBT_TARGETS;
            } else if (adventureComponent instanceof net.kyori.adventure.text.ObjectComponent objectComponent) {
                net.kyori.adventure.text.object.ObjectContents objectContents = objectComponent.contents();
                if (objectContents instanceof net.kyori.adventure.text.object.SpriteObjectContents) {
                    return OBJECT_ATLAS_TARGETS;
                } else if (objectContents instanceof net.kyori.adventure.text.object.PlayerHeadObjectContents) {
                    return OBJECT_PLAYER_TARGETS;
                }
                return OBJECT_TARGETS;
            }
        }
        return GENERIC_TARGETS;
    }

    /**
     * @param a A {@link ComponentTarget}.
     * @param b A different {@link ComponentTarget}.
     * @return Whether target {@code a} implies target {@code b},
     * or a meaningless result if {@code a = b}.
     */
    public static boolean implies(ComponentTarget a, ComponentTarget b) {
        if (a == ComponentTarget.ALL) {
            return true;
        } else if (a == ComponentTarget.TRANSLATABLE) {
            return b == ComponentTarget.TRANSLATABLE_WITHOUT_FALLBACK;
        } else if (a == ComponentTarget.NBT) {
            return b == ComponentTarget.NBT_BLOCK || b == ComponentTarget.NBT_ENTITY || b == ComponentTarget.NBT_STORAGE;
        } else if (a == ComponentTarget.OBJECT) {
            return b == ComponentTarget.OBJECT_ATLAS || b == ComponentTarget.OBJECT_PLAYER;
        }
        return false;
    }

    public static List<ComponentTarget> simplifyTargets(Collection<ComponentTarget> targets) {
        return targets.stream().filter(target -> targets.stream().noneMatch(otherTarget -> otherTarget != target && implies(otherTarget, target))).toList();
    }

    public static ComponentTarget getMostSpecificTarget

    public static ComponentTarget getByOrdinal(int ordinal) {
        return VALUES[ordinal];
    }

}

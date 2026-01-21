package org.fiddlemc.fiddle.impl.minecraft.packet.mapping.item;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.PrioritizableLifecycleEventType;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.fiddlemc.fiddle.api.client.view.ClientView;
import org.fiddlemc.fiddle.api.minecraft.packet.mapping.item.ItemMappingPipeline;
import org.fiddlemc.fiddle.api.minecraft.packet.mapping.item.ItemMappingRegistrar;
import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pipeline of {@link ItemMapping}s.
 */
public final class ItemMappingPipelineImpl implements ItemMappingPipeline {

    private static ItemMappingPipelineImpl INSTANCE;

    public static ItemMappingPipelineImpl get() {
        if (INSTANCE == null) {
            INSTANCE = (ItemMappingPipelineImpl) ItemMappingPipeline.get();
        }
        return INSTANCE;
    }

    public static final class ComposeEventImpl implements ComposeEvent, PaperLifecycleEvent {

        /**
         * Will be made null after baking.
         */
        private @Nullable ItemMappingRegistrarImpl registrar = new ItemMappingRegistrarImpl();

        public ItemMappingRegistrar getRegistrar() {
            return this.registrar;
        }

        public void bake() {
            Map<List<ItemMapping>, List<IntObjectPair<Item>>> transposed = new HashMap<>();
            for (int awarenessLevelI = 0; awarenessLevelI < this.registrar.mappings.length; awarenessLevelI++) {
                for (Map.Entry<Item, List<ItemMapping>> entry : this.registrar.mappings[awarenessLevelI].entrySet()) {
                    transposed.computeIfAbsent(entry.getValue(), $ -> new ArrayList<>()).add(IntObjectPair.of(awarenessLevelI, entry.getKey()));
                }
            }
            ItemMappingPipelineImpl pipeline = ItemMappingPipelineImpl.get();
            for (Map.Entry<List<ItemMapping>, List<IntObjectPair<Item>>> entry : transposed.entrySet()) {
                for (IntObjectPair<Item> target : entry.getValue()) {
                    pipeline.mappings[target.firstInt()].put(target.second(), entry.getKey().toArray(ItemMapping[]::new));
                }
            }
            this.registrar = null;
        }

    }

    public static final class ComposeEventType extends PrioritizableLifecycleEventType.Simple<BootstrapContext, ComposeEvent> {

        private ComposeEventType() {
            super("fiddle_item_mapping/compose", BootstrapContext.class);
        }

    }

    private static ComposeEventType COMPOSE_EVENT_TYPE;

    @Override
    public ComposeEventType compose() {
        if (COMPOSE_EVENT_TYPE == null) {
            COMPOSE_EVENT_TYPE = new ComposeEventType();
        }
        return COMPOSE_EVENT_TYPE;
    }

    /**
     * The registered item mappings.
     *
     * <p>
     * The mappings are organized in an array where {@link ClientView.AwarenessLevel#ordinal()}
     * is the index, and then in a map where {@link Item} is the key.
     * The array does not contain null values, but some maps may not contain every item as a key.
     * The maps do not contain empty arrays.
     * </p>
     */
    private final Map<Item, ItemMapping[]>[] mappings;

    public ItemMappingPipelineImpl() {
        this.mappings = new Map[ClientView.AwarenessLevel.values().length];
        for (int i = 0; i < this.mappings.length; i++) {
            this.mappings[i] = new HashMap<>(16, 0.5f);
        }
    }

    /**
     * {@linkplain ItemMapping#apply Applies} all applicable mappings to the item stack.
     *
     * <p>
     * This method will not modify the given {@code itemStack}.
     * </p>
     *
     * @param context The context of this mapping, where the {@link ItemMapping.Context#getOriginal} is null.
     * @return The resulting {@link ItemStack}, which may or may not be the given {@code itemStack}.
     */
    public ItemStack apply(ItemStack itemStack, ItemMappingContextImpl context) {
        Map<Item, ItemMapping[]> mapForAwarenessLevel = this.mappings[context.getClientView().getAwarenessLevel().ordinal()];
        ItemMapping @Nullable [] mappings = mapForAwarenessLevel.get(itemStack.getItem());
        if (mappings == null) {
            return itemStack;
        }
        ItemStack current = itemStack.copy();
        ItemMappingContextImpl contextWithOriginal = new ItemMappingContextImpl(itemStack, context.getClientView(), context.isItemStackInItemFrame(), context.isStonecutterRecipeResult());
        for (ItemMapping mapping : mappings) {
            @Nullable ItemStack result = mapping.apply(current, contextWithOriginal);
            if (result != null) {
                current = result;
            }
        }
        return current;
    }

}

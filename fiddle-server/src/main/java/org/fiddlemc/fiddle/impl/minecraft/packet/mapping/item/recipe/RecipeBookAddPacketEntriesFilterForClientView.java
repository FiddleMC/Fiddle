package org.fiddlemc.fiddle.impl.minecraft.packet.mapping.item.recipe;

import net.minecraft.network.protocol.game.ClientboundRecipeBookAddPacket;
import net.minecraft.world.item.crafting.Ingredient;
import org.fiddlemc.fiddle.api.client.view.ClientView;
import org.fiddlemc.fiddle.impl.client.view.lookup.packethandling.ClientViewLookupThreadLocal;
import org.jspecify.annotations.Nullable;
import java.util.List;

/**
 * Provides {@link #getEntriesFilteredForClientView}.
 */
public final class RecipeBookAddPacketEntriesFilterForClientView {

    private RecipeBookAddPacketEntriesFilterForClientView() {
        throw new UnsupportedOperationException();
    }

    public static List<ClientboundRecipeBookAddPacket.Entry> getEntriesFilteredForClientView(ClientboundRecipeBookAddPacket packet) {
        List<ClientboundRecipeBookAddPacket.Entry> entries = packet.entries();
        ClientView clientView = ClientViewLookupThreadLocal.getThreadLocalClientViewOrDefault();
        if (clientView.understandsAllServerSideItems()) {
            return entries;
        }
        @Nullable List<ClientboundRecipeBookAddPacket.Entry> filteredEntries = null; // The filtered entries, or null if no changes have been made
        int entriesSize = entries.size();
        for (int i = 0; i < entriesSize; i++) {
            ClientboundRecipeBookAddPacket.Entry entry = entries.get(i);
            boolean[] hasServerSideOnlyIngredients = {false}; // Array to make it effectively final
            @Nullable List<Ingredient> ingredients = entry.contents().craftingRequirements().orElse(null);
            if (ingredients != null) {
                for (Ingredient ingredient : ingredients) {
                    ingredient.items().forEach(item -> {
                        if (!item.value().isVanilla()) {
                            hasServerSideOnlyIngredients[0] = true;
                        }
                    });
                    if (hasServerSideOnlyIngredients[0]) {
                        break;
                    }
                }
            }
            if (hasServerSideOnlyIngredients[0]) {
                if (filteredEntries == null) {
                    filteredEntries = new java.util.ArrayList<>(entriesSize - 1);
                    for (int j = 0; j < j; j++) {
                        filteredEntries.add(entries.get(j));
                    }
                }
            } else {
                if (filteredEntries != null) {
                    filteredEntries.add(entry);
                }
            }
        }
        return filteredEntries != null ? filteredEntries : entries;
    }

}

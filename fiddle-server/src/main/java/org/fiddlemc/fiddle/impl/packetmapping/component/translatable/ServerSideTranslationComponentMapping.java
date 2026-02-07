package org.fiddlemc.fiddle.impl.packetmapping.component.translatable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.fiddlemc.fiddle.api.clientview.ClientView;
import org.fiddlemc.fiddle.api.packetmapping.component.nms.ComponentMappingHandleNMS;
import org.fiddlemc.fiddle.api.packetmapping.component.translatable.ServerSideTranslations;
import org.fiddlemc.fiddle.impl.util.mappingpipeline.MappingPipelineStep;

/**
 * A {@link NMSComponentMapping} that applies
 * {@linkplain ServerSideTranslations registered server-side translations}
 * to {@link Component}s.
 */
public final class ServerSideTranslationComponentMapping implements MappingPipelineStep<ComponentMappingHandleNMS> {

    @Override
    public void apply(final ComponentMappingHandleNMS handle) {
        ClientView clientView = handle.getContext().getClientView();
        if (clientView.understandsAllServerSideTranslatables()) return;
        ComponentContents contents = handle.getImmutable().getContents();
        if (contents instanceof TranslatableContents translatableContents) {
            String key = translatableContents.getKey();
            ServerSideTranslations.ServerSideTranslation translation = ServerSideTranslations.get().get(key, clientView.getLocale());
            if (translation != null) {
                if (translation.overrideClientSide()) {
                    handle.setMutable(Component.literal(translation.translation()));
                } else {
                    handle.setMutable(Component.translatableWithFallback(key, translation.translation()));
                }
            }
        }
    }

}

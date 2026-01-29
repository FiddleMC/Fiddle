package org.fiddlemc.fiddle.api.packetmapping;

import org.fiddlemc.fiddle.api.util.pipeline.MappingPipeline;
import org.jspecify.annotations.Nullable;

/**
 * A pipeline that applies {@linkplain PacketDataMapping mappings} to packet data of type {@link T}.
 */
public interface PacketDataMappingPipeline<T, H extends PacketDataMappingHandle<T>, C extends PacketDataMappingContext, M extends PacketDataMapping<T, H, C>, R extends PacketDataMappingRegistrar<? extends T>> extends MappingPipeline<R> {

    /**
     * @return The smallest possible list containing all mappings in this pipeline
     * that may apply to the given data and context.
     * It may be null to indicate that there are none.
     */
    @Nullable M @Nullable [] getMappingsThatMayApplyTo(T data, C context);

    /**
     * @return A new {@linkplain H handle} for the given data.
     */
    H createHandle(T data);

    /**
     * {@linkplain PacketDataMapping#apply Applies} all applicable mappings to the data.
     *
     * <p>
     * This method will not mutate the given {@code itemStack}.
     * </p>
     *
     * @param context The context of this mapping.
     * @return The resulting data, which may be the given instance if no changes were made.
     */
    default T apply(T data, C context) {
        M @Nullable [] mappings = this.getMappingsThatMayApplyTo(data, context);
        if (mappings == null || mappings.length == 0) {
            return data;
        }
        H handle = this.createHandle(data);
        for (M mapping : mappings) {
            mapping.apply(handle, context);
        }
        return handle.getImmutable();
    }

}

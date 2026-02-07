package org.fiddlemc.fiddle.impl.util.composable;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import org.fiddlemc.fiddle.api.clientview.ClientView;

/**
 * An implementation of {@link KeyedBuilderComposeEventImpl} that includes
 * a {@link ClientView.AwarenessLevel} and another int-like half as part of a {@link Pair} key.
 */
public abstract class AwarenessLevelPairKeyedBuilderComposeEventImpl<K, E, B> extends KeyedBuilderComposeEventImpl<Pair<ClientView.AwarenessLevel, K>, E, B, IntIntPair> {

    protected abstract int keyPartToInt(K key);

    protected abstract K intToKeyPart(int internalKey);

    @Override
    protected IntIntPair keyToInternalKey(final Pair<ClientView.AwarenessLevel, K> key) {
        return IntIntPair.of(key.left().ordinal(), this.keyPartToInt(key.right()));
    }

    @Override
    protected Pair<ClientView.AwarenessLevel, K> internalKeyToKey(final IntIntPair internalKey) {
        return Pair.of(ClientView.AwarenessLevel.getAll()[internalKey.leftInt()], this.intToKeyPart(internalKey.rightInt()));
    }

}

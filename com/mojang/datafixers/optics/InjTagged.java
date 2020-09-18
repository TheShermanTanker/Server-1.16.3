package com.mojang.datafixers.optics;

import java.util.Objects;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;

public final class InjTagged<K, A, B> implements Prism<Pair<K, ?>, Pair<K, ?>, A, B> {
    private final K key;
    
    public InjTagged(final K key) {
        this.key = key;
    }
    
    public Either<Pair<K, ?>, A> match(final Pair<K, ?> pair) {
        return (Either<Pair<K, ?>, A>)(Objects.equals(this.key, pair.getFirst()) ? Either.right(pair.getSecond()) : Either.left(pair));
    }
    
    public Pair<K, ?> build(final B b) {
        return Pair.of(this.key, b);
    }
    
    public String toString() {
        return new StringBuilder().append("inj[").append(this.key).append("]").toString();
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof InjTagged && Objects.equals(((InjTagged)obj).key, this.key);
    }
}

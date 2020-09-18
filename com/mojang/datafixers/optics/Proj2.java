package com.mojang.datafixers.optics;

import com.mojang.datafixers.util.Pair;

public final class Proj2<F, G, G2> implements Lens<Pair<F, G>, Pair<F, G2>, G, G2> {
    public G view(final Pair<F, G> pair) {
        return pair.getSecond();
    }
    
    public Pair<F, G2> update(final G2 newValue, final Pair<F, G> pair) {
        return Pair.<F, G2>of(pair.getFirst(), newValue);
    }
    
    public String toString() {
        return "\u03c02";
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof Proj2;
    }
}

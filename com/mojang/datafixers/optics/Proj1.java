package com.mojang.datafixers.optics;

import com.mojang.datafixers.util.Pair;

public final class Proj1<F, G, F2> implements Lens<Pair<F, G>, Pair<F2, G>, F, F2> {
    public F view(final Pair<F, G> pair) {
        return pair.getFirst();
    }
    
    public Pair<F2, G> update(final F2 newValue, final Pair<F, G> pair) {
        return Pair.<F2, G>of(newValue, pair.getSecond());
    }
    
    public String toString() {
        return "\u03c01";
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof Proj1;
    }
}

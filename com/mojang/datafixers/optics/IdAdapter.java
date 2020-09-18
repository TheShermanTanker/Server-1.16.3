package com.mojang.datafixers.optics;

class IdAdapter<S, T> implements Adapter<S, T, S, T> {
    public S from(final S s) {
        return s;
    }
    
    public T to(final T b) {
        return b;
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof IdAdapter;
    }
    
    public String toString() {
        return "id";
    }
}

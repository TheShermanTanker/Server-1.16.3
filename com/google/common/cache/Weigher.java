package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Weigher<K, V> {
    int weigh(final K object1, final V object2);
}

package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;

public interface ObjectIterable<K> extends Iterable<K> {
    ObjectIterator<K> iterator();
}

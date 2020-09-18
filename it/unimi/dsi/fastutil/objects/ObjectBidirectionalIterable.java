package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;

public interface ObjectBidirectionalIterable<K> extends ObjectIterable<K> {
    ObjectBidirectionalIterator<K> iterator();
}

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2ObjectSortedMap<V> extends Int2ObjectMap<V>, SortedMap<Integer, V> {
    Int2ObjectSortedMap<V> subMap(final int integer1, final int integer2);
    
    Int2ObjectSortedMap<V> headMap(final int integer);
    
    Int2ObjectSortedMap<V> tailMap(final int integer);
    
    int firstIntKey();
    
    int lastIntKey();
    
    @Deprecated
    default Int2ObjectSortedMap<V> subMap(final Integer from, final Integer to) {
        return this.subMap((int)from, (int)to);
    }
    
    @Deprecated
    default Int2ObjectSortedMap<V> headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    default Int2ObjectSortedMap<V> tailMap(final Integer from) {
        return this.tailMap((int)from);
    }
    
    @Deprecated
    default Integer firstKey() {
        return this.firstIntKey();
    }
    
    @Deprecated
    default Integer lastKey() {
        return this.lastIntKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Integer, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, V>>)this.int2ObjectEntrySet();
    }
    
    ObjectSortedSet<Entry<V>> int2ObjectEntrySet();
    
    IntSortedSet keySet();
    
    ObjectCollection<V> values();
    
    IntComparator comparator();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Entry<V>>, FastEntrySet<V> {
        ObjectBidirectionalIterator<Entry<V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<V>> fastIterator(final Entry<V> entry);
    }
}

package it.unimi.dsi.fastutil.floats;

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

public interface Float2ObjectSortedMap<V> extends Float2ObjectMap<V>, SortedMap<Float, V> {
    Float2ObjectSortedMap<V> subMap(final float float1, final float float2);
    
    Float2ObjectSortedMap<V> headMap(final float float1);
    
    Float2ObjectSortedMap<V> tailMap(final float float1);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    @Deprecated
    default Float2ObjectSortedMap<V> subMap(final Float from, final Float to) {
        return this.subMap((float)from, (float)to);
    }
    
    @Deprecated
    default Float2ObjectSortedMap<V> headMap(final Float to) {
        return this.headMap((float)to);
    }
    
    @Deprecated
    default Float2ObjectSortedMap<V> tailMap(final Float from) {
        return this.tailMap((float)from);
    }
    
    @Deprecated
    default Float firstKey() {
        return this.firstFloatKey();
    }
    
    @Deprecated
    default Float lastKey() {
        return this.lastFloatKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Float, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Float, V>>)this.float2ObjectEntrySet();
    }
    
    ObjectSortedSet<Entry<V>> float2ObjectEntrySet();
    
    FloatSortedSet keySet();
    
    ObjectCollection<V> values();
    
    FloatComparator comparator();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Entry<V>>, FastEntrySet<V> {
        ObjectBidirectionalIterator<Entry<V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<V>> fastIterator(final Entry<V> entry);
    }
}

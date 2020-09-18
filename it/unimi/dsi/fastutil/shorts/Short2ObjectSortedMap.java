package it.unimi.dsi.fastutil.shorts;

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

public interface Short2ObjectSortedMap<V> extends Short2ObjectMap<V>, SortedMap<Short, V> {
    Short2ObjectSortedMap<V> subMap(final short short1, final short short2);
    
    Short2ObjectSortedMap<V> headMap(final short short1);
    
    Short2ObjectSortedMap<V> tailMap(final short short1);
    
    short firstShortKey();
    
    short lastShortKey();
    
    @Deprecated
    default Short2ObjectSortedMap<V> subMap(final Short from, final Short to) {
        return this.subMap((short)from, (short)to);
    }
    
    @Deprecated
    default Short2ObjectSortedMap<V> headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    default Short2ObjectSortedMap<V> tailMap(final Short from) {
        return this.tailMap((short)from);
    }
    
    @Deprecated
    default Short firstKey() {
        return this.firstShortKey();
    }
    
    @Deprecated
    default Short lastKey() {
        return this.lastShortKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Short, V>>)this.short2ObjectEntrySet();
    }
    
    ObjectSortedSet<Entry<V>> short2ObjectEntrySet();
    
    ShortSortedSet keySet();
    
    ObjectCollection<V> values();
    
    ShortComparator comparator();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Entry<V>>, FastEntrySet<V> {
        ObjectBidirectionalIterator<Entry<V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<V>> fastIterator(final Entry<V> entry);
    }
}

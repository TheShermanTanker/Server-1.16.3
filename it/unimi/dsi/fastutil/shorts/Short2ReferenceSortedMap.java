package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2ReferenceSortedMap<V> extends Short2ReferenceMap<V>, SortedMap<Short, V> {
    Short2ReferenceSortedMap<V> subMap(final short short1, final short short2);
    
    Short2ReferenceSortedMap<V> headMap(final short short1);
    
    Short2ReferenceSortedMap<V> tailMap(final short short1);
    
    short firstShortKey();
    
    short lastShortKey();
    
    @Deprecated
    default Short2ReferenceSortedMap<V> subMap(final Short from, final Short to) {
        return this.subMap((short)from, (short)to);
    }
    
    @Deprecated
    default Short2ReferenceSortedMap<V> headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    default Short2ReferenceSortedMap<V> tailMap(final Short from) {
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
        return (ObjectSortedSet<Map.Entry<Short, V>>)this.short2ReferenceEntrySet();
    }
    
    ObjectSortedSet<Entry<V>> short2ReferenceEntrySet();
    
    ShortSortedSet keySet();
    
    ReferenceCollection<V> values();
    
    ShortComparator comparator();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Entry<V>>, FastEntrySet<V> {
        ObjectBidirectionalIterator<Entry<V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<V>> fastIterator(final Entry<V> entry);
    }
}

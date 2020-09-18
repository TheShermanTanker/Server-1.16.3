package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2FloatSortedMap extends Short2FloatMap, SortedMap<Short, Float> {
    Short2FloatSortedMap subMap(final short short1, final short short2);
    
    Short2FloatSortedMap headMap(final short short1);
    
    Short2FloatSortedMap tailMap(final short short1);
    
    short firstShortKey();
    
    short lastShortKey();
    
    @Deprecated
    default Short2FloatSortedMap subMap(final Short from, final Short to) {
        return this.subMap((short)from, (short)to);
    }
    
    @Deprecated
    default Short2FloatSortedMap headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    default Short2FloatSortedMap tailMap(final Short from) {
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
    default ObjectSortedSet<Map.Entry<Short, Float>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Short, Float>>)this.short2FloatEntrySet();
    }
    
    ObjectSortedSet<Entry> short2FloatEntrySet();
    
    ShortSortedSet keySet();
    
    FloatCollection values();
    
    ShortComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

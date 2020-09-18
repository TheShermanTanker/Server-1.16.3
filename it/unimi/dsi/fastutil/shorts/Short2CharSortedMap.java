package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2CharSortedMap extends Short2CharMap, SortedMap<Short, Character> {
    Short2CharSortedMap subMap(final short short1, final short short2);
    
    Short2CharSortedMap headMap(final short short1);
    
    Short2CharSortedMap tailMap(final short short1);
    
    short firstShortKey();
    
    short lastShortKey();
    
    @Deprecated
    default Short2CharSortedMap subMap(final Short from, final Short to) {
        return this.subMap((short)from, (short)to);
    }
    
    @Deprecated
    default Short2CharSortedMap headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    default Short2CharSortedMap tailMap(final Short from) {
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
    default ObjectSortedSet<Map.Entry<Short, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Short, Character>>)this.short2CharEntrySet();
    }
    
    ObjectSortedSet<Entry> short2CharEntrySet();
    
    ShortSortedSet keySet();
    
    CharCollection values();
    
    ShortComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

package it.unimi.dsi.fastutil.ints;

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

public interface Int2CharSortedMap extends Int2CharMap, SortedMap<Integer, Character> {
    Int2CharSortedMap subMap(final int integer1, final int integer2);
    
    Int2CharSortedMap headMap(final int integer);
    
    Int2CharSortedMap tailMap(final int integer);
    
    int firstIntKey();
    
    int lastIntKey();
    
    @Deprecated
    default Int2CharSortedMap subMap(final Integer from, final Integer to) {
        return this.subMap((int)from, (int)to);
    }
    
    @Deprecated
    default Int2CharSortedMap headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    default Int2CharSortedMap tailMap(final Integer from) {
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
    default ObjectSortedSet<Map.Entry<Integer, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, Character>>)this.int2CharEntrySet();
    }
    
    ObjectSortedSet<Entry> int2CharEntrySet();
    
    IntSortedSet keySet();
    
    CharCollection values();
    
    IntComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

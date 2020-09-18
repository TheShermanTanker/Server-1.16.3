package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Int2ByteSortedMap extends Int2ByteMap, SortedMap<Integer, Byte> {
    Int2ByteSortedMap subMap(final int integer1, final int integer2);
    
    Int2ByteSortedMap headMap(final int integer);
    
    Int2ByteSortedMap tailMap(final int integer);
    
    int firstIntKey();
    
    int lastIntKey();
    
    @Deprecated
    default Int2ByteSortedMap subMap(final Integer from, final Integer to) {
        return this.subMap((int)from, (int)to);
    }
    
    @Deprecated
    default Int2ByteSortedMap headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    default Int2ByteSortedMap tailMap(final Integer from) {
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
    default ObjectSortedSet<Map.Entry<Integer, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, Byte>>)this.int2ByteEntrySet();
    }
    
    ObjectSortedSet<Entry> int2ByteEntrySet();
    
    IntSortedSet keySet();
    
    ByteCollection values();
    
    IntComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

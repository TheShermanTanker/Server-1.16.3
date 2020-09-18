package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2ShortSortedMap extends Byte2ShortMap, SortedMap<Byte, Short> {
    Byte2ShortSortedMap subMap(final byte byte1, final byte byte2);
    
    Byte2ShortSortedMap headMap(final byte byte1);
    
    Byte2ShortSortedMap tailMap(final byte byte1);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    @Deprecated
    default Byte2ShortSortedMap subMap(final Byte from, final Byte to) {
        return this.subMap((byte)from, (byte)to);
    }
    
    @Deprecated
    default Byte2ShortSortedMap headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    default Byte2ShortSortedMap tailMap(final Byte from) {
        return this.tailMap((byte)from);
    }
    
    @Deprecated
    default Byte firstKey() {
        return this.firstByteKey();
    }
    
    @Deprecated
    default Byte lastKey() {
        return this.lastByteKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Byte, Short>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, Short>>)this.byte2ShortEntrySet();
    }
    
    ObjectSortedSet<Entry> byte2ShortEntrySet();
    
    ByteSortedSet keySet();
    
    ShortCollection values();
    
    ByteComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

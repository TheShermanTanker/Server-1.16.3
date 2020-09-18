package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2ByteSortedMap extends Byte2ByteMap, SortedMap<Byte, Byte> {
    Byte2ByteSortedMap subMap(final byte byte1, final byte byte2);
    
    Byte2ByteSortedMap headMap(final byte byte1);
    
    Byte2ByteSortedMap tailMap(final byte byte1);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    @Deprecated
    default Byte2ByteSortedMap subMap(final Byte from, final Byte to) {
        return this.subMap((byte)from, (byte)to);
    }
    
    @Deprecated
    default Byte2ByteSortedMap headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    default Byte2ByteSortedMap tailMap(final Byte from) {
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
    default ObjectSortedSet<Map.Entry<Byte, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, Byte>>)this.byte2ByteEntrySet();
    }
    
    ObjectSortedSet<Entry> byte2ByteEntrySet();
    
    ByteSortedSet keySet();
    
    ByteCollection values();
    
    ByteComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Byte2BooleanSortedMap extends Byte2BooleanMap, SortedMap<Byte, Boolean> {
    Byte2BooleanSortedMap subMap(final byte byte1, final byte byte2);
    
    Byte2BooleanSortedMap headMap(final byte byte1);
    
    Byte2BooleanSortedMap tailMap(final byte byte1);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    @Deprecated
    default Byte2BooleanSortedMap subMap(final Byte from, final Byte to) {
        return this.subMap((byte)from, (byte)to);
    }
    
    @Deprecated
    default Byte2BooleanSortedMap headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    default Byte2BooleanSortedMap tailMap(final Byte from) {
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
    default ObjectSortedSet<Map.Entry<Byte, Boolean>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, Boolean>>)this.byte2BooleanEntrySet();
    }
    
    ObjectSortedSet<Entry> byte2BooleanEntrySet();
    
    ByteSortedSet keySet();
    
    BooleanCollection values();
    
    ByteComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

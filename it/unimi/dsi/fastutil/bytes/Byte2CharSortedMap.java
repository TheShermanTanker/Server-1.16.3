package it.unimi.dsi.fastutil.bytes;

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

public interface Byte2CharSortedMap extends Byte2CharMap, SortedMap<Byte, Character> {
    Byte2CharSortedMap subMap(final byte byte1, final byte byte2);
    
    Byte2CharSortedMap headMap(final byte byte1);
    
    Byte2CharSortedMap tailMap(final byte byte1);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    @Deprecated
    default Byte2CharSortedMap subMap(final Byte from, final Byte to) {
        return this.subMap((byte)from, (byte)to);
    }
    
    @Deprecated
    default Byte2CharSortedMap headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    default Byte2CharSortedMap tailMap(final Byte from) {
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
    default ObjectSortedSet<Map.Entry<Byte, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, Character>>)this.byte2CharEntrySet();
    }
    
    ObjectSortedSet<Entry> byte2CharEntrySet();
    
    ByteSortedSet keySet();
    
    CharCollection values();
    
    ByteComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

package it.unimi.dsi.fastutil.bytes;

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

public interface Byte2ObjectSortedMap<V> extends Byte2ObjectMap<V>, SortedMap<Byte, V> {
    Byte2ObjectSortedMap<V> subMap(final byte byte1, final byte byte2);
    
    Byte2ObjectSortedMap<V> headMap(final byte byte1);
    
    Byte2ObjectSortedMap<V> tailMap(final byte byte1);
    
    byte firstByteKey();
    
    byte lastByteKey();
    
    @Deprecated
    default Byte2ObjectSortedMap<V> subMap(final Byte from, final Byte to) {
        return this.subMap((byte)from, (byte)to);
    }
    
    @Deprecated
    default Byte2ObjectSortedMap<V> headMap(final Byte to) {
        return this.headMap((byte)to);
    }
    
    @Deprecated
    default Byte2ObjectSortedMap<V> tailMap(final Byte from) {
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
    default ObjectSortedSet<Map.Entry<Byte, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Byte, V>>)this.byte2ObjectEntrySet();
    }
    
    ObjectSortedSet<Entry<V>> byte2ObjectEntrySet();
    
    ByteSortedSet keySet();
    
    ObjectCollection<V> values();
    
    ByteComparator comparator();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Entry<V>>, FastEntrySet<V> {
        ObjectBidirectionalIterator<Entry<V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<V>> fastIterator(final Entry<V> entry);
    }
}

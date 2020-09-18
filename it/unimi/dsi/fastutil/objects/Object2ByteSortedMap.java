package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ByteSortedMap<K> extends Object2ByteMap<K>, SortedMap<K, Byte> {
    Object2ByteSortedMap<K> subMap(final K object1, final K object2);
    
    Object2ByteSortedMap<K> headMap(final K object);
    
    Object2ByteSortedMap<K> tailMap(final K object);
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<K, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Byte>>)this.object2ByteEntrySet();
    }
    
    ObjectSortedSet<Entry<K>> object2ByteEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    ByteCollection values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Entry<K>>, FastEntrySet<K> {
        ObjectBidirectionalIterator<Entry<K>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K>> fastIterator(final Entry<K> entry);
    }
}

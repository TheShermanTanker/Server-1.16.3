package it.unimi.dsi.fastutil.floats;

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

public interface Float2ByteSortedMap extends Float2ByteMap, SortedMap<Float, Byte> {
    Float2ByteSortedMap subMap(final float float1, final float float2);
    
    Float2ByteSortedMap headMap(final float float1);
    
    Float2ByteSortedMap tailMap(final float float1);
    
    float firstFloatKey();
    
    float lastFloatKey();
    
    @Deprecated
    default Float2ByteSortedMap subMap(final Float from, final Float to) {
        return this.subMap((float)from, (float)to);
    }
    
    @Deprecated
    default Float2ByteSortedMap headMap(final Float to) {
        return this.headMap((float)to);
    }
    
    @Deprecated
    default Float2ByteSortedMap tailMap(final Float from) {
        return this.tailMap((float)from);
    }
    
    @Deprecated
    default Float firstKey() {
        return this.firstFloatKey();
    }
    
    @Deprecated
    default Float lastKey() {
        return this.lastFloatKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Float, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Float, Byte>>)this.float2ByteEntrySet();
    }
    
    ObjectSortedSet<Entry> float2ByteEntrySet();
    
    FloatSortedSet keySet();
    
    ByteCollection values();
    
    FloatComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

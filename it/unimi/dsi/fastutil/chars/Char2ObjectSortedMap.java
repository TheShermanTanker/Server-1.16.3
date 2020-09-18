package it.unimi.dsi.fastutil.chars;

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

public interface Char2ObjectSortedMap<V> extends Char2ObjectMap<V>, SortedMap<Character, V> {
    Char2ObjectSortedMap<V> subMap(final char character1, final char character2);
    
    Char2ObjectSortedMap<V> headMap(final char character);
    
    Char2ObjectSortedMap<V> tailMap(final char character);
    
    char firstCharKey();
    
    char lastCharKey();
    
    @Deprecated
    default Char2ObjectSortedMap<V> subMap(final Character from, final Character to) {
        return this.subMap((char)from, (char)to);
    }
    
    @Deprecated
    default Char2ObjectSortedMap<V> headMap(final Character to) {
        return this.headMap((char)to);
    }
    
    @Deprecated
    default Char2ObjectSortedMap<V> tailMap(final Character from) {
        return this.tailMap((char)from);
    }
    
    @Deprecated
    default Character firstKey() {
        return this.firstCharKey();
    }
    
    @Deprecated
    default Character lastKey() {
        return this.lastCharKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Character, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Character, V>>)this.char2ObjectEntrySet();
    }
    
    ObjectSortedSet<Entry<V>> char2ObjectEntrySet();
    
    CharSortedSet keySet();
    
    ObjectCollection<V> values();
    
    CharComparator comparator();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Entry<V>>, FastEntrySet<V> {
        ObjectBidirectionalIterator<Entry<V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<V>> fastIterator(final Entry<V> entry);
    }
}

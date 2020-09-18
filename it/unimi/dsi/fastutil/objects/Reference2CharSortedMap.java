package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2CharSortedMap<K> extends Reference2CharMap<K>, SortedMap<K, Character> {
    Reference2CharSortedMap<K> subMap(final K object1, final K object2);
    
    Reference2CharSortedMap<K> headMap(final K object);
    
    Reference2CharSortedMap<K> tailMap(final K object);
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<K, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Character>>)this.reference2CharEntrySet();
    }
    
    ObjectSortedSet<Entry<K>> reference2CharEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    CharCollection values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Entry<K>>, FastEntrySet<K> {
        ObjectBidirectionalIterator<Entry<K>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K>> fastIterator(final Entry<K> entry);
    }
}

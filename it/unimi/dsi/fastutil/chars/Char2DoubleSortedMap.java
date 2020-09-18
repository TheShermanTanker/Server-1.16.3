package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2DoubleSortedMap extends Char2DoubleMap, SortedMap<Character, Double> {
    Char2DoubleSortedMap subMap(final char character1, final char character2);
    
    Char2DoubleSortedMap headMap(final char character);
    
    Char2DoubleSortedMap tailMap(final char character);
    
    char firstCharKey();
    
    char lastCharKey();
    
    @Deprecated
    default Char2DoubleSortedMap subMap(final Character from, final Character to) {
        return this.subMap((char)from, (char)to);
    }
    
    @Deprecated
    default Char2DoubleSortedMap headMap(final Character to) {
        return this.headMap((char)to);
    }
    
    @Deprecated
    default Char2DoubleSortedMap tailMap(final Character from) {
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
    default ObjectSortedSet<Map.Entry<Character, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Character, Double>>)this.char2DoubleEntrySet();
    }
    
    ObjectSortedSet<Entry> char2DoubleEntrySet();
    
    CharSortedSet keySet();
    
    DoubleCollection values();
    
    CharComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Char2FloatSortedMap extends Char2FloatMap, SortedMap<Character, Float> {
    Char2FloatSortedMap subMap(final char character1, final char character2);
    
    Char2FloatSortedMap headMap(final char character);
    
    Char2FloatSortedMap tailMap(final char character);
    
    char firstCharKey();
    
    char lastCharKey();
    
    @Deprecated
    default Char2FloatSortedMap subMap(final Character from, final Character to) {
        return this.subMap((char)from, (char)to);
    }
    
    @Deprecated
    default Char2FloatSortedMap headMap(final Character to) {
        return this.headMap((char)to);
    }
    
    @Deprecated
    default Char2FloatSortedMap tailMap(final Character from) {
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
    default ObjectSortedSet<Map.Entry<Character, Float>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Character, Float>>)this.char2FloatEntrySet();
    }
    
    ObjectSortedSet<Entry> char2FloatEntrySet();
    
    CharSortedSet keySet();
    
    FloatCollection values();
    
    CharComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

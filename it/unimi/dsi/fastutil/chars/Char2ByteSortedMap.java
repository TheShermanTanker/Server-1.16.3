package it.unimi.dsi.fastutil.chars;

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

public interface Char2ByteSortedMap extends Char2ByteMap, SortedMap<Character, Byte> {
    Char2ByteSortedMap subMap(final char character1, final char character2);
    
    Char2ByteSortedMap headMap(final char character);
    
    Char2ByteSortedMap tailMap(final char character);
    
    char firstCharKey();
    
    char lastCharKey();
    
    @Deprecated
    default Char2ByteSortedMap subMap(final Character from, final Character to) {
        return this.subMap((char)from, (char)to);
    }
    
    @Deprecated
    default Char2ByteSortedMap headMap(final Character to) {
        return this.headMap((char)to);
    }
    
    @Deprecated
    default Char2ByteSortedMap tailMap(final Character from) {
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
    default ObjectSortedSet<Map.Entry<Character, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Character, Byte>>)this.char2ByteEntrySet();
    }
    
    ObjectSortedSet<Entry> char2ByteEntrySet();
    
    CharSortedSet keySet();
    
    ByteCollection values();
    
    CharComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}

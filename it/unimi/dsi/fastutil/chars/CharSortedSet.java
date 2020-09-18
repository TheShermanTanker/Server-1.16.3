package it.unimi.dsi.fastutil.chars;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public interface CharSortedSet extends CharSet, SortedSet<Character>, CharBidirectionalIterable {
    CharBidirectionalIterator iterator(final char character);
    
    CharBidirectionalIterator iterator();
    
    CharSortedSet subSet(final char character1, final char character2);
    
    CharSortedSet headSet(final char character);
    
    CharSortedSet tailSet(final char character);
    
    CharComparator comparator();
    
    char firstChar();
    
    char lastChar();
    
    @Deprecated
    default CharSortedSet subSet(final Character from, final Character to) {
        return this.subSet((char)from, (char)to);
    }
    
    @Deprecated
    default CharSortedSet headSet(final Character to) {
        return this.headSet((char)to);
    }
    
    @Deprecated
    default CharSortedSet tailSet(final Character from) {
        return this.tailSet((char)from);
    }
    
    @Deprecated
    default Character first() {
        return this.firstChar();
    }
    
    @Deprecated
    default Character last() {
        return this.lastChar();
    }
}

package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.BigList;

public interface CharBigList extends BigList<Character>, CharCollection, Size64, Comparable<BigList<? extends Character>> {
    CharBigListIterator iterator();
    
    CharBigListIterator listIterator();
    
    CharBigListIterator listIterator(final long long1);
    
    CharBigList subList(final long long1, final long long2);
    
    void getElements(final long long1, final char[][] arr, final long long3, final long long4);
    
    void removeElements(final long long1, final long long2);
    
    void addElements(final long long1, final char[][] arr);
    
    void addElements(final long long1, final char[][] arr, final long long3, final long long4);
    
    void add(final long long1, final char character);
    
    boolean addAll(final long long1, final CharCollection charCollection);
    
    boolean addAll(final long long1, final CharBigList charBigList);
    
    boolean addAll(final CharBigList charBigList);
    
    char getChar(final long long1);
    
    char removeChar(final long long1);
    
    char set(final long long1, final char character);
    
    long indexOf(final char character);
    
    long lastIndexOf(final char character);
    
    @Deprecated
    void add(final long long1, final Character character);
    
    @Deprecated
    Character get(final long long1);
    
    @Deprecated
    long indexOf(final Object object);
    
    @Deprecated
    long lastIndexOf(final Object object);
    
    @Deprecated
    Character remove(final long long1);
    
    @Deprecated
    Character set(final long long1, final Character character);
}

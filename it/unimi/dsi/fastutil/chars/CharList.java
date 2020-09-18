package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public interface CharList extends List<Character>, Comparable<List<? extends Character>>, CharCollection {
    CharListIterator iterator();
    
    CharListIterator listIterator();
    
    CharListIterator listIterator(final int integer);
    
    CharList subList(final int integer1, final int integer2);
    
    void size(final int integer);
    
    void getElements(final int integer1, final char[] arr, final int integer3, final int integer4);
    
    void removeElements(final int integer1, final int integer2);
    
    void addElements(final int integer, final char[] arr);
    
    void addElements(final int integer1, final char[] arr, final int integer3, final int integer4);
    
    boolean add(final char character);
    
    void add(final int integer, final char character);
    
    @Deprecated
    default void add(final int index, final Character key) {
        this.add(index, (char)key);
    }
    
    boolean addAll(final int integer, final CharCollection charCollection);
    
    boolean addAll(final int integer, final CharList charList);
    
    boolean addAll(final CharList charList);
    
    char set(final int integer, final char character);
    
    char getChar(final int integer);
    
    int indexOf(final char character);
    
    int lastIndexOf(final char character);
    
    @Deprecated
    default boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    default Character get(final int index) {
        return this.getChar(index);
    }
    
    @Deprecated
    default int indexOf(final Object o) {
        return this.indexOf((char)o);
    }
    
    @Deprecated
    default int lastIndexOf(final Object o) {
        return this.lastIndexOf((char)o);
    }
    
    @Deprecated
    default boolean add(final Character k) {
        return this.add((char)k);
    }
    
    char removeChar(final int integer);
    
    @Deprecated
    default boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Deprecated
    default Character remove(final int index) {
        return this.removeChar(index);
    }
    
    @Deprecated
    default Character set(final int index, final Character k) {
        return this.set(index, (char)k);
    }
}

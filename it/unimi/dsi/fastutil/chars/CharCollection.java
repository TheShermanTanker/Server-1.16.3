package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.Collection;

public interface CharCollection extends Collection<Character>, CharIterable {
    CharIterator iterator();
    
    boolean add(final char character);
    
    boolean contains(final char character);
    
    boolean rem(final char character);
    
    @Deprecated
    default boolean add(final Character key) {
        return this.add((char)key);
    }
    
    @Deprecated
    default boolean contains(final Object key) {
        return key != null && this.contains((char)key);
    }
    
    @Deprecated
    default boolean remove(final Object key) {
        return key != null && this.rem((char)key);
    }
    
    char[] toCharArray();
    
    @Deprecated
    char[] toCharArray(final char[] arr);
    
    char[] toArray(final char[] arr);
    
    boolean addAll(final CharCollection charCollection);
    
    boolean containsAll(final CharCollection charCollection);
    
    boolean removeAll(final CharCollection charCollection);
    
    @Deprecated
    default boolean removeIf(final Predicate<? super Character> filter) {
        return this.removeIf(key -> filter.test(SafeMath.safeIntToChar(key)));
    }
    
    default boolean removeIf(final IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final CharIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test((int)each.nextChar())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    boolean retainAll(final CharCollection charCollection);
}

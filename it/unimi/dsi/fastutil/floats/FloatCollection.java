package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;
import java.util.Collection;

public interface FloatCollection extends Collection<Float>, FloatIterable {
    FloatIterator iterator();
    
    boolean add(final float float1);
    
    boolean contains(final float float1);
    
    boolean rem(final float float1);
    
    @Deprecated
    default boolean add(final Float key) {
        return this.add((float)key);
    }
    
    @Deprecated
    default boolean contains(final Object key) {
        return key != null && this.contains((float)key);
    }
    
    @Deprecated
    default boolean remove(final Object key) {
        return key != null && this.rem((float)key);
    }
    
    float[] toFloatArray();
    
    @Deprecated
    float[] toFloatArray(final float[] arr);
    
    float[] toArray(final float[] arr);
    
    boolean addAll(final FloatCollection floatCollection);
    
    boolean containsAll(final FloatCollection floatCollection);
    
    boolean removeAll(final FloatCollection floatCollection);
    
    @Deprecated
    default boolean removeIf(final Predicate<? super Float> filter) {
        return this.removeIf(key -> filter.test(SafeMath.safeDoubleToFloat(key)));
    }
    
    default boolean removeIf(final DoublePredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final FloatIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test((double)each.nextFloat())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    boolean retainAll(final FloatCollection floatCollection);
}

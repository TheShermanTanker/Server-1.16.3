package com.google.common.base;

import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class PairwiseEquivalence<T> extends Equivalence<Iterable<T>> implements Serializable {
    final Equivalence<? super T> elementEquivalence;
    private static final long serialVersionUID = 1L;
    
    PairwiseEquivalence(final Equivalence<? super T> elementEquivalence) {
        this.elementEquivalence = Preconditions.<Equivalence<? super T>>checkNotNull(elementEquivalence);
    }
    
    @Override
    protected boolean doEquivalent(final Iterable<T> iterableA, final Iterable<T> iterableB) {
        final Iterator<T> iteratorA = (Iterator<T>)iterableA.iterator();
        final Iterator<T> iteratorB = (Iterator<T>)iterableB.iterator();
        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            if (!this.elementEquivalence.equivalent(iteratorA.next(), iteratorB.next())) {
                return false;
            }
        }
        return !iteratorA.hasNext() && !iteratorB.hasNext();
    }
    
    @Override
    protected int doHash(final Iterable<T> iterable) {
        int hash = 78721;
        for (final T element : iterable) {
            hash = hash * 24943 + this.elementEquivalence.hash(element);
        }
        return hash;
    }
    
    public boolean equals(@Nullable final Object object) {
        if (object instanceof PairwiseEquivalence) {
            final PairwiseEquivalence<?> that = object;
            return this.elementEquivalence.equals(that.elementEquivalence);
        }
        return false;
    }
    
    public int hashCode() {
        return this.elementEquivalence.hashCode() ^ 0x46A3EB07;
    }
    
    public String toString() {
        return new StringBuilder().append(this.elementEquivalence).append(".pairwise()").toString();
    }
}

package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface FloatBidirectionalIterator extends FloatIterator, ObjectBidirectionalIterator<Float> {
    float previousFloat();
    
    @Deprecated
    default Float previous() {
        return this.previousFloat();
    }
    
    default int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousFloat();
        }
        return n - i - 1;
    }
    
    default int skip(final int n) {
        return super.skip(n);
    }
}

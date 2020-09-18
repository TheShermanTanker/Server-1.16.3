package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface LongBidirectionalIterator extends LongIterator, ObjectBidirectionalIterator<Long> {
    long previousLong();
    
    @Deprecated
    default Long previous() {
        return this.previousLong();
    }
    
    default int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousLong();
        }
        return n - i - 1;
    }
    
    default int skip(final int n) {
        return super.skip(n);
    }
}

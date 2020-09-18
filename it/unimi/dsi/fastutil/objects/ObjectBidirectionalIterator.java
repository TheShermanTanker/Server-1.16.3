package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.BidirectionalIterator;

public interface ObjectBidirectionalIterator<K> extends ObjectIterator<K>, BidirectionalIterator<K> {
    default int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previous();
        }
        return n - i - 1;
    }
    
    default int skip(final int n) {
        return super.skip(n);
    }
}

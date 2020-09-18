package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface CharBidirectionalIterator extends CharIterator, ObjectBidirectionalIterator<Character> {
    char previousChar();
    
    @Deprecated
    default Character previous() {
        return this.previousChar();
    }
    
    default int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousChar();
        }
        return n - i - 1;
    }
    
    default int skip(final int n) {
        return super.skip(n);
    }
}

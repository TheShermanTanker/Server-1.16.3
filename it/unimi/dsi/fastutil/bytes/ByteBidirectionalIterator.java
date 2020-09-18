package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface ByteBidirectionalIterator extends ByteIterator, ObjectBidirectionalIterator<Byte> {
    byte previousByte();
    
    @Deprecated
    default Byte previous() {
        return this.previousByte();
    }
    
    default int back(final int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousByte();
        }
        return n - i - 1;
    }
    
    default int skip(final int n) {
        return super.skip(n);
    }
}

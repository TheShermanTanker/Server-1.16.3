package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;

public interface ObjectIterator<K> extends Iterator<K> {
    default int skip(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Argument must be nonnegative: ").append(n).toString());
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.next();
        }
        return n - i - 1;
    }
}

package org.apache.commons.io.input;

import java.io.Serializable;
import java.io.Reader;

public class CharSequenceReader extends Reader implements Serializable {
    private static final long serialVersionUID = 3724187752191401220L;
    private final CharSequence charSequence;
    private int idx;
    private int mark;
    
    public CharSequenceReader(final CharSequence charSequence) {
        this.charSequence = ((charSequence != null) ? charSequence : "");
    }
    
    public void close() {
        this.idx = 0;
        this.mark = 0;
    }
    
    public void mark(final int readAheadLimit) {
        this.mark = this.idx;
    }
    
    public boolean markSupported() {
        return true;
    }
    
    public int read() {
        if (this.idx >= this.charSequence.length()) {
            return -1;
        }
        return this.charSequence.charAt(this.idx++);
    }
    
    public int read(final char[] array, final int offset, final int length) {
        if (this.idx >= this.charSequence.length()) {
            return -1;
        }
        if (array == null) {
            throw new NullPointerException("Character array is missing");
        }
        if (length < 0 || offset < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Array Size=").append(array.length).append(", offset=").append(offset).append(", length=").append(length).toString());
        }
        int count = 0;
        for (int i = 0; i < length; ++i) {
            final int c = this.read();
            if (c == -1) {
                return count;
            }
            array[offset + i] = (char)c;
            ++count;
        }
        return count;
    }
    
    public void reset() {
        this.idx = this.mark;
    }
    
    public long skip(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("Number of characters to skip is less than zero: ").append(n).toString());
        }
        if (this.idx >= this.charSequence.length()) {
            return -1L;
        }
        final int dest = (int)Math.min((long)this.charSequence.length(), this.idx + n);
        final int count = dest - this.idx;
        this.idx = dest;
        return count;
    }
    
    public String toString() {
        return this.charSequence.toString();
    }
}

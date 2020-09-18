package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;

public class NullWriter extends Writer {
    public static final NullWriter NULL_WRITER;
    
    public Writer append(final char c) {
        return this;
    }
    
    public Writer append(final CharSequence csq, final int start, final int end) {
        return this;
    }
    
    public Writer append(final CharSequence csq) {
        return this;
    }
    
    public void write(final int idx) {
    }
    
    public void write(final char[] chr) {
    }
    
    public void write(final char[] chr, final int st, final int end) {
    }
    
    public void write(final String str) {
    }
    
    public void write(final String str, final int st, final int end) {
    }
    
    public void flush() {
    }
    
    public void close() {
    }
    
    static {
        NULL_WRITER = new NullWriter();
    }
}

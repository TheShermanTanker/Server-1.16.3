package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

public class StringBuilderWriter extends Writer implements Serializable {
    private static final long serialVersionUID = -146927496096066153L;
    private final StringBuilder builder;
    
    public StringBuilderWriter() {
        this.builder = new StringBuilder();
    }
    
    public StringBuilderWriter(final int capacity) {
        this.builder = new StringBuilder(capacity);
    }
    
    public StringBuilderWriter(final StringBuilder builder) {
        this.builder = ((builder != null) ? builder : new StringBuilder());
    }
    
    public Writer append(final char value) {
        this.builder.append(value);
        return this;
    }
    
    public Writer append(final CharSequence value) {
        this.builder.append(value);
        return this;
    }
    
    public Writer append(final CharSequence value, final int start, final int end) {
        this.builder.append(value, start, end);
        return this;
    }
    
    public void close() {
    }
    
    public void flush() {
    }
    
    public void write(final String value) {
        if (value != null) {
            this.builder.append(value);
        }
    }
    
    public void write(final char[] value, final int offset, final int length) {
        if (value != null) {
            this.builder.append(value, offset, length);
        }
    }
    
    public StringBuilder getBuilder() {
        return this.builder;
    }
    
    public String toString() {
        return this.builder.toString();
    }
}

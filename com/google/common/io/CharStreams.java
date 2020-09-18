package com.google.common.io;

import java.io.Writer;
import java.io.EOFException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import com.google.common.base.Preconditions;
import java.nio.CharBuffer;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class CharStreams {
    static CharBuffer createBuffer() {
        return CharBuffer.allocate(2048);
    }
    
    private CharStreams() {
    }
    
    @CanIgnoreReturnValue
    public static long copy(final Readable from, final Appendable to) throws IOException {
        Preconditions.<Readable>checkNotNull(from);
        Preconditions.<Appendable>checkNotNull(to);
        final CharBuffer buf = createBuffer();
        long total = 0L;
        while (from.read(buf) != -1) {
            buf.flip();
            to.append((CharSequence)buf);
            total += buf.remaining();
            buf.clear();
        }
        return total;
    }
    
    public static String toString(final Readable r) throws IOException {
        return toStringBuilder(r).toString();
    }
    
    private static StringBuilder toStringBuilder(final Readable r) throws IOException {
        final StringBuilder sb = new StringBuilder();
        copy(r, (Appendable)sb);
        return sb;
    }
    
    public static List<String> readLines(final Readable r) throws IOException {
        final List<String> result = (List<String>)new ArrayList();
        final LineReader lineReader = new LineReader(r);
        String line;
        while ((line = lineReader.readLine()) != null) {
            result.add(line);
        }
        return result;
    }
    
    @CanIgnoreReturnValue
    public static <T> T readLines(final Readable readable, final LineProcessor<T> processor) throws IOException {
        Preconditions.<Readable>checkNotNull(readable);
        Preconditions.<LineProcessor<T>>checkNotNull(processor);
        final LineReader lineReader = new LineReader(readable);
        String line;
        while ((line = lineReader.readLine()) != null && processor.processLine(line)) {}
        return processor.getResult();
    }
    
    @CanIgnoreReturnValue
    public static long exhaust(final Readable readable) throws IOException {
        long total = 0L;
        final CharBuffer buf = createBuffer();
        long read;
        while ((read = readable.read(buf)) != -1L) {
            total += read;
            buf.clear();
        }
        return total;
    }
    
    public static void skipFully(final Reader reader, long n) throws IOException {
        Preconditions.<Reader>checkNotNull(reader);
        while (n > 0L) {
            final long amt = reader.skip(n);
            if (amt == 0L) {
                throw new EOFException();
            }
            n -= amt;
        }
    }
    
    public static Writer nullWriter() {
        return NullWriter.INSTANCE;
    }
    
    public static Writer asWriter(final Appendable target) {
        if (target instanceof Writer) {
            return (Writer)target;
        }
        return new AppendableWriter(target);
    }
    
    private static final class NullWriter extends Writer {
        private static final NullWriter INSTANCE;
        
        public void write(final int c) {
        }
        
        public void write(final char[] cbuf) {
            Preconditions.<char[]>checkNotNull(cbuf);
        }
        
        public void write(final char[] cbuf, final int off, final int len) {
            Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
        }
        
        public void write(final String str) {
            Preconditions.<String>checkNotNull(str);
        }
        
        public void write(final String str, final int off, final int len) {
            Preconditions.checkPositionIndexes(off, off + len, str.length());
        }
        
        public Writer append(final CharSequence csq) {
            Preconditions.<CharSequence>checkNotNull(csq);
            return this;
        }
        
        public Writer append(final CharSequence csq, final int start, final int end) {
            Preconditions.checkPositionIndexes(start, end, csq.length());
            return this;
        }
        
        public Writer append(final char c) {
            return this;
        }
        
        public void flush() {
        }
        
        public void close() {
        }
        
        public String toString() {
            return "CharStreams.nullWriter()";
        }
        
        static {
            INSTANCE = new NullWriter();
        }
    }
}

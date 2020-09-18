package com.google.common.io;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public abstract class CharSink {
    protected CharSink() {
    }
    
    public abstract Writer openStream() throws IOException;
    
    public Writer openBufferedStream() throws IOException {
        final Writer writer = this.openStream();
        return (Writer)((writer instanceof BufferedWriter) ? writer : new BufferedWriter(writer));
    }
    
    public void write(final CharSequence charSequence) throws IOException {
        Preconditions.<CharSequence>checkNotNull(charSequence);
        final Closer closer = Closer.create();
        try {
            final Writer out = closer.<Writer>register(this.openStream());
            out.append(charSequence);
            out.flush();
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public void writeLines(final Iterable<? extends CharSequence> lines) throws IOException {
        this.writeLines(lines, System.getProperty("line.separator"));
    }
    
    public void writeLines(final Iterable<? extends CharSequence> lines, final String lineSeparator) throws IOException {
        Preconditions.<Iterable<? extends CharSequence>>checkNotNull(lines);
        Preconditions.<String>checkNotNull(lineSeparator);
        final Closer closer = Closer.create();
        try {
            final Writer out = closer.<Writer>register(this.openBufferedStream());
            for (final CharSequence line : lines) {
                out.append(line).append((CharSequence)lineSeparator);
            }
            out.flush();
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @CanIgnoreReturnValue
    public long writeFrom(final Readable readable) throws IOException {
        Preconditions.<Readable>checkNotNull(readable);
        final Closer closer = Closer.create();
        try {
            final Writer out = closer.<Writer>register(this.openStream());
            final long written = CharStreams.copy(readable, (Appendable)out);
            out.flush();
            return written;
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
}

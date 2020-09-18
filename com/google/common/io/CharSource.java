package com.google.common.io;

import com.google.common.base.Ascii;
import com.google.common.collect.AbstractIterator;
import com.google.common.base.Splitter;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import java.io.Writer;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import com.google.common.annotations.Beta;
import java.nio.charset.Charset;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public abstract class CharSource {
    protected CharSource() {
    }
    
    @Beta
    public ByteSource asByteSource(final Charset charset) {
        return new AsByteSource(charset);
    }
    
    public abstract Reader openStream() throws IOException;
    
    public BufferedReader openBufferedStream() throws IOException {
        final Reader reader = this.openStream();
        return (reader instanceof BufferedReader) ? reader : new BufferedReader(reader);
    }
    
    @Beta
    public Optional<Long> lengthIfKnown() {
        return Optional.<Long>absent();
    }
    
    @Beta
    public long length() throws IOException {
        final Optional<Long> lengthIfKnown = this.lengthIfKnown();
        if (lengthIfKnown.isPresent()) {
            return lengthIfKnown.get();
        }
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.<Reader>register(this.openStream());
            return this.countBySkipping(reader);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    private long countBySkipping(final Reader reader) throws IOException {
        long count = 0L;
        long read;
        while ((read = reader.skip(Long.MAX_VALUE)) != 0L) {
            count += read;
        }
        return count;
    }
    
    @CanIgnoreReturnValue
    public long copyTo(final Appendable appendable) throws IOException {
        Preconditions.<Appendable>checkNotNull(appendable);
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.<Reader>register(this.openStream());
            return CharStreams.copy((Readable)reader, appendable);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @CanIgnoreReturnValue
    public long copyTo(final CharSink sink) throws IOException {
        Preconditions.<CharSink>checkNotNull(sink);
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.<Reader>register(this.openStream());
            final Writer writer = closer.<Writer>register(sink.openStream());
            return CharStreams.copy((Readable)reader, (Appendable)writer);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public String read() throws IOException {
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.<Reader>register(this.openStream());
            return CharStreams.toString((Readable)reader);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @Nullable
    public String readFirstLine() throws IOException {
        final Closer closer = Closer.create();
        try {
            final BufferedReader reader = closer.<BufferedReader>register(this.openBufferedStream());
            return reader.readLine();
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public ImmutableList<String> readLines() throws IOException {
        final Closer closer = Closer.create();
        try {
            final BufferedReader reader = closer.<BufferedReader>register(this.openBufferedStream());
            final List<String> result = Lists.newArrayList();
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            return ImmutableList.<String>copyOf((java.util.Collection<? extends String>)result);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @Beta
    @CanIgnoreReturnValue
    public <T> T readLines(final LineProcessor<T> processor) throws IOException {
        Preconditions.<LineProcessor<T>>checkNotNull(processor);
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.<Reader>register(this.openStream());
            return CharStreams.<T>readLines((Readable)reader, processor);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public boolean isEmpty() throws IOException {
        final Optional<Long> lengthIfKnown = this.lengthIfKnown();
        if (lengthIfKnown.isPresent() && lengthIfKnown.get() == 0L) {
            return true;
        }
        final Closer closer = Closer.create();
        try {
            final Reader reader = closer.<Reader>register(this.openStream());
            return reader.read() == -1;
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public static CharSource concat(final Iterable<? extends CharSource> sources) {
        return new ConcatenatedCharSource(sources);
    }
    
    public static CharSource concat(final Iterator<? extends CharSource> sources) {
        return concat(ImmutableList.copyOf((java.util.Iterator<?>)sources));
    }
    
    public static CharSource concat(final CharSource... sources) {
        return concat(ImmutableList.<CharSource>copyOf(sources));
    }
    
    public static CharSource wrap(final CharSequence charSequence) {
        return new CharSequenceCharSource(charSequence);
    }
    
    public static CharSource empty() {
        return EmptyCharSource.INSTANCE;
    }
    
    private final class AsByteSource extends ByteSource {
        final Charset charset;
        
        AsByteSource(final Charset charset) {
            this.charset = Preconditions.<Charset>checkNotNull(charset);
        }
        
        @Override
        public CharSource asCharSource(final Charset charset) {
            if (charset.equals(this.charset)) {
                return CharSource.this;
            }
            return super.asCharSource(charset);
        }
        
        @Override
        public InputStream openStream() throws IOException {
            return new ReaderInputStream(CharSource.this.openStream(), this.charset, 8192);
        }
        
        public String toString() {
            return CharSource.this.toString() + ".asByteSource(" + this.charset + ")";
        }
    }
    
    private static class CharSequenceCharSource extends CharSource {
        private static final Splitter LINE_SPLITTER;
        private final CharSequence seq;
        
        protected CharSequenceCharSource(final CharSequence seq) {
            this.seq = Preconditions.<CharSequence>checkNotNull(seq);
        }
        
        @Override
        public Reader openStream() {
            return new CharSequenceReader(this.seq);
        }
        
        @Override
        public String read() {
            return this.seq.toString();
        }
        
        @Override
        public boolean isEmpty() {
            return this.seq.length() == 0;
        }
        
        @Override
        public long length() {
            return this.seq.length();
        }
        
        @Override
        public Optional<Long> lengthIfKnown() {
            return Optional.<Long>of((long)this.seq.length());
        }
        
        private Iterable<String> lines() {
            return (Iterable<String>)new Iterable<String>() {
                public Iterator<String> iterator() {
                    return (Iterator<String>)new AbstractIterator<String>() {
                        Iterator<String> lines = CharSequenceCharSource.LINE_SPLITTER.split(CharSequenceCharSource.this.seq).iterator();
                        
                        @Override
                        protected String computeNext() {
                            if (this.lines.hasNext()) {
                                final String next = (String)this.lines.next();
                                if (this.lines.hasNext() || !next.isEmpty()) {
                                    return next;
                                }
                            }
                            return this.endOfData();
                        }
                    };
                }
            };
        }
        
        @Override
        public String readFirstLine() {
            final Iterator<String> lines = (Iterator<String>)this.lines().iterator();
            return lines.hasNext() ? ((String)lines.next()) : null;
        }
        
        @Override
        public ImmutableList<String> readLines() {
            return ImmutableList.<String>copyOf((java.lang.Iterable<? extends String>)this.lines());
        }
        
        @Override
        public <T> T readLines(final LineProcessor<T> processor) throws IOException {
            for (final String line : this.lines()) {
                if (!processor.processLine(line)) {
                    break;
                }
            }
            return processor.getResult();
        }
        
        public String toString() {
            return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
        }
        
        static {
            LINE_SPLITTER = Splitter.onPattern("\r\n|\n|\r");
        }
    }
    
    private static final class EmptyCharSource extends CharSequenceCharSource {
        private static final EmptyCharSource INSTANCE;
        
        private EmptyCharSource() {
            super("");
        }
        
        @Override
        public String toString() {
            return "CharSource.empty()";
        }
        
        static {
            INSTANCE = new EmptyCharSource();
        }
    }
    
    private static final class ConcatenatedCharSource extends CharSource {
        private final Iterable<? extends CharSource> sources;
        
        ConcatenatedCharSource(final Iterable<? extends CharSource> sources) {
            this.sources = Preconditions.<Iterable<? extends CharSource>>checkNotNull(sources);
        }
        
        @Override
        public Reader openStream() throws IOException {
            return new MultiReader(this.sources.iterator());
        }
        
        @Override
        public boolean isEmpty() throws IOException {
            for (final CharSource source : this.sources) {
                if (!source.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public Optional<Long> lengthIfKnown() {
            long result = 0L;
            for (final CharSource source : this.sources) {
                final Optional<Long> lengthIfKnown = source.lengthIfKnown();
                if (!lengthIfKnown.isPresent()) {
                    return Optional.<Long>absent();
                }
                result += lengthIfKnown.get();
            }
            return Optional.<Long>of(result);
        }
        
        @Override
        public long length() throws IOException {
            long result = 0L;
            for (final CharSource source : this.sources) {
                result += source.length();
            }
            return result;
        }
        
        public String toString() {
            return new StringBuilder().append("CharSource.concat(").append(this.sources).append(")").toString();
        }
    }
}

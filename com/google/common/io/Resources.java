package com.google.common.io;

import java.io.InputStream;
import com.google.common.base.Preconditions;
import com.google.common.base.MoreObjects;
import java.io.OutputStream;
import com.google.common.collect.Lists;
import java.util.List;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.nio.charset.Charset;
import java.net.URL;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class Resources {
    private Resources() {
    }
    
    public static ByteSource asByteSource(final URL url) {
        return new UrlByteSource(url);
    }
    
    public static CharSource asCharSource(final URL url, final Charset charset) {
        return asByteSource(url).asCharSource(charset);
    }
    
    public static byte[] toByteArray(final URL url) throws IOException {
        return asByteSource(url).read();
    }
    
    public static String toString(final URL url, final Charset charset) throws IOException {
        return asCharSource(url, charset).read();
    }
    
    @CanIgnoreReturnValue
    public static <T> T readLines(final URL url, final Charset charset, final LineProcessor<T> callback) throws IOException {
        return asCharSource(url, charset).<T>readLines(callback);
    }
    
    public static List<String> readLines(final URL url, final Charset charset) throws IOException {
        return Resources.readLines(url, charset, (LineProcessor<List>)new LineProcessor<List<String>>() {
            final List<String> result = Lists.newArrayList();
            
            public boolean processLine(final String line) {
                this.result.add(line);
                return true;
            }
            
            public List<String> getResult() {
                return this.result;
            }
        });
    }
    
    public static void copy(final URL from, final OutputStream to) throws IOException {
        asByteSource(from).copyTo(to);
    }
    
    @CanIgnoreReturnValue
    public static URL getResource(final String resourceName) {
        final ClassLoader loader = MoreObjects.<ClassLoader>firstNonNull(Thread.currentThread().getContextClassLoader(), Resources.class.getClassLoader());
        final URL url = loader.getResource(resourceName);
        Preconditions.checkArgument(url != null, "resource %s not found.", resourceName);
        return url;
    }
    
    public static URL getResource(final Class<?> contextClass, final String resourceName) {
        final URL url = contextClass.getResource(resourceName);
        Preconditions.checkArgument(url != null, "resource %s relative to %s not found.", resourceName, contextClass.getName());
        return url;
    }
    
    private static final class UrlByteSource extends ByteSource {
        private final URL url;
        
        private UrlByteSource(final URL url) {
            this.url = Preconditions.<URL>checkNotNull(url);
        }
        
        @Override
        public InputStream openStream() throws IOException {
            return this.url.openStream();
        }
        
        public String toString() {
            return new StringBuilder().append("Resources.asByteSource(").append(this.url).append(")").toString();
        }
    }
}

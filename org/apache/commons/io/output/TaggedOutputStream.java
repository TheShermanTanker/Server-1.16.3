package org.apache.commons.io.output;

import java.io.IOException;
import org.apache.commons.io.TaggedIOException;
import java.util.UUID;
import java.io.OutputStream;
import java.io.Serializable;

public class TaggedOutputStream extends ProxyOutputStream {
    private final Serializable tag;
    
    public TaggedOutputStream(final OutputStream proxy) {
        super(proxy);
        this.tag = (Serializable)UUID.randomUUID();
    }
    
    public boolean isCauseOf(final Exception exception) {
        return TaggedIOException.isTaggedWith((Throwable)exception, this.tag);
    }
    
    public void throwIfCauseOf(final Exception exception) throws IOException {
        TaggedIOException.throwCauseIfTaggedWith((Throwable)exception, this.tag);
    }
    
    @Override
    protected void handleIOException(final IOException e) throws IOException {
        throw new TaggedIOException(e, this.tag);
    }
}

package org.apache.commons.codec;

public interface Encoder {
    Object encode(final Object object) throws EncoderException;
}

package org.apache.commons.codec;

public interface Decoder {
    Object decode(final Object object) throws DecoderException;
}
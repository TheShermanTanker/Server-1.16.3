package org.apache.commons.codec;

public interface StringEncoder extends Encoder {
    String encode(final String string) throws EncoderException;
}

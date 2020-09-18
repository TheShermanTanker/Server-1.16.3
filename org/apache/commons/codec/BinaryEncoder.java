package org.apache.commons.codec;

public interface BinaryEncoder extends Encoder {
    byte[] encode(final byte[] arr) throws EncoderException;
}

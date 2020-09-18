package org.apache.commons.codec;

public interface BinaryDecoder extends Decoder {
    byte[] decode(final byte[] arr) throws DecoderException;
}

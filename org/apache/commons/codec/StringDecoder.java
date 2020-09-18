package org.apache.commons.codec;

public interface StringDecoder extends Decoder {
    String decode(final String string) throws DecoderException;
}

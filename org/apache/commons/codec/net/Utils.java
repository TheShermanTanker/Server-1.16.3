package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;

class Utils {
    static int digit16(final byte b) throws DecoderException {
        final int i = Character.digit((char)b, 16);
        if (i == -1) {
            throw new DecoderException(new StringBuilder().append("Invalid URL encoding: not a valid digit (radix 16): ").append((int)b).toString());
        }
        return i;
    }
}

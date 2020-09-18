package com.google.common.hash;

import java.nio.charset.Charset;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@CanIgnoreReturnValue
abstract class AbstractHasher implements Hasher {
    public final Hasher putBoolean(final boolean b) {
        return this.putByte((byte)(b ? 1 : 0));
    }
    
    public final Hasher putDouble(final double d) {
        return this.putLong(Double.doubleToRawLongBits(d));
    }
    
    public final Hasher putFloat(final float f) {
        return this.putInt(Float.floatToRawIntBits(f));
    }
    
    public Hasher putUnencodedChars(final CharSequence charSequence) {
        for (int i = 0, len = charSequence.length(); i < len; ++i) {
            this.putChar(charSequence.charAt(i));
        }
        return this;
    }
    
    public Hasher putString(final CharSequence charSequence, final Charset charset) {
        return this.putBytes(charSequence.toString().getBytes(charset));
    }
}

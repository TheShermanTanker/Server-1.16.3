package com.google.common.hash;

import java.nio.charset.Charset;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@Beta
@CanIgnoreReturnValue
public interface PrimitiveSink {
    PrimitiveSink putByte(final byte byte1);
    
    PrimitiveSink putBytes(final byte[] arr);
    
    PrimitiveSink putBytes(final byte[] arr, final int integer2, final int integer3);
    
    PrimitiveSink putShort(final short short1);
    
    PrimitiveSink putInt(final int integer);
    
    PrimitiveSink putLong(final long long1);
    
    PrimitiveSink putFloat(final float float1);
    
    PrimitiveSink putDouble(final double double1);
    
    PrimitiveSink putBoolean(final boolean boolean1);
    
    PrimitiveSink putChar(final char character);
    
    PrimitiveSink putUnencodedChars(final CharSequence charSequence);
    
    PrimitiveSink putString(final CharSequence charSequence, final Charset charset);
}

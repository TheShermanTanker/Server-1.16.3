package com.google.common.hash;

import java.nio.charset.Charset;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@Beta
@CanIgnoreReturnValue
public interface Hasher extends PrimitiveSink {
    Hasher putByte(final byte byte1);
    
    Hasher putBytes(final byte[] arr);
    
    Hasher putBytes(final byte[] arr, final int integer2, final int integer3);
    
    Hasher putShort(final short short1);
    
    Hasher putInt(final int integer);
    
    Hasher putLong(final long long1);
    
    Hasher putFloat(final float float1);
    
    Hasher putDouble(final double double1);
    
    Hasher putBoolean(final boolean boolean1);
    
    Hasher putChar(final char character);
    
    Hasher putUnencodedChars(final CharSequence charSequence);
    
    Hasher putString(final CharSequence charSequence, final Charset charset);
    
     <T> Hasher putObject(final T object, final Funnel<? super T> funnel);
    
    HashCode hash();
    
    @Deprecated
    int hashCode();
}

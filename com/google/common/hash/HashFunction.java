package com.google.common.hash;

import java.nio.charset.Charset;
import com.google.common.annotations.Beta;

@Beta
public interface HashFunction {
    Hasher newHasher();
    
    Hasher newHasher(final int integer);
    
    HashCode hashInt(final int integer);
    
    HashCode hashLong(final long long1);
    
    HashCode hashBytes(final byte[] arr);
    
    HashCode hashBytes(final byte[] arr, final int integer2, final int integer3);
    
    HashCode hashUnencodedChars(final CharSequence charSequence);
    
    HashCode hashString(final CharSequence charSequence, final Charset charset);
    
     <T> HashCode hashObject(final T object, final Funnel<? super T> funnel);
    
    int bits();
}

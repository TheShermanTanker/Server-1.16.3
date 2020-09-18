package com.google.common.io;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface ByteProcessor<T> {
    @CanIgnoreReturnValue
    boolean processBytes(final byte[] arr, final int integer2, final int integer3) throws IOException;
    
    T getResult();
}

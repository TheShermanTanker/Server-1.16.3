package com.google.common.io;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface LineProcessor<T> {
    @CanIgnoreReturnValue
    boolean processLine(final String string) throws IOException;
    
    T getResult();
}

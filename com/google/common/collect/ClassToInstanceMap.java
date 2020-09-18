package com.google.common.collect;

import javax.annotation.Nullable;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;

@GwtCompatible
public interface ClassToInstanceMap<B> extends Map<Class<? extends B>, B> {
    @CanIgnoreReturnValue
     <T extends B> T getInstance(final Class<T> class1);
    
    @CanIgnoreReturnValue
     <T extends B> T putInstance(final Class<T> class1, @Nullable final T object);
}

package com.google.gson;

public interface ExclusionStrategy {
    boolean shouldSkipField(final FieldAttributes fieldAttributes);
    
    boolean shouldSkipClass(final Class<?> class1);
}

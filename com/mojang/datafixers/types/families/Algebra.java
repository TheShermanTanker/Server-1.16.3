package com.mojang.datafixers.types.families;

import com.mojang.datafixers.RewriteResult;

public interface Algebra {
    RewriteResult<?, ?> apply(final int integer);
    
    String toString(final int integer);
}

package com.mojang.datafixers;

import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;

public interface DataFixer {
     <T> Dynamic<T> update(final DSL.TypeReference typeReference, final Dynamic<T> dynamic, final int integer3, final int integer4);
    
    Schema getSchema(final int integer);
}

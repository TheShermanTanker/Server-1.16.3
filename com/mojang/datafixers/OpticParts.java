package com.mojang.datafixers;

import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.kinds.K1;
import com.google.common.reflect.TypeToken;
import java.util.Set;

public final class OpticParts<A, B> {
    private final Set<TypeToken<? extends K1>> bounds;
    private final Optic<?, ?, ?, A, B> optic;
    
    public OpticParts(final Set<TypeToken<? extends K1>> bounds, final Optic<?, ?, ?, A, B> optic) {
        this.bounds = bounds;
        this.optic = optic;
    }
    
    public Set<TypeToken<? extends K1>> bounds() {
        return this.bounds;
    }
    
    public Optic<?, ?, ?, A, B> optic() {
        return this.optic;
    }
}

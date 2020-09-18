package com.mojang.datafixers.optics;

import java.util.function.Function;
import com.mojang.datafixers.util.Either;

public final class Inj1<F, G, F2> implements Prism<Either<F, G>, Either<F2, G>, F, F2> {
    public Either<Either<F2, G>, F> match(final Either<F, G> either) {
        return either.<Either<Either<F2, G>, F>>map((java.util.function.Function<? super F, ? extends Either<Either<F2, G>, F>>)Either::right, (java.util.function.Function<? super G, ? extends Either<Either<F2, G>, F>>)(g -> Either.<Either<Object, Object>, Object>left(Either.right(g))));
    }
    
    public Either<F2, G> build(final F2 f2) {
        return Either.<F2, G>left(f2);
    }
    
    public String toString() {
        return "inj1";
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof Inj1;
    }
}

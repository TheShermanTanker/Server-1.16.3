package com.mojang.datafixers.optics;

import java.util.function.Function;
import com.mojang.datafixers.util.Either;

public final class Inj2<F, G, G2> implements Prism<Either<F, G>, Either<F, G2>, G, G2> {
    public Either<Either<F, G2>, G> match(final Either<F, G> either) {
        return either.<Either<Either<F, G2>, G>>map((java.util.function.Function<? super F, ? extends Either<Either<F, G2>, G>>)(f -> Either.<Either<Object, Object>, Object>left(Either.left(f))), (java.util.function.Function<? super G, ? extends Either<Either<F, G2>, G>>)Either::right);
    }
    
    public Either<F, G2> build(final G2 g2) {
        return Either.<F, G2>right(g2);
    }
    
    public String toString() {
        return "inj2";
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof Inj2;
    }
}

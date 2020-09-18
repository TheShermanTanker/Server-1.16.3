package com.mojang.datafixers;

import java.util.function.Function;
import javax.annotation.Nullable;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.types.Type;

public interface OpticFinder<FT> {
    Type<FT> type();
    
     <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(final Type<A> type1, final Type<FR> type2, final boolean boolean3);
    
    default <A> Either<TypedOptic<A, ?, FT, FT>, Type.FieldNotFoundException> findType(final Type<A> containerType, final boolean recurse) {
        return this.<A, FT>findType(containerType, this.type(), recurse);
    }
    
    default <GT> OpticFinder<FT> inField(@Nullable final String name, final Type<GT> type) {
        final OpticFinder<FT> outer = this;
        return new OpticFinder<FT>() {
            public Type<FT> type() {
                return outer.type();
            }
            
            public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(final Type<A> containerType, final Type<FR> resultType, final boolean recurse) {
                final Either<TypedOptic<GT, ?, FT, FR>, Type.FieldNotFoundException> secondOptic = outer.<GT, FR>findType(type, resultType, recurse);
                return secondOptic.<Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException>>map((java.util.function.Function<? super TypedOptic<GT, ?, FT, FR>, ? extends Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException>>)(l -> this.cap((Type<Object>)containerType, (TypedOptic<GT, Object, FT, Object>)l, recurse)), (java.util.function.Function<? super Type.FieldNotFoundException, ? extends Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException>>)Either::right);
            }
            
            private <A, FR, GR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> cap(final Type<A> containterType, final TypedOptic<GT, GR, FT, FR> l1, final boolean recurse) {
                final Either<TypedOptic<A, ?, GT, GR>, Type.FieldNotFoundException> first = DSL.<GT>fieldFinder(name, type).<A, GR>findType(containterType, l1.tType(), recurse);
                return first.<TypedOptic<A, ?, FT, FR>>mapLeft((java.util.function.Function<? super TypedOptic<A, ?, GT, GR>, ? extends TypedOptic<A, ?, FT, FR>>)(l -> l.compose(l1)));
            }
        };
    }
}

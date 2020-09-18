package com.mojang.datafixers.optics;

import java.util.Objects;
import java.util.Optional;
import com.google.common.reflect.TypeToken;
import java.util.Set;
import com.mojang.datafixers.kinds.App2;
import java.util.function.Function;
import com.mojang.datafixers.kinds.K2;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K1;

public interface Optic<Proof extends K1, S, T, A, B> {
     <P extends K2> Function<App2<P, A, B>, App2<P, S, T>> eval(final App<? extends Proof, P> app);
    
    default <Proof2 extends Proof, A1, B1> Optic<Proof2, S, T, A1, B1> compose(final Optic<? super Proof2, A, B, A1, B1> optic) {
        return new CompositionOptic<Proof2, S, T, Object, Object, A1, B1>(this, optic);
    }
    
    default <Proof2 extends K1, A1, B1> Optic<?, S, T, A1, B1> composeUnchecked(final Optic<?, A, B, A1, B1> optic) {
        return new CompositionOptic<Object, S, T, Object, Object, A1, B1>(this, optic);
    }
    
    default <Proof2 extends K1> Optional<Optic<? super Proof2, S, T, A, B>> upCast(final Set<TypeToken<? extends K1>> proofBounds, final TypeToken<Proof2> proof) {
        if (proofBounds.stream().allMatch(bound -> bound.isSupertypeOf(proof))) {
            return (Optional<Optic<? super Proof2, S, T, A, B>>)Optional.of(this);
        }
        return (Optional<Optic<? super Proof2, S, T, A, B>>)Optional.empty();
    }
    
    public static final class CompositionOptic<Proof extends K1, S, T, A, B, A1, B1> implements Optic<Proof, S, T, A1, B1> {
        protected final Optic<? super Proof, S, T, A, B> outer;
        protected final Optic<? super Proof, A, B, A1, B1> inner;
        
        public CompositionOptic(final Optic<? super Proof, S, T, A, B> outer, final Optic<? super Proof, A, B, A1, B1> inner) {
            this.outer = outer;
            this.inner = inner;
        }
        
        public <P extends K2> Function<App2<P, A1, B1>, App2<P, S, T>> eval(final App<? extends Proof, P> proof) {
            return (Function<App2<P, A1, B1>, App2<P, S, T>>)this.outer.<P>eval(proof).compose((Function)this.inner.<P>eval(proof));
        }
        
        public String toString() {
            return new StringBuilder().append("(").append(this.outer).append(" \u25e6 ").append(this.inner).append(")").toString();
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final CompositionOptic<?, ?, ?, ?, ?, ?, ?> that = o;
            return Objects.equals(this.outer, that.outer) && Objects.equals(this.inner, that.inner);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.outer, this.inner });
        }
        
        public Optic<? super Proof, S, T, A, B> outer() {
            return this.outer;
        }
        
        public Optic<? super Proof, A, B, A1, B1> inner() {
            return this.inner;
        }
    }
}

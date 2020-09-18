package com.mojang.datafixers.types;

import java.util.Objects;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.Codec;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Function;

public final class Func<A, B> extends Type<Function<A, B>> {
    protected final Type<A> first;
    protected final Type<B> second;
    
    public Func(final Type<A> first, final Type<B> second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public TypeTemplate buildTemplate() {
        throw new UnsupportedOperationException("No template for function types.");
    }
    
    @Override
    protected Codec<Function<A, B>> buildCodec() {
        return Codec.<Function<A, B>>of(Encoder.<Function<A, B>>error("Cannot save a function"), Decoder.<Function<A, B>>error("Cannot read a function"));
    }
    
    public String toString() {
        return new StringBuilder().append("(").append(this.first).append(" -> ").append(this.second).append(")").toString();
    }
    
    @Override
    public boolean equals(final Object obj, final boolean ignoreRecursionPoints, final boolean checkIndex) {
        if (!(obj instanceof Func)) {
            return false;
        }
        final Func<?, ?> that = obj;
        return this.first.equals(that.first, ignoreRecursionPoints, checkIndex) && this.second.equals(that.second, ignoreRecursionPoints, checkIndex);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.first, this.second });
    }
    
    public Type<A> first() {
        return this.first;
    }
    
    public Type<B> second() {
        return this.second;
    }
}

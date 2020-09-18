package com.mojang.datafixers;

import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import java.util.Optional;
import com.mojang.datafixers.util.Pair;
import java.util.function.Consumer;
import com.mojang.serialization.Dynamic;
import java.util.BitSet;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import com.mojang.datafixers.types.Type;
import javax.annotation.Nullable;
import com.mojang.datafixers.schemas.Schema;
import org.apache.logging.log4j.Logger;

public abstract class DataFix {
    private static final Logger LOGGER;
    private final Schema outputSchema;
    private final boolean changesType;
    @Nullable
    private TypeRewriteRule rule;
    
    public DataFix(final Schema outputSchema, final boolean changesType) {
        this.outputSchema = outputSchema;
        this.changesType = changesType;
    }
    
    protected <A> TypeRewriteRule fixTypeEverywhere(final String name, final Type<A> type, final Function<DynamicOps<?>, Function<A, A>> function) {
        return this.<A, A>fixTypeEverywhere(name, type, type, function, new BitSet());
    }
    
    protected <A, B> TypeRewriteRule convertUnchecked(final String name, final Type<A> type, final Type<B> newType) {
        return this.<A, B>fixTypeEverywhere(name, type, newType, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<A, B>>)(ops -> Function.identity()), new BitSet());
    }
    
    protected TypeRewriteRule writeAndRead(final String name, final Type<?> type, final Type<?> newType) {
        return this.writeFixAndRead(name, type, newType, (Function<Dynamic<?>, Dynamic<?>>)Function.identity());
    }
    
    protected <A, B> TypeRewriteRule writeFixAndRead(final String name, final Type<A> type, final Type<B> newType, final Function<Dynamic<?>, Dynamic<?>> fix) {
        return this.fixTypeEverywhere(name, (Type<Object>)type, (Type<Object>)newType, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Object, Object>>)(ops -> input -> {
            final Optional<? extends Dynamic<?>> written = type.writeDynamic(ops, input).resultOrPartial((Consumer<String>)DataFix.LOGGER::error);
            if (!written.isPresent()) {
                throw new RuntimeException("Could not write the object in " + name);
            }
            final Optional<? extends Pair<Typed<B>, ?>> read = newType.readTyped((Dynamic)fix.apply(written.get())).resultOrPartial(DataFix.LOGGER::error);
            if (!read.isPresent()) {
                throw new RuntimeException("Could not read the new object in " + name);
            }
            return ((Pair)read.get()).getFirst().getValue();
        }));
    }
    
    protected <A, B> TypeRewriteRule fixTypeEverywhere(final String name, final Type<A> type, final Type<B> newType, final Function<DynamicOps<?>, Function<A, B>> function) {
        return this.<A, B>fixTypeEverywhere(name, type, newType, function, new BitSet());
    }
    
    protected <A, B> TypeRewriteRule fixTypeEverywhere(final String name, final Type<A> type, final Type<B> newType, final Function<DynamicOps<?>, Function<A, B>> function, final BitSet bitSet) {
        return this.<A, Object>fixTypeEverywhere(type, RewriteResult.create(View.create(name, (Type<A>)type, (Type<B>)newType, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<A, B>>)new NamedFunctionWrapper(name, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Object, Object>>)function)), bitSet));
    }
    
    protected <A> TypeRewriteRule fixTypeEverywhereTyped(final String name, final Type<A> type, final Function<Typed<?>, Typed<?>> function) {
        return this.<A>fixTypeEverywhereTyped(name, type, function, new BitSet());
    }
    
    protected <A> TypeRewriteRule fixTypeEverywhereTyped(final String name, final Type<A> type, final Function<Typed<?>, Typed<?>> function, final BitSet bitSet) {
        return this.<A, A>fixTypeEverywhereTyped(name, type, type, function, bitSet);
    }
    
    protected <A, B> TypeRewriteRule fixTypeEverywhereTyped(final String name, final Type<A> type, final Type<B> newType, final Function<Typed<?>, Typed<?>> function) {
        return this.<A, B>fixTypeEverywhereTyped(name, type, newType, function, new BitSet());
    }
    
    protected <A, B> TypeRewriteRule fixTypeEverywhereTyped(final String name, final Type<A> type, final Type<B> newType, final Function<Typed<?>, Typed<?>> function, final BitSet bitSet) {
        return this.<A, Object>fixTypeEverywhere(type, DataFix.checked(name, (Type<A>)type, (Type<B>)newType, function, bitSet));
    }
    
    public static <A, B> RewriteResult<A, B> checked(final String name, final Type<A> type, final Type<B> newType, final Function<Typed<?>, Typed<?>> function, final BitSet bitSet) {
        return RewriteResult.<A, B>create(View.create(name, (Type<A>)type, (Type<B>)newType, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<A, B>>)new NamedFunctionWrapper(name, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Object, Object>>)(ops -> a -> {
            final Typed<?> result = function.apply(new Typed(type, ops, a));
            if (!newType.equals(result.type, true, false)) {
                throw new IllegalStateException(String.format("Dynamic type check failed: %s not equal to %s", new Object[] { newType, result.type }));
            }
            return result.value;
        }))), bitSet);
    }
    
    protected <A, B> TypeRewriteRule fixTypeEverywhere(final Type<A> type, final RewriteResult<A, B> view) {
        return TypeRewriteRule.checkOnce(TypeRewriteRule.everywhere(TypeRewriteRule.<A>ifSame(type, view), DataFixerUpper.OPTIMIZATION_RULE, true, true), (Consumer<Type<?>>)this::onFail);
    }
    
    protected void onFail(final Type<?> type) {
        DataFix.LOGGER.info(new StringBuilder().append("Not matched: ").append(this).append(" ").append(type).toString());
    }
    
    public final int getVersionKey() {
        return this.getOutputSchema().getVersionKey();
    }
    
    public TypeRewriteRule getRule() {
        if (this.rule == null) {
            this.rule = this.makeRule();
        }
        return this.rule;
    }
    
    protected abstract TypeRewriteRule makeRule();
    
    protected Schema getInputSchema() {
        if (this.changesType) {
            return this.outputSchema.getParent();
        }
        return this.getOutputSchema();
    }
    
    protected Schema getOutputSchema() {
        return this.outputSchema;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    private static final class NamedFunctionWrapper<A, B> implements Function<DynamicOps<?>, Function<A, B>> {
        private final String name;
        private final Function<DynamicOps<?>, Function<A, B>> delegate;
        
        public NamedFunctionWrapper(final String name, final Function<DynamicOps<?>, Function<A, B>> delegate) {
            this.name = name;
            this.delegate = delegate;
        }
        
        public Function<A, B> apply(final DynamicOps<?> ops) {
            return (Function<A, B>)this.delegate.apply(ops);
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final NamedFunctionWrapper<?, ?> that = o;
            return Objects.equals(this.name, that.name);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.name });
        }
    }
}

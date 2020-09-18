package com.mojang.datafixers;

import com.mojang.datafixers.functions.Functions;
import java.util.Iterator;
import java.util.Collection;
import com.mojang.datafixers.functions.PointFreeRule;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableList;
import java.util.Objects;
import java.util.List;
import java.util.Optional;
import com.mojang.datafixers.types.Type;

public interface TypeRewriteRule {
     <A> Optional<RewriteResult<A, ?>> rewrite(final Type<A> type);
    
    default TypeRewriteRule nop() {
        return Nop.INSTANCE;
    }
    
    default TypeRewriteRule seq(final List<TypeRewriteRule> rules) {
        return new Seq(rules);
    }
    
    default TypeRewriteRule seq(final TypeRewriteRule first, final TypeRewriteRule second) {
        if (Objects.equals(first, nop())) {
            return second;
        }
        if (Objects.equals(second, nop())) {
            return first;
        }
        return seq(ImmutableList.of(first, second));
    }
    
    default TypeRewriteRule seq(final TypeRewriteRule firstRule, final TypeRewriteRule... rules) {
        if (rules.length == 0) {
            return firstRule;
        }
        int lastRule;
        TypeRewriteRule tail;
        for (lastRule = rules.length - 1, tail = rules[lastRule]; lastRule > 0; --lastRule, tail = seq(rules[lastRule], tail)) {}
        return seq(firstRule, tail);
    }
    
    default TypeRewriteRule orElse(final TypeRewriteRule first, final TypeRewriteRule second) {
        return orElse(first, (Supplier<TypeRewriteRule>)(() -> second));
    }
    
    default TypeRewriteRule orElse(final TypeRewriteRule first, final Supplier<TypeRewriteRule> second) {
        return new OrElse(first, second);
    }
    
    default TypeRewriteRule all(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
        return new All(rule, recurse, checkIndex);
    }
    
    default TypeRewriteRule one(final TypeRewriteRule rule) {
        return new One(rule);
    }
    
    default TypeRewriteRule once(final TypeRewriteRule rule) {
        return orElse(rule, (Supplier<TypeRewriteRule>)(() -> one(once(rule))));
    }
    
    default TypeRewriteRule checkOnce(final TypeRewriteRule rule, final Consumer<Type<?>> onFail) {
        return rule;
    }
    
    default TypeRewriteRule everywhere(final TypeRewriteRule rule, final PointFreeRule optimizationRule, final boolean recurse, final boolean checkIndex) {
        return new Everywhere(rule, optimizationRule, recurse, checkIndex);
    }
    
    default <B> TypeRewriteRule ifSame(final Type<B> targetType, final RewriteResult<B, ?> value) {
        return new IfSame<>(targetType, value);
    }
    
    public enum Nop implements TypeRewriteRule, Supplier<TypeRewriteRule> {
        INSTANCE;
        
        public <A> Optional<RewriteResult<A, ?>> rewrite(final Type<A> type) {
            return (Optional<RewriteResult<A, ?>>)Optional.of(RewriteResult.<A>nop(type));
        }
        
        public TypeRewriteRule get() {
            return this;
        }
    }
    
    public static final class Seq implements TypeRewriteRule {
        protected final List<TypeRewriteRule> rules;
        private final int hashCode;
        
        public Seq(final List<TypeRewriteRule> rules) {
            this.rules = ImmutableList.copyOf((java.util.Collection<?>)rules);
            this.hashCode = this.rules.hashCode();
        }
        
        public <A> Optional<RewriteResult<A, ?>> rewrite(final Type<A> type) {
            RewriteResult<A, ?> result = RewriteResult.nop(type);
            for (final TypeRewriteRule rule : this.rules) {
                final Optional<RewriteResult<A, ?>> newResult = this.cap1(rule, result);
                if (!newResult.isPresent()) {
                    return (Optional<RewriteResult<A, ?>>)Optional.empty();
                }
                result = newResult.get();
            }
            return (Optional<RewriteResult<A, ?>>)Optional.of(result);
        }
        
        protected <A, B> Optional<RewriteResult<A, ?>> cap1(final TypeRewriteRule rule, final RewriteResult<A, B> f) {
            return (Optional<RewriteResult<A, ?>>)rule.<B>rewrite(f.view.newType).map(s -> s.compose(f));
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Seq)) {
                return false;
            }
            final Seq that = (Seq)obj;
            return Objects.equals(this.rules, that.rules);
        }
        
        public int hashCode() {
            return this.hashCode;
        }
    }
    
    public static final class OrElse implements TypeRewriteRule {
        protected final TypeRewriteRule first;
        protected final Supplier<TypeRewriteRule> second;
        private final int hashCode;
        
        public OrElse(final TypeRewriteRule first, final Supplier<TypeRewriteRule> second) {
            this.first = first;
            this.second = second;
            this.hashCode = Objects.hash(new Object[] { first, second });
        }
        
        public <A> Optional<RewriteResult<A, ?>> rewrite(final Type<A> type) {
            final Optional<RewriteResult<A, ?>> view = this.first.<A>rewrite(type);
            if (view.isPresent()) {
                return view;
            }
            return ((TypeRewriteRule)this.second.get()).<A>rewrite(type);
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof OrElse)) {
                return false;
            }
            final OrElse that = (OrElse)obj;
            return Objects.equals(this.first, that.first) && Objects.equals(this.second, that.second);
        }
        
        public int hashCode() {
            return this.hashCode;
        }
    }
    
    public static class All implements TypeRewriteRule {
        private final TypeRewriteRule rule;
        private final boolean recurse;
        private final boolean checkIndex;
        private final int hashCode;
        
        public All(final TypeRewriteRule rule, final boolean recurse, final boolean checkIndex) {
            this.rule = rule;
            this.recurse = recurse;
            this.checkIndex = checkIndex;
            this.hashCode = Objects.hash(new Object[] { rule, recurse, checkIndex });
        }
        
        public <A> Optional<RewriteResult<A, ?>> rewrite(final Type<A> type) {
            return (Optional<RewriteResult<A, ?>>)Optional.of(type.all(this.rule, this.recurse, this.checkIndex));
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof All)) {
                return false;
            }
            final All that = (All)obj;
            return Objects.equals(this.rule, that.rule) && this.recurse == that.recurse && this.checkIndex == that.checkIndex;
        }
        
        public int hashCode() {
            return this.hashCode;
        }
    }
    
    public static class One implements TypeRewriteRule {
        private final TypeRewriteRule rule;
        
        public One(final TypeRewriteRule rule) {
            this.rule = rule;
        }
        
        public <A> Optional<RewriteResult<A, ?>> rewrite(final Type<A> type) {
            return type.one(this.rule);
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof One)) {
                return false;
            }
            final One that = (One)obj;
            return Objects.equals(this.rule, that.rule);
        }
        
        public int hashCode() {
            return this.rule.hashCode();
        }
    }
    
    public static class CheckOnce implements TypeRewriteRule {
        private final TypeRewriteRule rule;
        private final Consumer<Type<?>> onFail;
        
        public CheckOnce(final TypeRewriteRule rule, final Consumer<Type<?>> onFail) {
            this.rule = rule;
            this.onFail = onFail;
        }
        
        public <A> Optional<RewriteResult<A, ?>> rewrite(final Type<A> type) {
            final Optional<RewriteResult<A, ?>> result = this.rule.<A>rewrite(type);
            if (!result.isPresent() || Objects.equals(((RewriteResult)result.get()).view.function(), Functions.id())) {
                this.onFail.accept(type);
            }
            return result;
        }
        
        public boolean equals(final Object o) {
            return this == o || (o instanceof CheckOnce && Objects.equals(this.rule, ((CheckOnce)o).rule));
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.rule });
        }
    }
    
    public static class Everywhere implements TypeRewriteRule {
        protected final TypeRewriteRule rule;
        protected final PointFreeRule optimizationRule;
        protected final boolean recurse;
        private final boolean checkIndex;
        private final int hashCode;
        
        public Everywhere(final TypeRewriteRule rule, final PointFreeRule optimizationRule, final boolean recurse, final boolean checkIndex) {
            this.rule = rule;
            this.optimizationRule = optimizationRule;
            this.recurse = recurse;
            this.checkIndex = checkIndex;
            this.hashCode = Objects.hash(new Object[] { rule, optimizationRule, recurse, checkIndex });
        }
        
        public <A> Optional<RewriteResult<A, ?>> rewrite(final Type<A> type) {
            return type.everywhere(this.rule, this.optimizationRule, this.recurse, this.checkIndex);
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Everywhere)) {
                return false;
            }
            final Everywhere that = (Everywhere)obj;
            return Objects.equals(this.rule, that.rule) && Objects.equals(this.optimizationRule, that.optimizationRule) && this.recurse == that.recurse && this.checkIndex == that.checkIndex;
        }
        
        public int hashCode() {
            return this.hashCode;
        }
    }
    
    public static class IfSame<B> implements TypeRewriteRule {
        private final Type<B> targetType;
        private final RewriteResult<B, ?> value;
        private final int hashCode;
        
        public IfSame(final Type<B> targetType, final RewriteResult<B, ?> value) {
            this.targetType = targetType;
            this.value = value;
            this.hashCode = Objects.hash(new Object[] { targetType, value });
        }
        
        public <A> Optional<RewriteResult<A, ?>> rewrite(final Type<A> type) {
            return type.<B>ifSame(this.targetType, this.value);
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof IfSame)) {
                return false;
            }
            final IfSame<?> that = obj;
            return Objects.equals(this.targetType, that.targetType) && Objects.equals(this.value, that.value);
        }
        
        public int hashCode() {
            return this.hashCode;
        }
    }
}

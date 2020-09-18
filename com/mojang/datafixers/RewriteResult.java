package com.mojang.datafixers;

import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;
import com.mojang.datafixers.types.templates.RecursivePoint;
import com.mojang.datafixers.types.Type;
import java.util.BitSet;

public final class RewriteResult<A, B> {
    protected final View<A, B> view;
    protected final BitSet recData;
    
    public RewriteResult(final View<A, B> view, final BitSet recData) {
        this.view = view;
        this.recData = recData;
    }
    
    public static <A, B> RewriteResult<A, B> create(final View<A, B> view, final BitSet recData) {
        return new RewriteResult<A, B>(view, recData);
    }
    
    public static <A> RewriteResult<A, A> nop(final Type<A> type) {
        return new RewriteResult<A, A>(View.<A>nopView(type), new BitSet());
    }
    
    public <C> RewriteResult<C, B> compose(final RewriteResult<C, A> that) {
        BitSet newData;
        if (this.view.type() instanceof RecursivePoint.RecursivePointType && that.view.type() instanceof RecursivePoint.RecursivePointType) {
            newData = ObjectUtils.<BitSet>clone(this.recData);
            newData.or(that.recData);
        }
        else {
            newData = this.recData;
        }
        return RewriteResult.<C, B>create(this.view.compose((View<A, A>)that.view), newData);
    }
    
    public BitSet recData() {
        return this.recData;
    }
    
    public View<A, B> view() {
        return this.view;
    }
    
    public String toString() {
        return new StringBuilder().append("RR[").append(this.view).append("]").toString();
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final RewriteResult<?, ?> that = o;
        return Objects.equals(this.view, that.view);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.view });
    }
}

package com.google.common.collect;

import com.google.common.base.Objects;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class ByFunctionOrdering<F, T> extends Ordering<F> implements Serializable {
    final Function<F, ? extends T> function;
    final Ordering<T> ordering;
    private static final long serialVersionUID = 0L;
    
    ByFunctionOrdering(final Function<F, ? extends T> function, final Ordering<T> ordering) {
        this.function = Preconditions.<Function<F, ? extends T>>checkNotNull(function);
        this.ordering = Preconditions.<Ordering<T>>checkNotNull(ordering);
    }
    
    @Override
    public int compare(final F left, final F right) {
        return this.ordering.compare((T)this.function.apply(left), (T)this.function.apply(right));
    }
    
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof ByFunctionOrdering) {
            final ByFunctionOrdering<?, ?> that = object;
            return this.function.equals(that.function) && this.ordering.equals(that.ordering);
        }
        return false;
    }
    
    public int hashCode() {
        return Objects.hashCode(this.function, this.ordering);
    }
    
    public String toString() {
        return new StringBuilder().append(this.ordering).append(".onResultOf(").append(this.function).append(")").toString();
    }
}

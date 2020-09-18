package com.mojang.datafixers.functions;

import org.apache.commons.lang3.StringUtils;
import java.util.Optional;
import com.mojang.datafixers.types.Type;
import javax.annotation.Nullable;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;

public abstract class PointFree<T> {
    private volatile boolean initialized;
    @Nullable
    private Function<DynamicOps<?>, T> value;
    
    public Function<DynamicOps<?>, T> evalCached() {
        if (!this.initialized) {
            synchronized (this) {
                if (!this.initialized) {
                    this.value = this.eval();
                    this.initialized = true;
                }
            }
        }
        return this.value;
    }
    
    public abstract Function<DynamicOps<?>, T> eval();
    
    Optional<? extends PointFree<T>> all(final PointFreeRule rule, final Type<T> type) {
        return Optional.of(this);
    }
    
    Optional<? extends PointFree<T>> one(final PointFreeRule rule, final Type<T> type) {
        return Optional.empty();
    }
    
    public final String toString() {
        return this.toString(0);
    }
    
    public static String indent(final int level) {
        return StringUtils.repeat("  ", level);
    }
    
    public abstract String toString(final int integer);
}

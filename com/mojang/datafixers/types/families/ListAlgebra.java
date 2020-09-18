package com.mojang.datafixers.types.families;

import java.util.Objects;
import java.util.stream.Collectors;
import com.mojang.datafixers.functions.PointFree;
import com.mojang.datafixers.RewriteResult;
import java.util.List;

public final class ListAlgebra implements Algebra {
    private final String name;
    private final List<RewriteResult<?, ?>> views;
    private int hashCode;
    
    public ListAlgebra(final String name, final List<RewriteResult<?, ?>> views) {
        this.name = name;
        this.views = views;
    }
    
    public RewriteResult<?, ?> apply(final int index) {
        return this.views.get(index);
    }
    
    public String toString() {
        return this.toString(0);
    }
    
    public String toString(final int level) {
        final String wrap = "\n" + PointFree.indent(level + 1);
        return "Algebra[" + this.name + wrap + (String)this.views.stream().map(view -> view.view().function().toString(level + 1)).collect(Collectors.joining((CharSequence)wrap)) + "\n" + PointFree.indent(level) + "]";
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListAlgebra)) {
            return false;
        }
        final ListAlgebra that = (ListAlgebra)o;
        return Objects.equals(this.views, that.views);
    }
    
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.views.hashCode();
        }
        return this.hashCode;
    }
}

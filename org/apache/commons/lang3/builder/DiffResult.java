package org.apache.commons.lang3.builder;

import java.util.Iterator;
import java.util.Collections;
import java.util.List;

public class DiffResult implements Iterable<Diff<?>> {
    public static final String OBJECTS_SAME_STRING = "";
    private static final String DIFFERS_STRING = "differs from";
    private final List<Diff<?>> diffs;
    private final Object lhs;
    private final Object rhs;
    private final ToStringStyle style;
    
    DiffResult(final Object lhs, final Object rhs, final List<Diff<?>> diffs, final ToStringStyle style) {
        if (lhs == null) {
            throw new IllegalArgumentException("Left hand object cannot be null");
        }
        if (rhs == null) {
            throw new IllegalArgumentException("Right hand object cannot be null");
        }
        if (diffs == null) {
            throw new IllegalArgumentException("List of differences cannot be null");
        }
        this.diffs = diffs;
        this.lhs = lhs;
        this.rhs = rhs;
        if (style == null) {
            this.style = ToStringStyle.DEFAULT_STYLE;
        }
        else {
            this.style = style;
        }
    }
    
    public List<Diff<?>> getDiffs() {
        return (List<Diff<?>>)Collections.unmodifiableList((List)this.diffs);
    }
    
    public int getNumberOfDiffs() {
        return this.diffs.size();
    }
    
    public ToStringStyle getToStringStyle() {
        return this.style;
    }
    
    public String toString() {
        return this.toString(this.style);
    }
    
    public String toString(final ToStringStyle style) {
        if (this.diffs.size() == 0) {
            return "";
        }
        final ToStringBuilder lhsBuilder = new ToStringBuilder(this.lhs, style);
        final ToStringBuilder rhsBuilder = new ToStringBuilder(this.rhs, style);
        for (final Diff<?> diff : this.diffs) {
            lhsBuilder.append(diff.getFieldName(), diff.getLeft());
            rhsBuilder.append(diff.getFieldName(), diff.getRight());
        }
        return String.format("%s %s %s", new Object[] { lhsBuilder.build(), "differs from", rhsBuilder.build() });
    }
    
    public Iterator<Diff<?>> iterator() {
        return (Iterator<Diff<?>>)this.diffs.iterator();
    }
}

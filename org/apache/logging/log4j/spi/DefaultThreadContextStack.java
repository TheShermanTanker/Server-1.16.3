package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.util.StringBuilders;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class DefaultThreadContextStack implements ThreadContextStack, StringBuilderFormattable {
    private static final long serialVersionUID = 5050501L;
    private static final ThreadLocal<MutableThreadContextStack> STACK;
    private final boolean useStack;
    
    public DefaultThreadContextStack(final boolean useStack) {
        this.useStack = useStack;
    }
    
    private MutableThreadContextStack getNonNullStackCopy() {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        return (MutableThreadContextStack)((values == null) ? new MutableThreadContextStack() : values.copy());
    }
    
    public boolean add(final String s) {
        if (!this.useStack) {
            return false;
        }
        final MutableThreadContextStack copy = this.getNonNullStackCopy();
        copy.add(s);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return true;
    }
    
    public boolean addAll(final Collection<? extends String> strings) {
        if (!this.useStack || strings.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack copy = this.getNonNullStackCopy();
        copy.addAll(strings);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return true;
    }
    
    public List<String> asList() {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (values == null) {
            return (List<String>)Collections.emptyList();
        }
        return values.asList();
    }
    
    public void clear() {
        DefaultThreadContextStack.STACK.remove();
    }
    
    public boolean contains(final Object o) {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        return values != null && values.contains(o);
    }
    
    public boolean containsAll(final Collection<?> objects) {
        if (objects.isEmpty()) {
            return true;
        }
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        return values != null && values.containsAll(objects);
    }
    
    public ThreadContextStack copy() {
        MutableThreadContextStack values = null;
        if (!this.useStack || (values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get()) == null) {
            return new MutableThreadContextStack();
        }
        return values.copy();
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof DefaultThreadContextStack) {
            final DefaultThreadContextStack other = (DefaultThreadContextStack)obj;
            if (this.useStack != other.useStack) {
                return false;
            }
        }
        if (!(obj instanceof ThreadContextStack)) {
            return false;
        }
        final ThreadContextStack other2 = (ThreadContextStack)obj;
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        return values != null && values.equals(other2);
    }
    
    public int getDepth() {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        return (values == null) ? 0 : values.getDepth();
    }
    
    public int hashCode() {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((values == null) ? 0 : values.hashCode());
        return result;
    }
    
    public boolean isEmpty() {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        return values == null || values.isEmpty();
    }
    
    public Iterator<String> iterator() {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (values == null) {
            final List<String> empty = (List<String>)Collections.emptyList();
            return (Iterator<String>)empty.iterator();
        }
        return values.iterator();
    }
    
    public String peek() {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (values == null || values.size() == 0) {
            return "";
        }
        return values.peek();
    }
    
    public String pop() {
        if (!this.useStack) {
            return "";
        }
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (values == null || values.size() == 0) {
            return "";
        }
        final MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
        final String result = copy.pop();
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return result;
    }
    
    public void push(final String message) {
        if (!this.useStack) {
            return;
        }
        this.add(message);
    }
    
    public boolean remove(final Object o) {
        if (!this.useStack) {
            return false;
        }
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (values == null || values.size() == 0) {
            return false;
        }
        final MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
        final boolean result = copy.remove(o);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return result;
    }
    
    public boolean removeAll(final Collection<?> objects) {
        if (!this.useStack || objects.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (values == null || values.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
        final boolean result = copy.removeAll(objects);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return result;
    }
    
    public boolean retainAll(final Collection<?> objects) {
        if (!this.useStack || objects.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (values == null || values.isEmpty()) {
            return false;
        }
        final MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
        final boolean result = copy.retainAll(objects);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
        return result;
    }
    
    public int size() {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        return (values == null) ? 0 : values.size();
    }
    
    public Object[] toArray() {
        final MutableThreadContextStack result = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (result == null) {
            return new String[0];
        }
        return result.toArray(new Object[result.size()]);
    }
    
    public <T> T[] toArray(final T[] ts) {
        final MutableThreadContextStack result = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (result == null) {
            if (ts.length > 0) {
                ts[0] = null;
            }
            return ts;
        }
        return result.<T>toArray(ts);
    }
    
    public String toString() {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        return (values == null) ? "[]" : values.toString();
    }
    
    public void formatTo(final StringBuilder buffer) {
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (values == null) {
            buffer.append("[]");
        }
        else {
            StringBuilders.appendValue(buffer, values);
        }
    }
    
    public void trim(final int depth) {
        if (depth < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        }
        final MutableThreadContextStack values = (MutableThreadContextStack)DefaultThreadContextStack.STACK.get();
        if (values == null) {
            return;
        }
        final MutableThreadContextStack copy = (MutableThreadContextStack)values.copy();
        copy.trim(depth);
        copy.freeze();
        DefaultThreadContextStack.STACK.set(copy);
    }
    
    public ThreadContext.ContextStack getImmutableStackOrNull() {
        return (ThreadContext.ContextStack)DefaultThreadContextStack.STACK.get();
    }
    
    static {
        STACK = new ThreadLocal();
    }
}

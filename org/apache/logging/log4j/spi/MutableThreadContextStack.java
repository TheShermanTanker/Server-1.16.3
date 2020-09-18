package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.ThreadContext;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class MutableThreadContextStack implements ThreadContextStack, StringBuilderFormattable {
    private static final long serialVersionUID = 50505011L;
    private final List<String> list;
    private boolean frozen;
    
    public MutableThreadContextStack() {
        this((List<String>)new ArrayList());
    }
    
    public MutableThreadContextStack(final List<String> list) {
        this.list = (List<String>)new ArrayList((Collection)list);
    }
    
    private MutableThreadContextStack(final MutableThreadContextStack stack) {
        this.list = (List<String>)new ArrayList((Collection)stack.list);
    }
    
    private void checkInvariants() {
        if (this.frozen) {
            throw new UnsupportedOperationException("context stack has been frozen");
        }
    }
    
    public String pop() {
        this.checkInvariants();
        if (this.list.isEmpty()) {
            return null;
        }
        final int last = this.list.size() - 1;
        final String result = (String)this.list.remove(last);
        return result;
    }
    
    public String peek() {
        if (this.list.isEmpty()) {
            return null;
        }
        final int last = this.list.size() - 1;
        return (String)this.list.get(last);
    }
    
    public void push(final String message) {
        this.checkInvariants();
        this.list.add(message);
    }
    
    public int getDepth() {
        return this.list.size();
    }
    
    public List<String> asList() {
        return this.list;
    }
    
    public void trim(final int depth) {
        this.checkInvariants();
        if (depth < 0) {
            throw new IllegalArgumentException("Maximum stack depth cannot be negative");
        }
        if (this.list == null) {
            return;
        }
        final List<String> copy = (List<String>)new ArrayList(this.list.size());
        for (int count = Math.min(depth, this.list.size()), i = 0; i < count; ++i) {
            copy.add(this.list.get(i));
        }
        this.list.clear();
        this.list.addAll((Collection)copy);
    }
    
    public ThreadContextStack copy() {
        return new MutableThreadContextStack(this);
    }
    
    public void clear() {
        this.checkInvariants();
        this.list.clear();
    }
    
    public int size() {
        return this.list.size();
    }
    
    public boolean isEmpty() {
        return this.list.isEmpty();
    }
    
    public boolean contains(final Object o) {
        return this.list.contains(o);
    }
    
    public Iterator<String> iterator() {
        return (Iterator<String>)this.list.iterator();
    }
    
    public Object[] toArray() {
        return this.list.toArray();
    }
    
    public <T> T[] toArray(final T[] ts) {
        return (T[])this.list.toArray((Object[])ts);
    }
    
    public boolean add(final String s) {
        this.checkInvariants();
        return this.list.add(s);
    }
    
    public boolean remove(final Object o) {
        this.checkInvariants();
        return this.list.remove(o);
    }
    
    public boolean containsAll(final Collection<?> objects) {
        return this.list.containsAll((Collection)objects);
    }
    
    public boolean addAll(final Collection<? extends String> strings) {
        this.checkInvariants();
        return this.list.addAll((Collection)strings);
    }
    
    public boolean removeAll(final Collection<?> objects) {
        this.checkInvariants();
        return this.list.removeAll((Collection)objects);
    }
    
    public boolean retainAll(final Collection<?> objects) {
        this.checkInvariants();
        return this.list.retainAll((Collection)objects);
    }
    
    public String toString() {
        return String.valueOf(this.list);
    }
    
    public void formatTo(final StringBuilder buffer) {
        buffer.append('[');
        for (int i = 0; i < this.list.size(); ++i) {
            if (i > 0) {
                buffer.append(',').append(' ');
            }
            buffer.append((String)this.list.get(i));
        }
        buffer.append(']');
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.list == null) ? 0 : this.list.hashCode());
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ThreadContextStack)) {
            return false;
        }
        final ThreadContextStack other = (ThreadContextStack)obj;
        final List<String> otherAsList = other.asList();
        if (this.list == null) {
            if (otherAsList != null) {
                return false;
            }
        }
        else if (!this.list.equals(otherAsList)) {
            return false;
        }
        return true;
    }
    
    public ThreadContext.ContextStack getImmutableStackOrNull() {
        return this.copy();
    }
    
    public void freeze() {
        this.frozen = true;
    }
    
    public boolean isFrozen() {
        return this.frozen;
    }
}

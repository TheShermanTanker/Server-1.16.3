package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;
import java.util.AbstractCollection;

public abstract class AbstractCharCollection extends AbstractCollection<Character> implements CharCollection {
    protected AbstractCharCollection() {
    }
    
    public abstract CharIterator iterator();
    
    public boolean add(final char k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean contains(final char k) {
        final CharIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextChar()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean rem(final char k) {
        final CharIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextChar()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean add(final Character key) {
        return super.add(key);
    }
    
    @Deprecated
    public boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    public boolean remove(final Object key) {
        return super.remove(key);
    }
    
    public char[] toArray(char[] a) {
        if (a == null || a.length < this.size()) {
            a = new char[this.size()];
        }
        CharIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    public char[] toCharArray() {
        return this.toArray(null);
    }
    
    @Deprecated
    public char[] toCharArray(final char[] a) {
        return this.toArray(a);
    }
    
    public boolean addAll(final CharCollection c) {
        boolean retVal = false;
        final CharIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.add(i.nextChar())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean containsAll(final CharCollection c) {
        final CharIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains(i.nextChar())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean removeAll(final CharCollection c) {
        boolean retVal = false;
        final CharIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.rem(i.nextChar())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean retainAll(final CharCollection c) {
        boolean retVal = false;
        final CharIterator i = this.iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextChar())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final CharIterator i = this.iterator();
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final char k = i.nextChar();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}

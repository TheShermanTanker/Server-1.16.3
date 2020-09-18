package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;

public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number> {
    private static final long serialVersionUID = 512176391864L;
    private int value;
    
    public MutableInt() {
    }
    
    public MutableInt(final int value) {
        this.value = value;
    }
    
    public MutableInt(final Number value) {
        this.value = value.intValue();
    }
    
    public MutableInt(final String value) throws NumberFormatException {
        this.value = Integer.parseInt(value);
    }
    
    public Integer getValue() {
        return this.value;
    }
    
    public void setValue(final int value) {
        this.value = value;
    }
    
    public void setValue(final Number value) {
        this.value = value.intValue();
    }
    
    public void increment() {
        ++this.value;
    }
    
    public int getAndIncrement() {
        final int last = this.value;
        ++this.value;
        return last;
    }
    
    public int incrementAndGet() {
        return ++this.value;
    }
    
    public void decrement() {
        --this.value;
    }
    
    public int getAndDecrement() {
        final int last = this.value;
        --this.value;
        return last;
    }
    
    public int decrementAndGet() {
        return --this.value;
    }
    
    public void add(final int operand) {
        this.value += operand;
    }
    
    public void add(final Number operand) {
        this.value += operand.intValue();
    }
    
    public void subtract(final int operand) {
        this.value -= operand;
    }
    
    public void subtract(final Number operand) {
        this.value -= operand.intValue();
    }
    
    public int addAndGet(final int operand) {
        return this.value += operand;
    }
    
    public int addAndGet(final Number operand) {
        return this.value += operand.intValue();
    }
    
    public int getAndAdd(final int operand) {
        final int last = this.value;
        this.value += operand;
        return last;
    }
    
    public int getAndAdd(final Number operand) {
        final int last = this.value;
        this.value += operand.intValue();
        return last;
    }
    
    public int intValue() {
        return this.value;
    }
    
    public long longValue() {
        return this.value;
    }
    
    public float floatValue() {
        return (float)this.value;
    }
    
    public double doubleValue() {
        return this.value;
    }
    
    public Integer toInteger() {
        return this.intValue();
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof MutableInt && this.value == ((MutableInt)obj).intValue();
    }
    
    public int hashCode() {
        return this.value;
    }
    
    public int compareTo(final MutableInt other) {
        return NumberUtils.compare(this.value, other.value);
    }
    
    public String toString() {
        return String.valueOf(this.value);
    }
}

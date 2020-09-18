package com.google.common.primitives;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtIncompatible;
import java.math.BigInteger;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class UnsignedInteger extends Number implements Comparable<UnsignedInteger> {
    public static final UnsignedInteger ZERO;
    public static final UnsignedInteger ONE;
    public static final UnsignedInteger MAX_VALUE;
    private final int value;
    
    private UnsignedInteger(final int value) {
        this.value = (value & -1);
    }
    
    public static UnsignedInteger fromIntBits(final int bits) {
        return new UnsignedInteger(bits);
    }
    
    public static UnsignedInteger valueOf(final long value) {
        Preconditions.checkArgument((value & 0xFFFFFFFFL) == value, "value (%s) is outside the range for an unsigned integer value", value);
        return fromIntBits((int)value);
    }
    
    public static UnsignedInteger valueOf(final BigInteger value) {
        Preconditions.<BigInteger>checkNotNull(value);
        Preconditions.checkArgument(value.signum() >= 0 && value.bitLength() <= 32, "value (%s) is outside the range for an unsigned integer value", value);
        return fromIntBits(value.intValue());
    }
    
    public static UnsignedInteger valueOf(final String string) {
        return valueOf(string, 10);
    }
    
    public static UnsignedInteger valueOf(final String string, final int radix) {
        return fromIntBits(UnsignedInts.parseUnsignedInt(string, radix));
    }
    
    public UnsignedInteger plus(final UnsignedInteger val) {
        return fromIntBits(this.value + Preconditions.<UnsignedInteger>checkNotNull(val).value);
    }
    
    public UnsignedInteger minus(final UnsignedInteger val) {
        return fromIntBits(this.value - Preconditions.<UnsignedInteger>checkNotNull(val).value);
    }
    
    @GwtIncompatible
    public UnsignedInteger times(final UnsignedInteger val) {
        return fromIntBits(this.value * Preconditions.<UnsignedInteger>checkNotNull(val).value);
    }
    
    public UnsignedInteger dividedBy(final UnsignedInteger val) {
        return fromIntBits(UnsignedInts.divide(this.value, Preconditions.<UnsignedInteger>checkNotNull(val).value));
    }
    
    public UnsignedInteger mod(final UnsignedInteger val) {
        return fromIntBits(UnsignedInts.remainder(this.value, Preconditions.<UnsignedInteger>checkNotNull(val).value));
    }
    
    public int intValue() {
        return this.value;
    }
    
    public long longValue() {
        return UnsignedInts.toLong(this.value);
    }
    
    public float floatValue() {
        return (float)this.longValue();
    }
    
    public double doubleValue() {
        return (double)this.longValue();
    }
    
    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(this.longValue());
    }
    
    public int compareTo(final UnsignedInteger other) {
        Preconditions.<UnsignedInteger>checkNotNull(other);
        return UnsignedInts.compare(this.value, other.value);
    }
    
    public int hashCode() {
        return this.value;
    }
    
    public boolean equals(@Nullable final Object obj) {
        if (obj instanceof UnsignedInteger) {
            final UnsignedInteger other = (UnsignedInteger)obj;
            return this.value == other.value;
        }
        return false;
    }
    
    public String toString() {
        return this.toString(10);
    }
    
    public String toString(final int radix) {
        return UnsignedInts.toString(this.value, radix);
    }
    
    static {
        ZERO = fromIntBits(0);
        ONE = fromIntBits(1);
        MAX_VALUE = fromIntBits(-1);
    }
}

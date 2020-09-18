package it.unimi.dsi.fastutil;

public final class SafeMath {
    private SafeMath() {
    }
    
    public static char safeIntToChar(final int value) {
        if (value < 0 || 65535 < value) {
            throw new IllegalArgumentException(new StringBuilder().append(value).append(" can't be represented as char").toString());
        }
        return (char)value;
    }
    
    public static byte safeIntToByte(final int value) {
        if (value < -128 || 127 < value) {
            throw new IllegalArgumentException(new StringBuilder().append(value).append(" can't be represented as byte (out of range)").toString());
        }
        return (byte)value;
    }
    
    public static short safeIntToShort(final int value) {
        if (value < -32768 || 32767 < value) {
            throw new IllegalArgumentException(new StringBuilder().append(value).append(" can't be represented as short (out of range)").toString());
        }
        return (short)value;
    }
    
    public static int safeLongToInt(final long value) {
        if (value < -2147483648L || 2147483647L < value) {
            throw new IllegalArgumentException(new StringBuilder().append(value).append(" can't be represented as int (out of range)").toString());
        }
        return (int)value;
    }
    
    public static float safeDoubleToFloat(final double value) {
        if (Double.isNaN(value)) {
            return Float.NaN;
        }
        if (Double.isInfinite(value)) {
            return (value < 0.0) ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
        }
        if (value < 1.401298464324817E-45 || 3.4028234663852886E38 < value) {
            throw new IllegalArgumentException(new StringBuilder().append(value).append(" can't be represented as float (out of range)").toString());
        }
        final float floatValue = (float)value;
        if (floatValue != value) {
            throw new IllegalArgumentException(new StringBuilder().append(value).append(" can't be represented as float (imprecise)").toString());
        }
        return floatValue;
    }
}

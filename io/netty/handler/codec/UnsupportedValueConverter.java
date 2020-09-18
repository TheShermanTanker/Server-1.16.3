package io.netty.handler.codec;

public final class UnsupportedValueConverter<V> implements ValueConverter<V> {
    private static final UnsupportedValueConverter INSTANCE;
    
    private UnsupportedValueConverter() {
    }
    
    public static <V> UnsupportedValueConverter<V> instance() {
        return (UnsupportedValueConverter<V>)UnsupportedValueConverter.INSTANCE;
    }
    
    public V convertObject(final Object value) {
        throw new UnsupportedOperationException();
    }
    
    public V convertBoolean(final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    public boolean convertToBoolean(final V value) {
        throw new UnsupportedOperationException();
    }
    
    public V convertByte(final byte value) {
        throw new UnsupportedOperationException();
    }
    
    public byte convertToByte(final V value) {
        throw new UnsupportedOperationException();
    }
    
    public V convertChar(final char value) {
        throw new UnsupportedOperationException();
    }
    
    public char convertToChar(final V value) {
        throw new UnsupportedOperationException();
    }
    
    public V convertShort(final short value) {
        throw new UnsupportedOperationException();
    }
    
    public short convertToShort(final V value) {
        throw new UnsupportedOperationException();
    }
    
    public V convertInt(final int value) {
        throw new UnsupportedOperationException();
    }
    
    public int convertToInt(final V value) {
        throw new UnsupportedOperationException();
    }
    
    public V convertLong(final long value) {
        throw new UnsupportedOperationException();
    }
    
    public long convertToLong(final V value) {
        throw new UnsupportedOperationException();
    }
    
    public V convertTimeMillis(final long value) {
        throw new UnsupportedOperationException();
    }
    
    public long convertToTimeMillis(final V value) {
        throw new UnsupportedOperationException();
    }
    
    public V convertFloat(final float value) {
        throw new UnsupportedOperationException();
    }
    
    public float convertToFloat(final V value) {
        throw new UnsupportedOperationException();
    }
    
    public V convertDouble(final double value) {
        throw new UnsupportedOperationException();
    }
    
    public double convertToDouble(final V value) {
        throw new UnsupportedOperationException();
    }
    
    static {
        INSTANCE = new UnsupportedValueConverter();
    }
}

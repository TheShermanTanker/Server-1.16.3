package io.netty.handler.codec;

public interface ValueConverter<T> {
    T convertObject(final Object object);
    
    T convertBoolean(final boolean boolean1);
    
    boolean convertToBoolean(final T object);
    
    T convertByte(final byte byte1);
    
    byte convertToByte(final T object);
    
    T convertChar(final char character);
    
    char convertToChar(final T object);
    
    T convertShort(final short short1);
    
    short convertToShort(final T object);
    
    T convertInt(final int integer);
    
    int convertToInt(final T object);
    
    T convertLong(final long long1);
    
    long convertToLong(final T object);
    
    T convertTimeMillis(final long long1);
    
    long convertToTimeMillis(final T object);
    
    T convertFloat(final float float1);
    
    float convertToFloat(final T object);
    
    T convertDouble(final double double1);
    
    double convertToDouble(final T object);
}

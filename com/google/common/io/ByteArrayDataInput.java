package com.google.common.io;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtIncompatible;
import java.io.DataInput;

@GwtIncompatible
public interface ByteArrayDataInput extends DataInput {
    void readFully(final byte[] arr);
    
    void readFully(final byte[] arr, final int integer2, final int integer3);
    
    int skipBytes(final int integer);
    
    @CanIgnoreReturnValue
    boolean readBoolean();
    
    @CanIgnoreReturnValue
    byte readByte();
    
    @CanIgnoreReturnValue
    int readUnsignedByte();
    
    @CanIgnoreReturnValue
    short readShort();
    
    @CanIgnoreReturnValue
    int readUnsignedShort();
    
    @CanIgnoreReturnValue
    char readChar();
    
    @CanIgnoreReturnValue
    int readInt();
    
    @CanIgnoreReturnValue
    long readLong();
    
    @CanIgnoreReturnValue
    float readFloat();
    
    @CanIgnoreReturnValue
    double readDouble();
    
    @CanIgnoreReturnValue
    String readLine();
    
    @CanIgnoreReturnValue
    String readUTF();
}

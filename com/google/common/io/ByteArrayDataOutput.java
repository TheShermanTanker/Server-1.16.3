package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import java.io.DataOutput;

@GwtIncompatible
public interface ByteArrayDataOutput extends DataOutput {
    void write(final int integer);
    
    void write(final byte[] arr);
    
    void write(final byte[] arr, final int integer2, final int integer3);
    
    void writeBoolean(final boolean boolean1);
    
    void writeByte(final int integer);
    
    void writeShort(final int integer);
    
    void writeChar(final int integer);
    
    void writeInt(final int integer);
    
    void writeLong(final long long1);
    
    void writeFloat(final float float1);
    
    void writeDouble(final double double1);
    
    void writeChars(final String string);
    
    void writeUTF(final String string);
    
    @Deprecated
    void writeBytes(final String string);
    
    byte[] toByteArray();
}

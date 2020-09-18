package io.netty.handler.codec.serialization;

import java.io.EOFException;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

class CompactObjectInputStream extends ObjectInputStream {
    private final ClassResolver classResolver;
    
    CompactObjectInputStream(final InputStream in, final ClassResolver classResolver) throws IOException {
        super(in);
        this.classResolver = classResolver;
    }
    
    protected void readStreamHeader() throws IOException {
        final int version = this.readByte() & 0xFF;
        if (version != 5) {
            throw new StreamCorruptedException(new StringBuilder().append("Unsupported version: ").append(version).toString());
        }
    }
    
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        final int type = this.read();
        if (type < 0) {
            throw new EOFException();
        }
        switch (type) {
            case 0: {
                return super.readClassDescriptor();
            }
            case 1: {
                final String className = this.readUTF();
                final Class<?> clazz = this.classResolver.resolve(className);
                return ObjectStreamClass.lookupAny((Class)clazz);
            }
            default: {
                throw new StreamCorruptedException(new StringBuilder().append("Unexpected class descriptor type: ").append(type).toString());
            }
        }
    }
    
    protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        Class<?> clazz;
        try {
            clazz = this.classResolver.resolve(desc.getName());
        }
        catch (ClassNotFoundException ignored) {
            clazz = super.resolveClass(desc);
        }
        return clazz;
    }
}

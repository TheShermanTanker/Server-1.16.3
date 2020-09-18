package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.MemcacheMessage;

public interface BinaryMemcacheMessage extends MemcacheMessage {
    byte magic();
    
    BinaryMemcacheMessage setMagic(final byte byte1);
    
    byte opcode();
    
    BinaryMemcacheMessage setOpcode(final byte byte1);
    
    short keyLength();
    
    byte extrasLength();
    
    byte dataType();
    
    BinaryMemcacheMessage setDataType(final byte byte1);
    
    int totalBodyLength();
    
    BinaryMemcacheMessage setTotalBodyLength(final int integer);
    
    int opaque();
    
    BinaryMemcacheMessage setOpaque(final int integer);
    
    long cas();
    
    BinaryMemcacheMessage setCas(final long long1);
    
    ByteBuf key();
    
    BinaryMemcacheMessage setKey(final ByteBuf byteBuf);
    
    ByteBuf extras();
    
    BinaryMemcacheMessage setExtras(final ByteBuf byteBuf);
    
    BinaryMemcacheMessage retain();
    
    BinaryMemcacheMessage retain(final int integer);
    
    BinaryMemcacheMessage touch();
    
    BinaryMemcacheMessage touch(final Object object);
}

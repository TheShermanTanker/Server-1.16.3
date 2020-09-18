package io.netty.handler.codec.haproxy;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;

public class HAProxyTLV extends DefaultByteBufHolder {
    private final Type type;
    private final byte typeByteValue;
    
    HAProxyTLV(final Type type, final byte typeByteValue, final ByteBuf content) {
        super(content);
        ObjectUtil.<Type>checkNotNull(type, "type");
        this.type = type;
        this.typeByteValue = typeByteValue;
    }
    
    public Type type() {
        return this.type;
    }
    
    public byte typeByteValue() {
        return this.typeByteValue;
    }
    
    @Override
    public HAProxyTLV copy() {
        return this.replace(this.content().copy());
    }
    
    @Override
    public HAProxyTLV duplicate() {
        return this.replace(this.content().duplicate());
    }
    
    @Override
    public HAProxyTLV retainedDuplicate() {
        return this.replace(this.content().retainedDuplicate());
    }
    
    @Override
    public HAProxyTLV replace(final ByteBuf content) {
        return new HAProxyTLV(this.type, this.typeByteValue, content);
    }
    
    @Override
    public HAProxyTLV retain() {
        super.retain();
        return this;
    }
    
    @Override
    public HAProxyTLV retain(final int increment) {
        super.retain(increment);
        return this;
    }
    
    @Override
    public HAProxyTLV touch() {
        super.touch();
        return this;
    }
    
    @Override
    public HAProxyTLV touch(final Object hint) {
        super.touch(hint);
        return this;
    }
    
    public enum Type {
        PP2_TYPE_ALPN, 
        PP2_TYPE_AUTHORITY, 
        PP2_TYPE_SSL, 
        PP2_TYPE_SSL_VERSION, 
        PP2_TYPE_SSL_CN, 
        PP2_TYPE_NETNS, 
        OTHER;
        
        public static Type typeForByteValue(final byte byteValue) {
            switch (byteValue) {
                case 1: {
                    return Type.PP2_TYPE_ALPN;
                }
                case 2: {
                    return Type.PP2_TYPE_AUTHORITY;
                }
                case 32: {
                    return Type.PP2_TYPE_SSL;
                }
                case 33: {
                    return Type.PP2_TYPE_SSL_VERSION;
                }
                case 34: {
                    return Type.PP2_TYPE_SSL_CN;
                }
                case 48: {
                    return Type.PP2_TYPE_NETNS;
                }
                default: {
                    return Type.OTHER;
                }
            }
        }
    }
}

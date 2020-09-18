package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface DnsRawRecord extends DnsRecord, ByteBufHolder {
    DnsRawRecord copy();
    
    DnsRawRecord duplicate();
    
    DnsRawRecord retainedDuplicate();
    
    DnsRawRecord replace(final ByteBuf byteBuf);
    
    DnsRawRecord retain();
    
    DnsRawRecord retain(final int integer);
    
    DnsRawRecord touch();
    
    DnsRawRecord touch(final Object object);
}

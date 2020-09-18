package io.netty.handler.codec.http2;

import io.netty.util.internal.StringUtil;

public class DefaultHttp2PingFrame implements Http2PingFrame {
    private final long content;
    private final boolean ack;
    
    public DefaultHttp2PingFrame(final long content) {
        this(content, false);
    }
    
    DefaultHttp2PingFrame(final long content, final boolean ack) {
        this.content = content;
        this.ack = ack;
    }
    
    public boolean ack() {
        return this.ack;
    }
    
    public String name() {
        return "PING";
    }
    
    public long content() {
        return this.content;
    }
    
    public boolean equals(final Object o) {
        if (!(o instanceof Http2PingFrame)) {
            return false;
        }
        final Http2PingFrame other = (Http2PingFrame)o;
        return this.ack == other.ack() && this.content == other.content();
    }
    
    public int hashCode() {
        int hash = super.hashCode();
        hash = hash * 31 + (this.ack ? 1 : 0);
        return hash;
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + "(content=" + this.content + ", ack=" + this.ack + ')';
    }
}

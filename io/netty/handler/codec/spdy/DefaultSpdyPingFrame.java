package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;

public class DefaultSpdyPingFrame implements SpdyPingFrame {
    private int id;
    
    public DefaultSpdyPingFrame(final int id) {
        this.setId(id);
    }
    
    public int id() {
        return this.id;
    }
    
    public SpdyPingFrame setId(final int id) {
        this.id = id;
        return this;
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + StringUtil.NEWLINE + "--> ID = " + this.id();
    }
}

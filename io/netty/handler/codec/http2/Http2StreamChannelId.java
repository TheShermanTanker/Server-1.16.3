package io.netty.handler.codec.http2;

import io.netty.channel.ChannelId;

final class Http2StreamChannelId implements ChannelId {
    private static final long serialVersionUID = -6642338822166867585L;
    private final int id;
    private final ChannelId parentId;
    
    Http2StreamChannelId(final ChannelId parentId, final int id) {
        this.parentId = parentId;
        this.id = id;
    }
    
    public String asShortText() {
        return this.parentId.asShortText() + '/' + this.id;
    }
    
    public String asLongText() {
        return this.parentId.asLongText() + '/' + this.id;
    }
    
    public int compareTo(final ChannelId o) {
        if (!(o instanceof Http2StreamChannelId)) {
            return this.parentId.compareTo(o);
        }
        final Http2StreamChannelId otherId = (Http2StreamChannelId)o;
        final int res = this.parentId.compareTo(otherId.parentId);
        if (res == 0) {
            return this.id - otherId.id;
        }
        return res;
    }
    
    public int hashCode() {
        return this.id * 31 + this.parentId.hashCode();
    }
    
    public boolean equals(final Object obj) {
        if (!(obj instanceof Http2StreamChannelId)) {
            return false;
        }
        final Http2StreamChannelId otherId = (Http2StreamChannelId)obj;
        return this.id == otherId.id && this.parentId.equals(otherId.parentId);
    }
    
    public String toString() {
        return this.asShortText();
    }
}

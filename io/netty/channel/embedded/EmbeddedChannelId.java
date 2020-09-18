package io.netty.channel.embedded;

import io.netty.channel.ChannelId;

final class EmbeddedChannelId implements ChannelId {
    private static final long serialVersionUID = -251711922203466130L;
    static final ChannelId INSTANCE;
    
    private EmbeddedChannelId() {
    }
    
    public String asShortText() {
        return this.toString();
    }
    
    public String asLongText() {
        return this.toString();
    }
    
    public int compareTo(final ChannelId o) {
        if (o instanceof EmbeddedChannelId) {
            return 0;
        }
        return this.asLongText().compareTo(o.asLongText());
    }
    
    public int hashCode() {
        return 0;
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof EmbeddedChannelId;
    }
    
    public String toString() {
        return "embedded";
    }
    
    static {
        INSTANCE = new EmbeddedChannelId();
    }
}

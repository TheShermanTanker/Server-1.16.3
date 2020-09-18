package io.netty.handler.codec.http2;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.ObjectUtil;

public class DefaultHttp2SettingsFrame implements Http2SettingsFrame {
    private final Http2Settings settings;
    
    public DefaultHttp2SettingsFrame(final Http2Settings settings) {
        this.settings = ObjectUtil.<Http2Settings>checkNotNull(settings, "settings");
    }
    
    public Http2Settings settings() {
        return this.settings;
    }
    
    public String name() {
        return "SETTINGS";
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + "(settings=" + this.settings + ')';
    }
}

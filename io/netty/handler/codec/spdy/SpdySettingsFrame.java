package io.netty.handler.codec.spdy;

import java.util.Set;

public interface SpdySettingsFrame extends SpdyFrame {
    public static final int SETTINGS_MINOR_VERSION = 0;
    public static final int SETTINGS_UPLOAD_BANDWIDTH = 1;
    public static final int SETTINGS_DOWNLOAD_BANDWIDTH = 2;
    public static final int SETTINGS_ROUND_TRIP_TIME = 3;
    public static final int SETTINGS_MAX_CONCURRENT_STREAMS = 4;
    public static final int SETTINGS_CURRENT_CWND = 5;
    public static final int SETTINGS_DOWNLOAD_RETRANS_RATE = 6;
    public static final int SETTINGS_INITIAL_WINDOW_SIZE = 7;
    public static final int SETTINGS_CLIENT_CERTIFICATE_VECTOR_SIZE = 8;
    
    Set<Integer> ids();
    
    boolean isSet(final int integer);
    
    int getValue(final int integer);
    
    SpdySettingsFrame setValue(final int integer1, final int integer2);
    
    SpdySettingsFrame setValue(final int integer1, final int integer2, final boolean boolean3, final boolean boolean4);
    
    SpdySettingsFrame removeValue(final int integer);
    
    boolean isPersistValue(final int integer);
    
    SpdySettingsFrame setPersistValue(final int integer, final boolean boolean2);
    
    boolean isPersisted(final int integer);
    
    SpdySettingsFrame setPersisted(final int integer, final boolean boolean2);
    
    boolean clearPreviouslyPersistedSettings();
    
    SpdySettingsFrame setClearPreviouslyPersistedSettings(final boolean boolean1);
}

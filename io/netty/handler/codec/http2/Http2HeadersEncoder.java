package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public interface Http2HeadersEncoder {
    public static final SensitivityDetector NEVER_SENSITIVE = new SensitivityDetector() {
        public boolean isSensitive(final CharSequence name, final CharSequence value) {
            return false;
        }
    };
    public static final SensitivityDetector ALWAYS_SENSITIVE = new SensitivityDetector() {
        public boolean isSensitive(final CharSequence name, final CharSequence value) {
            return true;
        }
    };
    
    void encodeHeaders(final int integer, final Http2Headers http2Headers, final ByteBuf byteBuf) throws Http2Exception;
    
    Configuration configuration();
    
    public interface SensitivityDetector {
        boolean isSensitive(final CharSequence charSequence1, final CharSequence charSequence2);
    }
    
    public interface Configuration {
        void maxHeaderTableSize(final long long1) throws Http2Exception;
        
        long maxHeaderTableSize();
        
        void maxHeaderListSize(final long long1) throws Http2Exception;
        
        long maxHeaderListSize();
    }
}

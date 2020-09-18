package io.netty.handler.codec.http.cookie;

public interface Cookie extends Comparable<Cookie> {
    public static final long UNDEFINED_MAX_AGE = Long.MIN_VALUE;
    
    String name();
    
    String value();
    
    void setValue(final String string);
    
    boolean wrap();
    
    void setWrap(final boolean boolean1);
    
    String domain();
    
    void setDomain(final String string);
    
    String path();
    
    void setPath(final String string);
    
    long maxAge();
    
    void setMaxAge(final long long1);
    
    boolean isSecure();
    
    void setSecure(final boolean boolean1);
    
    boolean isHttpOnly();
    
    void setHttpOnly(final boolean boolean1);
}

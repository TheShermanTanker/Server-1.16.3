package io.netty.handler.codec.http;

import java.util.Set;

@Deprecated
public interface Cookie extends io.netty.handler.codec.http.cookie.Cookie {
    @Deprecated
    String getName();
    
    @Deprecated
    String getValue();
    
    @Deprecated
    String getDomain();
    
    @Deprecated
    String getPath();
    
    @Deprecated
    String getComment();
    
    @Deprecated
    String comment();
    
    @Deprecated
    void setComment(final String string);
    
    @Deprecated
    long getMaxAge();
    
    @Deprecated
    long maxAge();
    
    @Deprecated
    void setMaxAge(final long long1);
    
    @Deprecated
    int getVersion();
    
    @Deprecated
    int version();
    
    @Deprecated
    void setVersion(final int integer);
    
    @Deprecated
    String getCommentUrl();
    
    @Deprecated
    String commentUrl();
    
    @Deprecated
    void setCommentUrl(final String string);
    
    @Deprecated
    boolean isDiscard();
    
    @Deprecated
    void setDiscard(final boolean boolean1);
    
    @Deprecated
    Set<Integer> getPorts();
    
    @Deprecated
    Set<Integer> ports();
    
    @Deprecated
    void setPorts(final int... arr);
    
    @Deprecated
    void setPorts(final Iterable<Integer> iterable);
}

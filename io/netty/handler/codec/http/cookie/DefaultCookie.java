package io.netty.handler.codec.http.cookie;

import io.netty.util.internal.ObjectUtil;

public class DefaultCookie implements Cookie {
    private final String name;
    private String value;
    private boolean wrap;
    private String domain;
    private String path;
    private long maxAge;
    private boolean secure;
    private boolean httpOnly;
    
    public DefaultCookie(String name, final String value) {
        this.maxAge = Long.MIN_VALUE;
        name = ObjectUtil.<String>checkNotNull(name, "name").trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        this.name = name;
        this.setValue(value);
    }
    
    public String name() {
        return this.name;
    }
    
    public String value() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = ObjectUtil.<String>checkNotNull(value, "value");
    }
    
    public boolean wrap() {
        return this.wrap;
    }
    
    public void setWrap(final boolean wrap) {
        this.wrap = wrap;
    }
    
    public String domain() {
        return this.domain;
    }
    
    public void setDomain(final String domain) {
        this.domain = CookieUtil.validateAttributeValue("domain", domain);
    }
    
    public String path() {
        return this.path;
    }
    
    public void setPath(final String path) {
        this.path = CookieUtil.validateAttributeValue("path", path);
    }
    
    public long maxAge() {
        return this.maxAge;
    }
    
    public void setMaxAge(final long maxAge) {
        this.maxAge = maxAge;
    }
    
    public boolean isSecure() {
        return this.secure;
    }
    
    public void setSecure(final boolean secure) {
        this.secure = secure;
    }
    
    public boolean isHttpOnly() {
        return this.httpOnly;
    }
    
    public void setHttpOnly(final boolean httpOnly) {
        this.httpOnly = httpOnly;
    }
    
    public int hashCode() {
        return this.name().hashCode();
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cookie)) {
            return false;
        }
        final Cookie that = (Cookie)o;
        if (!this.name().equals(that.name())) {
            return false;
        }
        if (this.path() == null) {
            if (that.path() != null) {
                return false;
            }
        }
        else {
            if (that.path() == null) {
                return false;
            }
            if (!this.path().equals(that.path())) {
                return false;
            }
        }
        if (this.domain() == null) {
            return that.domain() == null;
        }
        return this.domain().equalsIgnoreCase(that.domain());
    }
    
    public int compareTo(final Cookie c) {
        int v = this.name().compareTo(c.name());
        if (v != 0) {
            return v;
        }
        if (this.path() == null) {
            if (c.path() != null) {
                return -1;
            }
        }
        else {
            if (c.path() == null) {
                return 1;
            }
            v = this.path().compareTo(c.path());
            if (v != 0) {
                return v;
            }
        }
        if (this.domain() == null) {
            if (c.domain() != null) {
                return -1;
            }
            return 0;
        }
        else {
            if (c.domain() == null) {
                return 1;
            }
            v = this.domain().compareToIgnoreCase(c.domain());
            return v;
        }
    }
    
    @Deprecated
    protected String validateValue(final String name, final String value) {
        return CookieUtil.validateAttributeValue(name, value);
    }
    
    public String toString() {
        final StringBuilder buf = CookieUtil.stringBuilder().append(this.name()).append('=').append(this.value());
        if (this.domain() != null) {
            buf.append(", domain=").append(this.domain());
        }
        if (this.path() != null) {
            buf.append(", path=").append(this.path());
        }
        if (this.maxAge() >= 0L) {
            buf.append(", maxAge=").append(this.maxAge()).append('s');
        }
        if (this.isSecure()) {
            buf.append(", secure");
        }
        if (this.isHttpOnly()) {
            buf.append(", HTTPOnly");
        }
        return buf.toString();
    }
}

package io.netty.handler.codec.http.cookie;

public abstract class CookieEncoder {
    protected final boolean strict;
    
    protected CookieEncoder(final boolean strict) {
        this.strict = strict;
    }
    
    protected void validateCookie(final String name, final String value) {
        if (this.strict) {
            int pos;
            if ((pos = CookieUtil.firstInvalidCookieNameOctet((CharSequence)name)) >= 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Cookie name contains an invalid char: ").append(name.charAt(pos)).toString());
            }
            final CharSequence unwrappedValue = CookieUtil.unwrapValue((CharSequence)value);
            if (unwrappedValue == null) {
                throw new IllegalArgumentException("Cookie value wrapping quotes are not balanced: " + value);
            }
            if ((pos = CookieUtil.firstInvalidCookieValueOctet(unwrappedValue)) >= 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Cookie value contains an invalid char: ").append(value.charAt(pos)).toString());
            }
        }
    }
}

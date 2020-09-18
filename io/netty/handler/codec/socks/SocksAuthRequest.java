package io.netty.handler.codec.socks;

import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import java.nio.charset.CharsetEncoder;

public final class SocksAuthRequest extends SocksRequest {
    private static final CharsetEncoder asciiEncoder;
    private static final SocksSubnegotiationVersion SUBNEGOTIATION_VERSION;
    private final String username;
    private final String password;
    
    public SocksAuthRequest(final String username, final String password) {
        super(SocksRequestType.AUTH);
        if (username == null) {
            throw new NullPointerException("username");
        }
        if (password == null) {
            throw new NullPointerException("username");
        }
        if (!SocksAuthRequest.asciiEncoder.canEncode((CharSequence)username) || !SocksAuthRequest.asciiEncoder.canEncode((CharSequence)password)) {
            throw new IllegalArgumentException("username: " + username + " or password: **** values should be in pure ascii");
        }
        if (username.length() > 255) {
            throw new IllegalArgumentException("username: " + username + " exceeds 255 char limit");
        }
        if (password.length() > 255) {
            throw new IllegalArgumentException("password: **** exceeds 255 char limit");
        }
        this.username = username;
        this.password = password;
    }
    
    public String username() {
        return this.username;
    }
    
    public String password() {
        return this.password;
    }
    
    @Override
    public void encodeAsByteBuf(final ByteBuf byteBuf) {
        byteBuf.writeByte(SocksAuthRequest.SUBNEGOTIATION_VERSION.byteValue());
        byteBuf.writeByte(this.username.length());
        byteBuf.writeCharSequence((CharSequence)this.username, CharsetUtil.US_ASCII);
        byteBuf.writeByte(this.password.length());
        byteBuf.writeCharSequence((CharSequence)this.password, CharsetUtil.US_ASCII);
    }
    
    static {
        asciiEncoder = CharsetUtil.encoder(CharsetUtil.US_ASCII);
        SUBNEGOTIATION_VERSION = SocksSubnegotiationVersion.AUTH_PASSWORD;
    }
}

package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class Socks5PasswordAuthResponseDecoder extends ReplayingDecoder<State> {
    public Socks5PasswordAuthResponseDecoder() {
        super(State.INIT);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        try {
            switch (this.state()) {
                case INIT: {
                    final byte version = in.readByte();
                    if (version != 1) {
                        throw new DecoderException(new StringBuilder().append("unsupported subnegotiation version: ").append((int)version).append(" (expected: 1)").toString());
                    }
                    out.add(new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.valueOf(in.readByte())));
                    this.checkpoint(State.SUCCESS);
                }
                case SUCCESS: {
                    final int readableBytes = this.actualReadableBytes();
                    if (readableBytes > 0) {
                        out.add(in.readRetainedSlice(readableBytes));
                        break;
                    }
                    break;
                }
                case FAILURE: {
                    in.skipBytes(this.actualReadableBytes());
                    break;
                }
            }
        }
        catch (Exception e) {
            this.fail(out, e);
        }
    }
    
    private void fail(final List<Object> out, Exception cause) {
        if (!(cause instanceof DecoderException)) {
            cause = (Exception)new DecoderException((Throwable)cause);
        }
        this.checkpoint(State.FAILURE);
        final Socks5Message m = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.FAILURE);
        m.setDecoderResult(DecoderResult.failure((Throwable)cause));
        out.add(m);
    }
    
    enum State {
        INIT, 
        SUCCESS, 
        FAILURE;
    }
}

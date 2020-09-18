package io.netty.handler.codec.socksx;

import io.netty.handler.codec.DecoderResult;

public abstract class AbstractSocksMessage implements SocksMessage {
    private DecoderResult decoderResult;
    
    public AbstractSocksMessage() {
        this.decoderResult = DecoderResult.SUCCESS;
    }
    
    public DecoderResult decoderResult() {
        return this.decoderResult;
    }
    
    public void setDecoderResult(final DecoderResult decoderResult) {
        if (decoderResult == null) {
            throw new NullPointerException("decoderResult");
        }
        this.decoderResult = decoderResult;
    }
}

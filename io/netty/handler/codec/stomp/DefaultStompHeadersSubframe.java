package io.netty.handler.codec.stomp;

import io.netty.handler.codec.DecoderResult;

public class DefaultStompHeadersSubframe implements StompHeadersSubframe {
    protected final StompCommand command;
    protected DecoderResult decoderResult;
    protected final DefaultStompHeaders headers;
    
    public DefaultStompHeadersSubframe(final StompCommand command) {
        this(command, null);
    }
    
    DefaultStompHeadersSubframe(final StompCommand command, final DefaultStompHeaders headers) {
        this.decoderResult = DecoderResult.SUCCESS;
        if (command == null) {
            throw new NullPointerException("command");
        }
        this.command = command;
        this.headers = ((headers == null) ? new DefaultStompHeaders() : headers);
    }
    
    public StompCommand command() {
        return this.command;
    }
    
    public StompHeaders headers() {
        return this.headers;
    }
    
    public DecoderResult decoderResult() {
        return this.decoderResult;
    }
    
    public void setDecoderResult(final DecoderResult decoderResult) {
        this.decoderResult = decoderResult;
    }
    
    public String toString() {
        return new StringBuilder().append("StompFrame{command=").append(this.command).append(", headers=").append(this.headers).append('}').toString();
    }
}

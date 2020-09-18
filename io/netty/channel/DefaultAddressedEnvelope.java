package io.netty.channel;

import io.netty.util.internal.StringUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import java.net.SocketAddress;

public class DefaultAddressedEnvelope<M, A extends SocketAddress> implements AddressedEnvelope<M, A> {
    private final M message;
    private final A sender;
    private final A recipient;
    
    public DefaultAddressedEnvelope(final M message, final A recipient, final A sender) {
        if (message == null) {
            throw new NullPointerException("message");
        }
        if (recipient == null && sender == null) {
            throw new NullPointerException("recipient and sender");
        }
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
    }
    
    public DefaultAddressedEnvelope(final M message, final A recipient) {
        this(message, recipient, null);
    }
    
    public M content() {
        return this.message;
    }
    
    public A sender() {
        return this.sender;
    }
    
    public A recipient() {
        return this.recipient;
    }
    
    public int refCnt() {
        if (this.message instanceof ReferenceCounted) {
            return ((ReferenceCounted)this.message).refCnt();
        }
        return 1;
    }
    
    public AddressedEnvelope<M, A> retain() {
        ReferenceCountUtil.<M>retain(this.message);
        return this;
    }
    
    public AddressedEnvelope<M, A> retain(final int increment) {
        ReferenceCountUtil.<M>retain(this.message, increment);
        return this;
    }
    
    public boolean release() {
        return ReferenceCountUtil.release(this.message);
    }
    
    public boolean release(final int decrement) {
        return ReferenceCountUtil.release(this.message, decrement);
    }
    
    public AddressedEnvelope<M, A> touch() {
        ReferenceCountUtil.<M>touch(this.message);
        return this;
    }
    
    public AddressedEnvelope<M, A> touch(final Object hint) {
        ReferenceCountUtil.<M>touch(this.message, hint);
        return this;
    }
    
    public String toString() {
        if (this.sender != null) {
            return StringUtil.simpleClassName(this) + '(' + this.sender + " => " + this.recipient + ", " + this.message + ')';
        }
        return StringUtil.simpleClassName(this) + "(=> " + this.recipient + ", " + this.message + ')';
    }
}

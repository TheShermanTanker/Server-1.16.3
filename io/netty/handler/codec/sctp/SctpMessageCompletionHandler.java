package io.netty.handler.codec.sctp;

import java.util.Iterator;
import io.netty.buffer.Unpooled;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import io.netty.buffer.ByteBuf;
import java.util.Map;
import io.netty.channel.sctp.SctpMessage;
import io.netty.handler.codec.MessageToMessageDecoder;

public class SctpMessageCompletionHandler extends MessageToMessageDecoder<SctpMessage> {
    private final Map<Integer, ByteBuf> fragments;
    
    public SctpMessageCompletionHandler() {
        this.fragments = (Map<Integer, ByteBuf>)new HashMap();
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final SctpMessage msg, final List<Object> out) throws Exception {
        final ByteBuf byteBuf = msg.content();
        final int protocolIdentifier = msg.protocolIdentifier();
        final int streamIdentifier = msg.streamIdentifier();
        final boolean isComplete = msg.isComplete();
        final boolean isUnordered = msg.isUnordered();
        ByteBuf frag = (ByteBuf)this.fragments.remove(streamIdentifier);
        if (frag == null) {
            frag = Unpooled.EMPTY_BUFFER;
        }
        if (isComplete && !frag.isReadable()) {
            out.add(msg);
        }
        else if (!isComplete && frag.isReadable()) {
            this.fragments.put(streamIdentifier, Unpooled.wrappedBuffer(frag, byteBuf));
        }
        else if (isComplete && frag.isReadable()) {
            final SctpMessage assembledMsg = new SctpMessage(protocolIdentifier, streamIdentifier, isUnordered, Unpooled.wrappedBuffer(frag, byteBuf));
            out.add(assembledMsg);
        }
        else {
            this.fragments.put(streamIdentifier, byteBuf);
        }
        byteBuf.retain();
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        for (final ByteBuf buffer : this.fragments.values()) {
            buffer.release();
        }
        this.fragments.clear();
        super.handlerRemoved(ctx);
    }
}

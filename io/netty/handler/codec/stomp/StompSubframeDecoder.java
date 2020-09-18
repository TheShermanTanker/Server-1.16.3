package io.netty.handler.codec.stomp;

import io.netty.handler.codec.Headers;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.AppendableCharSequence;
import io.netty.handler.codec.DecoderException;
import java.util.Locale;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.DecoderResult;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class StompSubframeDecoder extends ReplayingDecoder<State> {
    private static final int DEFAULT_CHUNK_SIZE = 8132;
    private static final int DEFAULT_MAX_LINE_LENGTH = 1024;
    private final int maxLineLength;
    private final int maxChunkSize;
    private final boolean validateHeaders;
    private int alreadyReadChunkSize;
    private LastStompContentSubframe lastContent;
    private long contentLength;
    
    public StompSubframeDecoder() {
        this(1024, 8132);
    }
    
    public StompSubframeDecoder(final boolean validateHeaders) {
        this(1024, 8132, validateHeaders);
    }
    
    public StompSubframeDecoder(final int maxLineLength, final int maxChunkSize) {
        this(maxLineLength, maxChunkSize, false);
    }
    
    public StompSubframeDecoder(final int maxLineLength, final int maxChunkSize, final boolean validateHeaders) {
        super(State.SKIP_CONTROL_CHARACTERS);
        this.contentLength = -1L;
        if (maxLineLength <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxLineLength must be a positive integer: ").append(maxLineLength).toString());
        }
        if (maxChunkSize <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("maxChunkSize must be a positive integer: ").append(maxChunkSize).toString());
        }
        this.maxChunkSize = maxChunkSize;
        this.maxLineLength = maxLineLength;
        this.validateHeaders = validateHeaders;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        switch (this.state()) {
            case SKIP_CONTROL_CHARACTERS: {
                skipControlCharacters(in);
                this.checkpoint(State.READ_HEADERS);
            }
            case READ_HEADERS: {
                StompCommand command = StompCommand.UNKNOWN;
                StompHeadersSubframe frame = null;
                try {
                    command = this.readCommand(in);
                    frame = new DefaultStompHeadersSubframe(command);
                    this.checkpoint(this.readHeaders(in, frame.headers()));
                    out.add(frame);
                    break;
                }
                catch (Exception e) {
                    if (frame == null) {
                        frame = new DefaultStompHeadersSubframe(command);
                    }
                    frame.setDecoderResult(DecoderResult.failure((Throwable)e));
                    out.add(frame);
                    this.checkpoint(State.BAD_FRAME);
                    return;
                }
            }
            case BAD_FRAME: {
                in.skipBytes(this.actualReadableBytes());
                return;
            }
        }
        try {
            Label_0474: {
                switch (this.state()) {
                    case READ_CONTENT: {
                        int toRead = in.readableBytes();
                        if (toRead == 0) {
                            return;
                        }
                        if (toRead > this.maxChunkSize) {
                            toRead = this.maxChunkSize;
                        }
                        if (this.contentLength >= 0L) {
                            final int remainingLength = (int)(this.contentLength - this.alreadyReadChunkSize);
                            if (toRead > remainingLength) {
                                toRead = remainingLength;
                            }
                            final ByteBuf chunkBuffer = ByteBufUtil.readBytes(ctx.alloc(), in, toRead);
                            final int alreadyReadChunkSize = this.alreadyReadChunkSize + toRead;
                            this.alreadyReadChunkSize = alreadyReadChunkSize;
                            if (alreadyReadChunkSize >= this.contentLength) {
                                this.lastContent = new DefaultLastStompContentSubframe(chunkBuffer);
                                this.checkpoint(State.FINALIZE_FRAME_READ);
                                break Label_0474;
                            }
                            out.add(new DefaultStompContentSubframe(chunkBuffer));
                            return;
                        }
                        else {
                            final int nulIndex = ByteBufUtil.indexOf(in, in.readerIndex(), in.writerIndex(), (byte)0);
                            if (nulIndex == in.readerIndex()) {
                                this.checkpoint(State.FINALIZE_FRAME_READ);
                                break Label_0474;
                            }
                            if (nulIndex > 0) {
                                toRead = nulIndex - in.readerIndex();
                            }
                            else {
                                toRead = in.writerIndex() - in.readerIndex();
                            }
                            final ByteBuf chunkBuffer = ByteBufUtil.readBytes(ctx.alloc(), in, toRead);
                            this.alreadyReadChunkSize += toRead;
                            if (nulIndex > 0) {
                                this.lastContent = new DefaultLastStompContentSubframe(chunkBuffer);
                                this.checkpoint(State.FINALIZE_FRAME_READ);
                                break Label_0474;
                            }
                            out.add(new DefaultStompContentSubframe(chunkBuffer));
                            return;
                        }
                        break;
                    }
                    case FINALIZE_FRAME_READ: {
                        skipNullCharacter(in);
                        if (this.lastContent == null) {
                            this.lastContent = LastStompContentSubframe.EMPTY_LAST_CONTENT;
                        }
                        out.add(this.lastContent);
                        this.resetDecoder();
                        break;
                    }
                }
            }
        }
        catch (Exception e2) {
            final StompContentSubframe errorContent = new DefaultLastStompContentSubframe(Unpooled.EMPTY_BUFFER);
            errorContent.setDecoderResult(DecoderResult.failure((Throwable)e2));
            out.add(errorContent);
            this.checkpoint(State.BAD_FRAME);
        }
    }
    
    private StompCommand readCommand(final ByteBuf in) {
        String commandStr = this.readLine(in, 16);
        StompCommand command = null;
        try {
            command = StompCommand.valueOf(commandStr);
        }
        catch (IllegalArgumentException ex) {}
        if (command == null) {
            commandStr = commandStr.toUpperCase(Locale.US);
            try {
                command = StompCommand.valueOf(commandStr);
            }
            catch (IllegalArgumentException ex2) {}
        }
        if (command == null) {
            throw new DecoderException("failed to read command from channel");
        }
        return command;
    }
    
    private State readHeaders(final ByteBuf buffer, final StompHeaders headers) {
        final AppendableCharSequence buf = new AppendableCharSequence(128);
        boolean headerRead;
        do {
            headerRead = this.readHeader(headers, buf, buffer);
        } while (headerRead);
        if (((Headers<CharSequence, V, T>)headers).contains((CharSequence)StompHeaders.CONTENT_LENGTH)) {
            this.contentLength = getContentLength(headers, 0L);
            if (this.contentLength == 0L) {
                return State.FINALIZE_FRAME_READ;
            }
        }
        return State.READ_CONTENT;
    }
    
    private static long getContentLength(final StompHeaders headers, final long defaultValue) {
        final long contentLength = ((Headers<CharSequence, V, T>)headers).getLong((CharSequence)StompHeaders.CONTENT_LENGTH, defaultValue);
        if (contentLength < 0L) {
            throw new DecoderException(new StringBuilder().append(StompHeaders.CONTENT_LENGTH).append(" must be non-negative").toString());
        }
        return contentLength;
    }
    
    private static void skipNullCharacter(final ByteBuf buffer) {
        final byte b = buffer.readByte();
        if (b != 0) {
            throw new IllegalStateException(new StringBuilder().append("unexpected byte in buffer ").append((int)b).append(" while expecting NULL byte").toString());
        }
    }
    
    private static void skipControlCharacters(final ByteBuf buffer) {
        byte b;
        do {
            b = buffer.readByte();
        } while (b == 13 || b == 10);
        buffer.readerIndex(buffer.readerIndex() - 1);
    }
    
    private String readLine(final ByteBuf buffer, final int initialBufferSize) {
        final AppendableCharSequence buf = new AppendableCharSequence(initialBufferSize);
        int lineLength = 0;
        while (true) {
            final byte nextByte = buffer.readByte();
            if (nextByte == 13) {
                continue;
            }
            if (nextByte == 10) {
                break;
            }
            if (lineLength >= this.maxLineLength) {
                this.invalidLineLength();
            }
            ++lineLength;
            buf.append((char)nextByte);
        }
        return buf.toString();
    }
    
    private boolean readHeader(final StompHeaders headers, final AppendableCharSequence buf, final ByteBuf buffer) {
        buf.reset();
        int lineLength = 0;
        String key = null;
        boolean valid = false;
        while (true) {
            final byte nextByte = buffer.readByte();
            if (nextByte == 58 && key == null) {
                key = buf.toString();
                valid = true;
                buf.reset();
            }
            else {
                if (nextByte == 13) {
                    continue;
                }
                if (nextByte == 10) {
                    break;
                }
                if (lineLength >= this.maxLineLength) {
                    this.invalidLineLength();
                }
                if (nextByte == 58 && key != null) {
                    valid = false;
                }
                ++lineLength;
                buf.append((char)nextByte);
            }
        }
        if (key == null && lineLength == 0) {
            return false;
        }
        if (valid) {
            ((Headers<CharSequence, CharSequence, Headers>)headers).add((CharSequence)key, (CharSequence)buf.toString());
        }
        else if (this.validateHeaders) {
            this.invalidHeader(key, buf.toString());
        }
        return true;
    }
    
    private void invalidHeader(final String key, final String value) {
        final String line = (key != null) ? (key + ":" + value) : value;
        throw new IllegalArgumentException("a header value or name contains a prohibited character ':', " + line);
    }
    
    private void invalidLineLength() {
        throw new TooLongFrameException(new StringBuilder().append("An STOMP line is larger than ").append(this.maxLineLength).append(" bytes.").toString());
    }
    
    private void resetDecoder() {
        this.checkpoint(State.SKIP_CONTROL_CHARACTERS);
        this.contentLength = -1L;
        this.alreadyReadChunkSize = 0;
        this.lastContent = null;
    }
    
    enum State {
        SKIP_CONTROL_CHARACTERS, 
        READ_HEADERS, 
        READ_CONTENT, 
        FINALIZE_FRAME_READ, 
        BAD_FRAME, 
        INVALID_CHUNK;
    }
}

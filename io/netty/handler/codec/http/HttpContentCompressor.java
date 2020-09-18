package io.netty.handler.codec.http;

import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

public class HttpContentCompressor extends HttpContentEncoder {
    private final int compressionLevel;
    private final int windowBits;
    private final int memLevel;
    private final int contentSizeThreshold;
    private ChannelHandlerContext ctx;
    
    public HttpContentCompressor() {
        this(6);
    }
    
    public HttpContentCompressor(final int compressionLevel) {
        this(compressionLevel, 15, 8, 0);
    }
    
    public HttpContentCompressor(final int compressionLevel, final int windowBits, final int memLevel) {
        this(compressionLevel, windowBits, memLevel, 0);
    }
    
    public HttpContentCompressor(final int compressionLevel, final int windowBits, final int memLevel, final int contentSizeThreshold) {
        if (compressionLevel < 0 || compressionLevel > 9) {
            throw new IllegalArgumentException(new StringBuilder().append("compressionLevel: ").append(compressionLevel).append(" (expected: 0-9)").toString());
        }
        if (windowBits < 9 || windowBits > 15) {
            throw new IllegalArgumentException(new StringBuilder().append("windowBits: ").append(windowBits).append(" (expected: 9-15)").toString());
        }
        if (memLevel < 1 || memLevel > 9) {
            throw new IllegalArgumentException(new StringBuilder().append("memLevel: ").append(memLevel).append(" (expected: 1-9)").toString());
        }
        if (contentSizeThreshold < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("contentSizeThreshold: ").append(contentSizeThreshold).append(" (expected: non negative number)").toString());
        }
        this.compressionLevel = compressionLevel;
        this.windowBits = windowBits;
        this.memLevel = memLevel;
        this.contentSizeThreshold = contentSizeThreshold;
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
    
    @Override
    protected Result beginEncode(final HttpResponse headers, final String acceptEncoding) throws Exception {
        if (this.contentSizeThreshold > 0 && headers instanceof HttpContent && ((HttpContent)headers).content().readableBytes() < this.contentSizeThreshold) {
            return null;
        }
        final String contentEncoding = headers.headers().get((CharSequence)HttpHeaderNames.CONTENT_ENCODING);
        if (contentEncoding != null) {
            return null;
        }
        final ZlibWrapper wrapper = this.determineWrapper(acceptEncoding);
        if (wrapper == null) {
            return null;
        }
        String targetContentEncoding = null;
        switch (wrapper) {
            case GZIP: {
                targetContentEncoding = "gzip";
                break;
            }
            case ZLIB: {
                targetContentEncoding = "deflate";
                break;
            }
            default: {
                throw new Error();
            }
        }
        return new Result(targetContentEncoding, new EmbeddedChannel(this.ctx.channel().id(), this.ctx.channel().metadata().hasDisconnect(), this.ctx.channel().config(), new ChannelHandler[] { ZlibCodecFactory.newZlibEncoder(wrapper, this.compressionLevel, this.windowBits, this.memLevel) }));
    }
    
    protected ZlibWrapper determineWrapper(final String acceptEncoding) {
        float starQ = -1.0f;
        float gzipQ = -1.0f;
        float deflateQ = -1.0f;
        for (final String encoding : acceptEncoding.split(",")) {
            float q = 1.0f;
            final int equalsPos = encoding.indexOf(61);
            if (equalsPos != -1) {
                try {
                    q = Float.parseFloat(encoding.substring(equalsPos + 1));
                }
                catch (NumberFormatException e) {
                    q = 0.0f;
                }
            }
            if (encoding.contains("*")) {
                starQ = q;
            }
            else if (encoding.contains("gzip") && q > gzipQ) {
                gzipQ = q;
            }
            else if (encoding.contains("deflate") && q > deflateQ) {
                deflateQ = q;
            }
        }
        if (gzipQ <= 0.0f && deflateQ <= 0.0f) {
            if (starQ > 0.0f) {
                if (gzipQ == -1.0f) {
                    return ZlibWrapper.GZIP;
                }
                if (deflateQ == -1.0f) {
                    return ZlibWrapper.ZLIB;
                }
            }
            return null;
        }
        if (gzipQ >= deflateQ) {
            return ZlibWrapper.GZIP;
        }
        return ZlibWrapper.ZLIB;
    }
}

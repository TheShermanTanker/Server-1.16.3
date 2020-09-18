package io.netty.handler.traffic;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.ChannelPromise;
import io.netty.util.Attribute;
import io.netty.channel.ChannelConfig;
import java.util.concurrent.TimeUnit;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelDuplexHandler;

public abstract class AbstractTrafficShapingHandler extends ChannelDuplexHandler {
    private static final InternalLogger logger;
    public static final long DEFAULT_CHECK_INTERVAL = 1000L;
    public static final long DEFAULT_MAX_TIME = 15000L;
    static final long DEFAULT_MAX_SIZE = 4194304L;
    static final long MINIMAL_WAIT = 10L;
    protected TrafficCounter trafficCounter;
    private volatile long writeLimit;
    private volatile long readLimit;
    protected volatile long maxTime;
    protected volatile long checkInterval;
    static final AttributeKey<Boolean> READ_SUSPENDED;
    static final AttributeKey<Runnable> REOPEN_TASK;
    volatile long maxWriteDelay;
    volatile long maxWriteSize;
    final int userDefinedWritabilityIndex;
    static final int CHANNEL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 1;
    static final int GLOBAL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 2;
    static final int GLOBALCHANNEL_DEFAULT_USER_DEFINED_WRITABILITY_INDEX = 3;
    
    void setTrafficCounter(final TrafficCounter newTrafficCounter) {
        this.trafficCounter = newTrafficCounter;
    }
    
    protected int userDefinedWritabilityIndex() {
        return 1;
    }
    
    protected AbstractTrafficShapingHandler(final long writeLimit, final long readLimit, final long checkInterval, final long maxTime) {
        this.maxTime = 15000L;
        this.checkInterval = 1000L;
        this.maxWriteDelay = 4000L;
        this.maxWriteSize = 4194304L;
        if (maxTime <= 0L) {
            throw new IllegalArgumentException("maxTime must be positive");
        }
        this.userDefinedWritabilityIndex = this.userDefinedWritabilityIndex();
        this.writeLimit = writeLimit;
        this.readLimit = readLimit;
        this.checkInterval = checkInterval;
        this.maxTime = maxTime;
    }
    
    protected AbstractTrafficShapingHandler(final long writeLimit, final long readLimit, final long checkInterval) {
        this(writeLimit, readLimit, checkInterval, 15000L);
    }
    
    protected AbstractTrafficShapingHandler(final long writeLimit, final long readLimit) {
        this(writeLimit, readLimit, 1000L, 15000L);
    }
    
    protected AbstractTrafficShapingHandler() {
        this(0L, 0L, 1000L, 15000L);
    }
    
    protected AbstractTrafficShapingHandler(final long checkInterval) {
        this(0L, 0L, checkInterval, 15000L);
    }
    
    public void configure(final long newWriteLimit, final long newReadLimit, final long newCheckInterval) {
        this.configure(newWriteLimit, newReadLimit);
        this.configure(newCheckInterval);
    }
    
    public void configure(final long newWriteLimit, final long newReadLimit) {
        this.writeLimit = newWriteLimit;
        this.readLimit = newReadLimit;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
        }
    }
    
    public void configure(final long newCheckInterval) {
        this.checkInterval = newCheckInterval;
        if (this.trafficCounter != null) {
            this.trafficCounter.configure(this.checkInterval);
        }
    }
    
    public long getWriteLimit() {
        return this.writeLimit;
    }
    
    public void setWriteLimit(final long writeLimit) {
        this.writeLimit = writeLimit;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
        }
    }
    
    public long getReadLimit() {
        return this.readLimit;
    }
    
    public void setReadLimit(final long readLimit) {
        this.readLimit = readLimit;
        if (this.trafficCounter != null) {
            this.trafficCounter.resetAccounting(TrafficCounter.milliSecondFromNano());
        }
    }
    
    public long getCheckInterval() {
        return this.checkInterval;
    }
    
    public void setCheckInterval(final long checkInterval) {
        this.checkInterval = checkInterval;
        if (this.trafficCounter != null) {
            this.trafficCounter.configure(checkInterval);
        }
    }
    
    public void setMaxTimeWait(final long maxTime) {
        if (maxTime <= 0L) {
            throw new IllegalArgumentException("maxTime must be positive");
        }
        this.maxTime = maxTime;
    }
    
    public long getMaxTimeWait() {
        return this.maxTime;
    }
    
    public long getMaxWriteDelay() {
        return this.maxWriteDelay;
    }
    
    public void setMaxWriteDelay(final long maxWriteDelay) {
        if (maxWriteDelay <= 0L) {
            throw new IllegalArgumentException("maxWriteDelay must be positive");
        }
        this.maxWriteDelay = maxWriteDelay;
    }
    
    public long getMaxWriteSize() {
        return this.maxWriteSize;
    }
    
    public void setMaxWriteSize(final long maxWriteSize) {
        this.maxWriteSize = maxWriteSize;
    }
    
    protected void doAccounting(final TrafficCounter counter) {
    }
    
    void releaseReadSuspended(final ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        channel.<Boolean>attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(false);
        channel.config().setAutoRead(true);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        final long size = this.calculateSize(msg);
        final long now = TrafficCounter.milliSecondFromNano();
        if (size > 0L) {
            long wait = this.trafficCounter.readTimeToWait(size, this.readLimit, this.maxTime, now);
            wait = this.checkWaitReadTime(ctx, wait, now);
            if (wait >= 10L) {
                final Channel channel = ctx.channel();
                final ChannelConfig config = channel.config();
                if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                    AbstractTrafficShapingHandler.logger.debug(new StringBuilder().append("Read suspend: ").append(wait).append(':').append(config.isAutoRead()).append(':').append(isHandlerActive(ctx)).toString());
                }
                if (config.isAutoRead() && isHandlerActive(ctx)) {
                    config.setAutoRead(false);
                    channel.<Boolean>attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(true);
                    final Attribute<Runnable> attr = channel.<Runnable>attr(AbstractTrafficShapingHandler.REOPEN_TASK);
                    Runnable reopenTask = attr.get();
                    if (reopenTask == null) {
                        reopenTask = (Runnable)new ReopenReadTimerTask(ctx);
                        attr.set(reopenTask);
                    }
                    ctx.executor().schedule(reopenTask, wait, TimeUnit.MILLISECONDS);
                    if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                        AbstractTrafficShapingHandler.logger.debug(new StringBuilder().append("Suspend final status => ").append(config.isAutoRead()).append(':').append(isHandlerActive(ctx)).append(" will reopened at: ").append(wait).toString());
                    }
                }
            }
        }
        this.informReadOperation(ctx, now);
        ctx.fireChannelRead(msg);
    }
    
    long checkWaitReadTime(final ChannelHandlerContext ctx, final long wait, final long now) {
        return wait;
    }
    
    void informReadOperation(final ChannelHandlerContext ctx, final long now) {
    }
    
    protected static boolean isHandlerActive(final ChannelHandlerContext ctx) {
        final Boolean suspended = ctx.channel().<Boolean>attr(AbstractTrafficShapingHandler.READ_SUSPENDED).get();
        return suspended == null || Boolean.FALSE.equals(suspended);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) {
        if (isHandlerActive(ctx)) {
            ctx.read();
        }
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        final long size = this.calculateSize(msg);
        final long now = TrafficCounter.milliSecondFromNano();
        if (size > 0L) {
            final long wait = this.trafficCounter.writeTimeToWait(size, this.writeLimit, this.maxTime, now);
            if (wait >= 10L) {
                if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                    AbstractTrafficShapingHandler.logger.debug(new StringBuilder().append("Write suspend: ").append(wait).append(':').append(ctx.channel().config().isAutoRead()).append(':').append(isHandlerActive(ctx)).toString());
                }
                this.submitWrite(ctx, msg, size, wait, now, promise);
                return;
            }
        }
        this.submitWrite(ctx, msg, size, 0L, now, promise);
    }
    
    @Deprecated
    protected void submitWrite(final ChannelHandlerContext ctx, final Object msg, final long delay, final ChannelPromise promise) {
        this.submitWrite(ctx, msg, this.calculateSize(msg), delay, TrafficCounter.milliSecondFromNano(), promise);
    }
    
    abstract void submitWrite(final ChannelHandlerContext channelHandlerContext, final Object object, final long long3, final long long4, final long long5, final ChannelPromise channelPromise);
    
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        this.setUserDefinedWritability(ctx, true);
        super.channelRegistered(ctx);
    }
    
    void setUserDefinedWritability(final ChannelHandlerContext ctx, final boolean writable) {
        final ChannelOutboundBuffer cob = ctx.channel().unsafe().outboundBuffer();
        if (cob != null) {
            cob.setUserDefinedWritability(this.userDefinedWritabilityIndex, writable);
        }
    }
    
    void checkWriteSuspend(final ChannelHandlerContext ctx, final long delay, final long queueSize) {
        if (queueSize > this.maxWriteSize || delay > this.maxWriteDelay) {
            this.setUserDefinedWritability(ctx, false);
        }
    }
    
    void releaseWriteSuspended(final ChannelHandlerContext ctx) {
        this.setUserDefinedWritability(ctx, true);
    }
    
    public TrafficCounter trafficCounter() {
        return this.trafficCounter;
    }
    
    public String toString() {
        final StringBuilder builder = new StringBuilder(290).append("TrafficShaping with Write Limit: ").append(this.writeLimit).append(" Read Limit: ").append(this.readLimit).append(" CheckInterval: ").append(this.checkInterval).append(" maxDelay: ").append(this.maxWriteDelay).append(" maxSize: ").append(this.maxWriteSize).append(" and Counter: ");
        if (this.trafficCounter != null) {
            builder.append(this.trafficCounter);
        }
        else {
            builder.append("none");
        }
        return builder.toString();
    }
    
    protected long calculateSize(final Object msg) {
        if (msg instanceof ByteBuf) {
            return ((ByteBuf)msg).readableBytes();
        }
        if (msg instanceof ByteBufHolder) {
            return ((ByteBufHolder)msg).content().readableBytes();
        }
        return -1L;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractTrafficShapingHandler.class);
        READ_SUSPENDED = AttributeKey.<Boolean>valueOf(AbstractTrafficShapingHandler.class.getName() + ".READ_SUSPENDED");
        REOPEN_TASK = AttributeKey.<Runnable>valueOf(AbstractTrafficShapingHandler.class.getName() + ".REOPEN_TASK");
    }
    
    static final class ReopenReadTimerTask implements Runnable {
        final ChannelHandlerContext ctx;
        
        ReopenReadTimerTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        
        public void run() {
            final Channel channel = this.ctx.channel();
            final ChannelConfig config = channel.config();
            if (!config.isAutoRead() && AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
                if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                    AbstractTrafficShapingHandler.logger.debug(new StringBuilder().append("Not unsuspend: ").append(config.isAutoRead()).append(':').append(AbstractTrafficShapingHandler.isHandlerActive(this.ctx)).toString());
                }
                channel.<Boolean>attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(false);
            }
            else {
                if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                    if (config.isAutoRead() && !AbstractTrafficShapingHandler.isHandlerActive(this.ctx)) {
                        AbstractTrafficShapingHandler.logger.debug(new StringBuilder().append("Unsuspend: ").append(config.isAutoRead()).append(':').append(AbstractTrafficShapingHandler.isHandlerActive(this.ctx)).toString());
                    }
                    else {
                        AbstractTrafficShapingHandler.logger.debug(new StringBuilder().append("Normal unsuspend: ").append(config.isAutoRead()).append(':').append(AbstractTrafficShapingHandler.isHandlerActive(this.ctx)).toString());
                    }
                }
                channel.<Boolean>attr(AbstractTrafficShapingHandler.READ_SUSPENDED).set(false);
                config.setAutoRead(true);
                channel.read();
            }
            if (AbstractTrafficShapingHandler.logger.isDebugEnabled()) {
                AbstractTrafficShapingHandler.logger.debug(new StringBuilder().append("Unsuspend final status => ").append(config.isAutoRead()).append(':').append(AbstractTrafficShapingHandler.isHandlerActive(this.ctx)).toString());
            }
        }
    }
}

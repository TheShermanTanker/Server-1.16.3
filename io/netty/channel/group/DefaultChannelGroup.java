package io.netty.channel.group;

import io.netty.util.internal.StringUtil;
import java.util.Map;
import java.util.LinkedHashMap;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBuf;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ServerChannel;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import java.util.concurrent.ConcurrentMap;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import io.netty.channel.Channel;
import java.util.AbstractSet;

public class DefaultChannelGroup extends AbstractSet<Channel> implements ChannelGroup {
    private static final AtomicInteger nextId;
    private final String name;
    private final EventExecutor executor;
    private final ConcurrentMap<ChannelId, Channel> serverChannels;
    private final ConcurrentMap<ChannelId, Channel> nonServerChannels;
    private final ChannelFutureListener remover;
    private final VoidChannelGroupFuture voidFuture;
    private final boolean stayClosed;
    private volatile boolean closed;
    
    public DefaultChannelGroup(final EventExecutor executor) {
        this(executor, false);
    }
    
    public DefaultChannelGroup(final String name, final EventExecutor executor) {
        this(name, executor, false);
    }
    
    public DefaultChannelGroup(final EventExecutor executor, final boolean stayClosed) {
        this("group-0x" + Integer.toHexString(DefaultChannelGroup.nextId.incrementAndGet()), executor, stayClosed);
    }
    
    public DefaultChannelGroup(final String name, final EventExecutor executor, final boolean stayClosed) {
        this.serverChannels = PlatformDependent.<ChannelId, Channel>newConcurrentHashMap();
        this.nonServerChannels = PlatformDependent.<ChannelId, Channel>newConcurrentHashMap();
        this.remover = new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture future) throws Exception {
                DefaultChannelGroup.this.remove(future.channel());
            }
        };
        this.voidFuture = new VoidChannelGroupFuture(this);
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
        this.executor = executor;
        this.stayClosed = stayClosed;
    }
    
    public String name() {
        return this.name;
    }
    
    public Channel find(final ChannelId id) {
        final Channel c = (Channel)this.nonServerChannels.get(id);
        if (c != null) {
            return c;
        }
        return (Channel)this.serverChannels.get(id);
    }
    
    public boolean isEmpty() {
        return this.nonServerChannels.isEmpty() && this.serverChannels.isEmpty();
    }
    
    public int size() {
        return this.nonServerChannels.size() + this.serverChannels.size();
    }
    
    public boolean contains(final Object o) {
        if (o instanceof ServerChannel) {
            return this.serverChannels.containsValue(o);
        }
        return o instanceof Channel && this.nonServerChannels.containsValue(o);
    }
    
    public boolean add(final Channel channel) {
        final ConcurrentMap<ChannelId, Channel> map = (channel instanceof ServerChannel) ? this.serverChannels : this.nonServerChannels;
        final boolean added = map.putIfAbsent(channel.id(), channel) == null;
        if (added) {
            channel.closeFuture().addListener(this.remover);
        }
        if (this.stayClosed && this.closed) {
            channel.close();
        }
        return added;
    }
    
    public boolean remove(final Object o) {
        Channel c = null;
        if (o instanceof ChannelId) {
            c = (Channel)this.nonServerChannels.remove(o);
            if (c == null) {
                c = (Channel)this.serverChannels.remove(o);
            }
        }
        else if (o instanceof Channel) {
            c = (Channel)o;
            if (c instanceof ServerChannel) {
                c = (Channel)this.serverChannels.remove(c.id());
            }
            else {
                c = (Channel)this.nonServerChannels.remove(c.id());
            }
        }
        if (c == null) {
            return false;
        }
        c.closeFuture().removeListener(this.remover);
        return true;
    }
    
    public void clear() {
        this.nonServerChannels.clear();
        this.serverChannels.clear();
    }
    
    public Iterator<Channel> iterator() {
        return (Iterator<Channel>)new CombinedIterator((java.util.Iterator<Object>)this.serverChannels.values().iterator(), (java.util.Iterator<Object>)this.nonServerChannels.values().iterator());
    }
    
    public Object[] toArray() {
        final Collection<Channel> channels = (Collection<Channel>)new ArrayList(this.size());
        channels.addAll(this.serverChannels.values());
        channels.addAll(this.nonServerChannels.values());
        return channels.toArray();
    }
    
    public <T> T[] toArray(final T[] a) {
        final Collection<Channel> channels = (Collection<Channel>)new ArrayList(this.size());
        channels.addAll(this.serverChannels.values());
        channels.addAll(this.nonServerChannels.values());
        return (T[])channels.toArray((Object[])a);
    }
    
    public ChannelGroupFuture close() {
        return this.close(ChannelMatchers.all());
    }
    
    public ChannelGroupFuture disconnect() {
        return this.disconnect(ChannelMatchers.all());
    }
    
    public ChannelGroupFuture deregister() {
        return this.deregister(ChannelMatchers.all());
    }
    
    public ChannelGroupFuture write(final Object message) {
        return this.write(message, ChannelMatchers.all());
    }
    
    private static Object safeDuplicate(final Object message) {
        if (message instanceof ByteBuf) {
            return ((ByteBuf)message).retainedDuplicate();
        }
        if (message instanceof ByteBufHolder) {
            return ((ByteBufHolder)message).retainedDuplicate();
        }
        return ReferenceCountUtil.retain(message);
    }
    
    public ChannelGroupFuture write(final Object message, final ChannelMatcher matcher) {
        return this.write(message, matcher, false);
    }
    
    public ChannelGroupFuture write(final Object message, final ChannelMatcher matcher, final boolean voidPromise) {
        if (message == null) {
            throw new NullPointerException("message");
        }
        if (matcher == null) {
            throw new NullPointerException("matcher");
        }
        ChannelGroupFuture future;
        if (voidPromise) {
            for (final Channel c : this.nonServerChannels.values()) {
                if (matcher.matches(c)) {
                    c.write(safeDuplicate(message), c.voidPromise());
                }
            }
            future = this.voidFuture;
        }
        else {
            final Map<Channel, ChannelFuture> futures = (Map<Channel, ChannelFuture>)new LinkedHashMap(this.size());
            for (final Channel c2 : this.nonServerChannels.values()) {
                if (matcher.matches(c2)) {
                    futures.put(c2, c2.write(safeDuplicate(message)));
                }
            }
            future = new DefaultChannelGroupFuture(this, futures, this.executor);
        }
        ReferenceCountUtil.release(message);
        return future;
    }
    
    public ChannelGroup flush() {
        return this.flush(ChannelMatchers.all());
    }
    
    public ChannelGroupFuture flushAndWrite(final Object message) {
        return this.writeAndFlush(message);
    }
    
    public ChannelGroupFuture writeAndFlush(final Object message) {
        return this.writeAndFlush(message, ChannelMatchers.all());
    }
    
    public ChannelGroupFuture disconnect(final ChannelMatcher matcher) {
        if (matcher == null) {
            throw new NullPointerException("matcher");
        }
        final Map<Channel, ChannelFuture> futures = (Map<Channel, ChannelFuture>)new LinkedHashMap(this.size());
        for (final Channel c : this.serverChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.disconnect());
            }
        }
        for (final Channel c : this.nonServerChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.disconnect());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }
    
    public ChannelGroupFuture close(final ChannelMatcher matcher) {
        if (matcher == null) {
            throw new NullPointerException("matcher");
        }
        final Map<Channel, ChannelFuture> futures = (Map<Channel, ChannelFuture>)new LinkedHashMap(this.size());
        if (this.stayClosed) {
            this.closed = true;
        }
        for (final Channel c : this.serverChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.close());
            }
        }
        for (final Channel c : this.nonServerChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.close());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }
    
    public ChannelGroupFuture deregister(final ChannelMatcher matcher) {
        if (matcher == null) {
            throw new NullPointerException("matcher");
        }
        final Map<Channel, ChannelFuture> futures = (Map<Channel, ChannelFuture>)new LinkedHashMap(this.size());
        for (final Channel c : this.serverChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.deregister());
            }
        }
        for (final Channel c : this.nonServerChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.deregister());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }
    
    public ChannelGroup flush(final ChannelMatcher matcher) {
        for (final Channel c : this.nonServerChannels.values()) {
            if (matcher.matches(c)) {
                c.flush();
            }
        }
        return this;
    }
    
    public ChannelGroupFuture flushAndWrite(final Object message, final ChannelMatcher matcher) {
        return this.writeAndFlush(message, matcher);
    }
    
    public ChannelGroupFuture writeAndFlush(final Object message, final ChannelMatcher matcher) {
        return this.writeAndFlush(message, matcher, false);
    }
    
    public ChannelGroupFuture writeAndFlush(final Object message, final ChannelMatcher matcher, final boolean voidPromise) {
        if (message == null) {
            throw new NullPointerException("message");
        }
        ChannelGroupFuture future;
        if (voidPromise) {
            for (final Channel c : this.nonServerChannels.values()) {
                if (matcher.matches(c)) {
                    c.writeAndFlush(safeDuplicate(message), c.voidPromise());
                }
            }
            future = this.voidFuture;
        }
        else {
            final Map<Channel, ChannelFuture> futures = (Map<Channel, ChannelFuture>)new LinkedHashMap(this.size());
            for (final Channel c2 : this.nonServerChannels.values()) {
                if (matcher.matches(c2)) {
                    futures.put(c2, c2.writeAndFlush(safeDuplicate(message)));
                }
            }
            future = new DefaultChannelGroupFuture(this, futures, this.executor);
        }
        ReferenceCountUtil.release(message);
        return future;
    }
    
    public ChannelGroupFuture newCloseFuture() {
        return this.newCloseFuture(ChannelMatchers.all());
    }
    
    public ChannelGroupFuture newCloseFuture(final ChannelMatcher matcher) {
        final Map<Channel, ChannelFuture> futures = (Map<Channel, ChannelFuture>)new LinkedHashMap(this.size());
        for (final Channel c : this.serverChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.closeFuture());
            }
        }
        for (final Channel c : this.nonServerChannels.values()) {
            if (matcher.matches(c)) {
                futures.put(c, c.closeFuture());
            }
        }
        return new DefaultChannelGroupFuture(this, futures, this.executor);
    }
    
    public int hashCode() {
        return System.identityHashCode(this);
    }
    
    public boolean equals(final Object o) {
        return this == o;
    }
    
    public int compareTo(final ChannelGroup o) {
        final int v = this.name().compareTo(o.name());
        if (v != 0) {
            return v;
        }
        return System.identityHashCode(this) - System.identityHashCode(o);
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + "(name: " + this.name() + ", size: " + this.size() + ')';
    }
    
    static {
        nextId = new AtomicInteger();
    }
}

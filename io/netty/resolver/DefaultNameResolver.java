package io.netty.resolver;

import java.util.Arrays;
import java.util.List;
import java.net.UnknownHostException;
import io.netty.util.internal.SocketUtils;
import java.net.InetAddress;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.EventExecutor;

public class DefaultNameResolver extends InetNameResolver {
    public DefaultNameResolver(final EventExecutor executor) {
        super(executor);
    }
    
    @Override
    protected void doResolve(final String inetHost, final Promise<InetAddress> promise) throws Exception {
        try {
            promise.setSuccess(SocketUtils.addressByName(inetHost));
        }
        catch (UnknownHostException e) {
            promise.setFailure((Throwable)e);
        }
    }
    
    @Override
    protected void doResolveAll(final String inetHost, final Promise<List<InetAddress>> promise) throws Exception {
        try {
            promise.setSuccess((List<InetAddress>)Arrays.asList((Object[])SocketUtils.allAddressesByName(inetHost)));
        }
        catch (UnknownHostException e) {
            promise.setFailure((Throwable)e);
        }
    }
}

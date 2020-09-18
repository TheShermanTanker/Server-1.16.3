package io.netty.handler.ipfilter;

import java.net.InetSocketAddress;

public interface IpFilterRule {
    boolean matches(final InetSocketAddress inetSocketAddress);
    
    IpFilterRuleType ruleType();
}

package io.netty.handler.ssl;

import io.netty.util.internal.ObjectUtil;
import java.util.List;

@Deprecated
public final class OpenSslNpnApplicationProtocolNegotiator implements OpenSslApplicationProtocolNegotiator {
    private final List<String> protocols;
    
    public OpenSslNpnApplicationProtocolNegotiator(final Iterable<String> protocols) {
        this.protocols = ObjectUtil.<List<String>>checkNotNull(ApplicationProtocolUtil.toList(protocols), "protocols");
    }
    
    public OpenSslNpnApplicationProtocolNegotiator(final String... protocols) {
        this.protocols = ObjectUtil.<List<String>>checkNotNull(ApplicationProtocolUtil.toList(protocols), "protocols");
    }
    
    public ApplicationProtocolConfig.Protocol protocol() {
        return ApplicationProtocolConfig.Protocol.NPN;
    }
    
    public List<String> protocols() {
        return this.protocols;
    }
    
    public ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior() {
        return ApplicationProtocolConfig.SelectorFailureBehavior.CHOOSE_MY_LAST_PROTOCOL;
    }
    
    public ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior() {
        return ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT;
    }
}

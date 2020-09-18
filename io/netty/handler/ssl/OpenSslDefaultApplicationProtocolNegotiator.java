package io.netty.handler.ssl;

import java.util.List;
import io.netty.util.internal.ObjectUtil;

@Deprecated
public final class OpenSslDefaultApplicationProtocolNegotiator implements OpenSslApplicationProtocolNegotiator {
    private final ApplicationProtocolConfig config;
    
    public OpenSslDefaultApplicationProtocolNegotiator(final ApplicationProtocolConfig config) {
        this.config = ObjectUtil.<ApplicationProtocolConfig>checkNotNull(config, "config");
    }
    
    public List<String> protocols() {
        return this.config.supportedProtocols();
    }
    
    public ApplicationProtocolConfig.Protocol protocol() {
        return this.config.protocol();
    }
    
    public ApplicationProtocolConfig.SelectorFailureBehavior selectorFailureBehavior() {
        return this.config.selectorFailureBehavior();
    }
    
    public ApplicationProtocolConfig.SelectedListenerFailureBehavior selectedListenerFailureBehavior() {
        return this.config.selectedListenerFailureBehavior();
    }
}

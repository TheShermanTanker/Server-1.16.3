package io.netty.handler.ssl;

import javax.net.ssl.SSLException;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashSet;
import io.netty.util.internal.PlatformDependent;
import java.util.List;
import org.eclipse.jetty.npn.NextProtoNego;
import io.netty.util.internal.ObjectUtil;
import javax.net.ssl.SSLEngine;

final class JettyNpnSslEngine extends JdkSslEngine {
    private static boolean available;
    
    static boolean isAvailable() {
        updateAvailability();
        return JettyNpnSslEngine.available;
    }
    
    private static void updateAvailability() {
        if (JettyNpnSslEngine.available) {
            return;
        }
        try {
            Class.forName("sun.security.ssl.NextProtoNegoExtension", true, (ClassLoader)null);
            JettyNpnSslEngine.available = true;
        }
        catch (Exception ex) {}
    }
    
    JettyNpnSslEngine(final SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator, final boolean server) {
        super(engine);
        ObjectUtil.<JdkApplicationProtocolNegotiator>checkNotNull(applicationNegotiator, "applicationNegotiator");
        if (server) {
            final JdkApplicationProtocolNegotiator.ProtocolSelectionListener protocolListener = ObjectUtil.<JdkApplicationProtocolNegotiator.ProtocolSelectionListener>checkNotNull(applicationNegotiator.protocolListenerFactory().newListener(this, applicationNegotiator.protocols()), "protocolListener");
            NextProtoNego.put(engine, (NextProtoNego.Provider)new NextProtoNego.ServerProvider() {
                public void unsupported() {
                    protocolListener.unsupported();
                }
                
                public List<String> protocols() {
                    return applicationNegotiator.protocols();
                }
                
                public void protocolSelected(final String protocol) {
                    try {
                        protocolListener.selected(protocol);
                    }
                    catch (Throwable t) {
                        PlatformDependent.throwException(t);
                    }
                }
            });
        }
        else {
            final JdkApplicationProtocolNegotiator.ProtocolSelector protocolSelector = ObjectUtil.<JdkApplicationProtocolNegotiator.ProtocolSelector>checkNotNull(applicationNegotiator.protocolSelectorFactory().newSelector(this, (Set<String>)new LinkedHashSet((Collection)applicationNegotiator.protocols())), "protocolSelector");
            NextProtoNego.put(engine, (NextProtoNego.Provider)new NextProtoNego.ClientProvider() {
                public boolean supports() {
                    return true;
                }
                
                public void unsupported() {
                    protocolSelector.unsupported();
                }
                
                public String selectProtocol(final List<String> protocols) {
                    try {
                        return protocolSelector.select(protocols);
                    }
                    catch (Throwable t) {
                        PlatformDependent.throwException(t);
                        return null;
                    }
                }
            });
        }
    }
    
    @Override
    public void closeInbound() throws SSLException {
        NextProtoNego.remove(this.getWrappedEngine());
        super.closeInbound();
    }
    
    @Override
    public void closeOutbound() {
        NextProtoNego.remove(this.getWrappedEngine());
        super.closeOutbound();
    }
}

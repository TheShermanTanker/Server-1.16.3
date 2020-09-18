package io.netty.handler.ssl;

interface OpenSslEngineMap {
    ReferenceCountedOpenSslEngine remove(final long long1);
    
    void add(final ReferenceCountedOpenSslEngine referenceCountedOpenSslEngine);
    
    ReferenceCountedOpenSslEngine get(final long long1);
}

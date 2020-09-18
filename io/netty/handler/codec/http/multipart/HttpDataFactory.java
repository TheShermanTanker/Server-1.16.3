package io.netty.handler.codec.http.multipart;

import java.nio.charset.Charset;
import io.netty.handler.codec.http.HttpRequest;

public interface HttpDataFactory {
    void setMaxLimit(final long long1);
    
    Attribute createAttribute(final HttpRequest httpRequest, final String string);
    
    Attribute createAttribute(final HttpRequest httpRequest, final String string, final long long3);
    
    Attribute createAttribute(final HttpRequest httpRequest, final String string2, final String string3);
    
    FileUpload createFileUpload(final HttpRequest httpRequest, final String string2, final String string3, final String string4, final String string5, final Charset charset, final long long7);
    
    void removeHttpDataFromClean(final HttpRequest httpRequest, final InterfaceHttpData interfaceHttpData);
    
    void cleanRequestHttpData(final HttpRequest httpRequest);
    
    void cleanAllHttpData();
    
    @Deprecated
    void cleanRequestHttpDatas(final HttpRequest httpRequest);
    
    @Deprecated
    void cleanAllHttpDatas();
}

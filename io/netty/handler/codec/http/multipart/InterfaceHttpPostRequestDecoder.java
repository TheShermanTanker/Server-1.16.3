package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.http.HttpContent;
import java.util.List;

public interface InterfaceHttpPostRequestDecoder {
    boolean isMultipart();
    
    void setDiscardThreshold(final int integer);
    
    int getDiscardThreshold();
    
    List<InterfaceHttpData> getBodyHttpDatas();
    
    List<InterfaceHttpData> getBodyHttpDatas(final String string);
    
    InterfaceHttpData getBodyHttpData(final String string);
    
    InterfaceHttpPostRequestDecoder offer(final HttpContent httpContent);
    
    boolean hasNext();
    
    InterfaceHttpData next();
    
    InterfaceHttpData currentPartialHttpData();
    
    void destroy();
    
    void cleanFiles();
    
    void removeHttpDataFromClean(final InterfaceHttpData interfaceHttpData);
}

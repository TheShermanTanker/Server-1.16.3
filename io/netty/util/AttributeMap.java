package io.netty.util;

public interface AttributeMap {
     <T> Attribute<T> attr(final AttributeKey<T> attributeKey);
    
     <T> boolean hasAttr(final AttributeKey<T> attributeKey);
}

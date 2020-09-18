package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.message.Message;
import java.util.Objects;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.MessageFactory2;

public class MessageFactory2Adapter implements MessageFactory2 {
    private final MessageFactory wrapped;
    
    public MessageFactory2Adapter(final MessageFactory wrapped) {
        this.wrapped = (MessageFactory)Objects.requireNonNull(wrapped);
    }
    
    public MessageFactory getOriginal() {
        return this.wrapped;
    }
    
    public Message newMessage(final CharSequence charSequence) {
        return new SimpleMessage(charSequence);
    }
    
    public Message newMessage(final String message, final Object p0) {
        return this.wrapped.newMessage(message, p0);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1) {
        return this.wrapped.newMessage(message, p0, p1);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2) {
        return this.wrapped.newMessage(message, p0, p1, p2);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.wrapped.newMessage(message, p0, p1, p2, p3);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.wrapped.newMessage(message, p0, p1, p2, p3, p4);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.wrapped.newMessage(message, p0, p1, p2, p3, p4, p5);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.wrapped.newMessage(message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.wrapped.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.wrapped.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.wrapped.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    public Message newMessage(final Object message) {
        return this.wrapped.newMessage(message);
    }
    
    public Message newMessage(final String message) {
        return this.wrapped.newMessage(message);
    }
    
    public Message newMessage(final String message, final Object... params) {
        return this.wrapped.newMessage(message, params);
    }
}

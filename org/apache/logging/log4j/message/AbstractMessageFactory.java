package org.apache.logging.log4j.message;

import java.io.Serializable;

public abstract class AbstractMessageFactory implements MessageFactory2, Serializable {
    private static final long serialVersionUID = -1307891137684031187L;
    
    public Message newMessage(final CharSequence message) {
        return new SimpleMessage(message);
    }
    
    public Message newMessage(final Object message) {
        return new ObjectMessage(message);
    }
    
    public Message newMessage(final String message) {
        return new SimpleMessage(message);
    }
    
    public Message newMessage(final String message, final Object p0) {
        return this.newMessage(message, p0);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1) {
        return this.newMessage(message, p0, p1);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2) {
        return this.newMessage(message, p0, p1, p2);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.newMessage(message, p0, p1, p2, p3);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.newMessage(message, p0, p1, p2, p3, p4);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.newMessage(message, p0, p1, p2, p3, p4, p5);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.newMessage(message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
}

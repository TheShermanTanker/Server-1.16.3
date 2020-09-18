package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.PerformanceSensitive;
import java.io.Serializable;

@PerformanceSensitive({ "allocation" })
public final class ReusableMessageFactory implements MessageFactory2, Serializable {
    public static final ReusableMessageFactory INSTANCE;
    private static final long serialVersionUID = -8970940216592525651L;
    private static ThreadLocal<ReusableParameterizedMessage> threadLocalParameterized;
    private static ThreadLocal<ReusableSimpleMessage> threadLocalSimpleMessage;
    private static ThreadLocal<ReusableObjectMessage> threadLocalObjectMessage;
    
    private static ReusableParameterizedMessage getParameterized() {
        ReusableParameterizedMessage result = (ReusableParameterizedMessage)ReusableMessageFactory.threadLocalParameterized.get();
        if (result == null) {
            result = new ReusableParameterizedMessage();
            ReusableMessageFactory.threadLocalParameterized.set(result);
        }
        return result.reserved ? new ReusableParameterizedMessage().reserve() : result.reserve();
    }
    
    private static ReusableSimpleMessage getSimple() {
        ReusableSimpleMessage result = (ReusableSimpleMessage)ReusableMessageFactory.threadLocalSimpleMessage.get();
        if (result == null) {
            result = new ReusableSimpleMessage();
            ReusableMessageFactory.threadLocalSimpleMessage.set(result);
        }
        return result;
    }
    
    private static ReusableObjectMessage getObject() {
        ReusableObjectMessage result = (ReusableObjectMessage)ReusableMessageFactory.threadLocalObjectMessage.get();
        if (result == null) {
            result = new ReusableObjectMessage();
            ReusableMessageFactory.threadLocalObjectMessage.set(result);
        }
        return result;
    }
    
    public static void release(final Message message) {
        if (message instanceof ReusableParameterizedMessage) {
            ((ReusableParameterizedMessage)message).reserved = false;
        }
    }
    
    public Message newMessage(final CharSequence charSequence) {
        final ReusableSimpleMessage result = getSimple();
        result.set(charSequence);
        return result;
    }
    
    public Message newMessage(final String message, final Object... params) {
        return getParameterized().set(message, params);
    }
    
    public Message newMessage(final String message, final Object p0) {
        return getParameterized().set(message, p0);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1) {
        return getParameterized().set(message, p0, p1);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2) {
        return getParameterized().set(message, p0, p1, p2);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return getParameterized().set(message, p0, p1, p2, p3);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return getParameterized().set(message, p0, p1, p2, p3, p4);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return getParameterized().set(message, p0, p1, p2, p3, p4, p5);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    public Message newMessage(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    public Message newMessage(final String message) {
        final ReusableSimpleMessage result = getSimple();
        result.set(message);
        return result;
    }
    
    public Message newMessage(final Object message) {
        final ReusableObjectMessage result = getObject();
        result.set(message);
        return result;
    }
    
    static {
        INSTANCE = new ReusableMessageFactory();
        ReusableMessageFactory.threadLocalParameterized = (ThreadLocal<ReusableParameterizedMessage>)new ThreadLocal();
        ReusableMessageFactory.threadLocalSimpleMessage = (ThreadLocal<ReusableSimpleMessage>)new ThreadLocal();
        ReusableMessageFactory.threadLocalObjectMessage = (ThreadLocal<ReusableObjectMessage>)new ThreadLocal();
    }
}

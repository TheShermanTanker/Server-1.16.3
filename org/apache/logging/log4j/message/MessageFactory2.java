package org.apache.logging.log4j.message;

public interface MessageFactory2 extends MessageFactory {
    Message newMessage(final CharSequence charSequence);
    
    Message newMessage(final String string, final Object object);
    
    Message newMessage(final String string, final Object object2, final Object object3);
    
    Message newMessage(final String string, final Object object2, final Object object3, final Object object4);
    
    Message newMessage(final String string, final Object object2, final Object object3, final Object object4, final Object object5);
    
    Message newMessage(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6);
    
    Message newMessage(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    Message newMessage(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    Message newMessage(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    Message newMessage(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    Message newMessage(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
}

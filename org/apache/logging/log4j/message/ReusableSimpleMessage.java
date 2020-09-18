package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public class ReusableSimpleMessage implements ReusableMessage, CharSequence {
    private static final long serialVersionUID = -9199974506498249809L;
    private static Object[] EMPTY_PARAMS;
    private CharSequence charSequence;
    
    public void set(final String message) {
        this.charSequence = (CharSequence)message;
    }
    
    public void set(final CharSequence charSequence) {
        this.charSequence = charSequence;
    }
    
    public String getFormattedMessage() {
        return String.valueOf(this.charSequence);
    }
    
    public String getFormat() {
        return this.getFormattedMessage();
    }
    
    public Object[] getParameters() {
        return ReusableSimpleMessage.EMPTY_PARAMS;
    }
    
    public Throwable getThrowable() {
        return null;
    }
    
    public void formatTo(final StringBuilder buffer) {
        buffer.append(this.charSequence);
    }
    
    public Object[] swapParameters(final Object[] emptyReplacement) {
        return emptyReplacement;
    }
    
    public short getParameterCount() {
        return 0;
    }
    
    public Message memento() {
        return new SimpleMessage(this.charSequence);
    }
    
    public int length() {
        return (this.charSequence == null) ? 0 : this.charSequence.length();
    }
    
    public char charAt(final int index) {
        return this.charSequence.charAt(index);
    }
    
    public CharSequence subSequence(final int start, final int end) {
        return this.charSequence.subSequence(start, end);
    }
    
    static {
        ReusableSimpleMessage.EMPTY_PARAMS = new Object[0];
    }
}

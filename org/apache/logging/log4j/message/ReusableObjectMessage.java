package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public class ReusableObjectMessage implements ReusableMessage {
    private static final long serialVersionUID = 6922476812535519960L;
    private transient Object obj;
    private transient String objectString;
    
    public void set(final Object object) {
        this.obj = object;
    }
    
    public String getFormattedMessage() {
        return String.valueOf(this.obj);
    }
    
    public void formatTo(final StringBuilder buffer) {
        if (this.obj == null || this.obj instanceof String) {
            buffer.append((String)this.obj);
        }
        else if (this.obj instanceof StringBuilderFormattable) {
            ((StringBuilderFormattable)this.obj).formatTo(buffer);
        }
        else if (this.obj instanceof CharSequence) {
            buffer.append((CharSequence)this.obj);
        }
        else if (this.obj instanceof Integer) {
            buffer.append((int)this.obj);
        }
        else if (this.obj instanceof Long) {
            buffer.append((long)this.obj);
        }
        else if (this.obj instanceof Double) {
            buffer.append((double)this.obj);
        }
        else if (this.obj instanceof Boolean) {
            buffer.append((boolean)this.obj);
        }
        else if (this.obj instanceof Character) {
            buffer.append((char)this.obj);
        }
        else if (this.obj instanceof Short) {
            buffer.append((int)(short)this.obj);
        }
        else if (this.obj instanceof Float) {
            buffer.append((float)this.obj);
        }
        else {
            buffer.append(this.obj);
        }
    }
    
    public String getFormat() {
        return this.getFormattedMessage();
    }
    
    public Object getParameter() {
        return this.obj;
    }
    
    public Object[] getParameters() {
        return new Object[] { this.obj };
    }
    
    public String toString() {
        return this.getFormattedMessage();
    }
    
    public Throwable getThrowable() {
        return (this.obj instanceof Throwable) ? ((Throwable)this.obj) : null;
    }
    
    public Object[] swapParameters(final Object[] emptyReplacement) {
        return emptyReplacement;
    }
    
    public short getParameterCount() {
        return 0;
    }
    
    public Message memento() {
        return new ObjectMessage(this.obj);
    }
}

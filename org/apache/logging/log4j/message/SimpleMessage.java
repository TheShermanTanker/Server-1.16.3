package org.apache.logging.log4j.message;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class SimpleMessage implements Message, StringBuilderFormattable, CharSequence {
    private static final long serialVersionUID = -8398002534962715992L;
    private String message;
    private transient CharSequence charSequence;
    
    public SimpleMessage() {
        this((String)null);
    }
    
    public SimpleMessage(final String message) {
        this.message = message;
        this.charSequence = (CharSequence)message;
    }
    
    public SimpleMessage(final CharSequence charSequence) {
        this.charSequence = charSequence;
    }
    
    public String getFormattedMessage() {
        if (this.message == null) {
            this.message = String.valueOf(this.charSequence);
        }
        return this.message;
    }
    
    public void formatTo(final StringBuilder buffer) {
        if (this.message != null) {
            buffer.append(this.message);
        }
        else {
            buffer.append(this.charSequence);
        }
    }
    
    public String getFormat() {
        return this.getFormattedMessage();
    }
    
    public Object[] getParameters() {
        return null;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SimpleMessage that = (SimpleMessage)o;
        if (this.charSequence != null) {
            if (!this.charSequence.equals(that.charSequence)) {
                return false;
            }
        }
        else if (that.charSequence != null) {
            return false;
        }
        return true;
        b = false;
        return b;
    }
    
    public int hashCode() {
        return (this.charSequence != null) ? this.charSequence.hashCode() : 0;
    }
    
    public String toString() {
        return this.getFormattedMessage();
    }
    
    public Throwable getThrowable() {
        return null;
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
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        this.getFormattedMessage();
        out.defaultWriteObject();
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.charSequence = (CharSequence)this.message;
    }
}

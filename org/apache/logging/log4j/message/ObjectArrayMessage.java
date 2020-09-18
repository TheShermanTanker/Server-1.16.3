package org.apache.logging.log4j.message;

import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

public final class ObjectArrayMessage implements Message {
    private static final Object[] EMPTY_OBJECT_ARRAY;
    private static final long serialVersionUID = -5903272448334166185L;
    private transient Object[] array;
    private transient String arrayString;
    
    public ObjectArrayMessage(final Object... obj) {
        this.array = ((obj == null) ? ObjectArrayMessage.EMPTY_OBJECT_ARRAY : obj);
    }
    
    private boolean equalObjectsOrStrings(final Object[] left, final Object[] right) {
        return Arrays.equals(left, right) || Arrays.toString(left).equals(Arrays.toString(right));
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ObjectArrayMessage that = (ObjectArrayMessage)o;
        return (this.array == null) ? (that.array == null) : this.equalObjectsOrStrings(this.array, that.array);
    }
    
    public String getFormat() {
        return this.getFormattedMessage();
    }
    
    public String getFormattedMessage() {
        if (this.arrayString == null) {
            this.arrayString = Arrays.toString(this.array);
        }
        return this.arrayString;
    }
    
    public Object[] getParameters() {
        return this.array;
    }
    
    public Throwable getThrowable() {
        return null;
    }
    
    public int hashCode() {
        return Arrays.hashCode(this.array);
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.array = (Object[])in.readObject();
    }
    
    public String toString() {
        return this.getFormattedMessage();
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.array);
    }
    
    static {
        EMPTY_OBJECT_ARRAY = new Object[0];
    }
}

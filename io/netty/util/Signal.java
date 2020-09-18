package io.netty.util;

public final class Signal extends Error implements Constant<Signal> {
    private static final long serialVersionUID = -221145131122459977L;
    private static final ConstantPool<Signal> pool;
    private final SignalConstant constant;
    
    public static Signal valueOf(final String name) {
        return Signal.pool.valueOf(name);
    }
    
    public static Signal valueOf(final Class<?> firstNameComponent, final String secondNameComponent) {
        return Signal.pool.valueOf(firstNameComponent, secondNameComponent);
    }
    
    private Signal(final int id, final String name) {
        this.constant = new SignalConstant(id, name);
    }
    
    public void expect(final Signal signal) {
        if (this != signal) {
            throw new IllegalStateException(new StringBuilder().append("unexpected signal: ").append(signal).toString());
        }
    }
    
    public Throwable initCause(final Throwable cause) {
        return (Throwable)this;
    }
    
    public Throwable fillInStackTrace() {
        return (Throwable)this;
    }
    
    public int id() {
        return this.constant.id();
    }
    
    public String name() {
        return this.constant.name();
    }
    
    public boolean equals(final Object obj) {
        return this == obj;
    }
    
    public int hashCode() {
        return System.identityHashCode(this);
    }
    
    public int compareTo(final Signal other) {
        if (this == other) {
            return 0;
        }
        return this.constant.compareTo(other.constant);
    }
    
    public String toString() {
        return this.name();
    }
    
    static {
        pool = new ConstantPool<Signal>() {
            @Override
            protected Signal newConstant(final int id, final String name) {
                return new Signal(id, name, null);
            }
        };
    }
    
    private static final class SignalConstant extends AbstractConstant<SignalConstant> {
        SignalConstant(final int id, final String name) {
            super(id, name);
        }
    }
}

package com.mojang.serialization;

public class Lifecycle {
    private static final Lifecycle STABLE;
    private static final Lifecycle EXPERIMENTAL;
    
    private Lifecycle() {
    }
    
    public static Lifecycle experimental() {
        return Lifecycle.EXPERIMENTAL;
    }
    
    public static Lifecycle stable() {
        return Lifecycle.STABLE;
    }
    
    public static Lifecycle deprecated(final int since) {
        return new Deprecated(since);
    }
    
    public Lifecycle add(final Lifecycle other) {
        if (this == Lifecycle.EXPERIMENTAL || other == Lifecycle.EXPERIMENTAL) {
            return Lifecycle.EXPERIMENTAL;
        }
        if (this instanceof Deprecated) {
            if (other instanceof Deprecated && ((Deprecated)other).since < ((Deprecated)this).since) {
                return other;
            }
            return this;
        }
        else {
            if (other instanceof Deprecated) {
                return other;
            }
            return Lifecycle.STABLE;
        }
    }
    
    static {
        STABLE = new Lifecycle() {
            public String toString() {
                return "Stable";
            }
        };
        EXPERIMENTAL = new Lifecycle() {
            public String toString() {
                return "Experimental";
            }
        };
    }
    
    public static final class Deprecated extends Lifecycle {
        private final int since;
        
        public Deprecated(final int since) {
            super(null);
            this.since = since;
        }
        
        public int since() {
            return this.since;
        }
    }
}

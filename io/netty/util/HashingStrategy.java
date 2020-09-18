package io.netty.util;

public interface HashingStrategy<T> {
    public static final HashingStrategy JAVA_HASHER = new HashingStrategy() {
        public int hashCode(final Object obj) {
            return (obj != null) ? obj.hashCode() : 0;
        }
        
        public boolean equals(final Object a, final Object b) {
            return a == b || (a != null && a.equals(b));
        }
    };
    
    int hashCode(final T object);
    
    boolean equals(final T object1, final T object2);
}

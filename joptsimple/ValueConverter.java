package joptsimple;

public interface ValueConverter<V> {
    V convert(final String string);
    
    Class<? extends V> valueType();
    
    String valuePattern();
}

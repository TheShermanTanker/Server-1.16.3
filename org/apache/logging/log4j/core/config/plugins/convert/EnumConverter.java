package org.apache.logging.log4j.core.config.plugins.convert;

import org.apache.logging.log4j.util.EnglishEnums;

public class EnumConverter<E extends Enum<E>> implements TypeConverter<E> {
    private final Class<E> clazz;
    
    public EnumConverter(final Class<E> clazz) {
        this.clazz = clazz;
    }
    
    public E convert(final String s) {
        return EnglishEnums.<E>valueOf(this.clazz, s);
    }
}

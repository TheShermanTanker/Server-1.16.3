package org.apache.logging.log4j.core.config.plugins.visitors;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import org.apache.logging.log4j.util.Strings;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.lang.reflect.Member;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.Logger;
import java.lang.annotation.Annotation;

public abstract class AbstractPluginVisitor<A extends Annotation> implements PluginVisitor<A> {
    protected static final Logger LOGGER;
    protected final Class<A> clazz;
    protected A annotation;
    protected String[] aliases;
    protected Class<?> conversionType;
    protected StrSubstitutor substitutor;
    protected Member member;
    
    protected AbstractPluginVisitor(final Class<A> clazz) {
        this.clazz = clazz;
    }
    
    public PluginVisitor<A> setAnnotation(final Annotation anAnnotation) {
        final Annotation a = (Annotation)Objects.requireNonNull(anAnnotation, "No annotation was provided");
        if (this.clazz.isInstance(a)) {
            this.annotation = (A)a;
        }
        return this;
    }
    
    public PluginVisitor<A> setAliases(final String... someAliases) {
        this.aliases = someAliases;
        return this;
    }
    
    public PluginVisitor<A> setConversionType(final Class<?> aConversionType) {
        this.conversionType = Objects.requireNonNull(aConversionType, "No conversion type class was provided");
        return this;
    }
    
    public PluginVisitor<A> setStrSubstitutor(final StrSubstitutor aSubstitutor) {
        this.substitutor = (StrSubstitutor)Objects.requireNonNull(aSubstitutor, "No StrSubstitutor was provided");
        return this;
    }
    
    public PluginVisitor<A> setMember(final Member aMember) {
        this.member = aMember;
        return this;
    }
    
    protected static String removeAttributeValue(final Map<String, String> attributes, final String name, final String... aliases) {
        for (final Map.Entry<String, String> entry : attributes.entrySet()) {
            final String key = (String)entry.getKey();
            final String value = (String)entry.getValue();
            if (key.equalsIgnoreCase(name)) {
                attributes.remove(key);
                return value;
            }
            if (aliases == null) {
                continue;
            }
            for (final String alias : aliases) {
                if (key.equalsIgnoreCase(alias)) {
                    attributes.remove(key);
                    return value;
                }
            }
        }
        return null;
    }
    
    protected Object convert(final String value, final Object defaultValue) {
        if (defaultValue instanceof String) {
            return TypeConverters.convert(value, this.conversionType, Strings.trimToNull((String)defaultValue));
        }
        return TypeConverters.convert(value, this.conversionType, defaultValue);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}

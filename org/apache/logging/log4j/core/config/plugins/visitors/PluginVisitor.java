package org.apache.logging.log4j.core.config.plugins.visitors;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.Configuration;
import java.lang.reflect.Member;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import java.lang.annotation.Annotation;

public interface PluginVisitor<A extends Annotation> {
    PluginVisitor<A> setAnnotation(final Annotation annotation);
    
    PluginVisitor<A> setAliases(final String... arr);
    
    PluginVisitor<A> setConversionType(final Class<?> class1);
    
    PluginVisitor<A> setStrSubstitutor(final StrSubstitutor strSubstitutor);
    
    PluginVisitor<A> setMember(final Member member);
    
    Object visit(final Configuration configuration, final Node node, final LogEvent logEvent, final StringBuilder stringBuilder);
}

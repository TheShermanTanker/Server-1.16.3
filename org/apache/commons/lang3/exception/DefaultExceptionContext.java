package org.apache.commons.lang3.exception;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.ArrayList;
import org.apache.commons.lang3.tuple.Pair;
import java.util.List;
import java.io.Serializable;

public class DefaultExceptionContext implements ExceptionContext, Serializable {
    private static final long serialVersionUID = 20110706L;
    private final List<Pair<String, Object>> contextValues;
    
    public DefaultExceptionContext() {
        this.contextValues = (List<Pair<String, Object>>)new ArrayList();
    }
    
    public DefaultExceptionContext addContextValue(final String label, final Object value) {
        this.contextValues.add(new ImmutablePair(label, value));
        return this;
    }
    
    public DefaultExceptionContext setContextValue(final String label, final Object value) {
        final Iterator<Pair<String, Object>> iter = (Iterator<Pair<String, Object>>)this.contextValues.iterator();
        while (iter.hasNext()) {
            final Pair<String, Object> p = (Pair<String, Object>)iter.next();
            if (StringUtils.equals((CharSequence)label, (CharSequence)p.getKey())) {
                iter.remove();
            }
        }
        this.addContextValue(label, value);
        return this;
    }
    
    public List<Object> getContextValues(final String label) {
        final List<Object> values = (List<Object>)new ArrayList();
        for (final Pair<String, Object> pair : this.contextValues) {
            if (StringUtils.equals((CharSequence)label, (CharSequence)pair.getKey())) {
                values.add(pair.getValue());
            }
        }
        return values;
    }
    
    public Object getFirstContextValue(final String label) {
        for (final Pair<String, Object> pair : this.contextValues) {
            if (StringUtils.equals((CharSequence)label, (CharSequence)pair.getKey())) {
                return pair.getValue();
            }
        }
        return null;
    }
    
    public Set<String> getContextLabels() {
        final Set<String> labels = (Set<String>)new HashSet();
        for (final Pair<String, Object> pair : this.contextValues) {
            labels.add(pair.getKey());
        }
        return labels;
    }
    
    public List<Pair<String, Object>> getContextEntries() {
        return this.contextValues;
    }
    
    public String getFormattedExceptionMessage(final String baseMessage) {
        final StringBuilder buffer = new StringBuilder(256);
        if (baseMessage != null) {
            buffer.append(baseMessage);
        }
        if (this.contextValues.size() > 0) {
            if (buffer.length() > 0) {
                buffer.append('\n');
            }
            buffer.append("Exception Context:\n");
            int i = 0;
            for (final Pair<String, Object> pair : this.contextValues) {
                buffer.append("\t[");
                buffer.append(++i);
                buffer.append(':');
                buffer.append((String)pair.getKey());
                buffer.append("=");
                final Object value = pair.getValue();
                if (value == null) {
                    buffer.append("null");
                }
                else {
                    String valueStr;
                    try {
                        valueStr = value.toString();
                    }
                    catch (Exception e) {
                        valueStr = "Exception thrown on toString(): " + ExceptionUtils.getStackTrace((Throwable)e);
                    }
                    buffer.append(valueStr);
                }
                buffer.append("]\n");
            }
            buffer.append("---------------------------------");
        }
        return buffer.toString();
    }
}

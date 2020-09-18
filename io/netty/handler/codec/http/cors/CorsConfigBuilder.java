package io.netty.handler.codec.http.cors;

import java.util.Date;
import io.netty.handler.codec.http.HttpHeaderNames;
import java.util.Collections;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.Map;
import io.netty.handler.codec.http.HttpMethod;
import java.util.Set;

public final class CorsConfigBuilder {
    final Set<String> origins;
    final boolean anyOrigin;
    boolean allowNullOrigin;
    boolean enabled;
    boolean allowCredentials;
    final Set<String> exposeHeaders;
    long maxAge;
    final Set<HttpMethod> requestMethods;
    final Set<String> requestHeaders;
    final Map<CharSequence, Callable<?>> preflightHeaders;
    private boolean noPreflightHeaders;
    boolean shortCircuit;
    
    public static CorsConfigBuilder forAnyOrigin() {
        return new CorsConfigBuilder();
    }
    
    public static CorsConfigBuilder forOrigin(final String origin) {
        if ("*".equals(origin)) {
            return new CorsConfigBuilder();
        }
        return new CorsConfigBuilder(new String[] { origin });
    }
    
    public static CorsConfigBuilder forOrigins(final String... origins) {
        return new CorsConfigBuilder(origins);
    }
    
    CorsConfigBuilder(final String... origins) {
        this.enabled = true;
        this.exposeHeaders = (Set<String>)new HashSet();
        this.requestMethods = (Set<HttpMethod>)new HashSet();
        this.requestHeaders = (Set<String>)new HashSet();
        this.preflightHeaders = (Map<CharSequence, Callable<?>>)new HashMap();
        this.origins = (Set<String>)new LinkedHashSet((Collection)Arrays.asList((Object[])origins));
        this.anyOrigin = false;
    }
    
    CorsConfigBuilder() {
        this.enabled = true;
        this.exposeHeaders = (Set<String>)new HashSet();
        this.requestMethods = (Set<HttpMethod>)new HashSet();
        this.requestHeaders = (Set<String>)new HashSet();
        this.preflightHeaders = (Map<CharSequence, Callable<?>>)new HashMap();
        this.anyOrigin = true;
        this.origins = (Set<String>)Collections.emptySet();
    }
    
    public CorsConfigBuilder allowNullOrigin() {
        this.allowNullOrigin = true;
        return this;
    }
    
    public CorsConfigBuilder disable() {
        this.enabled = false;
        return this;
    }
    
    public CorsConfigBuilder exposeHeaders(final String... headers) {
        this.exposeHeaders.addAll((Collection)Arrays.asList((Object[])headers));
        return this;
    }
    
    public CorsConfigBuilder exposeHeaders(final CharSequence... headers) {
        for (final CharSequence header : headers) {
            this.exposeHeaders.add(header.toString());
        }
        return this;
    }
    
    public CorsConfigBuilder allowCredentials() {
        this.allowCredentials = true;
        return this;
    }
    
    public CorsConfigBuilder maxAge(final long max) {
        this.maxAge = max;
        return this;
    }
    
    public CorsConfigBuilder allowedRequestMethods(final HttpMethod... methods) {
        this.requestMethods.addAll((Collection)Arrays.asList((Object[])methods));
        return this;
    }
    
    public CorsConfigBuilder allowedRequestHeaders(final String... headers) {
        this.requestHeaders.addAll((Collection)Arrays.asList((Object[])headers));
        return this;
    }
    
    public CorsConfigBuilder allowedRequestHeaders(final CharSequence... headers) {
        for (final CharSequence header : headers) {
            this.requestHeaders.add(header.toString());
        }
        return this;
    }
    
    public CorsConfigBuilder preflightResponseHeader(final CharSequence name, final Object... values) {
        if (values.length == 1) {
            this.preflightHeaders.put(name, new ConstantValueGenerator(values[0]));
        }
        else {
            this.preflightResponseHeader(name, (java.lang.Iterable<Object>)Arrays.asList(values));
        }
        return this;
    }
    
    public <T> CorsConfigBuilder preflightResponseHeader(final CharSequence name, final Iterable<T> value) {
        this.preflightHeaders.put(name, new ConstantValueGenerator((Object)value));
        return this;
    }
    
    public <T> CorsConfigBuilder preflightResponseHeader(final CharSequence name, final Callable<T> valueGenerator) {
        this.preflightHeaders.put(name, valueGenerator);
        return this;
    }
    
    public CorsConfigBuilder noPreflightResponseHeaders() {
        this.noPreflightHeaders = true;
        return this;
    }
    
    public CorsConfigBuilder shortCircuit() {
        this.shortCircuit = true;
        return this;
    }
    
    public CorsConfig build() {
        if (this.preflightHeaders.isEmpty() && !this.noPreflightHeaders) {
            this.preflightHeaders.put(HttpHeaderNames.DATE, DateValueGenerator.INSTANCE);
            this.preflightHeaders.put(HttpHeaderNames.CONTENT_LENGTH, new ConstantValueGenerator("0"));
        }
        return new CorsConfig(this);
    }
    
    private static final class ConstantValueGenerator implements Callable<Object> {
        private final Object value;
        
        private ConstantValueGenerator(final Object value) {
            if (value == null) {
                throw new IllegalArgumentException("value must not be null");
            }
            this.value = value;
        }
        
        public Object call() {
            return this.value;
        }
    }
    
    private static final class DateValueGenerator implements Callable<Date> {
        static final DateValueGenerator INSTANCE;
        
        public Date call() throws Exception {
            return new Date();
        }
        
        static {
            INSTANCE = new DateValueGenerator();
        }
    }
}

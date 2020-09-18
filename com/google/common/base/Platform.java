package com.google.common.base;

import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import javax.annotation.Nullable;
import java.util.Locale;
import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform {
    private static final Logger logger;
    private static final PatternCompiler patternCompiler;
    
    private Platform() {
    }
    
    static long systemNanoTime() {
        return System.nanoTime();
    }
    
    static CharMatcher precomputeCharMatcher(final CharMatcher matcher) {
        return matcher.precomputedInternal();
    }
    
    static <T extends Enum<T>> Optional<T> getEnumIfPresent(final Class<T> enumClass, final String value) {
        final WeakReference<? extends Enum<?>> ref = Enums.<T>getEnumConstants(enumClass).get(value);
        return (ref == null) ? Optional.<T>absent() : Optional.<T>of(enumClass.cast(ref.get()));
    }
    
    static String formatCompact4Digits(final double value) {
        return String.format(Locale.ROOT, "%.4g", new Object[] { value });
    }
    
    static boolean stringIsNullOrEmpty(@Nullable final String string) {
        return string == null || string.isEmpty();
    }
    
    static CommonPattern compilePattern(final String pattern) {
        Preconditions.<String>checkNotNull(pattern);
        return Platform.patternCompiler.compile(pattern);
    }
    
    static boolean usingJdkPatternCompiler() {
        return Platform.patternCompiler instanceof JdkPatternCompiler;
    }
    
    private static PatternCompiler loadPatternCompiler() {
        final ServiceLoader<PatternCompiler> loader = (ServiceLoader<PatternCompiler>)ServiceLoader.load((Class)PatternCompiler.class);
        try {
            final Iterator<PatternCompiler> it = (Iterator<PatternCompiler>)loader.iterator();
            while (it.hasNext()) {
                try {
                    return (PatternCompiler)it.next();
                }
                catch (ServiceConfigurationError e) {
                    logPatternCompilerError(e);
                    continue;
                }
                break;
            }
        }
        catch (ServiceConfigurationError e2) {
            logPatternCompilerError(e2);
        }
        return new JdkPatternCompiler();
    }
    
    private static void logPatternCompilerError(final ServiceConfigurationError e) {
        Platform.logger.log(Level.WARNING, "Error loading regex compiler, falling back to next option", (Throwable)e);
    }
    
    static {
        logger = Logger.getLogger(Platform.class.getName());
        patternCompiler = loadPatternCompiler();
    }
    
    private static final class JdkPatternCompiler implements PatternCompiler {
        public CommonPattern compile(final String pattern) {
            return new JdkPattern(Pattern.compile(pattern));
        }
    }
}

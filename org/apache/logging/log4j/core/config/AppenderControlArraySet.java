package org.apache.logging.log4j.core.config;

import java.util.HashMap;
import org.apache.logging.log4j.core.Appender;
import java.util.Map;
import java.util.Objects;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive
public class AppenderControlArraySet {
    private final AtomicReference<AppenderControl[]> appenderArray;
    
    public AppenderControlArraySet() {
        this.appenderArray = (AtomicReference<AppenderControl[]>)new AtomicReference(new AppenderControl[0]);
    }
    
    public boolean add(final AppenderControl control) {
        boolean success;
        do {
            final AppenderControl[] arr$;
            final AppenderControl[] original = arr$ = (AppenderControl[])this.appenderArray.get();
            for (final AppenderControl existing : arr$) {
                if (existing.equals(control)) {
                    return false;
                }
            }
            final AppenderControl[] copy = (AppenderControl[])Arrays.copyOf((Object[])original, original.length + 1);
            copy[copy.length - 1] = control;
            success = this.appenderArray.compareAndSet(original, copy);
        } while (!success);
        return true;
    }
    
    public AppenderControl remove(final String name) {
        boolean success;
        do {
            success = true;
            final AppenderControl[] original = (AppenderControl[])this.appenderArray.get();
            int i = 0;
            while (i < original.length) {
                final AppenderControl appenderControl = original[i];
                if (Objects.equals(name, appenderControl.getAppenderName())) {
                    final AppenderControl[] copy = this.removeElementAt(i, original);
                    if (this.appenderArray.compareAndSet(original, copy)) {
                        return appenderControl;
                    }
                    success = false;
                    break;
                }
                else {
                    ++i;
                }
            }
        } while (!success);
        return null;
    }
    
    private AppenderControl[] removeElementAt(final int i, final AppenderControl[] array) {
        final AppenderControl[] result = (AppenderControl[])Arrays.copyOf((Object[])array, array.length - 1);
        System.arraycopy(array, i + 1, result, i, result.length - i);
        return result;
    }
    
    public Map<String, Appender> asMap() {
        final Map<String, Appender> result = (Map<String, Appender>)new HashMap();
        for (final AppenderControl appenderControl : (AppenderControl[])this.appenderArray.get()) {
            result.put(appenderControl.getAppenderName(), appenderControl.getAppender());
        }
        return result;
    }
    
    public AppenderControl[] clear() {
        return (AppenderControl[])this.appenderArray.getAndSet(new AppenderControl[0]);
    }
    
    public boolean isEmpty() {
        return ((AppenderControl[])this.appenderArray.get()).length == 0;
    }
    
    public AppenderControl[] get() {
        return (AppenderControl[])this.appenderArray.get();
    }
}

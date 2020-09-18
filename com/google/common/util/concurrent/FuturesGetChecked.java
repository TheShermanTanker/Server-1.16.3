package com.google.common.util.concurrent;

import java.util.concurrent.CopyOnWriteArraySet;
import java.lang.ref.WeakReference;
import java.util.Set;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.ExecutionException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Future;
import java.lang.reflect.Constructor;
import com.google.common.collect.Ordering;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
final class FuturesGetChecked {
    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST;
    
    @CanIgnoreReturnValue
    static <V, X extends Exception> V getChecked(final Future<V> future, final Class<X> exceptionClass) throws X, Exception {
        return FuturesGetChecked.<V, X>getChecked(bestGetCheckedTypeValidator(), future, exceptionClass);
    }
    
    @CanIgnoreReturnValue
    @VisibleForTesting
    static <V, X extends Exception> V getChecked(final GetCheckedTypeValidator validator, final Future<V> future, final Class<X> exceptionClass) throws X, Exception {
        validator.validateClass(exceptionClass);
        try {
            return (V)future.get();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw FuturesGetChecked.<X>newWithCause(exceptionClass, (Throwable)e);
        }
        catch (ExecutionException e2) {
            FuturesGetChecked.<X>wrapAndThrowExceptionOrError(e2.getCause(), exceptionClass);
            throw new AssertionError();
        }
    }
    
    @CanIgnoreReturnValue
    static <V, X extends Exception> V getChecked(final Future<V> future, final Class<X> exceptionClass, final long timeout, final TimeUnit unit) throws X, Exception {
        bestGetCheckedTypeValidator().validateClass(exceptionClass);
        try {
            return (V)future.get(timeout, unit);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw FuturesGetChecked.<X>newWithCause(exceptionClass, (Throwable)e);
        }
        catch (TimeoutException e2) {
            throw FuturesGetChecked.<X>newWithCause(exceptionClass, (Throwable)e2);
        }
        catch (ExecutionException e3) {
            FuturesGetChecked.<X>wrapAndThrowExceptionOrError(e3.getCause(), exceptionClass);
            throw new AssertionError();
        }
    }
    
    private static GetCheckedTypeValidator bestGetCheckedTypeValidator() {
        return GetCheckedTypeValidatorHolder.BEST_VALIDATOR;
    }
    
    @VisibleForTesting
    static GetCheckedTypeValidator weakSetValidator() {
        return GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
    }
    
    @VisibleForTesting
    static GetCheckedTypeValidator classValueValidator() {
        return GetCheckedTypeValidatorHolder.ClassValueValidator.INSTANCE;
    }
    
    private static <X extends Exception> void wrapAndThrowExceptionOrError(final Throwable cause, final Class<X> exceptionClass) throws X, Exception {
        if (cause instanceof Error) {
            throw new ExecutionError((Error)cause);
        }
        if (cause instanceof RuntimeException) {
            throw new UncheckedExecutionException(cause);
        }
        throw FuturesGetChecked.<X>newWithCause(exceptionClass, cause);
    }
    
    private static boolean hasConstructorUsableByGetChecked(final Class<? extends Exception> exceptionClass) {
        try {
            final Exception unused = FuturesGetChecked.<Exception>newWithCause(exceptionClass, (Throwable)new Exception());
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    private static <X extends Exception> X newWithCause(final Class<X> exceptionClass, final Throwable cause) {
        final List<Constructor<X>> constructors = (List<Constructor<X>>)Arrays.asList((Object[])exceptionClass.getConstructors());
        for (final Constructor<X> constructor : FuturesGetChecked.<X>preferringStrings(constructors)) {
            final X instance = FuturesGetChecked.<X>newFromConstructor(constructor, cause);
            if (instance != null) {
                if (instance.getCause() == null) {
                    instance.initCause(cause);
                }
                return instance;
            }
        }
        throw new IllegalArgumentException(new StringBuilder().append("No appropriate constructor for exception of type ").append(exceptionClass).append(" in response to chained exception").toString(), cause);
    }
    
    private static <X extends Exception> List<Constructor<X>> preferringStrings(final List<Constructor<X>> constructors) {
        return FuturesGetChecked.WITH_STRING_PARAM_FIRST.<Constructor<X>>sortedCopy((java.lang.Iterable<Constructor<X>>)constructors);
    }
    
    @Nullable
    private static <X> X newFromConstructor(final Constructor<X> constructor, final Throwable cause) {
        final Class<?>[] paramTypes = constructor.getParameterTypes();
        final Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; ++i) {
            final Class<?> paramType = paramTypes[i];
            if (paramType.equals(String.class)) {
                params[i] = cause.toString();
            }
            else {
                if (!paramType.equals(Throwable.class)) {
                    return null;
                }
                params[i] = cause;
            }
        }
        try {
            return (X)constructor.newInstance(params);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
        catch (InstantiationException e2) {
            return null;
        }
        catch (IllegalAccessException e3) {
            return null;
        }
        catch (InvocationTargetException e4) {
            return null;
        }
    }
    
    @VisibleForTesting
    static boolean isCheckedException(final Class<? extends Exception> type) {
        return !RuntimeException.class.isAssignableFrom((Class)type);
    }
    
    @VisibleForTesting
    static void checkExceptionClassValidity(final Class<? extends Exception> exceptionClass) {
        Preconditions.checkArgument(isCheckedException(exceptionClass), "Futures.getChecked exception type (%s) must not be a RuntimeException", exceptionClass);
        Preconditions.checkArgument(hasConstructorUsableByGetChecked(exceptionClass), "Futures.getChecked exception type (%s) must be an accessible class with an accessible constructor whose parameters (if any) must be of type String and/or Throwable", exceptionClass);
    }
    
    private FuturesGetChecked() {
    }
    
    static {
        WITH_STRING_PARAM_FIRST = Ordering.<Comparable>natural().onResultOf(new Function<Constructor<?>, Boolean>() {
            public Boolean apply(final Constructor<?> input) {
                return Arrays.asList((Object[])input.getParameterTypes()).contains(String.class);
            }
        }).<Constructor<?>>reverse();
    }
    
    @VisibleForTesting
    static class GetCheckedTypeValidatorHolder {
        static final String CLASS_VALUE_VALIDATOR_NAME;
        static final GetCheckedTypeValidator BEST_VALIDATOR;
        
        static GetCheckedTypeValidator getBestValidator() {
            try {
                final Class<?> theClass = Class.forName(GetCheckedTypeValidatorHolder.CLASS_VALUE_VALIDATOR_NAME);
                return (GetCheckedTypeValidator)theClass.getEnumConstants()[0];
            }
            catch (Throwable t) {
                return FuturesGetChecked.weakSetValidator();
            }
        }
        
        static {
            CLASS_VALUE_VALIDATOR_NAME = GetCheckedTypeValidatorHolder.class.getName() + "$ClassValueValidator";
            BEST_VALIDATOR = getBestValidator();
        }
        
        @IgnoreJRERequirement
        enum ClassValueValidator implements GetCheckedTypeValidator {
            INSTANCE;
            
            private static final ClassValue<Boolean> isValidClass;
            
            public void validateClass(final Class<? extends Exception> exceptionClass) {
                ClassValueValidator.isValidClass.get((Class)exceptionClass);
            }
            
            static {
                isValidClass = new ClassValue<Boolean>() {
                    protected Boolean computeValue(final Class<?> type) {
                        FuturesGetChecked.checkExceptionClassValidity(type.asSubclass((Class)Exception.class));
                        return true;
                    }
                };
            }
        }
        
        enum WeakSetValidator implements GetCheckedTypeValidator {
            INSTANCE;
            
            private static final Set<WeakReference<Class<? extends Exception>>> validClasses;
            
            public void validateClass(final Class<? extends Exception> exceptionClass) {
                for (final WeakReference<Class<? extends Exception>> knownGood : WeakSetValidator.validClasses) {
                    if (exceptionClass.equals(knownGood.get())) {
                        return;
                    }
                }
                FuturesGetChecked.checkExceptionClassValidity(exceptionClass);
                if (WeakSetValidator.validClasses.size() > 1000) {
                    WeakSetValidator.validClasses.clear();
                }
                WeakSetValidator.validClasses.add(new WeakReference((Object)exceptionClass));
            }
            
            static {
                validClasses = (Set)new CopyOnWriteArraySet();
            }
        }
    }
    
    @VisibleForTesting
    interface GetCheckedTypeValidator {
        void validateClass(final Class<? extends Exception> class1);
    }
}

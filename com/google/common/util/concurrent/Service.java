package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface Service {
    @CanIgnoreReturnValue
    Service startAsync();
    
    boolean isRunning();
    
    State state();
    
    @CanIgnoreReturnValue
    Service stopAsync();
    
    void awaitRunning();
    
    void awaitRunning(final long long1, final TimeUnit timeUnit) throws TimeoutException;
    
    void awaitTerminated();
    
    void awaitTerminated(final long long1, final TimeUnit timeUnit) throws TimeoutException;
    
    Throwable failureCause();
    
    void addListener(final Listener listener, final Executor executor);
    
    @Beta
    public enum State {
        NEW {
            @Override
            boolean isTerminal() {
                return false;
            }
        }, 
        STARTING {
            @Override
            boolean isTerminal() {
                return false;
            }
        }, 
        RUNNING {
            @Override
            boolean isTerminal() {
                return false;
            }
        }, 
        STOPPING {
            @Override
            boolean isTerminal() {
                return false;
            }
        }, 
        TERMINATED {
            @Override
            boolean isTerminal() {
                return true;
            }
        }, 
        FAILED {
            @Override
            boolean isTerminal() {
                return true;
            }
        };
        
        abstract boolean isTerminal();
    }
    
    @Beta
    public abstract static class Listener {
        public void starting() {
        }
        
        public void running() {
        }
        
        public void stopping(final State from) {
        }
        
        public void terminated(final State from) {
        }
        
        public void failed(final State from, final Throwable failure) {
        }
    }
}

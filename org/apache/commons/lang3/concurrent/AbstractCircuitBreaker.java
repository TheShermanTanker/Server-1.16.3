package org.apache.commons.lang3.concurrent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractCircuitBreaker<T> implements CircuitBreaker<T> {
    public static final String PROPERTY_NAME = "open";
    protected final AtomicReference<State> state;
    private final PropertyChangeSupport changeSupport;
    
    public AbstractCircuitBreaker() {
        this.state = (AtomicReference<State>)new AtomicReference(State.CLOSED);
        this.changeSupport = new PropertyChangeSupport(this);
    }
    
    public boolean isOpen() {
        return isOpen((State)this.state.get());
    }
    
    public boolean isClosed() {
        return !this.isOpen();
    }
    
    public abstract boolean checkState();
    
    public abstract boolean incrementAndCheckState(final T object);
    
    public void close() {
        this.changeState(State.CLOSED);
    }
    
    public void open() {
        this.changeState(State.OPEN);
    }
    
    protected static boolean isOpen(final State state) {
        return state == State.OPEN;
    }
    
    protected void changeState(final State newState) {
        if (this.state.compareAndSet(newState.oppositeState(), newState)) {
            this.changeSupport.firePropertyChange("open", !isOpen(newState), isOpen(newState));
        }
    }
    
    public void addChangeListener(final PropertyChangeListener listener) {
        this.changeSupport.addPropertyChangeListener(listener);
    }
    
    public void removeChangeListener(final PropertyChangeListener listener) {
        this.changeSupport.removePropertyChangeListener(listener);
    }
    
    protected enum State {
        CLOSED {
            @Override
            public State oppositeState() {
                return AbstractCircuitBreaker$State$1.OPEN;
            }
        }, 
        OPEN {
            @Override
            public State oppositeState() {
                return AbstractCircuitBreaker$State$2.CLOSED;
            }
        };
        
        public abstract State oppositeState();
    }
}

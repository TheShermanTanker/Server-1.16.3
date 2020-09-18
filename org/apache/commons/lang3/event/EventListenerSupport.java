package org.apache.commons.lang3.event;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Array;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.Validate;
import java.util.List;
import java.io.Serializable;

public class EventListenerSupport<L> implements Serializable {
    private static final long serialVersionUID = 3593265990380473632L;
    private List<L> listeners;
    private transient L proxy;
    private transient L[] prototypeArray;
    
    public static <T> EventListenerSupport<T> create(final Class<T> listenerInterface) {
        return new EventListenerSupport<T>(listenerInterface);
    }
    
    public EventListenerSupport(final Class<L> listenerInterface) {
        this(listenerInterface, Thread.currentThread().getContextClassLoader());
    }
    
    public EventListenerSupport(final Class<L> listenerInterface, final ClassLoader classLoader) {
        this();
        Validate.<Class<L>>notNull(listenerInterface, "Listener interface cannot be null.", new Object[0]);
        Validate.<ClassLoader>notNull(classLoader, "ClassLoader cannot be null.", new Object[0]);
        Validate.isTrue(listenerInterface.isInterface(), "Class {0} is not an interface", listenerInterface.getName());
        this.initializeTransientFields(listenerInterface, classLoader);
    }
    
    private EventListenerSupport() {
        this.listeners = (List<L>)new CopyOnWriteArrayList();
    }
    
    public L fire() {
        return this.proxy;
    }
    
    public void addListener(final L listener) {
        this.addListener(listener, true);
    }
    
    public void addListener(final L listener, final boolean allowDuplicate) {
        Validate.<L>notNull(listener, "Listener object cannot be null.", new Object[0]);
        if (allowDuplicate) {
            this.listeners.add(listener);
        }
        else if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }
    
    int getListenerCount() {
        return this.listeners.size();
    }
    
    public void removeListener(final L listener) {
        Validate.<L>notNull(listener, "Listener object cannot be null.", new Object[0]);
        this.listeners.remove(listener);
    }
    
    public L[] getListeners() {
        return (L[])this.listeners.toArray((Object[])this.prototypeArray);
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        final ArrayList<L> serializableListeners = (ArrayList<L>)new ArrayList();
        ObjectOutputStream testObjectOutputStream = new ObjectOutputStream((OutputStream)new ByteArrayOutputStream());
        for (final L listener : this.listeners) {
            try {
                testObjectOutputStream.writeObject(listener);
                serializableListeners.add(listener);
            }
            catch (IOException exception) {
                testObjectOutputStream = new ObjectOutputStream((OutputStream)new ByteArrayOutputStream());
            }
        }
        objectOutputStream.writeObject(serializableListeners.toArray((Object[])this.prototypeArray));
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        final L[] srcListeners = (L[])objectInputStream.readObject();
        this.listeners = (List<L>)new CopyOnWriteArrayList((Object[])srcListeners);
        final Class<L> listenerInterface = (Class<L>)srcListeners.getClass().getComponentType();
        this.initializeTransientFields(listenerInterface, Thread.currentThread().getContextClassLoader());
    }
    
    private void initializeTransientFields(final Class<L> listenerInterface, final ClassLoader classLoader) {
        final L[] array = (L[])Array.newInstance((Class)listenerInterface, 0);
        this.prototypeArray = array;
        this.createProxy(listenerInterface, classLoader);
    }
    
    private void createProxy(final Class<L> listenerInterface, final ClassLoader classLoader) {
        this.proxy = (L)listenerInterface.cast(Proxy.newProxyInstance(classLoader, new Class[] { listenerInterface }, this.createInvocationHandler()));
    }
    
    protected InvocationHandler createInvocationHandler() {
        return (InvocationHandler)new ProxyInvocationHandler();
    }
    
    protected class ProxyInvocationHandler implements InvocationHandler {
        public Object invoke(final Object unusedProxy, final Method method, final Object[] args) throws Throwable {
            for (final L listener : EventListenerSupport.this.listeners) {
                method.invoke(listener, args);
            }
            return null;
        }
    }
}

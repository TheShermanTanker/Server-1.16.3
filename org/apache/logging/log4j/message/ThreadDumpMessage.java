package org.apache.logging.log4j.message;

import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.management.ThreadInfo;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.util.StringBuilderFormattable;

@AsynchronouslyFormattable
public class ThreadDumpMessage implements Message, StringBuilderFormattable {
    private static final long serialVersionUID = -1103400781608841088L;
    private static final ThreadInfoFactory FACTORY;
    private volatile Map<ThreadInformation, StackTraceElement[]> threads;
    private final String title;
    private String formattedMessage;
    
    public ThreadDumpMessage(final String title) {
        this.title = ((title == null) ? "" : title);
        this.threads = ThreadDumpMessage.FACTORY.createThreadInfo();
    }
    
    private ThreadDumpMessage(final String formattedMsg, final String title) {
        this.formattedMessage = formattedMsg;
        this.title = ((title == null) ? "" : title);
    }
    
    public String toString() {
        return this.getFormattedMessage();
    }
    
    public String getFormattedMessage() {
        if (this.formattedMessage != null) {
            return this.formattedMessage;
        }
        final StringBuilder sb = new StringBuilder(255);
        this.formatTo(sb);
        return sb.toString();
    }
    
    public void formatTo(final StringBuilder sb) {
        sb.append(this.title);
        if (this.title.length() > 0) {
            sb.append('\n');
        }
        for (final Map.Entry<ThreadInformation, StackTraceElement[]> entry : this.threads.entrySet()) {
            final ThreadInformation info = (ThreadInformation)entry.getKey();
            info.printThreadInfo(sb);
            info.printStack(sb, (StackTraceElement[])entry.getValue());
            sb.append('\n');
        }
    }
    
    public String getFormat() {
        return (this.title == null) ? "" : this.title;
    }
    
    public Object[] getParameters() {
        return null;
    }
    
    protected Object writeReplace() {
        return new ThreadDumpMessageProxy(this);
    }
    
    private void readObject(final ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
    
    public Throwable getThrowable() {
        return null;
    }
    
    static {
        final Method[] methods = ThreadInfo.class.getMethods();
        boolean basic = true;
        for (final Method method : methods) {
            if (method.getName().equals("getLockInfo")) {
                basic = false;
                break;
            }
        }
        FACTORY = (basic ? new BasicThreadInfoFactory() : new ExtendedThreadInfoFactory());
    }
    
    private static class ThreadDumpMessageProxy implements Serializable {
        private static final long serialVersionUID = -3476620450287648269L;
        private final String formattedMsg;
        private final String title;
        
        ThreadDumpMessageProxy(final ThreadDumpMessage msg) {
            this.formattedMsg = msg.getFormattedMessage();
            this.title = msg.title;
        }
        
        protected Object readResolve() {
            return new ThreadDumpMessage(this.formattedMsg, this.title, null);
        }
    }
    
    private static class BasicThreadInfoFactory implements ThreadInfoFactory {
        public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
            final Map<Thread, StackTraceElement[]> map = (Map<Thread, StackTraceElement[]>)Thread.getAllStackTraces();
            final Map<ThreadInformation, StackTraceElement[]> threads = (Map<ThreadInformation, StackTraceElement[]>)new HashMap(map.size());
            for (final Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
                threads.put(new BasicThreadInformation((Thread)entry.getKey()), entry.getValue());
            }
            return threads;
        }
    }
    
    private static class ExtendedThreadInfoFactory implements ThreadInfoFactory {
        public Map<ThreadInformation, StackTraceElement[]> createThreadInfo() {
            final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
            final ThreadInfo[] array = bean.dumpAllThreads(true, true);
            final Map<ThreadInformation, StackTraceElement[]> threads = (Map<ThreadInformation, StackTraceElement[]>)new HashMap(array.length);
            for (final ThreadInfo info : array) {
                threads.put(new ExtendedThreadInformation(info), info.getStackTrace());
            }
            return threads;
        }
    }
    
    private interface ThreadInfoFactory {
        Map<ThreadInformation, StackTraceElement[]> createThreadInfo();
    }
}

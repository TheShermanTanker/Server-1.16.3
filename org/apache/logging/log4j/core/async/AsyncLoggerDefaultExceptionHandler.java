package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.ExceptionHandler;

public class AsyncLoggerDefaultExceptionHandler implements ExceptionHandler<RingBufferLogEvent> {
    public void handleEventException(final Throwable throwable, final long sequence, final RingBufferLogEvent event) {
        final StringBuilder sb = new StringBuilder(512);
        sb.append("AsyncLogger error handling event seq=").append(sequence).append(", value='");
        try {
            sb.append((CharSequence)event);
        }
        catch (Exception ignored) {
            sb.append("[ERROR calling ").append(event.getClass()).append(".toString(): ");
            sb.append(ignored).append("]");
        }
        sb.append("':");
        System.err.println(sb);
        throwable.printStackTrace();
    }
    
    public void handleOnStartException(final Throwable throwable) {
        System.err.println("AsyncLogger error starting:");
        throwable.printStackTrace();
    }
    
    public void handleOnShutdownException(final Throwable throwable) {
        System.err.println("AsyncLogger error shutting down:");
        throwable.printStackTrace();
    }
}

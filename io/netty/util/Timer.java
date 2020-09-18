package io.netty.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface Timer {
    Timeout newTimeout(final TimerTask timerTask, final long long2, final TimeUnit timeUnit);
    
    Set<Timeout> stop();
}

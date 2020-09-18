package com.google.common.eventbus;

public interface SubscriberExceptionHandler {
    void handleException(final Throwable throwable, final SubscriberExceptionContext subscriberExceptionContext);
}

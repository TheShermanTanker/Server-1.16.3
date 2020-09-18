package org.apache.logging.log4j.message;

interface ThreadInformation {
    void printThreadInfo(final StringBuilder stringBuilder);
    
    void printStack(final StringBuilder stringBuilder, final StackTraceElement[] arr);
}

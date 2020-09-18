package org.apache.logging.log4j.message;

public interface FlowMessageFactory {
    EntryMessage newEntryMessage(final Message message);
    
    ExitMessage newExitMessage(final Object object, final Message message);
    
    ExitMessage newExitMessage(final EntryMessage entryMessage);
    
    ExitMessage newExitMessage(final Object object, final EntryMessage entryMessage);
}

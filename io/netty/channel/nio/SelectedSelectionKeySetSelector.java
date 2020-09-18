package io.netty.channel.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Set;
import java.nio.channels.spi.SelectorProvider;
import java.nio.channels.Selector;

final class SelectedSelectionKeySetSelector extends Selector {
    private final SelectedSelectionKeySet selectionKeys;
    private final Selector delegate;
    
    SelectedSelectionKeySetSelector(final Selector delegate, final SelectedSelectionKeySet selectionKeys) {
        this.delegate = delegate;
        this.selectionKeys = selectionKeys;
    }
    
    public boolean isOpen() {
        return this.delegate.isOpen();
    }
    
    public SelectorProvider provider() {
        return this.delegate.provider();
    }
    
    public Set<SelectionKey> keys() {
        return (Set<SelectionKey>)this.delegate.keys();
    }
    
    public Set<SelectionKey> selectedKeys() {
        return (Set<SelectionKey>)this.delegate.selectedKeys();
    }
    
    public int selectNow() throws IOException {
        this.selectionKeys.reset();
        return this.delegate.selectNow();
    }
    
    public int select(final long timeout) throws IOException {
        this.selectionKeys.reset();
        return this.delegate.select(timeout);
    }
    
    public int select() throws IOException {
        this.selectionKeys.reset();
        return this.delegate.select();
    }
    
    public Selector wakeup() {
        return this.delegate.wakeup();
    }
    
    public void close() throws IOException {
        this.delegate.close();
    }
}

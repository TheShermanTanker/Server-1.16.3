package io.netty.channel.nio;

import java.util.Arrays;
import java.util.Iterator;
import java.nio.channels.SelectionKey;
import java.util.AbstractSet;

final class SelectedSelectionKeySet extends AbstractSet<SelectionKey> {
    SelectionKey[] keys;
    int size;
    
    SelectedSelectionKeySet() {
        this.keys = new SelectionKey[1024];
    }
    
    public boolean add(final SelectionKey o) {
        if (o == null) {
            return false;
        }
        this.keys[this.size++] = o;
        if (this.size == this.keys.length) {
            this.increaseCapacity();
        }
        return true;
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean remove(final Object o) {
        return false;
    }
    
    public boolean contains(final Object o) {
        return false;
    }
    
    public Iterator<SelectionKey> iterator() {
        throw new UnsupportedOperationException();
    }
    
    void reset() {
        this.reset(0);
    }
    
    void reset(final int start) {
        Arrays.fill((Object[])this.keys, start, this.size, null);
        this.size = 0;
    }
    
    private void increaseCapacity() {
        final SelectionKey[] newKeys = new SelectionKey[this.keys.length << 1];
        System.arraycopy(this.keys, 0, newKeys, 0, this.size);
        this.keys = newKeys;
    }
}

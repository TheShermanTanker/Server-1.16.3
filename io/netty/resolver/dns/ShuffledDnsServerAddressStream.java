package io.netty.resolver.dns;

import java.util.Random;
import io.netty.util.internal.PlatformDependent;
import java.net.InetSocketAddress;

final class ShuffledDnsServerAddressStream implements DnsServerAddressStream {
    private final InetSocketAddress[] addresses;
    private int i;
    
    ShuffledDnsServerAddressStream(final InetSocketAddress[] addresses) {
        this.addresses = addresses;
        this.shuffle();
    }
    
    private ShuffledDnsServerAddressStream(final InetSocketAddress[] addresses, final int startIdx) {
        this.addresses = addresses;
        this.i = startIdx;
    }
    
    private void shuffle() {
        final InetSocketAddress[] addresses = this.addresses;
        final Random r = PlatformDependent.threadLocalRandom();
        for (int i = addresses.length - 1; i >= 0; --i) {
            final InetSocketAddress tmp = addresses[i];
            final int j = r.nextInt(i + 1);
            addresses[i] = addresses[j];
            addresses[j] = tmp;
        }
    }
    
    public InetSocketAddress next() {
        int i = this.i;
        final InetSocketAddress next = this.addresses[i];
        if (++i < this.addresses.length) {
            this.i = i;
        }
        else {
            this.i = 0;
            this.shuffle();
        }
        return next;
    }
    
    public int size() {
        return this.addresses.length;
    }
    
    public ShuffledDnsServerAddressStream duplicate() {
        return new ShuffledDnsServerAddressStream(this.addresses, this.i);
    }
    
    public String toString() {
        return SequentialDnsServerAddressStream.toString("shuffled", this.i, this.addresses);
    }
}

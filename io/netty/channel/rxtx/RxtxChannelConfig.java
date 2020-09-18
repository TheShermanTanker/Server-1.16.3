package io.netty.channel.rxtx;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;

@Deprecated
public interface RxtxChannelConfig extends ChannelConfig {
    RxtxChannelConfig setBaudrate(final int integer);
    
    RxtxChannelConfig setStopbits(final Stopbits stopbits);
    
    RxtxChannelConfig setDatabits(final Databits databits);
    
    RxtxChannelConfig setParitybit(final Paritybit paritybit);
    
    int getBaudrate();
    
    Stopbits getStopbits();
    
    Databits getDatabits();
    
    Paritybit getParitybit();
    
    boolean isDtr();
    
    RxtxChannelConfig setDtr(final boolean boolean1);
    
    boolean isRts();
    
    RxtxChannelConfig setRts(final boolean boolean1);
    
    int getWaitTimeMillis();
    
    RxtxChannelConfig setWaitTimeMillis(final int integer);
    
    RxtxChannelConfig setReadTimeout(final int integer);
    
    int getReadTimeout();
    
    RxtxChannelConfig setConnectTimeoutMillis(final int integer);
    
    @Deprecated
    RxtxChannelConfig setMaxMessagesPerRead(final int integer);
    
    RxtxChannelConfig setWriteSpinCount(final int integer);
    
    RxtxChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    RxtxChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    RxtxChannelConfig setAutoRead(final boolean boolean1);
    
    RxtxChannelConfig setAutoClose(final boolean boolean1);
    
    RxtxChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    RxtxChannelConfig setWriteBufferLowWaterMark(final int integer);
    
    RxtxChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
    
    RxtxChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
    
    public enum Stopbits {
        STOPBITS_1(1), 
        STOPBITS_2(2), 
        STOPBITS_1_5(3);
        
        private final int value;
        
        private Stopbits(final int value) {
            this.value = value;
        }
        
        public int value() {
            return this.value;
        }
        
        public static Stopbits valueOf(final int value) {
            for (final Stopbits stopbit : values()) {
                if (stopbit.value == value) {
                    return stopbit;
                }
            }
            throw new IllegalArgumentException("unknown " + Stopbits.class.getSimpleName() + " value: " + value);
        }
    }
    
    public enum Databits {
        DATABITS_5(5), 
        DATABITS_6(6), 
        DATABITS_7(7), 
        DATABITS_8(8);
        
        private final int value;
        
        private Databits(final int value) {
            this.value = value;
        }
        
        public int value() {
            return this.value;
        }
        
        public static Databits valueOf(final int value) {
            for (final Databits databit : values()) {
                if (databit.value == value) {
                    return databit;
                }
            }
            throw new IllegalArgumentException("unknown " + Databits.class.getSimpleName() + " value: " + value);
        }
    }
    
    public enum Paritybit {
        NONE(0), 
        ODD(1), 
        EVEN(2), 
        MARK(3), 
        SPACE(4);
        
        private final int value;
        
        private Paritybit(final int value) {
            this.value = value;
        }
        
        public int value() {
            return this.value;
        }
        
        public static Paritybit valueOf(final int value) {
            for (final Paritybit paritybit : values()) {
                if (paritybit.value == value) {
                    return paritybit;
                }
            }
            throw new IllegalArgumentException("unknown " + Paritybit.class.getSimpleName() + " value: " + value);
        }
    }
}

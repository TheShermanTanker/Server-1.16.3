package io.netty.handler.codec.mqtt;

public enum MqttQoS {
    AT_MOST_ONCE(0), 
    AT_LEAST_ONCE(1), 
    EXACTLY_ONCE(2), 
    FAILURE(128);
    
    private final int value;
    
    private MqttQoS(final int value) {
        this.value = value;
    }
    
    public int value() {
        return this.value;
    }
    
    public static MqttQoS valueOf(final int value) {
        for (final MqttQoS q : values()) {
            if (q.value == value) {
                return q;
            }
        }
        throw new IllegalArgumentException(new StringBuilder().append("invalid QoS: ").append(value).toString());
    }
}

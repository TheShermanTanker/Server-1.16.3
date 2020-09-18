package io.netty.handler.codec.mqtt;

public final class MqttUnsubAckMessage extends MqttMessage {
    public MqttUnsubAckMessage(final MqttFixedHeader mqttFixedHeader, final MqttMessageIdVariableHeader variableHeader) {
        super(mqttFixedHeader, variableHeader, null);
    }
    
    @Override
    public MqttMessageIdVariableHeader variableHeader() {
        return (MqttMessageIdVariableHeader)super.variableHeader();
    }
}

package io.netty.handler.codec.http.websocketx;

public enum WebSocketVersion {
    UNKNOWN, 
    V00, 
    V07, 
    V08, 
    V13;
    
    public String toHttpHeaderValue() {
        if (this == WebSocketVersion.V00) {
            return "0";
        }
        if (this == WebSocketVersion.V07) {
            return "7";
        }
        if (this == WebSocketVersion.V08) {
            return "8";
        }
        if (this == WebSocketVersion.V13) {
            return "13";
        }
        throw new IllegalStateException(new StringBuilder().append("Unknown web socket version: ").append(this).toString());
    }
}

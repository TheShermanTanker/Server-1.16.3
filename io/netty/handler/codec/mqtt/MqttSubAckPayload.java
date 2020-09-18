package io.netty.handler.codec.mqtt;

import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class MqttSubAckPayload {
    private final List<Integer> grantedQoSLevels;
    
    public MqttSubAckPayload(final int... grantedQoSLevels) {
        if (grantedQoSLevels == null) {
            throw new NullPointerException("grantedQoSLevels");
        }
        final List<Integer> list = (List<Integer>)new ArrayList(grantedQoSLevels.length);
        for (final int v : grantedQoSLevels) {
            list.add(v);
        }
        this.grantedQoSLevels = (List<Integer>)Collections.unmodifiableList((List)list);
    }
    
    public MqttSubAckPayload(final Iterable<Integer> grantedQoSLevels) {
        if (grantedQoSLevels == null) {
            throw new NullPointerException("grantedQoSLevels");
        }
        final List<Integer> list = (List<Integer>)new ArrayList();
        for (final Integer v : grantedQoSLevels) {
            if (v == null) {
                break;
            }
            list.add(v);
        }
        this.grantedQoSLevels = (List<Integer>)Collections.unmodifiableList((List)list);
    }
    
    public List<Integer> grantedQoSLevels() {
        return this.grantedQoSLevels;
    }
    
    public String toString() {
        return StringUtil.simpleClassName(this) + '[' + "grantedQoSLevels=" + this.grantedQoSLevels + ']';
    }
}

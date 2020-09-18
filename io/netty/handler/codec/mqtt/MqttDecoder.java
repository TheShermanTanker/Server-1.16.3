package io.netty.handler.codec.mqtt;

import io.netty.util.CharsetUtil;
import java.util.ArrayList;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public final class MqttDecoder extends ReplayingDecoder<DecoderState> {
    private static final int DEFAULT_MAX_BYTES_IN_MESSAGE = 8092;
    private MqttFixedHeader mqttFixedHeader;
    private Object variableHeader;
    private int bytesRemainingInVariablePart;
    private final int maxBytesInMessage;
    
    public MqttDecoder() {
        this(8092);
    }
    
    public MqttDecoder(final int maxBytesInMessage) {
        super(DecoderState.READ_FIXED_HEADER);
        this.maxBytesInMessage = maxBytesInMessage;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf buffer, final List<Object> out) throws Exception {
        switch (this.state()) {
            case READ_FIXED_HEADER: {
                try {
                    this.mqttFixedHeader = decodeFixedHeader(buffer);
                    this.bytesRemainingInVariablePart = this.mqttFixedHeader.remainingLength();
                    this.checkpoint(DecoderState.READ_VARIABLE_HEADER);
                }
                catch (Exception cause) {
                    out.add(this.invalidMessage((Throwable)cause));
                }
            }
            case READ_VARIABLE_HEADER: {
                try {
                    if (this.bytesRemainingInVariablePart > this.maxBytesInMessage) {
                        throw new DecoderException(new StringBuilder().append("too large message: ").append(this.bytesRemainingInVariablePart).append(" bytes").toString());
                    }
                    final Result<?> decodedVariableHeader = decodeVariableHeader(buffer, this.mqttFixedHeader);
                    this.variableHeader = ((Result<Object>)decodedVariableHeader).value;
                    this.bytesRemainingInVariablePart -= ((Result<Object>)decodedVariableHeader).numberOfBytesConsumed;
                    this.checkpoint(DecoderState.READ_PAYLOAD);
                }
                catch (Exception cause) {
                    out.add(this.invalidMessage((Throwable)cause));
                }
            }
            case READ_PAYLOAD: {
                try {
                    final Result<?> decodedPayload = decodePayload(buffer, this.mqttFixedHeader.messageType(), this.bytesRemainingInVariablePart, this.variableHeader);
                    this.bytesRemainingInVariablePart -= ((Result<Object>)decodedPayload).numberOfBytesConsumed;
                    if (this.bytesRemainingInVariablePart != 0) {
                        throw new DecoderException(new StringBuilder().append("non-zero remaining payload bytes: ").append(this.bytesRemainingInVariablePart).append(" (").append(this.mqttFixedHeader.messageType()).append(')').toString());
                    }
                    this.checkpoint(DecoderState.READ_FIXED_HEADER);
                    final MqttMessage message = MqttMessageFactory.newMessage(this.mqttFixedHeader, this.variableHeader, ((Result<Object>)decodedPayload).value);
                    this.mqttFixedHeader = null;
                    this.variableHeader = null;
                    out.add(message);
                    break;
                }
                catch (Exception cause) {
                    out.add(this.invalidMessage((Throwable)cause));
                }
            }
            case BAD_MESSAGE: {
                buffer.skipBytes(this.actualReadableBytes());
                break;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    private MqttMessage invalidMessage(final Throwable cause) {
        this.checkpoint(DecoderState.BAD_MESSAGE);
        return MqttMessageFactory.newInvalidMessage(cause);
    }
    
    private static MqttFixedHeader decodeFixedHeader(final ByteBuf buffer) {
        final short b1 = buffer.readUnsignedByte();
        final MqttMessageType messageType = MqttMessageType.valueOf(b1 >> 4);
        final boolean dupFlag = (b1 & 0x8) == 0x8;
        final int qosLevel = (b1 & 0x6) >> 1;
        final boolean retain = (b1 & 0x1) != 0x0;
        int remainingLength = 0;
        int multiplier = 1;
        int loops = 0;
        short digit;
        do {
            digit = buffer.readUnsignedByte();
            remainingLength += (digit & 0x7F) * multiplier;
            multiplier *= 128;
            ++loops;
        } while ((digit & 0x80) != 0x0 && loops < 4);
        if (loops == 4 && (digit & 0x80) != 0x0) {
            throw new DecoderException(new StringBuilder().append("remaining length exceeds 4 digits (").append(messageType).append(')').toString());
        }
        final MqttFixedHeader decodedFixedHeader = new MqttFixedHeader(messageType, dupFlag, MqttQoS.valueOf(qosLevel), retain, remainingLength);
        return MqttCodecUtil.validateFixedHeader(MqttCodecUtil.resetUnusedFields(decodedFixedHeader));
    }
    
    private static Result<?> decodeVariableHeader(final ByteBuf buffer, final MqttFixedHeader mqttFixedHeader) {
        switch (mqttFixedHeader.messageType()) {
            case CONNECT: {
                return decodeConnectionVariableHeader(buffer);
            }
            case CONNACK: {
                return decodeConnAckVariableHeader(buffer);
            }
            case SUBSCRIBE:
            case UNSUBSCRIBE:
            case SUBACK:
            case UNSUBACK:
            case PUBACK:
            case PUBREC:
            case PUBCOMP:
            case PUBREL: {
                return decodeMessageIdVariableHeader(buffer);
            }
            case PUBLISH: {
                return decodePublishVariableHeader(buffer, mqttFixedHeader);
            }
            case PINGREQ:
            case PINGRESP:
            case DISCONNECT: {
                return new Result<>(null, 0);
            }
            default: {
                return new Result<>(null, 0);
            }
        }
    }
    
    private static Result<MqttConnectVariableHeader> decodeConnectionVariableHeader(final ByteBuf buffer) {
        final Result<String> protoString = decodeString(buffer);
        int numberOfBytesConsumed = ((Result<Object>)protoString).numberOfBytesConsumed;
        final byte protocolLevel = buffer.readByte();
        ++numberOfBytesConsumed;
        final MqttVersion mqttVersion = MqttVersion.fromProtocolNameAndLevel((String)((Result<Object>)protoString).value, protocolLevel);
        final int b1 = buffer.readUnsignedByte();
        ++numberOfBytesConsumed;
        final Result<Integer> keepAlive = decodeMsbLsb(buffer);
        numberOfBytesConsumed += ((Result<Object>)keepAlive).numberOfBytesConsumed;
        final boolean hasUserName = (b1 & 0x80) == 0x80;
        final boolean hasPassword = (b1 & 0x40) == 0x40;
        final boolean willRetain = (b1 & 0x20) == 0x20;
        final int willQos = (b1 & 0x18) >> 3;
        final boolean willFlag = (b1 & 0x4) == 0x4;
        final boolean cleanSession = (b1 & 0x2) == 0x2;
        if (mqttVersion == MqttVersion.MQTT_3_1_1) {
            final boolean zeroReservedFlag = (b1 & 0x1) == 0x0;
            if (!zeroReservedFlag) {
                throw new DecoderException("non-zero reserved flag");
            }
        }
        final MqttConnectVariableHeader mqttConnectVariableHeader = new MqttConnectVariableHeader(mqttVersion.protocolName(), mqttVersion.protocolLevel(), hasUserName, hasPassword, willRetain, willQos, willFlag, cleanSession, (int)((Result<Object>)keepAlive).value);
        return new Result<MqttConnectVariableHeader>(mqttConnectVariableHeader, numberOfBytesConsumed);
    }
    
    private static Result<MqttConnAckVariableHeader> decodeConnAckVariableHeader(final ByteBuf buffer) {
        final boolean sessionPresent = (buffer.readUnsignedByte() & 0x1) == 0x1;
        final byte returnCode = buffer.readByte();
        final int numberOfBytesConsumed = 2;
        final MqttConnAckVariableHeader mqttConnAckVariableHeader = new MqttConnAckVariableHeader(MqttConnectReturnCode.valueOf(returnCode), sessionPresent);
        return new Result<MqttConnAckVariableHeader>(mqttConnAckVariableHeader, 2);
    }
    
    private static Result<MqttMessageIdVariableHeader> decodeMessageIdVariableHeader(final ByteBuf buffer) {
        final Result<Integer> messageId = decodeMessageId(buffer);
        return new Result<MqttMessageIdVariableHeader>(MqttMessageIdVariableHeader.from((int)((Result<Object>)messageId).value), ((Result<Object>)messageId).numberOfBytesConsumed);
    }
    
    private static Result<MqttPublishVariableHeader> decodePublishVariableHeader(final ByteBuf buffer, final MqttFixedHeader mqttFixedHeader) {
        final Result<String> decodedTopic = decodeString(buffer);
        if (!MqttCodecUtil.isValidPublishTopicName((String)((Result<Object>)decodedTopic).value)) {
            throw new DecoderException("invalid publish topic name: " + (String)((Result<Object>)decodedTopic).value + " (contains wildcards)");
        }
        int numberOfBytesConsumed = ((Result<Object>)decodedTopic).numberOfBytesConsumed;
        int messageId = -1;
        if (mqttFixedHeader.qosLevel().value() > 0) {
            final Result<Integer> decodedMessageId = decodeMessageId(buffer);
            messageId = (int)((Result<Object>)decodedMessageId).value;
            numberOfBytesConsumed += ((Result<Object>)decodedMessageId).numberOfBytesConsumed;
        }
        final MqttPublishVariableHeader mqttPublishVariableHeader = new MqttPublishVariableHeader((String)((Result<Object>)decodedTopic).value, messageId);
        return new Result<MqttPublishVariableHeader>(mqttPublishVariableHeader, numberOfBytesConsumed);
    }
    
    private static Result<Integer> decodeMessageId(final ByteBuf buffer) {
        final Result<Integer> messageId = decodeMsbLsb(buffer);
        if (!MqttCodecUtil.isValidMessageId((int)((Result<Object>)messageId).value)) {
            throw new DecoderException(new StringBuilder().append("invalid messageId: ").append(((Result<Object>)messageId).value).toString());
        }
        return messageId;
    }
    
    private static Result<?> decodePayload(final ByteBuf buffer, final MqttMessageType messageType, final int bytesRemainingInVariablePart, final Object variableHeader) {
        switch (messageType) {
            case CONNECT: {
                return decodeConnectionPayload(buffer, (MqttConnectVariableHeader)variableHeader);
            }
            case SUBSCRIBE: {
                return decodeSubscribePayload(buffer, bytesRemainingInVariablePart);
            }
            case SUBACK: {
                return decodeSubackPayload(buffer, bytesRemainingInVariablePart);
            }
            case UNSUBSCRIBE: {
                return decodeUnsubscribePayload(buffer, bytesRemainingInVariablePart);
            }
            case PUBLISH: {
                return decodePublishPayload(buffer, bytesRemainingInVariablePart);
            }
            default: {
                return new Result<>(null, 0);
            }
        }
    }
    
    private static Result<MqttConnectPayload> decodeConnectionPayload(final ByteBuf buffer, final MqttConnectVariableHeader mqttConnectVariableHeader) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder.decodeString:(Lio/netty/buffer/ByteBuf;)Lio/netty/handler/codec/mqtt/MqttDecoder$Result;
        //     4: astore_2        /* decodedClientId */
        //     5: aload_2         /* decodedClientId */
        //     6: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$000:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)Ljava/lang/Object;
        //     9: checkcast       Ljava/lang/String;
        //    12: astore_3        /* decodedClientIdValue */
        //    13: aload_1         /* mqttConnectVariableHeader */
        //    14: invokevirtual   io/netty/handler/codec/mqtt/MqttConnectVariableHeader.name:()Ljava/lang/String;
        //    17: aload_1         /* mqttConnectVariableHeader */
        //    18: invokevirtual   io/netty/handler/codec/mqtt/MqttConnectVariableHeader.version:()I
        //    21: i2b            
        //    22: invokestatic    io/netty/handler/codec/mqtt/MqttVersion.fromProtocolNameAndLevel:(Ljava/lang/String;B)Lio/netty/handler/codec/mqtt/MqttVersion;
        //    25: astore          mqttVersion
        //    27: aload           mqttVersion
        //    29: aload_3         /* decodedClientIdValue */
        //    30: invokestatic    io/netty/handler/codec/mqtt/MqttCodecUtil.isValidClientId:(Lio/netty/handler/codec/mqtt/MqttVersion;Ljava/lang/String;)Z
        //    33: ifne            64
        //    36: new             Lio/netty/handler/codec/mqtt/MqttIdentifierRejectedException;
        //    39: dup            
        //    40: new             Ljava/lang/StringBuilder;
        //    43: dup            
        //    44: invokespecial   java/lang/StringBuilder.<init>:()V
        //    47: ldc_w           "invalid clientIdentifier: "
        //    50: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    53: aload_3         /* decodedClientIdValue */
        //    54: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    57: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    60: invokespecial   io/netty/handler/codec/mqtt/MqttIdentifierRejectedException.<init>:(Ljava/lang/String;)V
        //    63: athrow         
        //    64: aload_2         /* decodedClientId */
        //    65: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$100:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)I
        //    68: istore          numberOfBytesConsumed
        //    70: aconst_null    
        //    71: astore          decodedWillTopic
        //    73: aconst_null    
        //    74: astore          decodedWillMessage
        //    76: aload_1         /* mqttConnectVariableHeader */
        //    77: invokevirtual   io/netty/handler/codec/mqtt/MqttConnectVariableHeader.isWillFlag:()Z
        //    80: ifeq            119
        //    83: aload_0         /* buffer */
        //    84: iconst_0       
        //    85: sipush          32767
        //    88: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder.decodeString:(Lio/netty/buffer/ByteBuf;II)Lio/netty/handler/codec/mqtt/MqttDecoder$Result;
        //    91: astore          decodedWillTopic
        //    93: iload           numberOfBytesConsumed
        //    95: aload           decodedWillTopic
        //    97: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$100:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)I
        //   100: iadd           
        //   101: istore          numberOfBytesConsumed
        //   103: aload_0         /* buffer */
        //   104: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder.decodeByteArray:(Lio/netty/buffer/ByteBuf;)Lio/netty/handler/codec/mqtt/MqttDecoder$Result;
        //   107: astore          decodedWillMessage
        //   109: iload           numberOfBytesConsumed
        //   111: aload           decodedWillMessage
        //   113: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$100:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)I
        //   116: iadd           
        //   117: istore          numberOfBytesConsumed
        //   119: aconst_null    
        //   120: astore          decodedUserName
        //   122: aconst_null    
        //   123: astore          decodedPassword
        //   125: aload_1         /* mqttConnectVariableHeader */
        //   126: invokevirtual   io/netty/handler/codec/mqtt/MqttConnectVariableHeader.hasUserName:()Z
        //   129: ifeq            148
        //   132: aload_0         /* buffer */
        //   133: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder.decodeString:(Lio/netty/buffer/ByteBuf;)Lio/netty/handler/codec/mqtt/MqttDecoder$Result;
        //   136: astore          decodedUserName
        //   138: iload           numberOfBytesConsumed
        //   140: aload           decodedUserName
        //   142: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$100:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)I
        //   145: iadd           
        //   146: istore          numberOfBytesConsumed
        //   148: aload_1         /* mqttConnectVariableHeader */
        //   149: invokevirtual   io/netty/handler/codec/mqtt/MqttConnectVariableHeader.hasPassword:()Z
        //   152: ifeq            171
        //   155: aload_0         /* buffer */
        //   156: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder.decodeByteArray:(Lio/netty/buffer/ByteBuf;)Lio/netty/handler/codec/mqtt/MqttDecoder$Result;
        //   159: astore          decodedPassword
        //   161: iload           numberOfBytesConsumed
        //   163: aload           decodedPassword
        //   165: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$100:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)I
        //   168: iadd           
        //   169: istore          numberOfBytesConsumed
        //   171: new             Lio/netty/handler/codec/mqtt/MqttConnectPayload;
        //   174: dup            
        //   175: aload_2         /* decodedClientId */
        //   176: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$000:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)Ljava/lang/Object;
        //   179: checkcast       Ljava/lang/String;
        //   182: aload           decodedWillTopic
        //   184: ifnull          198
        //   187: aload           decodedWillTopic
        //   189: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$000:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)Ljava/lang/Object;
        //   192: checkcast       Ljava/lang/String;
        //   195: goto            199
        //   198: aconst_null    
        //   199: aload           decodedWillMessage
        //   201: ifnull          215
        //   204: aload           decodedWillMessage
        //   206: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$000:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)Ljava/lang/Object;
        //   209: checkcast       [B
        //   212: goto            216
        //   215: aconst_null    
        //   216: aload           decodedUserName
        //   218: ifnull          232
        //   221: aload           decodedUserName
        //   223: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$000:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)Ljava/lang/Object;
        //   226: checkcast       Ljava/lang/String;
        //   229: goto            233
        //   232: aconst_null    
        //   233: aload           decodedPassword
        //   235: ifnull          249
        //   238: aload           decodedPassword
        //   240: invokestatic    io/netty/handler/codec/mqtt/MqttDecoder$Result.access$000:(Lio/netty/handler/codec/mqtt/MqttDecoder$Result;)Ljava/lang/Object;
        //   243: checkcast       [B
        //   246: goto            250
        //   249: aconst_null    
        //   250: invokespecial   io/netty/handler/codec/mqtt/MqttConnectPayload.<init>:(Ljava/lang/String;Ljava/lang/String;[BLjava/lang/String;[B)V
        //   253: astore          mqttConnectPayload
        //   255: new             Lio/netty/handler/codec/mqtt/MqttDecoder$Result;
        //   258: dup            
        //   259: aload           mqttConnectPayload
        //   261: iload           numberOfBytesConsumed
        //   263: invokespecial   io/netty/handler/codec/mqtt/MqttDecoder$Result.<init>:(Ljava/lang/Object;I)V
        //   266: areturn        
        //    Signature:
        //  (Lio/netty/buffer/ByteBuf;Lio/netty/handler/codec/mqtt/MqttConnectVariableHeader;)Lio/netty/handler/codec/mqtt/MqttDecoder$Result<Lio/netty/handler/codec/mqtt/MqttConnectPayload;>;
        //    MethodParameters:
        //  Name                       Flags  
        //  -------------------------  -----
        //  buffer                     
        //  mqttConnectVariableHeader  
        //    StackMapTable: 00 0C FE 00 40 07 00 0A 07 01 01 07 01 03 FE 00 36 01 07 00 0A 07 00 0A FD 00 1C 07 00 0A 07 00 0A 16 FF 00 1A 00 0A 07 00 9D 07 01 12 07 00 0A 07 01 01 07 01 03 01 07 00 0A 07 00 0A 07 00 0A 07 00 0A 00 03 08 00 AB 08 00 AB 07 01 01 FF 00 00 00 0A 07 00 9D 07 01 12 07 00 0A 07 01 01 07 01 03 01 07 00 0A 07 00 0A 07 00 0A 07 00 0A 00 04 08 00 AB 08 00 AB 07 01 01 07 01 01 FF 00 0F 00 0A 07 00 9D 07 01 12 07 00 0A 07 01 01 07 01 03 01 07 00 0A 07 00 0A 07 00 0A 07 00 0A 00 04 08 00 AB 08 00 AB 07 01 01 07 01 01 FF 00 00 00 0A 07 00 9D 07 01 12 07 00 0A 07 01 01 07 01 03 01 07 00 0A 07 00 0A 07 00 0A 07 00 0A 00 05 08 00 AB 08 00 AB 07 01 01 07 01 01 07 01 9D FF 00 0F 00 0A 07 00 9D 07 01 12 07 00 0A 07 01 01 07 01 03 01 07 00 0A 07 00 0A 07 00 0A 07 00 0A 00 05 08 00 AB 08 00 AB 07 01 01 07 01 01 07 01 9D FF 00 00 00 0A 07 00 9D 07 01 12 07 00 0A 07 01 01 07 01 03 01 07 00 0A 07 00 0A 07 00 0A 07 00 0A 00 06 08 00 AB 08 00 AB 07 01 01 07 01 01 07 01 9D 07 01 01 FF 00 0F 00 0A 07 00 9D 07 01 12 07 00 0A 07 01 01 07 01 03 01 07 00 0A 07 00 0A 07 00 0A 07 00 0A 00 06 08 00 AB 08 00 AB 07 01 01 07 01 01 07 01 9D 07 01 01 FF 00 00 00 0A 07 00 9D 07 01 12 07 00 0A 07 01 01 07 01 03 01 07 00 0A 07 00 0A 07 00 0A 07 00 0A 00 07 08 00 AB 08 00 AB 07 01 01 07 01 01 07 01 9D 07 01 01 07 01 9D
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2362)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.decompiler.ast.TypeAnalysis.isSameType(TypeAnalysis.java:3072)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:790)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
        //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
        //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
        //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
        //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
        //     at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
        //     at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
        //     at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
        //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
        //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.base/java.lang.Thread.run(Thread.java:832)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static Result<MqttSubscribePayload> decodeSubscribePayload(final ByteBuf buffer, final int bytesRemainingInVariablePart) {
        final List<MqttTopicSubscription> subscribeTopics = (List<MqttTopicSubscription>)new ArrayList();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            final Result<String> decodedTopicName = decodeString(buffer);
            numberOfBytesConsumed += ((Result<Object>)decodedTopicName).numberOfBytesConsumed;
            final int qos = buffer.readUnsignedByte() & 0x3;
            ++numberOfBytesConsumed;
            subscribeTopics.add(new MqttTopicSubscription((String)((Result<Object>)decodedTopicName).value, MqttQoS.valueOf(qos)));
        }
        return new Result<MqttSubscribePayload>(new MqttSubscribePayload(subscribeTopics), numberOfBytesConsumed);
    }
    
    private static Result<MqttSubAckPayload> decodeSubackPayload(final ByteBuf buffer, final int bytesRemainingInVariablePart) {
        final List<Integer> grantedQos = (List<Integer>)new ArrayList();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            int qos = buffer.readUnsignedByte();
            if (qos != MqttQoS.FAILURE.value()) {
                qos &= 0x3;
            }
            ++numberOfBytesConsumed;
            grantedQos.add(qos);
        }
        return new Result<MqttSubAckPayload>(new MqttSubAckPayload((Iterable<Integer>)grantedQos), numberOfBytesConsumed);
    }
    
    private static Result<MqttUnsubscribePayload> decodeUnsubscribePayload(final ByteBuf buffer, final int bytesRemainingInVariablePart) {
        final List<String> unsubscribeTopics = (List<String>)new ArrayList();
        int numberOfBytesConsumed = 0;
        while (numberOfBytesConsumed < bytesRemainingInVariablePart) {
            final Result<String> decodedTopicName = decodeString(buffer);
            numberOfBytesConsumed += ((Result<Object>)decodedTopicName).numberOfBytesConsumed;
            unsubscribeTopics.add(((Result<Object>)decodedTopicName).value);
        }
        return new Result<MqttUnsubscribePayload>(new MqttUnsubscribePayload(unsubscribeTopics), numberOfBytesConsumed);
    }
    
    private static Result<ByteBuf> decodePublishPayload(final ByteBuf buffer, final int bytesRemainingInVariablePart) {
        final ByteBuf b = buffer.readRetainedSlice(bytesRemainingInVariablePart);
        return new Result<ByteBuf>(b, bytesRemainingInVariablePart);
    }
    
    private static Result<String> decodeString(final ByteBuf buffer) {
        return decodeString(buffer, 0, Integer.MAX_VALUE);
    }
    
    private static Result<String> decodeString(final ByteBuf buffer, final int minBytes, final int maxBytes) {
        final Result<Integer> decodedSize = decodeMsbLsb(buffer);
        final int size = (int)((Result<Object>)decodedSize).value;
        int numberOfBytesConsumed = ((Result<Object>)decodedSize).numberOfBytesConsumed;
        if (size < minBytes || size > maxBytes) {
            buffer.skipBytes(size);
            numberOfBytesConsumed += size;
            return new Result<String>(null, numberOfBytesConsumed);
        }
        final String s = buffer.toString(buffer.readerIndex(), size, CharsetUtil.UTF_8);
        buffer.skipBytes(size);
        numberOfBytesConsumed += size;
        return new Result<String>(s, numberOfBytesConsumed);
    }
    
    private static Result<byte[]> decodeByteArray(final ByteBuf buffer) {
        final Result<Integer> decodedSize = decodeMsbLsb(buffer);
        final int size = (int)((Result<Object>)decodedSize).value;
        final byte[] bytes = new byte[size];
        buffer.readBytes(bytes);
        return new Result<byte[]>(bytes, ((Result<Object>)decodedSize).numberOfBytesConsumed + size);
    }
    
    private static Result<Integer> decodeMsbLsb(final ByteBuf buffer) {
        return decodeMsbLsb(buffer, 0, 65535);
    }
    
    private static Result<Integer> decodeMsbLsb(final ByteBuf buffer, final int min, final int max) {
        final short msbSize = buffer.readUnsignedByte();
        final short lsbSize = buffer.readUnsignedByte();
        final int numberOfBytesConsumed = 2;
        int result = msbSize << 8 | lsbSize;
        if (result < min || result > max) {
            result = -1;
        }
        return new Result<Integer>(result, 2);
    }
    
    enum DecoderState {
        READ_FIXED_HEADER, 
        READ_VARIABLE_HEADER, 
        READ_PAYLOAD, 
        BAD_MESSAGE;
    }
    
    private static final class Result<T> {
        private final T value;
        private final int numberOfBytesConsumed;
        
        Result(final T value, final int numberOfBytesConsumed) {
            this.value = value;
            this.numberOfBytesConsumed = numberOfBytesConsumed;
        }
    }
}

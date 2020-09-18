package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.EncoderException;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;
import io.netty.buffer.ByteBuf;

public interface Socks5AddressEncoder {
    public static final Socks5AddressEncoder DEFAULT = new Socks5AddressEncoder() {
        public void encodeAddress(final Socks5AddressType addrType, final String addrValue, final ByteBuf out) throws Exception {
            final byte typeVal = addrType.byteValue();
            if (typeVal == Socks5AddressType.IPv4.byteValue()) {
                if (addrValue != null) {
                    out.writeBytes(NetUtil.createByteArrayFromIpAddressString(addrValue));
                }
                else {
                    out.writeInt(0);
                }
            }
            else if (typeVal == Socks5AddressType.DOMAIN.byteValue()) {
                if (addrValue != null) {
                    out.writeByte(addrValue.length());
                    out.writeCharSequence((CharSequence)addrValue, CharsetUtil.US_ASCII);
                }
                else {
                    out.writeByte(1);
                    out.writeByte(0);
                }
            }
            else {
                if (typeVal != Socks5AddressType.IPv6.byteValue()) {
                    throw new EncoderException(new StringBuilder().append("unsupported addrType: ").append(addrType.byteValue() & 0xFF).toString());
                }
                if (addrValue != null) {
                    out.writeBytes(NetUtil.createByteArrayFromIpAddressString(addrValue));
                }
                else {
                    out.writeLong(0L);
                    out.writeLong(0L);
                }
            }
        }
    };
    
    void encodeAddress(final Socks5AddressType socks5AddressType, final String string, final ByteBuf byteBuf) throws Exception;
}

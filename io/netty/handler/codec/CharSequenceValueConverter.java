package io.netty.handler.codec;

import io.netty.util.internal.PlatformDependent;
import java.text.ParseException;
import java.util.Date;
import io.netty.util.AsciiString;

public class CharSequenceValueConverter implements ValueConverter<CharSequence> {
    public static final CharSequenceValueConverter INSTANCE;
    private static final AsciiString TRUE_ASCII;
    
    public CharSequence convertObject(final Object value) {
        if (value instanceof CharSequence) {
            return (CharSequence)value;
        }
        return (CharSequence)value.toString();
    }
    
    public CharSequence convertInt(final int value) {
        return (CharSequence)String.valueOf(value);
    }
    
    public CharSequence convertLong(final long value) {
        return (CharSequence)String.valueOf(value);
    }
    
    public CharSequence convertDouble(final double value) {
        return (CharSequence)String.valueOf(value);
    }
    
    public CharSequence convertChar(final char value) {
        return (CharSequence)String.valueOf(value);
    }
    
    public CharSequence convertBoolean(final boolean value) {
        return (CharSequence)String.valueOf(value);
    }
    
    public CharSequence convertFloat(final float value) {
        return (CharSequence)String.valueOf(value);
    }
    
    public boolean convertToBoolean(final CharSequence value) {
        return AsciiString.contentEqualsIgnoreCase(value, (CharSequence)CharSequenceValueConverter.TRUE_ASCII);
    }
    
    public CharSequence convertByte(final byte value) {
        return (CharSequence)String.valueOf((int)value);
    }
    
    public byte convertToByte(final CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString)value).byteAt(0);
        }
        return Byte.parseByte(value.toString());
    }
    
    public char convertToChar(final CharSequence value) {
        return value.charAt(0);
    }
    
    public CharSequence convertShort(final short value) {
        return (CharSequence)String.valueOf((int)value);
    }
    
    public short convertToShort(final CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString)value).parseShort();
        }
        return Short.parseShort(value.toString());
    }
    
    public int convertToInt(final CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString)value).parseInt();
        }
        return Integer.parseInt(value.toString());
    }
    
    public long convertToLong(final CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString)value).parseLong();
        }
        return Long.parseLong(value.toString());
    }
    
    public CharSequence convertTimeMillis(final long value) {
        return (CharSequence)DateFormatter.format(new Date(value));
    }
    
    public long convertToTimeMillis(final CharSequence value) {
        final Date date = DateFormatter.parseHttpDate(value);
        if (date == null) {
            PlatformDependent.throwException((Throwable)new ParseException(new StringBuilder().append("header can't be parsed into a Date: ").append(value).toString(), 0));
            return 0L;
        }
        return date.getTime();
    }
    
    public float convertToFloat(final CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString)value).parseFloat();
        }
        return Float.parseFloat(value.toString());
    }
    
    public double convertToDouble(final CharSequence value) {
        if (value instanceof AsciiString) {
            return ((AsciiString)value).parseDouble();
        }
        return Double.parseDouble(value.toString());
    }
    
    static {
        INSTANCE = new CharSequenceValueConverter();
        TRUE_ASCII = new AsciiString("true");
    }
}

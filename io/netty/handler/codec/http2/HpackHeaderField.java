package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;

class HpackHeaderField {
    static final int HEADER_ENTRY_OVERHEAD = 32;
    final CharSequence name;
    final CharSequence value;
    
    static long sizeOf(final CharSequence name, final CharSequence value) {
        return name.length() + value.length() + 32;
    }
    
    HpackHeaderField(final CharSequence name, final CharSequence value) {
        this.name = ObjectUtil.<CharSequence>checkNotNull(name, "name");
        this.value = ObjectUtil.<CharSequence>checkNotNull(value, "value");
    }
    
    final int size() {
        return this.name.length() + this.value.length() + 32;
    }
    
    public final int hashCode() {
        return super.hashCode();
    }
    
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HpackHeaderField)) {
            return false;
        }
        final HpackHeaderField other = (HpackHeaderField)obj;
        return (HpackUtil.equalsConstantTime(this.name, other.name) & HpackUtil.equalsConstantTime(this.value, other.value)) != 0x0;
    }
    
    public String toString() {
        return new StringBuilder().append(this.name).append(": ").append(this.value).toString();
    }
}

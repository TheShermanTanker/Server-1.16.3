package com.google.common.hash;

import java.nio.charset.Charset;
import com.google.common.base.Preconditions;

abstract class AbstractCompositeHashFunction extends AbstractStreamingHashFunction {
    final HashFunction[] functions;
    private static final long serialVersionUID = 0L;
    
    AbstractCompositeHashFunction(final HashFunction... functions) {
        for (final HashFunction function : functions) {
            Preconditions.<HashFunction>checkNotNull(function);
        }
        this.functions = functions;
    }
    
    abstract HashCode makeHash(final Hasher[] arr);
    
    public Hasher newHasher() {
        final Hasher[] hashers = new Hasher[this.functions.length];
        for (int i = 0; i < hashers.length; ++i) {
            hashers[i] = this.functions[i].newHasher();
        }
        return new Hasher() {
            public Hasher putByte(final byte b) {
                for (final Hasher hasher : hashers) {
                    hasher.putByte(b);
                }
                return this;
            }
            
            public Hasher putBytes(final byte[] bytes) {
                for (final Hasher hasher : hashers) {
                    hasher.putBytes(bytes);
                }
                return this;
            }
            
            public Hasher putBytes(final byte[] bytes, final int off, final int len) {
                for (final Hasher hasher : hashers) {
                    hasher.putBytes(bytes, off, len);
                }
                return this;
            }
            
            public Hasher putShort(final short s) {
                for (final Hasher hasher : hashers) {
                    hasher.putShort(s);
                }
                return this;
            }
            
            public Hasher putInt(final int i) {
                for (final Hasher hasher : hashers) {
                    hasher.putInt(i);
                }
                return this;
            }
            
            public Hasher putLong(final long l) {
                for (final Hasher hasher : hashers) {
                    hasher.putLong(l);
                }
                return this;
            }
            
            public Hasher putFloat(final float f) {
                for (final Hasher hasher : hashers) {
                    hasher.putFloat(f);
                }
                return this;
            }
            
            public Hasher putDouble(final double d) {
                for (final Hasher hasher : hashers) {
                    hasher.putDouble(d);
                }
                return this;
            }
            
            public Hasher putBoolean(final boolean b) {
                for (final Hasher hasher : hashers) {
                    hasher.putBoolean(b);
                }
                return this;
            }
            
            public Hasher putChar(final char c) {
                for (final Hasher hasher : hashers) {
                    hasher.putChar(c);
                }
                return this;
            }
            
            public Hasher putUnencodedChars(final CharSequence chars) {
                for (final Hasher hasher : hashers) {
                    hasher.putUnencodedChars(chars);
                }
                return this;
            }
            
            public Hasher putString(final CharSequence chars, final Charset charset) {
                for (final Hasher hasher : hashers) {
                    hasher.putString(chars, charset);
                }
                return this;
            }
            
            public <T> Hasher putObject(final T instance, final Funnel<? super T> funnel) {
                for (final Hasher hasher : hashers) {
                    hasher.<T>putObject(instance, funnel);
                }
                return this;
            }
            
            public HashCode hash() {
                return AbstractCompositeHashFunction.this.makeHash(hashers);
            }
        };
    }
}

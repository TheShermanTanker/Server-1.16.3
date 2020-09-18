package com.mojang.math;

public final class Matrix3f {
    private static final float G;
    private static final float CS;
    private static final float SS;
    private static final float SQ2;
    protected float m00;
    protected float m01;
    protected float m02;
    protected float m10;
    protected float m11;
    protected float m12;
    protected float m20;
    protected float m21;
    protected float m22;
    
    public Matrix3f() {
    }
    
    public Matrix3f(final Quaternion d) {
        final float float3 = d.i();
        final float float4 = d.j();
        final float float5 = d.k();
        final float float6 = d.r();
        final float float7 = 2.0f * float3 * float3;
        final float float8 = 2.0f * float4 * float4;
        final float float9 = 2.0f * float5 * float5;
        this.m00 = 1.0f - float8 - float9;
        this.m11 = 1.0f - float9 - float7;
        this.m22 = 1.0f - float7 - float8;
        final float float10 = float3 * float4;
        final float float11 = float4 * float5;
        final float float12 = float5 * float3;
        final float float13 = float3 * float6;
        final float float14 = float4 * float6;
        final float float15 = float5 * float6;
        this.m10 = 2.0f * (float10 + float15);
        this.m01 = 2.0f * (float10 - float15);
        this.m20 = 2.0f * (float12 - float14);
        this.m02 = 2.0f * (float12 + float14);
        this.m21 = 2.0f * (float11 + float13);
        this.m12 = 2.0f * (float11 - float13);
    }
    
    public Matrix3f(final Matrix4f b) {
        this.m00 = b.m00;
        this.m01 = b.m01;
        this.m02 = b.m02;
        this.m10 = b.m10;
        this.m11 = b.m11;
        this.m12 = b.m12;
        this.m20 = b.m20;
        this.m21 = b.m21;
        this.m22 = b.m22;
    }
    
    public Matrix3f(final Matrix3f a) {
        this.m00 = a.m00;
        this.m01 = a.m01;
        this.m02 = a.m02;
        this.m10 = a.m10;
        this.m11 = a.m11;
        this.m12 = a.m12;
        this.m20 = a.m20;
        this.m21 = a.m21;
        this.m22 = a.m22;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Matrix3f a3 = (Matrix3f)object;
        return Float.compare(a3.m00, this.m00) == 0 && Float.compare(a3.m01, this.m01) == 0 && Float.compare(a3.m02, this.m02) == 0 && Float.compare(a3.m10, this.m10) == 0 && Float.compare(a3.m11, this.m11) == 0 && Float.compare(a3.m12, this.m12) == 0 && Float.compare(a3.m20, this.m20) == 0 && Float.compare(a3.m21, this.m21) == 0 && Float.compare(a3.m22, this.m22) == 0;
    }
    
    public int hashCode() {
        int integer2 = (this.m00 != 0.0f) ? Float.floatToIntBits(this.m00) : 0;
        integer2 = 31 * integer2 + ((this.m01 != 0.0f) ? Float.floatToIntBits(this.m01) : 0);
        integer2 = 31 * integer2 + ((this.m02 != 0.0f) ? Float.floatToIntBits(this.m02) : 0);
        integer2 = 31 * integer2 + ((this.m10 != 0.0f) ? Float.floatToIntBits(this.m10) : 0);
        integer2 = 31 * integer2 + ((this.m11 != 0.0f) ? Float.floatToIntBits(this.m11) : 0);
        integer2 = 31 * integer2 + ((this.m12 != 0.0f) ? Float.floatToIntBits(this.m12) : 0);
        integer2 = 31 * integer2 + ((this.m20 != 0.0f) ? Float.floatToIntBits(this.m20) : 0);
        integer2 = 31 * integer2 + ((this.m21 != 0.0f) ? Float.floatToIntBits(this.m21) : 0);
        integer2 = 31 * integer2 + ((this.m22 != 0.0f) ? Float.floatToIntBits(this.m22) : 0);
        return integer2;
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Matrix3f:\n");
        stringBuilder2.append(this.m00);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m01);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m02);
        stringBuilder2.append("\n");
        stringBuilder2.append(this.m10);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m11);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m12);
        stringBuilder2.append("\n");
        stringBuilder2.append(this.m20);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m21);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m22);
        stringBuilder2.append("\n");
        return stringBuilder2.toString();
    }
    
    public void set(final int integer1, final int integer2, final float float3) {
        if (integer1 == 0) {
            if (integer2 == 0) {
                this.m00 = float3;
            }
            else if (integer2 == 1) {
                this.m01 = float3;
            }
            else {
                this.m02 = float3;
            }
        }
        else if (integer1 == 1) {
            if (integer2 == 0) {
                this.m10 = float3;
            }
            else if (integer2 == 1) {
                this.m11 = float3;
            }
            else {
                this.m12 = float3;
            }
        }
        else if (integer2 == 0) {
            this.m20 = float3;
        }
        else if (integer2 == 1) {
            this.m21 = float3;
        }
        else {
            this.m22 = float3;
        }
    }
    
    public void mul(final Matrix3f a) {
        final float float3 = this.m00 * a.m00 + this.m01 * a.m10 + this.m02 * a.m20;
        final float float4 = this.m00 * a.m01 + this.m01 * a.m11 + this.m02 * a.m21;
        final float float5 = this.m00 * a.m02 + this.m01 * a.m12 + this.m02 * a.m22;
        final float float6 = this.m10 * a.m00 + this.m11 * a.m10 + this.m12 * a.m20;
        final float float7 = this.m10 * a.m01 + this.m11 * a.m11 + this.m12 * a.m21;
        final float float8 = this.m10 * a.m02 + this.m11 * a.m12 + this.m12 * a.m22;
        final float float9 = this.m20 * a.m00 + this.m21 * a.m10 + this.m22 * a.m20;
        final float float10 = this.m20 * a.m01 + this.m21 * a.m11 + this.m22 * a.m21;
        final float float11 = this.m20 * a.m02 + this.m21 * a.m12 + this.m22 * a.m22;
        this.m00 = float3;
        this.m01 = float4;
        this.m02 = float5;
        this.m10 = float6;
        this.m11 = float7;
        this.m12 = float8;
        this.m20 = float9;
        this.m21 = float10;
        this.m22 = float11;
    }
    
    static {
        G = 3.0f + 2.0f * (float)Math.sqrt(2.0);
        CS = (float)Math.cos(0.39269908169872414);
        SS = (float)Math.sin(0.39269908169872414);
        SQ2 = 1.0f / (float)Math.sqrt(2.0);
    }
}

package com.mojang.math;

public final class Matrix4f {
    protected float m00;
    protected float m01;
    protected float m02;
    protected float m03;
    protected float m10;
    protected float m11;
    protected float m12;
    protected float m13;
    protected float m20;
    protected float m21;
    protected float m22;
    protected float m23;
    protected float m30;
    protected float m31;
    protected float m32;
    protected float m33;
    
    public Matrix4f() {
    }
    
    public Matrix4f(final Matrix4f b) {
        this.m00 = b.m00;
        this.m01 = b.m01;
        this.m02 = b.m02;
        this.m03 = b.m03;
        this.m10 = b.m10;
        this.m11 = b.m11;
        this.m12 = b.m12;
        this.m13 = b.m13;
        this.m20 = b.m20;
        this.m21 = b.m21;
        this.m22 = b.m22;
        this.m23 = b.m23;
        this.m30 = b.m30;
        this.m31 = b.m31;
        this.m32 = b.m32;
        this.m33 = b.m33;
    }
    
    public Matrix4f(final Quaternion d) {
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
        this.m33 = 1.0f;
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
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Matrix4f b3 = (Matrix4f)object;
        return Float.compare(b3.m00, this.m00) == 0 && Float.compare(b3.m01, this.m01) == 0 && Float.compare(b3.m02, this.m02) == 0 && Float.compare(b3.m03, this.m03) == 0 && Float.compare(b3.m10, this.m10) == 0 && Float.compare(b3.m11, this.m11) == 0 && Float.compare(b3.m12, this.m12) == 0 && Float.compare(b3.m13, this.m13) == 0 && Float.compare(b3.m20, this.m20) == 0 && Float.compare(b3.m21, this.m21) == 0 && Float.compare(b3.m22, this.m22) == 0 && Float.compare(b3.m23, this.m23) == 0 && Float.compare(b3.m30, this.m30) == 0 && Float.compare(b3.m31, this.m31) == 0 && Float.compare(b3.m32, this.m32) == 0 && Float.compare(b3.m33, this.m33) == 0;
    }
    
    public int hashCode() {
        int integer2 = (this.m00 != 0.0f) ? Float.floatToIntBits(this.m00) : 0;
        integer2 = 31 * integer2 + ((this.m01 != 0.0f) ? Float.floatToIntBits(this.m01) : 0);
        integer2 = 31 * integer2 + ((this.m02 != 0.0f) ? Float.floatToIntBits(this.m02) : 0);
        integer2 = 31 * integer2 + ((this.m03 != 0.0f) ? Float.floatToIntBits(this.m03) : 0);
        integer2 = 31 * integer2 + ((this.m10 != 0.0f) ? Float.floatToIntBits(this.m10) : 0);
        integer2 = 31 * integer2 + ((this.m11 != 0.0f) ? Float.floatToIntBits(this.m11) : 0);
        integer2 = 31 * integer2 + ((this.m12 != 0.0f) ? Float.floatToIntBits(this.m12) : 0);
        integer2 = 31 * integer2 + ((this.m13 != 0.0f) ? Float.floatToIntBits(this.m13) : 0);
        integer2 = 31 * integer2 + ((this.m20 != 0.0f) ? Float.floatToIntBits(this.m20) : 0);
        integer2 = 31 * integer2 + ((this.m21 != 0.0f) ? Float.floatToIntBits(this.m21) : 0);
        integer2 = 31 * integer2 + ((this.m22 != 0.0f) ? Float.floatToIntBits(this.m22) : 0);
        integer2 = 31 * integer2 + ((this.m23 != 0.0f) ? Float.floatToIntBits(this.m23) : 0);
        integer2 = 31 * integer2 + ((this.m30 != 0.0f) ? Float.floatToIntBits(this.m30) : 0);
        integer2 = 31 * integer2 + ((this.m31 != 0.0f) ? Float.floatToIntBits(this.m31) : 0);
        integer2 = 31 * integer2 + ((this.m32 != 0.0f) ? Float.floatToIntBits(this.m32) : 0);
        integer2 = 31 * integer2 + ((this.m33 != 0.0f) ? Float.floatToIntBits(this.m33) : 0);
        return integer2;
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Matrix4f:\n");
        stringBuilder2.append(this.m00);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m01);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m02);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m03);
        stringBuilder2.append("\n");
        stringBuilder2.append(this.m10);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m11);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m12);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m13);
        stringBuilder2.append("\n");
        stringBuilder2.append(this.m20);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m21);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m22);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m23);
        stringBuilder2.append("\n");
        stringBuilder2.append(this.m30);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m31);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m32);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m33);
        stringBuilder2.append("\n");
        return stringBuilder2.toString();
    }
}

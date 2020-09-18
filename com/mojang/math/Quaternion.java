package com.mojang.math;

public final class Quaternion {
    public static final Quaternion ONE;
    private float i;
    private float j;
    private float k;
    private float r;
    
    public Quaternion(final float float1, final float float2, final float float3, final float float4) {
        this.i = float1;
        this.j = float2;
        this.k = float3;
        this.r = float4;
    }
    
    public Quaternion(final Vector3f g, float float2, final boolean boolean3) {
        if (boolean3) {
            float2 *= 0.017453292f;
        }
        final float float3 = sin(float2 / 2.0f);
        this.i = g.x() * float3;
        this.j = g.y() * float3;
        this.k = g.z() * float3;
        this.r = cos(float2 / 2.0f);
    }
    
    public Quaternion(final Quaternion d) {
        this.i = d.i;
        this.j = d.j;
        this.k = d.k;
        this.r = d.r;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Quaternion d3 = (Quaternion)object;
        return Float.compare(d3.i, this.i) == 0 && Float.compare(d3.j, this.j) == 0 && Float.compare(d3.k, this.k) == 0 && Float.compare(d3.r, this.r) == 0;
    }
    
    public int hashCode() {
        int integer2 = Float.floatToIntBits(this.i);
        integer2 = 31 * integer2 + Float.floatToIntBits(this.j);
        integer2 = 31 * integer2 + Float.floatToIntBits(this.k);
        integer2 = 31 * integer2 + Float.floatToIntBits(this.r);
        return integer2;
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Quaternion[").append(this.r()).append(" + ");
        stringBuilder2.append(this.i()).append("i + ");
        stringBuilder2.append(this.j()).append("j + ");
        stringBuilder2.append(this.k()).append("k]");
        return stringBuilder2.toString();
    }
    
    public float i() {
        return this.i;
    }
    
    public float j() {
        return this.j;
    }
    
    public float k() {
        return this.k;
    }
    
    public float r() {
        return this.r;
    }
    
    public void mul(final Quaternion d) {
        final float float3 = this.i();
        final float float4 = this.j();
        final float float5 = this.k();
        final float float6 = this.r();
        final float float7 = d.i();
        final float float8 = d.j();
        final float float9 = d.k();
        final float float10 = d.r();
        this.i = float6 * float7 + float3 * float10 + float4 * float9 - float5 * float8;
        this.j = float6 * float8 - float3 * float9 + float4 * float10 + float5 * float7;
        this.k = float6 * float9 + float3 * float8 - float4 * float7 + float5 * float10;
        this.r = float6 * float10 - float3 * float7 - float4 * float8 - float5 * float9;
    }
    
    public void conj() {
        this.i = -this.i;
        this.j = -this.j;
        this.k = -this.k;
    }
    
    private static float cos(final float float1) {
        return (float)Math.cos((double)float1);
    }
    
    private static float sin(final float float1) {
        return (float)Math.sin((double)float1);
    }
    
    static {
        ONE = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
    }
}

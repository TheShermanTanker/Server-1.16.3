package com.mojang.math;

import net.minecraft.world.phys.Vec3;

public final class Vector3f {
    public static Vector3f XN;
    public static Vector3f XP;
    public static Vector3f YN;
    public static Vector3f YP;
    public static Vector3f ZN;
    public static Vector3f ZP;
    private float x;
    private float y;
    private float z;
    
    public Vector3f() {
    }
    
    public Vector3f(final float float1, final float float2, final float float3) {
        this.x = float1;
        this.y = float2;
        this.z = float3;
    }
    
    public Vector3f(final Vec3 dck) {
        this((float)dck.x, (float)dck.y, (float)dck.z);
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Vector3f g3 = (Vector3f)object;
        return Float.compare(g3.x, this.x) == 0 && Float.compare(g3.y, this.y) == 0 && Float.compare(g3.z, this.z) == 0;
    }
    
    public int hashCode() {
        int integer2 = Float.floatToIntBits(this.x);
        integer2 = 31 * integer2 + Float.floatToIntBits(this.y);
        integer2 = 31 * integer2 + Float.floatToIntBits(this.z);
        return integer2;
    }
    
    public float x() {
        return this.x;
    }
    
    public float y() {
        return this.y;
    }
    
    public float z() {
        return this.z;
    }
    
    public void set(final float float1, final float float2, final float float3) {
        this.x = float1;
        this.y = float2;
        this.z = float3;
    }
    
    public void transform(final Quaternion d) {
        final Quaternion d2 = new Quaternion(d);
        d2.mul(new Quaternion(this.x(), this.y(), this.z(), 0.0f));
        final Quaternion d3 = new Quaternion(d);
        d3.conj();
        d2.mul(d3);
        this.set(d2.i(), d2.j(), d2.k());
    }
    
    public String toString() {
        return new StringBuilder().append("[").append(this.x).append(", ").append(this.y).append(", ").append(this.z).append("]").toString();
    }
    
    static {
        Vector3f.XN = new Vector3f(-1.0f, 0.0f, 0.0f);
        Vector3f.XP = new Vector3f(1.0f, 0.0f, 0.0f);
        Vector3f.YN = new Vector3f(0.0f, -1.0f, 0.0f);
        Vector3f.YP = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f.ZN = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f.ZP = new Vector3f(0.0f, 0.0f, 1.0f);
    }
}

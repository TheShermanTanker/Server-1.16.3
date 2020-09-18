package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.function.IntConsumer;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.Arrays;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.Map;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class Object2ByteOpenHashMap<K> extends AbstractObject2ByteMap<K> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient byte[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Object2ByteMap.FastEntrySet<K> entries;
    protected transient ObjectSet<K> keys;
    protected transient ByteCollection values;
    
    public Object2ByteOpenHashMap(final int expected, final float f) {
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        final int arraySize = HashCommon.arraySize(expected, f);
        this.n = arraySize;
        this.minN = arraySize;
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = (K[])new Object[this.n + 1];
        this.value = new byte[this.n + 1];
    }
    
    public Object2ByteOpenHashMap(final int expected) {
        this(expected, 0.75f);
    }
    
    public Object2ByteOpenHashMap() {
        this(16, 0.75f);
    }
    
    public Object2ByteOpenHashMap(final Map<? extends K, ? extends Byte> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Object2ByteOpenHashMap(final Map<? extends K, ? extends Byte> m) {
        this(m, 0.75f);
    }
    
    public Object2ByteOpenHashMap(final Object2ByteMap<K> m, final float f) {
        this(m.size(), f);
        this.putAll(m);
    }
    
    public Object2ByteOpenHashMap(final Object2ByteMap<K> m) {
        this(m, 0.75f);
    }
    
    public Object2ByteOpenHashMap(final K[] k, final byte[] v, final float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2ByteOpenHashMap(final K[] k, final byte[] v) {
        this(k, v, 0.75f);
    }
    
    private int realSize() {
        return this.containsNullKey ? (this.size - 1) : this.size;
    }
    
    private void ensureCapacity(final int capacity) {
        final int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    private void tryCapacity(final long capacity) {
        final int needed = (int)Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil((double)(capacity / this.f)))));
        if (needed > this.n) {
            this.rehash(needed);
        }
    }
    
    private byte removeEntry(final int pos) {
        final byte oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    private byte removeNullEntry() {
        this.containsNullKey = false;
        this.key[this.n] = null;
        final byte oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends Byte> m) {
        if (this.f <= 0.5) {
            this.ensureCapacity(m.size());
        }
        else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }
    
    private int find(final K k) {
        if (k == null) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return -(pos + 1);
        }
        if (k.equals(curr)) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return pos;
            }
        }
        return -(pos + 1);
    }
    
    private void insert(final int pos, final K k, final byte v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }
    
    public byte put(final K k, final byte v) {
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        final byte oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    private byte addToValue(final int pos, final byte incr) {
        final byte oldValue = this.value[pos];
        this.value[pos] = (byte)(oldValue + incr);
        return oldValue;
    }
    
    public byte addTo(final K k, final byte incr) {
        int pos;
        if (k == null) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) != null) {
                if (curr.equals(k)) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (curr.equals(k)) {
                        return this.addToValue(pos, incr);
                    }
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = (byte)(this.defRetValue + incr);
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
    }
    
    protected final void shiftKeys(int pos) {
        final K[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            K curr;
            while ((curr = key[pos]) != null) {
                final int slot = HashCommon.mix(curr.hashCode()) & this.mask;
                Label_0090: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0090;
                        }
                        if (slot > pos) {
                            break Label_0090;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0090;
                    }
                    pos = (pos + 1 & this.mask);
                    continue;
                }
                key[last] = curr;
                this.value[last] = this.value[pos];
                continue Label_0006;
            }
            break;
        }
        key[last] = null;
    }
    
    public byte removeByte(final Object k) {
        if (k == null) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (k.equals(curr)) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k.equals(curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public byte getByte(final Object k) {
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return this.defRetValue;
        }
        if (k.equals(curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final Object k) {
        if (k == null) {
            return this.containsNullKey;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return false;
        }
        if (k.equals(curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsValue(final byte v) {
        final byte[] value = this.value;
        final K[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] != null && value[i] == v) {
                return true;
            }
        }
        return false;
    }
    
    public byte getOrDefault(final Object k, final byte defaultValue) {
        if (k == null) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
            return defaultValue;
        }
        if (k.equals(curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (k.equals(curr)) {
                return this.value[pos];
            }
        }
        return defaultValue;
    }
    
    public byte putIfAbsent(final K k, final byte v) {
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }
    
    public boolean remove(final Object k, final byte v) {
        if (k == null) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & this.mask)]) == null) {
                return false;
            }
            if (k.equals(curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (k.equals(curr) && v == this.value[pos]) {
                    this.removeEntry(pos);
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean replace(final K k, final byte oldValue, final byte v) {
        final int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }
    
    public byte replace(final K k, final byte v) {
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final byte oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }
    
    public byte computeByteIfAbsent(final K k, final ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        final byte newValue = SafeMath.safeIntToByte(mappingFunction.applyAsInt(k));
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }
    
    public byte computeByteIfPresent(final K k, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        final Byte newValue = (Byte)remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (k == null) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public byte computeByte(final K k, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        final Byte newValue = (Byte)remappingFunction.apply(k, ((pos >= 0) ? Byte.valueOf(this.value[pos]) : null));
        if (newValue == null) {
            if (pos >= 0) {
                if (k == null) {
                    this.removeNullEntry();
                }
                else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        final byte newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        return this.value[pos] = newVal;
    }
    
    public byte mergeByte(final K k, final byte v, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        final Byte newValue = (Byte)remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (k == null) {
                this.removeNullEntry();
            }
            else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        return this.value[pos] = newValue;
    }
    
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill((Object[])this.key, null);
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public Object2ByteMap.FastEntrySet<K> object2ByteEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ObjectSet<K> keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ByteCollection values() {
        if (this.values == null) {
            this.values = new AbstractByteCollection() {
                @Override
                public ByteIterator iterator() {
                    return new ValueIterator();
                }
                
                public int size() {
                    return Object2ByteOpenHashMap.this.size;
                }
                
                @Override
                public boolean contains(final byte v) {
                    return Object2ByteOpenHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Object2ByteOpenHashMap.this.clear();
                }
                
                public void forEach(final IntConsumer consumer) {
                    if (Object2ByteOpenHashMap.this.containsNullKey) {
                        consumer.accept((int)Object2ByteOpenHashMap.this.value[Object2ByteOpenHashMap.this.n]);
                    }
                    int pos = Object2ByteOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Object2ByteOpenHashMap.this.key[pos] != null) {
                            consumer.accept((int)Object2ByteOpenHashMap.this.value[pos]);
                        }
                    }
                }
            };
        }
        return this.values;
    }
    
    public boolean trim() {
        final int l = HashCommon.arraySize(this.size, this.f);
        if (l >= this.n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        }
        catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }
    
    public boolean trim(final int n) {
        final int l = HashCommon.nextPowerOfTwo((int)Math.ceil((double)(n / this.f)));
        if (l >= n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        }
        catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }
    
    protected void rehash(final int newN) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.key:[Ljava/lang/Object;
        //     4: astore_2        /* key */
        //     5: aload_0         /* this */
        //     6: getfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.value:[B
        //     9: astore_3        /* value */
        //    10: iload_1         /* newN */
        //    11: iconst_1       
        //    12: isub           
        //    13: istore          mask
        //    15: iload_1         /* newN */
        //    16: iconst_1       
        //    17: iadd           
        //    18: anewarray       Ljava/lang/Object;
        //    21: astore          newKey
        //    23: iload_1         /* newN */
        //    24: iconst_1       
        //    25: iadd           
        //    26: newarray        B
        //    28: astore          newValue
        //    30: aload_0         /* this */
        //    31: getfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.n:I
        //    34: istore          i
        //    36: aload_0         /* this */
        //    37: invokespecial   it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.realSize:()I
        //    40: istore          j
        //    42: iload           j
        //    44: iinc            j, -1
        //    47: ifeq            125
        //    50: aload_2         /* key */
        //    51: iinc            i, -1
        //    54: iload           i
        //    56: aaload         
        //    57: ifnonnull       63
        //    60: goto            50
        //    63: aload           newKey
        //    65: aload_2         /* key */
        //    66: iload           i
        //    68: aaload         
        //    69: invokevirtual   java/lang/Object.hashCode:()I
        //    72: invokestatic    it/unimi/dsi/fastutil/HashCommon.mix:(I)I
        //    75: iload           mask
        //    77: iand           
        //    78: dup            
        //    79: istore          pos
        //    81: aaload         
        //    82: ifnull          104
        //    85: aload           newKey
        //    87: iload           pos
        //    89: iconst_1       
        //    90: iadd           
        //    91: iload           mask
        //    93: iand           
        //    94: dup            
        //    95: istore          pos
        //    97: aaload         
        //    98: ifnull          104
        //   101: goto            85
        //   104: aload           newKey
        //   106: iload           pos
        //   108: aload_2         /* key */
        //   109: iload           i
        //   111: aaload         
        //   112: aastore        
        //   113: aload           newValue
        //   115: iload           pos
        //   117: aload_3         /* value */
        //   118: iload           i
        //   120: baload         
        //   121: bastore        
        //   122: goto            42
        //   125: aload           newValue
        //   127: iload_1         /* newN */
        //   128: aload_3         /* value */
        //   129: aload_0         /* this */
        //   130: getfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.n:I
        //   133: baload         
        //   134: bastore        
        //   135: aload_0         /* this */
        //   136: iload_1         /* newN */
        //   137: putfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.n:I
        //   140: aload_0         /* this */
        //   141: iload           mask
        //   143: putfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.mask:I
        //   146: aload_0         /* this */
        //   147: aload_0         /* this */
        //   148: getfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.n:I
        //   151: aload_0         /* this */
        //   152: getfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.f:F
        //   155: invokestatic    it/unimi/dsi/fastutil/HashCommon.maxFill:(IF)I
        //   158: putfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.maxFill:I
        //   161: aload_0         /* this */
        //   162: aload           newKey
        //   164: putfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.key:[Ljava/lang/Object;
        //   167: aload_0         /* this */
        //   168: aload           newValue
        //   170: putfield        it/unimi/dsi/fastutil/objects/Object2ByteOpenHashMap.value:[B
        //   173: return         
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  newN  
        //    StackMapTable: 00 06 FF 00 2A 00 0A 07 00 02 01 07 00 A9 07 00 AA 01 07 00 A9 07 00 AA 01 00 01 00 00 07 0C FF 00 15 00 0A 07 00 02 01 07 00 A9 07 00 AA 01 07 00 A9 07 00 AA 01 01 01 00 00 12 F9 00 14
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.AstOptimizer$MakeAssignmentExpressionsOptimization.run(AstOptimizer.java:2912)
        //     at com.strobel.decompiler.ast.AstOptimizer.runOptimization(AstOptimizer.java:3876)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:214)
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
    
    public Object2ByteOpenHashMap<K> clone() {
        Object2ByteOpenHashMap<K> c;
        try {
            c = (Object2ByteOpenHashMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.containsNullKey = this.containsNullKey;
        c.key = this.key.clone();
        c.value = this.value.clone();
        return c;
    }
    
    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        int t = 0;
        while (j-- != 0) {
            while (this.key[i] == null) {
                ++i;
            }
            if (this != this.key[i]) {
                t = this.key[i].hashCode();
            }
            t ^= this.value[i];
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += this.value[this.n];
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final K[] key = this.key;
        final byte[] value = this.value;
        final MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            final int e = i.nextEntry();
            s.writeObject(key[e]);
            s.writeByte((int)value[e]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        final Object[] key2 = new Object[this.n + 1];
        this.key = (K[])key2;
        final K[] key = (K[])key2;
        final byte[] value2 = new byte[this.n + 1];
        this.value = value2;
        final byte[] value = value2;
        int i = this.size;
        while (i-- != 0) {
            final K k = (K)s.readObject();
            final byte v = s.readByte();
            int pos;
            if (k == null) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(k.hashCode()) & this.mask); key[pos] != null; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Object2ByteMap.Entry<K>, Map.Entry<K, Byte> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public K getKey() {
            return Object2ByteOpenHashMap.this.key[this.index];
        }
        
        public byte getByteValue() {
            return Object2ByteOpenHashMap.this.value[this.index];
        }
        
        public byte setValue(final byte v) {
            final byte oldValue = Object2ByteOpenHashMap.this.value[this.index];
            Object2ByteOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Byte getValue() {
            return Object2ByteOpenHashMap.this.value[this.index];
        }
        
        @Deprecated
        public Byte setValue(final Byte v) {
            return this.setValue((byte)v);
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<K, Byte> e = (Map.Entry<K, Byte>)o;
            return Objects.equals(Object2ByteOpenHashMap.this.key[this.index], e.getKey()) && Object2ByteOpenHashMap.this.value[this.index] == (byte)e.getValue();
        }
        
        public int hashCode() {
            return ((Object2ByteOpenHashMap.this.key[this.index] == null) ? 0 : Object2ByteOpenHashMap.this.key[this.index].hashCode()) ^ Object2ByteOpenHashMap.this.value[this.index];
        }
        
        public String toString() {
            return new StringBuilder().append(Object2ByteOpenHashMap.this.key[this.index]).append("=>").append((int)Object2ByteOpenHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ObjectArrayList<K> wrapped;
        
        private MapIterator() {
            this.pos = Object2ByteOpenHashMap.this.n;
            this.last = -1;
            this.c = Object2ByteOpenHashMap.this.size;
            this.mustReturnNullKey = Object2ByteOpenHashMap.this.containsNullKey;
        }
        
        public boolean hasNext() {
            return this.c != 0;
        }
        
        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                return this.last = Object2ByteOpenHashMap.this.n;
            }
            final K[] key = Object2ByteOpenHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(k.hashCode()) & Object2ByteOpenHashMap.this.mask); !k.equals(key[p]); p = (p + 1 & Object2ByteOpenHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final K[] key = Object2ByteOpenHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Object2ByteOpenHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(curr.hashCode()) & Object2ByteOpenHashMap.this.mask;
                    Label_0102: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0102;
                            }
                            if (slot > pos) {
                                break Label_0102;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0102;
                        }
                        pos = (pos + 1 & Object2ByteOpenHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ObjectArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Object2ByteOpenHashMap.this.value[last] = Object2ByteOpenHashMap.this.value[pos];
                    continue Label_0009;
                }
                break;
            }
            key[last] = null;
        }
        
        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Object2ByteOpenHashMap.this.n) {
                Object2ByteOpenHashMap.this.containsNullKey = false;
                Object2ByteOpenHashMap.this.key[Object2ByteOpenHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Object2ByteOpenHashMap.this.removeByte(this.wrapped.set(-this.pos - 1, null));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Object2ByteOpenHashMap this$0 = Object2ByteOpenHashMap.this;
            --this$0.size;
            this.last = -1;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
    }
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Object2ByteMap.Entry<K>> {
        private MapEntry entry;
        
        public MapEntry next() {
            return this.entry = new MapEntry(this.nextEntry());
        }
        
        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2ByteMap.Entry<K>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Object2ByteMap.Entry<K>> implements Object2ByteMap.FastEntrySet<K> {
        @Override
        public ObjectIterator<Object2ByteMap.Entry<K>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Object2ByteMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                return false;
            }
            final K k = (K)e.getKey();
            final byte v = (byte)e.getValue();
            if (k == null) {
                return Object2ByteOpenHashMap.this.containsNullKey && Object2ByteOpenHashMap.this.value[Object2ByteOpenHashMap.this.n] == v;
            }
            final K[] key = Object2ByteOpenHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & Object2ByteOpenHashMap.this.mask)]) == null) {
                return false;
            }
            if (k.equals(curr)) {
                return Object2ByteOpenHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Object2ByteOpenHashMap.this.mask)]) != null) {
                if (k.equals(curr)) {
                    return Object2ByteOpenHashMap.this.value[pos] == v;
                }
            }
            return false;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                return false;
            }
            final K k = (K)e.getKey();
            final byte v = (byte)e.getValue();
            if (k == null) {
                if (Object2ByteOpenHashMap.this.containsNullKey && Object2ByteOpenHashMap.this.value[Object2ByteOpenHashMap.this.n] == v) {
                    Object2ByteOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Object2ByteOpenHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(k.hashCode()) & Object2ByteOpenHashMap.this.mask)]) == null) {
                    return false;
                }
                if (!curr.equals(k)) {
                    while ((curr = key[pos = (pos + 1 & Object2ByteOpenHashMap.this.mask)]) != null) {
                        if (curr.equals(k) && Object2ByteOpenHashMap.this.value[pos] == v) {
                            Object2ByteOpenHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Object2ByteOpenHashMap.this.value[pos] == v) {
                    Object2ByteOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Object2ByteOpenHashMap.this.size;
        }
        
        public void clear() {
            Object2ByteOpenHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Object2ByteMap.Entry<K>> consumer) {
            if (Object2ByteOpenHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Object2ByteOpenHashMap.this.key[Object2ByteOpenHashMap.this.n], Object2ByteOpenHashMap.this.value[Object2ByteOpenHashMap.this.n]));
            }
            int pos = Object2ByteOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Object2ByteOpenHashMap.this.key[pos] != null) {
                    consumer.accept(new BasicEntry(Object2ByteOpenHashMap.this.key[pos], Object2ByteOpenHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Object2ByteMap.Entry<K>> consumer) {
            final BasicEntry<K> entry = new BasicEntry<K>();
            if (Object2ByteOpenHashMap.this.containsNullKey) {
                entry.key = Object2ByteOpenHashMap.this.key[Object2ByteOpenHashMap.this.n];
                entry.value = Object2ByteOpenHashMap.this.value[Object2ByteOpenHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Object2ByteOpenHashMap.this.n;
            while (pos-- != 0) {
                if (Object2ByteOpenHashMap.this.key[pos] != null) {
                    entry.key = Object2ByteOpenHashMap.this.key[pos];
                    entry.value = Object2ByteOpenHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectIterator<K> {
        public KeyIterator() {
        }
        
        public K next() {
            return Object2ByteOpenHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractObjectSet<K> {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final Consumer<? super K> consumer) {
            if (Object2ByteOpenHashMap.this.containsNullKey) {
                consumer.accept(Object2ByteOpenHashMap.this.key[Object2ByteOpenHashMap.this.n]);
            }
            int pos = Object2ByteOpenHashMap.this.n;
            while (pos-- != 0) {
                final K k = Object2ByteOpenHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Object2ByteOpenHashMap.this.size;
        }
        
        public boolean contains(final Object k) {
            return Object2ByteOpenHashMap.this.containsKey(k);
        }
        
        public boolean remove(final Object k) {
            final int oldSize = Object2ByteOpenHashMap.this.size;
            Object2ByteOpenHashMap.this.removeByte(k);
            return Object2ByteOpenHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Object2ByteOpenHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ByteIterator {
        public ValueIterator() {
        }
        
        @Override
        public byte nextByte() {
            return Object2ByteOpenHashMap.this.value[this.nextEntry()];
        }
    }
}

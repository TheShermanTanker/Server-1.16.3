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

public class Reference2ByteOpenCustomHashMap<K> extends AbstractReference2ByteMap<K> implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient K[] key;
    protected transient byte[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected Strategy<K> strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Reference2ByteMap.FastEntrySet<K> entries;
    protected transient ReferenceSet<K> keys;
    protected transient ByteCollection values;
    
    public Reference2ByteOpenCustomHashMap(final int expected, final float f, final Strategy<K> strategy) {
        this.strategy = strategy;
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
    
    public Reference2ByteOpenCustomHashMap(final int expected, final Strategy<K> strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public Reference2ByteOpenCustomHashMap(final Strategy<K> strategy) {
        this(16, 0.75f, strategy);
    }
    
    public Reference2ByteOpenCustomHashMap(final Map<? extends K, ? extends Byte> m, final float f, final Strategy<K> strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Reference2ByteOpenCustomHashMap(final Map<? extends K, ? extends Byte> m, final Strategy<K> strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Reference2ByteOpenCustomHashMap(final Reference2ByteMap<K> m, final float f, final Strategy<K> strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }
    
    public Reference2ByteOpenCustomHashMap(final Reference2ByteMap<K> m, final Strategy<K> strategy) {
        this(m, 0.75f, strategy);
    }
    
    public Reference2ByteOpenCustomHashMap(final K[] k, final byte[] v, final float f, final Strategy<K> strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Reference2ByteOpenCustomHashMap(final K[] k, final byte[] v, final Strategy<K> strategy) {
        this(k, v, 0.75f, (Strategy<Object>)strategy);
    }
    
    public Strategy<K> strategy() {
        return this.strategy;
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
        if (this.strategy.equals(k, null)) {
            return this.containsNullKey ? this.n : (-(this.n + 1));
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == null) {
            return -(pos + 1);
        }
        if (this.strategy.equals(k, curr)) {
            return pos;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (this.strategy.equals(k, curr)) {
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
        if (this.strategy.equals(k, null)) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        }
        else {
            final K[] key = this.key;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != null) {
                if (this.strategy.equals(curr, k)) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                    if (this.strategy.equals(curr, k)) {
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
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                Label_0097: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0097;
                        }
                        if (slot > pos) {
                            break Label_0097;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0097;
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
        if (this.strategy.equals((K)k, null)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        else {
            final K[] key = this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
                return this.defRetValue;
            }
            if (this.strategy.equals((K)k, curr)) {
                return this.removeEntry(pos);
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (this.strategy.equals((K)k, curr)) {
                    return this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
    }
    
    public byte getByte(final Object k) {
        if (this.strategy.equals((K)k, null)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
            return this.defRetValue;
        }
        if (this.strategy.equals((K)k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (this.strategy.equals((K)k, curr)) {
                return this.value[pos];
            }
        }
        return this.defRetValue;
    }
    
    @Override
    public boolean containsKey(final Object k) {
        if (this.strategy.equals((K)k, null)) {
            return this.containsNullKey;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
            return false;
        }
        if (this.strategy.equals((K)k, curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (this.strategy.equals((K)k, curr)) {
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
        if (this.strategy.equals((K)k, null)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        final K[] key = this.key;
        int pos;
        K curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
            return defaultValue;
        }
        if (this.strategy.equals((K)k, curr)) {
            return this.value[pos];
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
            if (this.strategy.equals((K)k, curr)) {
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
        if (this.strategy.equals((K)k, null)) {
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
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode((K)k)) & this.mask)]) == null) {
                return false;
            }
            if (this.strategy.equals((K)k, curr) && v == this.value[pos]) {
                this.removeEntry(pos);
                return true;
            }
            while ((curr = key[pos = (pos + 1 & this.mask)]) != null) {
                if (this.strategy.equals((K)k, curr) && v == this.value[pos]) {
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
            if (this.strategy.equals(k, null)) {
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
                if (this.strategy.equals(k, null)) {
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
            if (this.strategy.equals(k, null)) {
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
    
    public Reference2ByteMap.FastEntrySet<K> reference2ByteEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }
    
    @Override
    public ReferenceSet<K> keySet() {
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
                    return Reference2ByteOpenCustomHashMap.this.size;
                }
                
                @Override
                public boolean contains(final byte v) {
                    return Reference2ByteOpenCustomHashMap.this.containsValue(v);
                }
                
                public void clear() {
                    Reference2ByteOpenCustomHashMap.this.clear();
                }
                
                public void forEach(final IntConsumer consumer) {
                    if (Reference2ByteOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept((int)Reference2ByteOpenCustomHashMap.this.value[Reference2ByteOpenCustomHashMap.this.n]);
                    }
                    int pos = Reference2ByteOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Reference2ByteOpenCustomHashMap.this.key[pos] != null) {
                            consumer.accept((int)Reference2ByteOpenCustomHashMap.this.value[pos]);
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
        final K[] key = this.key;
        final byte[] value = this.value;
        final int mask = newN - 1;
        final K[] newKey = (K[])new Object[newN + 1];
        final byte[] newValue = new byte[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == null) {}
            int pos;
            if (newKey[pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask)] != null) {
                while (newKey[pos = (pos + 1 & mask)] != null) {}
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
        }
        newValue[newN] = value[this.n];
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }
    
    public Reference2ByteOpenCustomHashMap<K> clone() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   java/lang/Object.clone:()Ljava/lang/Object;
        //     4: checkcast       Lit/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap;
        //     7: astore_1        /* c */
        //     8: goto            20
        //    11: astore_2        /* cantHappen */
        //    12: new             Ljava/lang/InternalError;
        //    15: dup            
        //    16: invokespecial   java/lang/InternalError.<init>:()V
        //    19: athrow         
        //    20: aload_1         /* c */
        //    21: aconst_null    
        //    22: putfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.keys:Lit/unimi/dsi/fastutil/objects/ReferenceSet;
        //    25: aload_1         /* c */
        //    26: aconst_null    
        //    27: putfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.values:Lit/unimi/dsi/fastutil/bytes/ByteCollection;
        //    30: aload_1         /* c */
        //    31: aconst_null    
        //    32: putfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.entries:Lit/unimi/dsi/fastutil/objects/Reference2ByteMap$FastEntrySet;
        //    35: aload_1         /* c */
        //    36: aload_0         /* this */
        //    37: getfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.containsNullKey:Z
        //    40: putfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.containsNullKey:Z
        //    43: aload_1         /* c */
        //    44: aload_0         /* this */
        //    45: getfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.key:[Ljava/lang/Object;
        //    48: invokevirtual   [java/lang/Object.clone:()Ljava/lang/Object;
        //    51: checkcast       [Ljava/lang/Object;
        //    54: putfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.key:[Ljava/lang/Object;
        //    57: aload_1         /* c */
        //    58: aload_0         /* this */
        //    59: getfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.value:[B
        //    62: invokevirtual   [B.clone:()Ljava/lang/Object;
        //    65: checkcast       [B
        //    68: putfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.value:[B
        //    71: aload_1         /* c */
        //    72: aload_0         /* this */
        //    73: getfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/Hash$Strategy;
        //    76: putfield        it/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap.strategy:Lit/unimi/dsi/fastutil/Hash$Strategy;
        //    79: aload_1         /* c */
        //    80: areturn        
        //    Signature:
        //  ()Lit/unimi/dsi/fastutil/objects/Reference2ByteOpenCustomHashMap<TK;>;
        //    StackMapTable: 00 02 4B 07 01 92 FC 00 08 07 00 02
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                  
        //  -----  -----  -----  -----  --------------------------------------
        //  0      8      11     20     Ljava/lang/CloneNotSupportedException;
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1061)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
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
                t = this.strategy.hashCode(this.key[i]);
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
            if (this.strategy.equals(k, null)) {
                pos = this.n;
                this.containsNullKey = true;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); key[pos] != null; pos = (pos + 1 & this.mask)) {}
            }
            key[pos] = k;
            value[pos] = v;
        }
    }
    
    private void checkTable() {
    }
    
    final class MapEntry implements Reference2ByteMap.Entry<K>, Map.Entry<K, Byte> {
        int index;
        
        MapEntry(final int index) {
            this.index = index;
        }
        
        MapEntry() {
        }
        
        public K getKey() {
            return Reference2ByteOpenCustomHashMap.this.key[this.index];
        }
        
        public byte getByteValue() {
            return Reference2ByteOpenCustomHashMap.this.value[this.index];
        }
        
        public byte setValue(final byte v) {
            final byte oldValue = Reference2ByteOpenCustomHashMap.this.value[this.index];
            Reference2ByteOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }
        
        @Deprecated
        public Byte getValue() {
            return Reference2ByteOpenCustomHashMap.this.value[this.index];
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
            return Reference2ByteOpenCustomHashMap.this.strategy.equals(Reference2ByteOpenCustomHashMap.this.key[this.index], (K)e.getKey()) && Reference2ByteOpenCustomHashMap.this.value[this.index] == (byte)e.getValue();
        }
        
        public int hashCode() {
            return Reference2ByteOpenCustomHashMap.this.strategy.hashCode(Reference2ByteOpenCustomHashMap.this.key[this.index]) ^ Reference2ByteOpenCustomHashMap.this.value[this.index];
        }
        
        public String toString() {
            return new StringBuilder().append(Reference2ByteOpenCustomHashMap.this.key[this.index]).append("=>").append((int)Reference2ByteOpenCustomHashMap.this.value[this.index]).toString();
        }
    }
    
    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ReferenceArrayList<K> wrapped;
        
        private MapIterator() {
            this.pos = Reference2ByteOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Reference2ByteOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Reference2ByteOpenCustomHashMap.this.containsNullKey;
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
                return this.last = Reference2ByteOpenCustomHashMap.this.n;
            }
            final K[] key = Reference2ByteOpenCustomHashMap.this.key;
            while (--this.pos >= 0) {
                if (key[this.pos] != null) {
                    return this.last = this.pos;
                }
            }
            this.last = Integer.MIN_VALUE;
            K k;
            int p;
            for (k = this.wrapped.get(-this.pos - 1), p = (HashCommon.mix(Reference2ByteOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2ByteOpenCustomHashMap.this.mask); !Reference2ByteOpenCustomHashMap.this.strategy.equals(k, key[p]); p = (p + 1 & Reference2ByteOpenCustomHashMap.this.mask)) {}
            return p;
        }
        
        private void shiftKeys(int pos) {
            final K[] key = Reference2ByteOpenCustomHashMap.this.key;
            int last = 0;
        Label_0009:
            while (true) {
                pos = ((last = pos) + 1 & Reference2ByteOpenCustomHashMap.this.mask);
                K curr;
                while ((curr = key[pos]) != null) {
                    final int slot = HashCommon.mix(Reference2ByteOpenCustomHashMap.this.strategy.hashCode(curr)) & Reference2ByteOpenCustomHashMap.this.mask;
                    Label_0112: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0112;
                            }
                            if (slot > pos) {
                                break Label_0112;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0112;
                        }
                        pos = (pos + 1 & Reference2ByteOpenCustomHashMap.this.mask);
                        continue;
                    }
                    if (pos < last) {
                        if (this.wrapped == null) {
                            this.wrapped = new ReferenceArrayList<K>(2);
                        }
                        this.wrapped.add(key[pos]);
                    }
                    key[last] = curr;
                    Reference2ByteOpenCustomHashMap.this.value[last] = Reference2ByteOpenCustomHashMap.this.value[pos];
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
            if (this.last == Reference2ByteOpenCustomHashMap.this.n) {
                Reference2ByteOpenCustomHashMap.this.containsNullKey = false;
                Reference2ByteOpenCustomHashMap.this.key[Reference2ByteOpenCustomHashMap.this.n] = null;
            }
            else {
                if (this.pos < 0) {
                    Reference2ByteOpenCustomHashMap.this.removeByte(this.wrapped.set(-this.pos - 1, null));
                    this.last = -1;
                    return;
                }
                this.shiftKeys(this.last);
            }
            final Reference2ByteOpenCustomHashMap this$0 = Reference2ByteOpenCustomHashMap.this;
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
    
    private class EntryIterator extends MapIterator implements ObjectIterator<Reference2ByteMap.Entry<K>> {
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
    
    private class FastEntryIterator extends MapIterator implements ObjectIterator<Reference2ByteMap.Entry<K>> {
        private final MapEntry entry;
        
        private FastEntryIterator() {
            this.entry = new MapEntry();
        }
        
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }
    
    private final class MapEntrySet extends AbstractObjectSet<Reference2ByteMap.Entry<K>> implements Reference2ByteMap.FastEntrySet<K> {
        @Override
        public ObjectIterator<Reference2ByteMap.Entry<K>> iterator() {
            return new EntryIterator();
        }
        
        @Override
        public ObjectIterator<Reference2ByteMap.Entry<K>> fastIterator() {
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
            if (Reference2ByteOpenCustomHashMap.this.strategy.equals(k, null)) {
                return Reference2ByteOpenCustomHashMap.this.containsNullKey && Reference2ByteOpenCustomHashMap.this.value[Reference2ByteOpenCustomHashMap.this.n] == v;
            }
            final K[] key = Reference2ByteOpenCustomHashMap.this.key;
            int pos;
            K curr;
            if ((curr = key[pos = (HashCommon.mix(Reference2ByteOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2ByteOpenCustomHashMap.this.mask)]) == null) {
                return false;
            }
            if (Reference2ByteOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Reference2ByteOpenCustomHashMap.this.value[pos] == v;
            }
            while ((curr = key[pos = (pos + 1 & Reference2ByteOpenCustomHashMap.this.mask)]) != null) {
                if (Reference2ByteOpenCustomHashMap.this.strategy.equals(k, curr)) {
                    return Reference2ByteOpenCustomHashMap.this.value[pos] == v;
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
            if (Reference2ByteOpenCustomHashMap.this.strategy.equals(k, null)) {
                if (Reference2ByteOpenCustomHashMap.this.containsNullKey && Reference2ByteOpenCustomHashMap.this.value[Reference2ByteOpenCustomHashMap.this.n] == v) {
                    Reference2ByteOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            else {
                final K[] key = Reference2ByteOpenCustomHashMap.this.key;
                int pos;
                K curr;
                if ((curr = key[pos = (HashCommon.mix(Reference2ByteOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2ByteOpenCustomHashMap.this.mask)]) == null) {
                    return false;
                }
                if (!Reference2ByteOpenCustomHashMap.this.strategy.equals(curr, k)) {
                    while ((curr = key[pos = (pos + 1 & Reference2ByteOpenCustomHashMap.this.mask)]) != null) {
                        if (Reference2ByteOpenCustomHashMap.this.strategy.equals(curr, k) && Reference2ByteOpenCustomHashMap.this.value[pos] == v) {
                            Reference2ByteOpenCustomHashMap.this.removeEntry(pos);
                            return true;
                        }
                    }
                    return false;
                }
                if (Reference2ByteOpenCustomHashMap.this.value[pos] == v) {
                    Reference2ByteOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
        }
        
        public int size() {
            return Reference2ByteOpenCustomHashMap.this.size;
        }
        
        public void clear() {
            Reference2ByteOpenCustomHashMap.this.clear();
        }
        
        public void forEach(final Consumer<? super Reference2ByteMap.Entry<K>> consumer) {
            if (Reference2ByteOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new BasicEntry(Reference2ByteOpenCustomHashMap.this.key[Reference2ByteOpenCustomHashMap.this.n], Reference2ByteOpenCustomHashMap.this.value[Reference2ByteOpenCustomHashMap.this.n]));
            }
            int pos = Reference2ByteOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Reference2ByteOpenCustomHashMap.this.key[pos] != null) {
                    consumer.accept(new BasicEntry(Reference2ByteOpenCustomHashMap.this.key[pos], Reference2ByteOpenCustomHashMap.this.value[pos]));
                }
            }
        }
        
        @Override
        public void fastForEach(final Consumer<? super Reference2ByteMap.Entry<K>> consumer) {
            final BasicEntry<K> entry = new BasicEntry<K>();
            if (Reference2ByteOpenCustomHashMap.this.containsNullKey) {
                entry.key = Reference2ByteOpenCustomHashMap.this.key[Reference2ByteOpenCustomHashMap.this.n];
                entry.value = Reference2ByteOpenCustomHashMap.this.value[Reference2ByteOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Reference2ByteOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Reference2ByteOpenCustomHashMap.this.key[pos] != null) {
                    entry.key = Reference2ByteOpenCustomHashMap.this.key[pos];
                    entry.value = Reference2ByteOpenCustomHashMap.this.value[pos];
                    consumer.accept(entry);
                }
            }
        }
    }
    
    private final class KeyIterator extends MapIterator implements ObjectIterator<K> {
        public KeyIterator() {
        }
        
        public K next() {
            return Reference2ByteOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }
    
    private final class KeySet extends AbstractReferenceSet<K> {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }
        
        public void forEach(final Consumer<? super K> consumer) {
            if (Reference2ByteOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Reference2ByteOpenCustomHashMap.this.key[Reference2ByteOpenCustomHashMap.this.n]);
            }
            int pos = Reference2ByteOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                final K k = Reference2ByteOpenCustomHashMap.this.key[pos];
                if (k != null) {
                    consumer.accept(k);
                }
            }
        }
        
        public int size() {
            return Reference2ByteOpenCustomHashMap.this.size;
        }
        
        public boolean contains(final Object k) {
            return Reference2ByteOpenCustomHashMap.this.containsKey(k);
        }
        
        public boolean remove(final Object k) {
            final int oldSize = Reference2ByteOpenCustomHashMap.this.size;
            Reference2ByteOpenCustomHashMap.this.removeByte(k);
            return Reference2ByteOpenCustomHashMap.this.size != oldSize;
        }
        
        public void clear() {
            Reference2ByteOpenCustomHashMap.this.clear();
        }
    }
    
    private final class ValueIterator extends MapIterator implements ByteIterator {
        public ValueIterator() {
        }
        
        @Override
        public byte nextByte() {
            return Reference2ByteOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }
}

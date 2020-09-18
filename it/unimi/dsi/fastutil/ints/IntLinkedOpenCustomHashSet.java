package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.Hash;
import java.io.Serializable;

public class IntLinkedOpenCustomHashSet extends AbstractIntSortedSet implements Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient int mask;
    protected transient boolean containsNull;
    protected IntHash.Strategy strategy;
    protected transient int first;
    protected transient int last;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    
    public IntLinkedOpenCustomHashSet(final int expected, final float f, final IntHash.Strategy strategy) {
        this.first = -1;
        this.last = -1;
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
        this.key = new int[this.n + 1];
        this.link = new long[this.n + 1];
    }
    
    public IntLinkedOpenCustomHashSet(final int expected, final IntHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }
    
    public IntLinkedOpenCustomHashSet(final IntHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }
    
    public IntLinkedOpenCustomHashSet(final Collection<? extends Integer> c, final float f, final IntHash.Strategy strategy) {
        this(c.size(), f, strategy);
        this.addAll(c);
    }
    
    public IntLinkedOpenCustomHashSet(final Collection<? extends Integer> c, final IntHash.Strategy strategy) {
        this(c, 0.75f, strategy);
    }
    
    public IntLinkedOpenCustomHashSet(final IntCollection c, final float f, final IntHash.Strategy strategy) {
        this(c.size(), f, strategy);
        this.addAll(c);
    }
    
    public IntLinkedOpenCustomHashSet(final IntCollection c, final IntHash.Strategy strategy) {
        this(c, 0.75f, strategy);
    }
    
    public IntLinkedOpenCustomHashSet(final IntIterator i, final float f, final IntHash.Strategy strategy) {
        this(16, f, strategy);
        while (i.hasNext()) {
            this.add(i.nextInt());
        }
    }
    
    public IntLinkedOpenCustomHashSet(final IntIterator i, final IntHash.Strategy strategy) {
        this(i, 0.75f, strategy);
    }
    
    public IntLinkedOpenCustomHashSet(final Iterator<?> i, final float f, final IntHash.Strategy strategy) {
        this(IntIterators.asIntIterator(i), f, strategy);
    }
    
    public IntLinkedOpenCustomHashSet(final Iterator<?> i, final IntHash.Strategy strategy) {
        this(IntIterators.asIntIterator(i), strategy);
    }
    
    public IntLinkedOpenCustomHashSet(final int[] a, final int offset, final int length, final float f, final IntHash.Strategy strategy) {
        this((length < 0) ? 0 : length, f, strategy);
        IntArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public IntLinkedOpenCustomHashSet(final int[] a, final int offset, final int length, final IntHash.Strategy strategy) {
        this(a, offset, length, 0.75f, strategy);
    }
    
    public IntLinkedOpenCustomHashSet(final int[] a, final float f, final IntHash.Strategy strategy) {
        this(a, 0, a.length, f, strategy);
    }
    
    public IntLinkedOpenCustomHashSet(final int[] a, final IntHash.Strategy strategy) {
        this(a, 0.75f, strategy);
    }
    
    public IntHash.Strategy strategy() {
        return this.strategy;
    }
    
    private int realSize() {
        return this.containsNull ? (this.size - 1) : this.size;
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
    
    public boolean addAll(final IntCollection c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll(c);
    }
    
    public boolean addAll(final Collection<? extends Integer> c) {
        if (this.f <= 0.5) {
            this.ensureCapacity(c.size());
        }
        else {
            this.tryCapacity(this.size() + c.size());
        }
        return super.addAll((Collection)c);
    }
    
    public boolean add(final int k) {
        int pos;
        if (this.strategy.equals(k, 0)) {
            if (this.containsNull) {
                return false;
            }
            pos = this.n;
            this.containsNull = true;
            this.key[this.n] = k;
        }
        else {
            final int[] key = this.key;
            int curr;
            if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) != 0) {
                if (this.strategy.equals(curr, k)) {
                    return false;
                }
                while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
                    if (this.strategy.equals(curr, k)) {
                        return false;
                    }
                }
            }
            key[pos] = k;
        }
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return true;
    }
    
    protected final void shiftKeys(int pos) {
        final int[] key = this.key;
        int last = 0;
    Label_0006:
        while (true) {
            pos = ((last = pos) + 1 & this.mask);
            int curr;
            while ((curr = key[pos]) != 0) {
                final int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                Label_0096: {
                    if (last <= pos) {
                        if (last >= slot) {
                            break Label_0096;
                        }
                        if (slot > pos) {
                            break Label_0096;
                        }
                    }
                    else if (last >= slot && slot > pos) {
                        break Label_0096;
                    }
                    pos = (pos + 1 & this.mask);
                    continue;
                }
                key[last] = curr;
                this.fixPointers(pos, last);
                continue Label_0006;
            }
            break;
        }
        key[last] = 0;
    }
    
    private boolean removeEntry(final int pos) {
        --this.size;
        this.fixPointers(pos);
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    private boolean removeNullEntry() {
        this.containsNull = false;
        this.key[this.n] = 0;
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return true;
    }
    
    public boolean remove(final int k) {
        if (this.strategy.equals(k, 0)) {
            return this.containsNull && this.removeNullEntry();
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return this.removeEntry(pos);
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return this.removeEntry(pos);
            }
        }
        return false;
    }
    
    public boolean contains(final int k) {
        if (this.strategy.equals(k, 0)) {
            return this.containsNull;
        }
        final int[] key = this.key;
        int pos;
        int curr;
        if ((curr = key[pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask)]) == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return true;
        }
        while ((curr = key[pos = (pos + 1 & this.mask)]) != 0) {
            if (this.strategy.equals(k, curr)) {
                return true;
            }
        }
        return false;
    }
    
    public int removeFirstInt() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int pos = this.first;
        this.first = (int)this.link[pos];
        if (0 <= this.first) {
            final long[] link = this.link;
            final int first = this.first;
            link[first] |= 0xFFFFFFFF00000000L;
        }
        final int k = this.key[pos];
        --this.size;
        if (this.strategy.equals(k, 0)) {
            this.containsNull = false;
            this.key[this.n] = 0;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return k;
    }
    
    public int removeLastInt() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int pos = this.last;
        this.last = (int)(this.link[pos] >>> 32);
        if (0 <= this.last) {
            final long[] link = this.link;
            final int last = this.last;
            link[last] |= 0xFFFFFFFFL;
        }
        final int k = this.key[pos];
        --this.size;
        if (this.strategy.equals(k, 0)) {
            this.containsNull = false;
            this.key[this.n] = 0;
        }
        else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return k;
    }
    
    private void moveIndexToFirst(final int i) {
        if (this.size == 1 || this.first == i) {
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            final long[] link = this.link;
            final int last = this.last;
            link[last] |= 0xFFFFFFFFL;
        }
        else {
            final long linki = this.link[i];
            final int prev = (int)(linki >>> 32);
            final int next = (int)linki;
            final long[] link2 = this.link;
            final int n = prev;
            link2[n] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            final long[] link3 = this.link;
            final int n2 = next;
            link3[n2] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
        }
        final long[] link4 = this.link;
        final int first = this.first;
        link4[first] ^= ((this.link[this.first] ^ ((long)i & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
        this.link[i] = (0xFFFFFFFF00000000L | ((long)this.first & 0xFFFFFFFFL));
        this.first = i;
    }
    
    private void moveIndexToLast(final int i) {
        if (this.size == 1 || this.last == i) {
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            final long[] link = this.link;
            final int first = this.first;
            link[first] |= 0xFFFFFFFF00000000L;
        }
        else {
            final long linki = this.link[i];
            final int prev = (int)(linki >>> 32);
            final int next = (int)linki;
            final long[] link2 = this.link;
            final int n = prev;
            link2[n] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            final long[] link3 = this.link;
            final int n2 = next;
            link3[n2] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
        }
        final long[] link4 = this.link;
        final int last = this.last;
        link4[last] ^= ((this.link[this.last] ^ ((long)i & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        this.link[i] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
        this.last = i;
    }
    
    public boolean addAndMoveToFirst(final int k) {
        int pos;
        if (this.strategy.equals(k, 0)) {
            if (this.containsNull) {
                this.moveIndexToFirst(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        }
        else {
            int[] key;
            for (key = this.key, pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); key[pos] != 0; pos = (pos + 1 & this.mask)) {
                if (this.strategy.equals(k, key[pos])) {
                    this.moveIndexToFirst(pos);
                    return false;
                }
            }
        }
        this.key[pos] = k;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int first = this.first;
            link[first] ^= ((this.link[this.first] ^ ((long)pos & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            this.link[pos] = (0xFFFFFFFF00000000L | ((long)this.first & 0xFFFFFFFFL));
            this.first = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return true;
    }
    
    public boolean addAndMoveToLast(final int k) {
        int pos;
        if (this.strategy.equals(k, 0)) {
            if (this.containsNull) {
                this.moveIndexToLast(this.n);
                return false;
            }
            this.containsNull = true;
            pos = this.n;
        }
        else {
            int[] key;
            for (key = this.key, pos = (HashCommon.mix(this.strategy.hashCode(k)) & this.mask); key[pos] != 0; pos = (pos + 1 & this.mask)) {
                if (this.strategy.equals(k, key[pos])) {
                    this.moveIndexToLast(pos);
                    return false;
                }
            }
        }
        this.key[pos] = k;
        if (this.size == 0) {
            final int n = pos;
            this.last = n;
            this.first = n;
            this.link[pos] = -1L;
        }
        else {
            final long[] link = this.link;
            final int last = this.last;
            link[last] ^= ((this.link[this.last] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[pos] = (((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL);
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return true;
    }
    
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNull = false;
        Arrays.fill(this.key, 0);
        final int n = -1;
        this.last = n;
        this.first = n;
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    protected void fixPointers(final int i) {
        if (this.size == 0) {
            final int n = -1;
            this.last = n;
            this.first = n;
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            if (0 <= this.first) {
                final long[] link = this.link;
                final int first = this.first;
                link[first] |= 0xFFFFFFFF00000000L;
            }
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            if (0 <= this.last) {
                final long[] link2 = this.link;
                final int last = this.last;
                link2[last] |= 0xFFFFFFFFL;
            }
            return;
        }
        final long linki = this.link[i];
        final int prev = (int)(linki >>> 32);
        final int next = (int)linki;
        final long[] link3 = this.link;
        final int n2 = prev;
        link3[n2] ^= ((this.link[prev] ^ (linki & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        final long[] link4 = this.link;
        final int n3 = next;
        link4[n3] ^= ((this.link[next] ^ (linki & 0xFFFFFFFF00000000L)) & 0xFFFFFFFF00000000L);
    }
    
    protected void fixPointers(final int s, final int d) {
        if (this.size == 1) {
            this.last = d;
            this.first = d;
            this.link[d] = -1L;
            return;
        }
        if (this.first == s) {
            this.first = d;
            final long[] link = this.link;
            final int n = (int)this.link[s];
            link[n] ^= ((this.link[(int)this.link[s]] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            this.link[d] = this.link[s];
            return;
        }
        if (this.last == s) {
            this.last = d;
            final long[] link2 = this.link;
            final int n2 = (int)(this.link[s] >>> 32);
            link2[n2] ^= ((this.link[(int)(this.link[s] >>> 32)] ^ ((long)d & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            this.link[d] = this.link[s];
            return;
        }
        final long links = this.link[s];
        final int prev = (int)(links >>> 32);
        final int next = (int)links;
        final long[] link3 = this.link;
        final int n3 = prev;
        link3[n3] ^= ((this.link[prev] ^ ((long)d & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
        final long[] link4 = this.link;
        final int n4 = next;
        link4[n4] ^= ((this.link[next] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
        this.link[d] = links;
    }
    
    public int firstInt() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }
    
    public int lastInt() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }
    
    public IntSortedSet tailSet(final int from) {
        throw new UnsupportedOperationException();
    }
    
    public IntSortedSet headSet(final int to) {
        throw new UnsupportedOperationException();
    }
    
    public IntSortedSet subSet(final int from, final int to) {
        throw new UnsupportedOperationException();
    }
    
    public IntComparator comparator() {
        return null;
    }
    
    public IntListIterator iterator(final int from) {
        return new SetIterator(from);
    }
    
    @Override
    public IntListIterator iterator() {
        return new SetIterator();
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
        final int[] key = this.key;
        final int mask = newN - 1;
        final int[] newKey = new int[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        final long[] link = this.link;
        final long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (this.strategy.equals(key[i], 0)) {
                pos = newN;
            }
            else {
                for (pos = (HashCommon.mix(this.strategy.hashCode(key[i])) & mask); newKey[pos] != 0; pos = (pos + 1 & mask)) {}
            }
            newKey[pos] = key[i];
            if (prev != -1) {
                final long[] array = newLink;
                final int n = newPrev;
                array[n] ^= ((newLink[newPrev] ^ ((long)pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
                final long[] array2 = newLink;
                final int n2 = pos;
                array2[n2] ^= ((newLink[pos] ^ ((long)newPrev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
                newPrev = pos;
            }
            else {
                final int first = pos;
                this.first = first;
                newPrev = first;
                newLink[pos] = -1L;
            }
            final int t = i;
            i = (int)link[i];
            prev = t;
        }
        this.link = newLink;
        if ((this.last = newPrev) != -1) {
            final long[] array3 = newLink;
            final int n3 = newPrev;
            array3[n3] |= 0xFFFFFFFFL;
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
    }
    
    public IntLinkedOpenCustomHashSet clone() {
        IntLinkedOpenCustomHashSet c;
        try {
            c = (IntLinkedOpenCustomHashSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = this.key.clone();
        c.containsNull = this.containsNull;
        c.link = this.link.clone();
        c.strategy = this.strategy;
        return c;
    }
    
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        while (j-- != 0) {
            while (this.key[i] == 0) {
                ++i;
            }
            h += this.strategy.hashCode(this.key[i]);
            ++i;
        }
        return h;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        final IntIterator i = this.iterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            s.writeInt(i.nextInt());
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/io/ObjectInputStream.defaultReadObject:()V
        //     4: aload_0         /* this */
        //     5: aload_0         /* this */
        //     6: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.size:I
        //     9: aload_0         /* this */
        //    10: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.f:F
        //    13: invokestatic    it/unimi/dsi/fastutil/HashCommon.arraySize:(IF)I
        //    16: putfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.n:I
        //    19: aload_0         /* this */
        //    20: aload_0         /* this */
        //    21: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.n:I
        //    24: aload_0         /* this */
        //    25: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.f:F
        //    28: invokestatic    it/unimi/dsi/fastutil/HashCommon.maxFill:(IF)I
        //    31: putfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.maxFill:I
        //    34: aload_0         /* this */
        //    35: aload_0         /* this */
        //    36: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.n:I
        //    39: iconst_1       
        //    40: isub           
        //    41: putfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.mask:I
        //    44: aload_0         /* this */
        //    45: aload_0         /* this */
        //    46: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.n:I
        //    49: iconst_1       
        //    50: iadd           
        //    51: newarray        I
        //    53: dup_x1         
        //    54: putfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.key:[I
        //    57: astore_2        /* key */
        //    58: aload_0         /* this */
        //    59: aload_0         /* this */
        //    60: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.n:I
        //    63: iconst_1       
        //    64: iadd           
        //    65: newarray        J
        //    67: dup_x1         
        //    68: putfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.link:[J
        //    71: astore_3        /* link */
        //    72: iconst_m1      
        //    73: istore          prev
        //    75: aload_0         /* this */
        //    76: aload_0         /* this */
        //    77: iconst_m1      
        //    78: dup_x1         
        //    79: putfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.last:I
        //    82: putfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.first:I
        //    85: aload_0         /* this */
        //    86: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.size:I
        //    89: istore          i
        //    91: iload           i
        //    93: iinc            i, -1
        //    96: ifeq            273
        //    99: aload_1         /* s */
        //   100: invokevirtual   java/io/ObjectInputStream.readInt:()I
        //   103: istore          k
        //   105: aload_0         /* this */
        //   106: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.strategy:Lit/unimi/dsi/fastutil/ints/IntHash$Strategy;
        //   109: iload           k
        //   111: iconst_0       
        //   112: invokeinterface it/unimi/dsi/fastutil/ints/IntHash$Strategy.equals:(II)Z
        //   117: ifeq            134
        //   120: aload_0         /* this */
        //   121: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.n:I
        //   124: istore          pos
        //   126: aload_0         /* this */
        //   127: iconst_1       
        //   128: putfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.containsNull:Z
        //   131: goto            181
        //   134: aload_2         /* key */
        //   135: aload_0         /* this */
        //   136: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.strategy:Lit/unimi/dsi/fastutil/ints/IntHash$Strategy;
        //   139: iload           k
        //   141: invokeinterface it/unimi/dsi/fastutil/ints/IntHash$Strategy.hashCode:(I)I
        //   146: invokestatic    it/unimi/dsi/fastutil/HashCommon.mix:(I)I
        //   149: aload_0         /* this */
        //   150: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.mask:I
        //   153: iand           
        //   154: dup            
        //   155: istore          pos
        //   157: iaload         
        //   158: ifeq            181
        //   161: aload_2         /* key */
        //   162: iload           pos
        //   164: iconst_1       
        //   165: iadd           
        //   166: aload_0         /* this */
        //   167: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.mask:I
        //   170: iand           
        //   171: dup            
        //   172: istore          pos
        //   174: iaload         
        //   175: ifeq            181
        //   178: goto            161
        //   181: aload_2         /* key */
        //   182: iload           pos
        //   184: iload           k
        //   186: iastore        
        //   187: aload_0         /* this */
        //   188: getfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.first:I
        //   191: iconst_m1      
        //   192: if_icmpeq       251
        //   195: aload_3         /* link */
        //   196: iload           prev
        //   198: dup2           
        //   199: laload         
        //   200: aload_3         /* link */
        //   201: iload           prev
        //   203: laload         
        //   204: iload           pos
        //   206: i2l            
        //   207: ldc2_w          4294967295
        //   210: land           
        //   211: lxor           
        //   212: ldc2_w          4294967295
        //   215: land           
        //   216: lxor           
        //   217: lastore        
        //   218: aload_3         /* link */
        //   219: iload           pos
        //   221: dup2           
        //   222: laload         
        //   223: aload_3         /* link */
        //   224: iload           pos
        //   226: laload         
        //   227: iload           prev
        //   229: i2l            
        //   230: ldc2_w          4294967295
        //   233: land           
        //   234: bipush          32
        //   236: lshl           
        //   237: lxor           
        //   238: ldc2_w          -4294967296
        //   241: land           
        //   242: lxor           
        //   243: lastore        
        //   244: iload           pos
        //   246: istore          prev
        //   248: goto            91
        //   251: aload_0         /* this */
        //   252: iload           pos
        //   254: dup_x1         
        //   255: putfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.first:I
        //   258: istore          prev
        //   260: aload_3         /* link */
        //   261: iload           pos
        //   263: dup2           
        //   264: laload         
        //   265: ldc2_w          -4294967296
        //   268: lor            
        //   269: lastore        
        //   270: goto            91
        //   273: aload_0         /* this */
        //   274: iload           prev
        //   276: putfield        it/unimi/dsi/fastutil/ints/IntLinkedOpenCustomHashSet.last:I
        //   279: iload           prev
        //   281: iconst_m1      
        //   282: if_icmpeq       295
        //   285: aload_3         /* link */
        //   286: iload           prev
        //   288: dup2           
        //   289: laload         
        //   290: ldc2_w          4294967295
        //   293: lor            
        //   294: lastore        
        //   295: return         
        //    Exceptions:
        //  throws java.io.IOException
        //  throws java.lang.ClassNotFoundException
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  s     
        //    StackMapTable: 00 07 FF 00 5B 00 07 07 00 02 07 01 6A 07 00 A0 07 01 3D 01 00 01 00 00 FF 00 2A 00 07 07 00 02 07 01 6A 07 00 A0 07 01 3D 01 01 01 00 00 FC 00 1A 01 13 FB 00 45 F8 00 15 15
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1119)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1291)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
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
    
    private void checkTable() {
    }
    
    private class SetIterator implements IntListIterator {
        int prev;
        int next;
        int curr;
        int index;
        
        SetIterator() {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            this.next = IntLinkedOpenCustomHashSet.this.first;
            this.index = 0;
        }
        
        SetIterator(final int from) {
            this.prev = -1;
            this.next = -1;
            this.curr = -1;
            this.index = -1;
            if (IntLinkedOpenCustomHashSet.this.strategy.equals(from, 0)) {
                if (IntLinkedOpenCustomHashSet.this.containsNull) {
                    this.next = (int)IntLinkedOpenCustomHashSet.this.link[IntLinkedOpenCustomHashSet.this.n];
                    this.prev = IntLinkedOpenCustomHashSet.this.n;
                    return;
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this set.").toString());
            }
            else {
                if (IntLinkedOpenCustomHashSet.this.strategy.equals(IntLinkedOpenCustomHashSet.this.key[IntLinkedOpenCustomHashSet.this.last], from)) {
                    this.prev = IntLinkedOpenCustomHashSet.this.last;
                    this.index = IntLinkedOpenCustomHashSet.this.size;
                    return;
                }
                final int[] key = IntLinkedOpenCustomHashSet.this.key;
                for (int pos = HashCommon.mix(IntLinkedOpenCustomHashSet.this.strategy.hashCode(from)) & IntLinkedOpenCustomHashSet.this.mask; key[pos] != 0; pos = (pos + 1 & IntLinkedOpenCustomHashSet.this.mask)) {
                    if (IntLinkedOpenCustomHashSet.this.strategy.equals(key[pos], from)) {
                        this.next = (int)IntLinkedOpenCustomHashSet.this.link[pos];
                        this.prev = pos;
                        return;
                    }
                }
                throw new NoSuchElementException(new StringBuilder().append("The key ").append(from).append(" does not belong to this set.").toString());
            }
        }
        
        public boolean hasNext() {
            return this.next != -1;
        }
        
        public boolean hasPrevious() {
            return this.prev != -1;
        }
        
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int)IntLinkedOpenCustomHashSet.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                ++this.index;
            }
            return IntLinkedOpenCustomHashSet.this.key[this.curr];
        }
        
        public int previousInt() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int)(IntLinkedOpenCustomHashSet.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                --this.index;
            }
            return IntLinkedOpenCustomHashSet.this.key[this.curr];
        }
        
        private final void ensureIndexKnown() {
            if (this.index >= 0) {
                return;
            }
            if (this.prev == -1) {
                this.index = 0;
                return;
            }
            if (this.next == -1) {
                this.index = IntLinkedOpenCustomHashSet.this.size;
                return;
            }
            int pos = IntLinkedOpenCustomHashSet.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)IntLinkedOpenCustomHashSet.this.link[pos];
                ++this.index;
            }
        }
        
        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }
        
        public int previousIndex() {
            this.ensureIndexKnown();
            return this.index - 1;
        }
        
        public void remove() {
            this.ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
                this.prev = (int)(IntLinkedOpenCustomHashSet.this.link[this.curr] >>> 32);
            }
            else {
                this.next = (int)IntLinkedOpenCustomHashSet.this.link[this.curr];
            }
            final IntLinkedOpenCustomHashSet this$0 = IntLinkedOpenCustomHashSet.this;
            --this$0.size;
            if (this.prev == -1) {
                IntLinkedOpenCustomHashSet.this.first = this.next;
            }
            else {
                final long[] link = IntLinkedOpenCustomHashSet.this.link;
                final int prev = this.prev;
                link[prev] ^= ((IntLinkedOpenCustomHashSet.this.link[this.prev] ^ ((long)this.next & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
            }
            if (this.next == -1) {
                IntLinkedOpenCustomHashSet.this.last = this.prev;
            }
            else {
                final long[] link2 = IntLinkedOpenCustomHashSet.this.link;
                final int next = this.next;
                link2[next] ^= ((IntLinkedOpenCustomHashSet.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L);
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos == IntLinkedOpenCustomHashSet.this.n) {
                IntLinkedOpenCustomHashSet.this.containsNull = false;
                IntLinkedOpenCustomHashSet.this.key[IntLinkedOpenCustomHashSet.this.n] = 0;
                return;
            }
            final int[] key = IntLinkedOpenCustomHashSet.this.key;
            int last = 0;
        Label_0280:
            while (true) {
                pos = ((last = pos) + 1 & IntLinkedOpenCustomHashSet.this.mask);
                int curr;
                while ((curr = key[pos]) != 0) {
                    final int slot = HashCommon.mix(IntLinkedOpenCustomHashSet.this.strategy.hashCode(curr)) & IntLinkedOpenCustomHashSet.this.mask;
                    Label_0382: {
                        if (last <= pos) {
                            if (last >= slot) {
                                break Label_0382;
                            }
                            if (slot > pos) {
                                break Label_0382;
                            }
                        }
                        else if (last >= slot && slot > pos) {
                            break Label_0382;
                        }
                        pos = (pos + 1 & IntLinkedOpenCustomHashSet.this.mask);
                        continue;
                    }
                    key[last] = curr;
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    IntLinkedOpenCustomHashSet.this.fixPointers(pos, last);
                    continue Label_0280;
                }
                break;
            }
            key[last] = 0;
        }
    }
}

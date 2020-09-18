package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.SortedSet;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;

public class Short2ByteAVLTreeMap extends AbstractShort2ByteSortedMap implements Serializable, Cloneable {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Short2ByteMap.Entry> entries;
    protected transient ShortSortedSet keys;
    protected transient ByteCollection values;
    protected transient boolean modified;
    protected Comparator<? super Short> storedComparator;
    protected transient ShortComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    
    public Short2ByteAVLTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = ShortComparators.asShortComparator(this.storedComparator);
    }
    
    public Short2ByteAVLTreeMap(final Comparator<? super Short> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Short2ByteAVLTreeMap(final Map<? extends Short, ? extends Byte> m) {
        this();
        this.putAll(m);
    }
    
    public Short2ByteAVLTreeMap(final SortedMap<Short, Byte> m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Short2ByteAVLTreeMap(final Short2ByteMap m) {
        this();
        this.putAll(m);
    }
    
    public Short2ByteAVLTreeMap(final Short2ByteSortedMap m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Short2ByteAVLTreeMap(final short[] k, final byte[] v, final Comparator<? super Short> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Short2ByteAVLTreeMap(final short[] k, final byte[] v) {
        this(k, v, null);
    }
    
    final int compare(final short k1, final short k2) {
        return (this.actualComparator == null) ? Short.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry findKey(final short k) {
        Entry e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry locateKey(final short k) {
        Entry e = this.tree;
        Entry last = this.tree;
        int cmp;
        for (cmp = 0; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {
            last = e;
        }
        return (cmp == 0) ? e : last;
    }
    
    private void allocatePaths() {
        this.dirPath = new boolean[48];
    }
    
    public byte addTo(final short k, final byte incr) {
        final Entry e = this.add(k);
        final byte oldValue = e.value;
        final Entry entry = e;
        entry.value += incr;
        return oldValue;
    }
    
    public byte put(final short k, final byte v) {
        final Entry e = this.add(k);
        final byte oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry add(final short k) {
        this.modified = false;
        Entry e = null;
        if (this.tree != null) {
            Entry p = this.tree;
            Entry q = null;
            Entry y = this.tree;
            Entry z = null;
            Entry w = null;
            int i = 0;
            int cmp;
            while ((cmp = this.compare(k, p.key)) != 0) {
                if (p.balance() != 0) {
                    i = 0;
                    z = q;
                    y = p;
                }
                final boolean[] dirPath = this.dirPath;
                final int n = i++;
                final boolean b = cmp > 0;
                dirPath[n] = b;
                if (b) {
                    if (!p.succ()) {
                        q = p;
                        p = p.right;
                        continue;
                    }
                    ++this.count;
                    e = new Entry(k, this.defRetValue);
                    this.modified = true;
                    if (p.right == null) {
                        this.lastEntry = e;
                    }
                    e.left = p;
                    e.right = p.right;
                    p.right(e);
                }
                else {
                    if (!p.pred()) {
                        q = p;
                        p = p.left;
                        continue;
                    }
                    ++this.count;
                    e = new Entry(k, this.defRetValue);
                    this.modified = true;
                    if (p.left == null) {
                        this.firstEntry = e;
                    }
                    e.right = p;
                    e.left = p.left;
                    p.left(e);
                }
                for (p = y, i = 0; p != e; p = (this.dirPath[i++] ? p.right : p.left)) {
                    if (this.dirPath[i]) {
                        p.incBalance();
                    }
                    else {
                        p.decBalance();
                    }
                }
                if (y.balance() == -2) {
                    final Entry x = y.left;
                    if (x.balance() == -1) {
                        w = x;
                        if (x.succ()) {
                            x.succ(false);
                            y.pred(x);
                        }
                        else {
                            y.left = x.right;
                        }
                        x.right = y;
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        assert x.balance() == 1;
                        w = x.right;
                        x.right = w.left;
                        w.left = x;
                        y.left = w.right;
                        w.right = y;
                        if (w.balance() == -1) {
                            x.balance(0);
                            y.balance(1);
                        }
                        else if (w.balance() == 0) {
                            x.balance(0);
                            y.balance(0);
                        }
                        else {
                            x.balance(-1);
                            y.balance(0);
                        }
                        w.balance(0);
                        if (w.pred()) {
                            x.succ(w);
                            w.pred(false);
                        }
                        if (w.succ()) {
                            y.pred(w);
                            w.succ(false);
                        }
                    }
                }
                else {
                    if (y.balance() != 2) {
                        return e;
                    }
                    final Entry x = y.right;
                    if (x.balance() == 1) {
                        w = x;
                        if (x.pred()) {
                            x.pred(false);
                            y.succ(x);
                        }
                        else {
                            y.right = x.left;
                        }
                        x.left = y;
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        assert x.balance() == -1;
                        w = x.left;
                        x.left = w.right;
                        w.right = x;
                        y.right = w.left;
                        w.left = y;
                        if (w.balance() == 1) {
                            x.balance(0);
                            y.balance(-1);
                        }
                        else if (w.balance() == 0) {
                            x.balance(0);
                            y.balance(0);
                        }
                        else {
                            x.balance(1);
                            y.balance(0);
                        }
                        w.balance(0);
                        if (w.pred()) {
                            y.succ(w);
                            w.pred(false);
                        }
                        if (w.succ()) {
                            x.pred(w);
                            w.succ(false);
                        }
                    }
                }
                if (z == null) {
                    this.tree = w;
                    return e;
                }
                if (z.left == y) {
                    z.left = w;
                    return e;
                }
                z.right = w;
                return e;
            }
            return p;
        }
        ++this.count;
        final Entry tree = new Entry(k, this.defRetValue);
        this.firstEntry = tree;
        this.lastEntry = tree;
        this.tree = tree;
        e = tree;
        this.modified = true;
        return e;
    }
    
    private Entry parent(final Entry e) {
        if (e == this.tree) {
            return null;
        }
        Entry y = e;
        Entry x = e;
        while (!y.succ()) {
            if (x.pred()) {
                Entry p = x.left;
                if (p == null || p.right != e) {
                    while (!y.succ()) {
                        y = y.right;
                    }
                    p = y.right;
                }
                return p;
            }
            x = x.left;
            y = y.right;
        }
        Entry p = y.right;
        if (p == null || p.left != e) {
            while (!x.pred()) {
                x = x.left;
            }
            p = x.left;
        }
        return p;
    }
    
    public byte remove(final short k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry p = this.tree;
        Entry q = null;
        boolean dir = false;
        final short kk = k;
        int cmp;
        while ((cmp = this.compare(kk, p.key)) != 0) {
            if (dir = (cmp > 0)) {
                q = p;
                if ((p = p.right()) == null) {
                    return this.defRetValue;
                }
                continue;
            }
            else {
                q = p;
                if ((p = p.left()) == null) {
                    return this.defRetValue;
                }
                continue;
            }
        }
        if (p.left == null) {
            this.firstEntry = p.next();
        }
        if (p.right == null) {
            this.lastEntry = p.prev();
        }
        if (p.succ()) {
            if (p.pred()) {
                if (q != null) {
                    if (dir) {
                        q.succ(p.right);
                    }
                    else {
                        q.pred(p.left);
                    }
                }
                else {
                    this.tree = (dir ? p.right : p.left);
                }
            }
            else {
                p.prev().right = p.right;
                if (q != null) {
                    if (dir) {
                        q.right = p.left;
                    }
                    else {
                        q.left = p.left;
                    }
                }
                else {
                    this.tree = p.left;
                }
            }
        }
        else {
            Entry r = p.right;
            if (r.pred()) {
                r.left = p.left;
                r.pred(p.pred());
                if (!r.pred()) {
                    r.prev().right = r;
                }
                if (q != null) {
                    if (dir) {
                        q.right = r;
                    }
                    else {
                        q.left = r;
                    }
                }
                else {
                    this.tree = r;
                }
                r.balance(p.balance());
                q = r;
                dir = true;
            }
            else {
                Entry s;
                while (true) {
                    s = r.left;
                    if (s.pred()) {
                        break;
                    }
                    r = s;
                }
                if (s.succ()) {
                    r.pred(s);
                }
                else {
                    r.left = s.right;
                }
                s.left = p.left;
                if (!p.pred()) {
                    (p.prev().right = s).pred(false);
                }
                s.right = p.right;
                s.succ(false);
                if (q != null) {
                    if (dir) {
                        q.right = s;
                    }
                    else {
                        q.left = s;
                    }
                }
                else {
                    this.tree = s;
                }
                s.balance(p.balance());
                q = r;
                dir = false;
            }
        }
        while (q != null) {
            final Entry y = q;
            q = this.parent(y);
            if (!dir) {
                dir = (q != null && q.left != y);
                y.incBalance();
                if (y.balance() == 1) {
                    break;
                }
                if (y.balance() != 2) {
                    continue;
                }
                final Entry x = y.right;
                assert x != null;
                if (x.balance() == -1) {
                    assert x.balance() == -1;
                    final Entry w = x.left;
                    x.left = w.right;
                    w.right = x;
                    y.right = w.left;
                    w.left = y;
                    if (w.balance() == 1) {
                        x.balance(0);
                        y.balance(-1);
                    }
                    else if (w.balance() == 0) {
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        assert w.balance() == -1;
                        x.balance(1);
                        y.balance(0);
                    }
                    w.balance(0);
                    if (w.pred()) {
                        y.succ(w);
                        w.pred(false);
                    }
                    if (w.succ()) {
                        x.pred(w);
                        w.succ(false);
                    }
                    if (q != null) {
                        if (dir) {
                            q.right = w;
                        }
                        else {
                            q.left = w;
                        }
                    }
                    else {
                        this.tree = w;
                    }
                }
                else {
                    if (q != null) {
                        if (dir) {
                            q.right = x;
                        }
                        else {
                            q.left = x;
                        }
                    }
                    else {
                        this.tree = x;
                    }
                    if (x.balance() == 0) {
                        y.right = x.left;
                        x.left = y;
                        x.balance(-1);
                        y.balance(1);
                        break;
                    }
                    assert x.balance() == 1;
                    if (x.pred()) {
                        y.succ(true);
                        x.pred(false);
                    }
                    else {
                        y.right = x.left;
                    }
                    (x.left = y).balance(0);
                    x.balance(0);
                }
            }
            else {
                dir = (q != null && q.left != y);
                y.decBalance();
                if (y.balance() == -1) {
                    break;
                }
                if (y.balance() != -2) {
                    continue;
                }
                final Entry x = y.left;
                assert x != null;
                if (x.balance() == 1) {
                    assert x.balance() == 1;
                    final Entry w = x.right;
                    x.right = w.left;
                    w.left = x;
                    y.left = w.right;
                    w.right = y;
                    if (w.balance() == -1) {
                        x.balance(0);
                        y.balance(1);
                    }
                    else if (w.balance() == 0) {
                        x.balance(0);
                        y.balance(0);
                    }
                    else {
                        assert w.balance() == 1;
                        x.balance(-1);
                        y.balance(0);
                    }
                    w.balance(0);
                    if (w.pred()) {
                        x.succ(w);
                        w.pred(false);
                    }
                    if (w.succ()) {
                        y.pred(w);
                        w.succ(false);
                    }
                    if (q != null) {
                        if (dir) {
                            q.right = w;
                        }
                        else {
                            q.left = w;
                        }
                    }
                    else {
                        this.tree = w;
                    }
                }
                else {
                    if (q != null) {
                        if (dir) {
                            q.right = x;
                        }
                        else {
                            q.left = x;
                        }
                    }
                    else {
                        this.tree = x;
                    }
                    if (x.balance() == 0) {
                        y.left = x.right;
                        x.right = y;
                        x.balance(1);
                        y.balance(-1);
                        break;
                    }
                    assert x.balance() == -1;
                    if (x.succ()) {
                        y.pred(true);
                        x.succ(false);
                    }
                    else {
                        y.left = x.right;
                    }
                    (x.right = y).balance(0);
                    x.balance(0);
                }
            }
        }
        this.modified = true;
        --this.count;
        return p.value;
    }
    
    public boolean containsValue(final byte v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final byte ev = i.nextByte();
            if (ev == v) {
                return true;
            }
        }
        return false;
    }
    
    public void clear() {
        this.count = 0;
        this.tree = null;
        this.entries = null;
        this.values = null;
        this.keys = null;
        final Entry entry = null;
        this.lastEntry = entry;
        this.firstEntry = entry;
    }
    
    public boolean containsKey(final short k) {
        return this.findKey(k) != null;
    }
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public byte get(final short k) {
        final Entry e = this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    public short firstShortKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public short lastShortKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    public ObjectSortedSet<Short2ByteMap.Entry> short2ByteEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Short2ByteMap.Entry>() {
                final Comparator<? super Short2ByteMap.Entry> comparator = (x, y) -> Short2ByteAVLTreeMap.this.actualComparator.compare(x.getShortKey(), y.getShortKey());
                
                public Comparator<? super Short2ByteMap.Entry> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Short2ByteMap.Entry> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Short2ByteMap.Entry> iterator(final Short2ByteMap.Entry from) {
                    return new EntryIterator(from.getShortKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                        return false;
                    }
                    final Entry f = Short2ByteAVLTreeMap.this.findKey((short)e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                        return false;
                    }
                    final Entry f = Short2ByteAVLTreeMap.this.findKey((short)e.getKey());
                    if (f == null || f.getByteValue() != (byte)e.getValue()) {
                        return false;
                    }
                    Short2ByteAVLTreeMap.this.remove(f.key);
                    return true;
                }
                
                public int size() {
                    return Short2ByteAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Short2ByteAVLTreeMap.this.clear();
                }
                
                public Short2ByteMap.Entry first() {
                    return Short2ByteAVLTreeMap.this.firstEntry;
                }
                
                public Short2ByteMap.Entry last() {
                    return Short2ByteAVLTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Short2ByteMap.Entry> subSet(final Short2ByteMap.Entry from, final Short2ByteMap.Entry to) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     1: getfield        it/unimi/dsi/fastutil/shorts/Short2ByteAVLTreeMap$1.this$0:Lit/unimi/dsi/fastutil/shorts/Short2ByteAVLTreeMap;
                    //     4: aload_1         /* from */
                    //     5: invokeinterface it/unimi/dsi/fastutil/shorts/Short2ByteMap$Entry.getShortKey:()S
                    //    10: aload_2         /* to */
                    //    11: invokeinterface it/unimi/dsi/fastutil/shorts/Short2ByteMap$Entry.getShortKey:()S
                    //    16: invokevirtual   it/unimi/dsi/fastutil/shorts/Short2ByteAVLTreeMap.subMap:(SS)Lit/unimi/dsi/fastutil/shorts/Short2ByteSortedMap;
                    //    19: invokeinterface it/unimi/dsi/fastutil/shorts/Short2ByteSortedMap.short2ByteEntrySet:()Lit/unimi/dsi/fastutil/objects/ObjectSortedSet;
                    //    24: areturn        
                    //    Signature:
                    //  (Lit/unimi/dsi/fastutil/shorts/Short2ByteMap$Entry;Lit/unimi/dsi/fastutil/shorts/Short2ByteMap$Entry;)Lit/unimi/dsi/fastutil/objects/ObjectSortedSet<Lit/unimi/dsi/fastutil/shorts/Short2ByteMap$Entry;>;
                    //    MethodParameters:
                    //  Name  Flags  
                    //  ----  -----
                    //  from  
                    //  to    
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
                    //     at java.base/java.util.Vector.get(Vector.java:749)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                    //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
                    //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                    //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.shouldInferVariableType(TypeAnalysis.java:613)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:265)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:272)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:272)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:158)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
                    //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:93)
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
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:440)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
                public ObjectSortedSet<Short2ByteMap.Entry> headSet(final Short2ByteMap.Entry to) {
                    return Short2ByteAVLTreeMap.this.headMap(to.getShortKey()).short2ByteEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Short2ByteMap.Entry> tailSet(final Short2ByteMap.Entry from) {
                    return Short2ByteAVLTreeMap.this.tailMap(from.getShortKey()).short2ByteEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public ShortSortedSet keySet() {
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
                
                @Override
                public boolean contains(final byte k) {
                    return Short2ByteAVLTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Short2ByteAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Short2ByteAVLTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public ShortComparator comparator() {
        return this.actualComparator;
    }
    
    public Short2ByteSortedMap headMap(final short to) {
        return new Submap((short)0, true, to, false);
    }
    
    public Short2ByteSortedMap tailMap(final short from) {
        return new Submap(from, false, (short)0, true);
    }
    
    public Short2ByteSortedMap subMap(final short from, final short to) {
        return new Submap(from, false, to, false);
    }
    
    public Short2ByteAVLTreeMap clone() {
        Short2ByteAVLTreeMap c;
        try {
            c = (Short2ByteAVLTreeMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.allocatePaths();
        if (this.count != 0) {
            final Entry rp = new Entry();
            final Entry rq = new Entry();
            Entry p = rp;
            rp.left(this.tree);
            Entry q = rq;
            rq.pred(null);
        Block_4:
            while (true) {
                if (!p.pred()) {
                    final Entry e = p.left.clone();
                    e.pred(q.left);
                    e.succ(q);
                    q.left(e);
                    p = p.left;
                    q = q.left;
                }
                else {
                    while (p.succ()) {
                        p = p.right;
                        if (p == null) {
                            break Block_4;
                        }
                        q = q.right;
                    }
                    p = p.right;
                    q = q.right;
                }
                if (!p.succ()) {
                    final Entry e = p.right.clone();
                    e.succ(q.right);
                    e.pred(q);
                    q.right(e);
                }
            }
            q.right = null;
            c.tree = rq.left;
            c.firstEntry = c.tree;
            while (c.firstEntry.left != null) {
                c.firstEntry = c.firstEntry.left;
            }
            c.lastEntry = c.tree;
            while (c.lastEntry.right != null) {
                c.lastEntry = c.lastEntry.right;
            }
            return c;
        }
        return c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        int n = this.count;
        final EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        while (n-- != 0) {
            final Entry e = i.nextEntry();
            s.writeShort((int)e.key);
            s.writeByte((int)e.value);
        }
    }
    
    private Entry readTree(final ObjectInputStream s, final int n, final Entry pred, final Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry top = new Entry(s.readShort(), s.readByte());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            final Entry top = new Entry(s.readShort(), s.readByte());
            top.right(new Entry(s.readShort(), s.readByte()));
            top.right.pred(top);
            top.balance(1);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry top2 = new Entry();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = s.readShort();
        top2.value = s.readByte();
        top2.right(this.readTree(s, rightN, top2, succ));
        if (n == (n & -n)) {
            top2.balance(1);
        }
        return top2;
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.setActualComparator();
        this.allocatePaths();
        if (this.count != 0) {
            this.tree = this.readTree(s, this.count, null, null);
            Entry e;
            for (e = this.tree; e.left() != null; e = e.left()) {}
            this.firstEntry = e;
            for (e = this.tree; e.right() != null; e = e.right()) {}
            this.lastEntry = e;
        }
    }
    
    private static final class Entry extends BasicEntry implements Cloneable {
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        private static final int BALANCE_MASK = 255;
        Entry left;
        Entry right;
        int info;
        
        Entry() {
            super((short)0, (byte)0);
        }
        
        Entry(final short k, final byte v) {
            super(k, v);
            this.info = -1073741824;
        }
        
        Entry left() {
            return ((this.info & 0x40000000) != 0x0) ? null : this.left;
        }
        
        Entry right() {
            return ((this.info & Integer.MIN_VALUE) != 0x0) ? null : this.right;
        }
        
        boolean pred() {
            return (this.info & 0x40000000) != 0x0;
        }
        
        boolean succ() {
            return (this.info & Integer.MIN_VALUE) != 0x0;
        }
        
        void pred(final boolean pred) {
            if (pred) {
                this.info |= 0x40000000;
            }
            else {
                this.info &= 0xBFFFFFFF;
            }
        }
        
        void succ(final boolean succ) {
            if (succ) {
                this.info |= Integer.MIN_VALUE;
            }
            else {
                this.info &= Integer.MAX_VALUE;
            }
        }
        
        void pred(final Entry pred) {
            this.info |= 0x40000000;
            this.left = pred;
        }
        
        void succ(final Entry succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }
        
        void left(final Entry left) {
            this.info &= 0xBFFFFFFF;
            this.left = left;
        }
        
        void right(final Entry right) {
            this.info &= Integer.MAX_VALUE;
            this.right = right;
        }
        
        int balance() {
            return (byte)this.info;
        }
        
        void balance(final int level) {
            this.info &= 0xFFFFFF00;
            this.info |= (level & 0xFF);
        }
        
        void incBalance() {
            this.info = ((this.info & 0xFFFFFF00) | ((byte)this.info + 1 & 0xFF));
        }
        
        protected void decBalance() {
            this.info = ((this.info & 0xFFFFFF00) | ((byte)this.info - 1 & 0xFF));
        }
        
        Entry next() {
            Entry next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0x0) {
                while ((next.info & 0x40000000) == 0x0) {
                    next = next.left;
                }
            }
            return next;
        }
        
        Entry prev() {
            Entry prev = this.left;
            if ((this.info & 0x40000000) == 0x0) {
                while ((prev.info & Integer.MIN_VALUE) == 0x0) {
                    prev = prev.right;
                }
            }
            return prev;
        }
        
        @Override
        public byte setValue(final byte value) {
            final byte oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        public Entry clone() {
            Entry c;
            try {
                c = (Entry)super.clone();
            }
            catch (CloneNotSupportedException cantHappen) {
                throw new InternalError();
            }
            c.key = this.key;
            c.value = this.value;
            c.info = this.info;
            return c;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<Short, Byte> e = (Map.Entry<Short, Byte>)o;
            return this.key == (short)e.getKey() && this.value == (byte)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append((int)this.key).append("=>").append((int)this.value).toString();
        }
    }
    
    private class TreeIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Short2ByteAVLTreeMap.this.firstEntry;
        }
        
        TreeIterator(final short k) {
            this.index = 0;
            final Entry locateKey = Short2ByteAVLTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Short2ByteAVLTreeMap.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                }
                else {
                    this.prev = this.next.prev();
                }
            }
        }
        
        public boolean hasNext() {
            return this.next != null;
        }
        
        public boolean hasPrevious() {
            return this.prev != null;
        }
        
        void updateNext() {
            this.next = this.next.next();
        }
        
        Entry nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Entry next = this.next;
            this.prev = next;
            this.curr = next;
            ++this.index;
            this.updateNext();
            return this.curr;
        }
        
        void updatePrevious() {
            this.prev = this.prev.prev();
        }
        
        Entry previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final Entry prev = this.prev;
            this.next = prev;
            this.curr = prev;
            --this.index;
            this.updatePrevious();
            return this.curr;
        }
        
        public int nextIndex() {
            return this.index;
        }
        
        public int previousIndex() {
            return this.index - 1;
        }
        
        public void remove() {
            if (this.curr == null) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
            }
            final Entry curr = this.curr;
            this.prev = curr;
            this.next = curr;
            this.updatePrevious();
            this.updateNext();
            Short2ByteAVLTreeMap.this.remove(this.curr.key);
            this.curr = null;
        }
        
        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
        
        public int back(final int n) {
            int i = n;
            while (i-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - i - 1;
        }
    }
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Short2ByteMap.Entry> {
        EntryIterator() {
        }
        
        EntryIterator(final short k) {
            super(k);
        }
        
        public Short2ByteMap.Entry next() {
            return this.nextEntry();
        }
        
        public Short2ByteMap.Entry previous() {
            return this.previousEntry();
        }
        
        @Override
        public void set(final Short2ByteMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Short2ByteMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements ShortListIterator {
        public KeyIterator() {
        }
        
        public KeyIterator(final short k) {
            super(k);
        }
        
        public short nextShort() {
            return this.nextEntry().key;
        }
        
        public short previousShort() {
            return this.previousEntry().key;
        }
    }
    
    private class KeySet extends AbstractShort2ByteSortedMap.KeySet {
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements ByteListIterator {
        public byte nextByte() {
            return this.nextEntry().value;
        }
        
        public byte previousByte() {
            return this.previousEntry().value;
        }
    }
    
    private final class Submap extends AbstractShort2ByteSortedMap implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        short from;
        short to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Short2ByteMap.Entry> entries;
        protected transient ShortSortedSet keys;
        protected transient ByteCollection values;
        final /* synthetic */ Short2ByteAVLTreeMap this$0;
        
        public Submap(final short from, final boolean bottom, final short to, final boolean top) {
            if (!bottom && !top && Short2ByteAVLTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append((int)from).append(") is larger than end key (").append((int)to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Short2ByteAVLTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final short k) {
            return (this.bottom || Short2ByteAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Short2ByteAVLTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Short2ByteMap.Entry> short2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Short2ByteMap.Entry>() {
                    @Override
                    public ObjectBidirectionalIterator<Short2ByteMap.Entry> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Short2ByteMap.Entry> iterator(final Short2ByteMap.Entry from) {
                        return new SubmapEntryIterator(from.getShortKey());
                    }
                    
                    public Comparator<? super Short2ByteMap.Entry> comparator() {
                        return Short2ByteAVLTreeMap.this.short2ByteEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                            return false;
                        }
                        final Short2ByteAVLTreeMap.Entry f = Short2ByteAVLTreeMap.this.findKey((short)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                            return false;
                        }
                        final Short2ByteAVLTreeMap.Entry f = Short2ByteAVLTreeMap.this.findKey((short)e.getKey());
                        if (f != null && Submap.this.in(f.key)) {
                            Submap.this.remove(f.key);
                        }
                        return f != null;
                    }
                    
                    public int size() {
                        int c = 0;
                        final Iterator<?> i = this.iterator();
                        while (i.hasNext()) {
                            ++c;
                            i.next();
                        }
                        return c;
                    }
                    
                    public boolean isEmpty() {
                        return !new SubmapIterator().hasNext();
                    }
                    
                    public void clear() {
                        Submap.this.clear();
                    }
                    
                    public Short2ByteMap.Entry first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Short2ByteMap.Entry last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Short2ByteMap.Entry> subSet(final Short2ByteMap.Entry from, final Short2ByteMap.Entry to) {
                        return Submap.this.subMap(from.getShortKey(), to.getShortKey()).short2ByteEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Short2ByteMap.Entry> headSet(final Short2ByteMap.Entry to) {
                        return Submap.this.headMap(to.getShortKey()).short2ByteEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Short2ByteMap.Entry> tailSet(final Short2ByteMap.Entry from) {
                        return Submap.this.tailMap(from.getShortKey()).short2ByteEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public ShortSortedSet keySet() {
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
                        return new SubmapValueIterator();
                    }
                    
                    @Override
                    public boolean contains(final byte k) {
                        return Submap.this.containsValue(k);
                    }
                    
                    public int size() {
                        return Submap.this.size();
                    }
                    
                    public void clear() {
                        Submap.this.clear();
                    }
                };
            }
            return this.values;
        }
        
        public boolean containsKey(final short k) {
            return this.in(k) && Short2ByteAVLTreeMap.this.containsKey(k);
        }
        
        public boolean containsValue(final byte v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final byte ev = i.nextEntry().value;
                if (ev == v) {
                    return true;
                }
            }
            return false;
        }
        
        public byte get(final short k) {
            final short kk = k;
            final Short2ByteAVLTreeMap.Entry e;
            return (this.in(kk) && (e = Short2ByteAVLTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
        }
        
        public byte put(final short k, final byte v) {
            Short2ByteAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append((int)k).append(") out of range [").append(this.bottom ? "-" : String.valueOf((int)this.from)).append(", ").append(this.top ? "-" : String.valueOf((int)this.to)).append(")").toString());
            }
            final byte oldValue = Short2ByteAVLTreeMap.this.put(k, v);
            return Short2ByteAVLTreeMap.this.modified ? this.defRetValue : oldValue;
        }
        
        public byte remove(final short k) {
            Short2ByteAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            final byte oldValue = Short2ByteAVLTreeMap.this.remove(k);
            return Short2ByteAVLTreeMap.this.modified ? oldValue : this.defRetValue;
        }
        
        public int size() {
            final SubmapIterator i = new SubmapIterator();
            int n = 0;
            while (i.hasNext()) {
                ++n;
                i.nextEntry();
            }
            return n;
        }
        
        public boolean isEmpty() {
            return !new SubmapIterator().hasNext();
        }
        
        public ShortComparator comparator() {
            return Short2ByteAVLTreeMap.this.actualComparator;
        }
        
        public Short2ByteSortedMap headMap(final short to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Short2ByteAVLTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Short2ByteSortedMap tailMap(final short from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Short2ByteAVLTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Short2ByteSortedMap subMap(short from, short to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Short2ByteAVLTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Short2ByteAVLTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Short2ByteAVLTreeMap.Entry firstEntry() {
            if (Short2ByteAVLTreeMap.this.tree == null) {
                return null;
            }
            Short2ByteAVLTreeMap.Entry e;
            if (this.bottom) {
                e = Short2ByteAVLTreeMap.this.firstEntry;
            }
            else {
                e = Short2ByteAVLTreeMap.this.locateKey(this.from);
                if (Short2ByteAVLTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Short2ByteAVLTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Short2ByteAVLTreeMap.Entry lastEntry() {
            if (Short2ByteAVLTreeMap.this.tree == null) {
                return null;
            }
            Short2ByteAVLTreeMap.Entry e;
            if (this.top) {
                e = Short2ByteAVLTreeMap.this.lastEntry;
            }
            else {
                e = Short2ByteAVLTreeMap.this.locateKey(this.to);
                if (Short2ByteAVLTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Short2ByteAVLTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public short firstShortKey() {
            final Short2ByteAVLTreeMap.Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public short lastShortKey() {
            final Short2ByteAVLTreeMap.Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractShort2ByteSortedMap.KeySet {
            @Override
            public ShortBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public ShortBidirectionalIterator iterator(final short from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final short k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Short2ByteAVLTreeMap this$0 = submap.this$0;
                            final Short2ByteAVLTreeMap.Entry lastEntry = submap.lastEntry();
                            this.prev = lastEntry;
                            if (this$0.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = submap.this$0.locateKey(k);
                        if (submap.this$0.compare(this.next.key, k) <= 0) {
                            this.prev = this.next;
                            this.next = this.next.next();
                        }
                        else {
                            this.prev = this.next.prev();
                        }
                    }
                }
            }
            
            @Override
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (!Submap.this.bottom && this.prev != null && Short2ByteAVLTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Short2ByteAVLTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Short2ByteMap.Entry> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final short k) {
                super(k);
            }
            
            public Short2ByteMap.Entry next() {
                return this.nextEntry();
            }
            
            public Short2ByteMap.Entry previous() {
                return this.previousEntry();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements ShortListIterator {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final short from) {
                super(from);
            }
            
            public short nextShort() {
                return this.nextEntry().key;
            }
            
            public short previousShort() {
                return this.previousEntry().key;
            }
        }
        
        private final class SubmapValueIterator extends SubmapIterator implements ByteListIterator {
            public byte nextByte() {
                return this.nextEntry().value;
            }
            
            public byte previousByte() {
                return this.previousEntry().value;
            }
        }
    }
}

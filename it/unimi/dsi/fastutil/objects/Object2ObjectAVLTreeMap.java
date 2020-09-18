package it.unimi.dsi.fastutil.objects;

import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import java.io.Serializable;

public class Object2ObjectAVLTreeMap<K, V> extends AbstractObject2ObjectSortedMap<K, V> implements Serializable, Cloneable {
    protected transient Entry<K, V> tree;
    protected int count;
    protected transient Entry<K, V> firstEntry;
    protected transient Entry<K, V> lastEntry;
    protected transient ObjectSortedSet<Object2ObjectMap.Entry<K, V>> entries;
    protected transient ObjectSortedSet<K> keys;
    protected transient ObjectCollection<V> values;
    protected transient boolean modified;
    protected Comparator<? super K> storedComparator;
    protected transient Comparator<? super K> actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    
    public Object2ObjectAVLTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = this.storedComparator;
    }
    
    public Object2ObjectAVLTreeMap(final Comparator<? super K> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Object2ObjectAVLTreeMap(final Map<? extends K, ? extends V> m) {
        this();
        this.putAll(m);
    }
    
    public Object2ObjectAVLTreeMap(final SortedMap<K, V> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends K, ? extends V>)m);
    }
    
    public Object2ObjectAVLTreeMap(final Object2ObjectMap<? extends K, ? extends V> m) {
        this();
        this.putAll((java.util.Map<? extends K, ? extends V>)m);
    }
    
    public Object2ObjectAVLTreeMap(final Object2ObjectSortedMap<K, V> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends K, ? extends V>)m);
    }
    
    public Object2ObjectAVLTreeMap(final K[] k, final V[] v, final Comparator<? super K> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2ObjectAVLTreeMap(final K[] k, final V[] v) {
        this(k, v, null);
    }
    
    final int compare(final K k1, final K k2) {
        return (this.actualComparator == null) ? ((Comparable)k1).compareTo(k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry<K, V> findKey(final K k) {
        Entry<K, V> e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry<K, V> locateKey(final K k) {
        Entry<K, V> e = this.tree;
        Entry<K, V> last = this.tree;
        int cmp;
        for (cmp = 0; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {
            last = e;
        }
        return (cmp == 0) ? e : last;
    }
    
    private void allocatePaths() {
        this.dirPath = new boolean[48];
    }
    
    public V put(final K k, final V v) {
        final Entry<K, V> e = this.add(k);
        final V oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry<K, V> add(final K k) {
        this.modified = false;
        Entry<K, V> e = null;
        if (this.tree != null) {
            Entry<K, V> p = this.tree;
            Entry<K, V> q = null;
            Entry<K, V> y = this.tree;
            Entry<K, V> z = null;
            Entry<K, V> w = null;
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
                    e = new Entry<K, V>(k, this.defRetValue);
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
                    e = new Entry<K, V>(k, this.defRetValue);
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
                    final Entry<K, V> x = y.left;
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
                    final Entry<K, V> x = y.right;
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
        final Entry<K, V> tree = new Entry<K, V>(k, this.defRetValue);
        this.firstEntry = tree;
        this.lastEntry = tree;
        this.tree = tree;
        e = tree;
        this.modified = true;
        return e;
    }
    
    private Entry<K, V> parent(final Entry<K, V> e) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //     5: if_acmpne       10
        //     8: aconst_null    
        //     9: areturn        
        //    10: aload_1         /* e */
        //    11: dup            
        //    12: astore_3        /* y */
        //    13: astore_2        /* x */
        //    14: aload_3         /* y */
        //    15: invokevirtual   it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.succ:()Z
        //    18: ifeq            65
        //    21: aload_3         /* y */
        //    22: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //    25: astore          p
        //    27: aload           p
        //    29: ifnull          41
        //    32: aload           p
        //    34: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //    37: aload_1         /* e */
        //    38: if_acmpeq       62
        //    41: aload_2         /* x */
        //    42: invokevirtual   it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.pred:()Z
        //    45: ifne            56
        //    48: aload_2         /* x */
        //    49: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //    52: astore_2        /* x */
        //    53: goto            41
        //    56: aload_2         /* x */
        //    57: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //    60: astore          p
        //    62: aload           p
        //    64: areturn        
        //    65: aload_2         /* x */
        //    66: invokevirtual   it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.pred:()Z
        //    69: ifeq            116
        //    72: aload_2         /* x */
        //    73: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //    76: astore          p
        //    78: aload           p
        //    80: ifnull          92
        //    83: aload           p
        //    85: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //    88: aload_1         /* e */
        //    89: if_acmpeq       113
        //    92: aload_3         /* y */
        //    93: invokevirtual   it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.succ:()Z
        //    96: ifne            107
        //    99: aload_3         /* y */
        //   100: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //   103: astore_3        /* y */
        //   104: goto            92
        //   107: aload_3         /* y */
        //   108: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //   111: astore          p
        //   113: aload           p
        //   115: areturn        
        //   116: aload_2         /* x */
        //   117: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //   120: astore_2        /* x */
        //   121: aload_3         /* y */
        //   122: getfield        it/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry;
        //   125: astore_3        /* y */
        //   126: goto            14
        //    Signature:
        //  (Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry<TK;TV;>;)Lit/unimi/dsi/fastutil/objects/Object2ObjectAVLTreeMap$Entry<TK;TV;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  e     
        //    StackMapTable: 00 0A 0A FD 00 03 07 00 1E 07 00 1E FC 00 1A 07 00 1E 0E 05 FA 00 02 FC 00 1A 07 00 1E 0E 05 FA 00 02
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.decompiler.ast.TypeAnalysis.isSameType(TypeAnalysis.java:3072)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:790)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1531)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1551)
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
    
    public V remove(final Object k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry<K, V> p = this.tree;
        Entry<K, V> q = null;
        boolean dir = false;
        final K kk = (K)k;
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
            Entry<K, V> r = p.right;
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
                Entry<K, V> s;
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
            final Entry<K, V> y = q;
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
                final Entry<K, V> x = y.right;
                assert x != null;
                if (x.balance() == -1) {
                    assert x.balance() == -1;
                    final Entry<K, V> w = x.left;
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
                final Entry<K, V> x = y.left;
                assert x != null;
                if (x.balance() == 1) {
                    assert x.balance() == 1;
                    final Entry<K, V> w = x.right;
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
    
    public boolean containsValue(final Object v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final V ev = i.next();
            if (Objects.equals(ev, v)) {
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
        final Entry<K, V> entry = null;
        this.lastEntry = entry;
        this.firstEntry = entry;
    }
    
    public boolean containsKey(final Object k) {
        return this.findKey(k) != null;
    }
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public V get(final Object k) {
        final Entry<K, V> e = (Entry<K, V>)this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    public K firstKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public K lastKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> object2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Object2ObjectMap.Entry<K, V>>() {
                final Comparator<? super Object2ObjectMap.Entry<K, V>> comparator = (x, y) -> Object2ObjectAVLTreeMap.this.actualComparator.compare(x.getKey(), y.getKey());
                
                public Comparator<? super Object2ObjectMap.Entry<K, V>> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> iterator(final Object2ObjectMap.Entry<K, V> from) {
                    return new EntryIterator((K)from.getKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    final Entry<K, V> f = (Entry<K, V>)Object2ObjectAVLTreeMap.this.findKey(e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    final Entry<K, V> f = (Entry<K, V>)Object2ObjectAVLTreeMap.this.findKey(e.getKey());
                    if (f == null || !Objects.equals(f.getValue(), e.getValue())) {
                        return false;
                    }
                    Object2ObjectAVLTreeMap.this.remove(f.key);
                    return true;
                }
                
                public int size() {
                    return Object2ObjectAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Object2ObjectAVLTreeMap.this.clear();
                }
                
                public Object2ObjectMap.Entry<K, V> first() {
                    return Object2ObjectAVLTreeMap.this.firstEntry;
                }
                
                public Object2ObjectMap.Entry<K, V> last() {
                    return Object2ObjectAVLTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> subSet(final Object2ObjectMap.Entry<K, V> from, final Object2ObjectMap.Entry<K, V> to) {
                    return (ObjectSortedSet<Object2ObjectMap.Entry<K, V>>)Object2ObjectAVLTreeMap.this.subMap(from.getKey(), to.getKey()).object2ObjectEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> headSet(final Object2ObjectMap.Entry<K, V> to) {
                    return (ObjectSortedSet<Object2ObjectMap.Entry<K, V>>)Object2ObjectAVLTreeMap.this.headMap(to.getKey()).object2ObjectEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> tailSet(final Object2ObjectMap.Entry<K, V> from) {
                    return (ObjectSortedSet<Object2ObjectMap.Entry<K, V>>)Object2ObjectAVLTreeMap.this.tailMap(from.getKey()).object2ObjectEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        if (this.keys == null) {
            this.keys = (ObjectSortedSet<K>)new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>() {
                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }
                
                public boolean contains(final Object k) {
                    return Object2ObjectAVLTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Object2ObjectAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Object2ObjectAVLTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public Comparator<? super K> comparator() {
        return this.actualComparator;
    }
    
    public Object2ObjectSortedMap<K, V> headMap(final K to) {
        return new Submap(null, true, to, false);
    }
    
    public Object2ObjectSortedMap<K, V> tailMap(final K from) {
        return new Submap(from, false, null, true);
    }
    
    public Object2ObjectSortedMap<K, V> subMap(final K from, final K to) {
        return new Submap(from, false, to, false);
    }
    
    public Object2ObjectAVLTreeMap<K, V> clone() {
        Object2ObjectAVLTreeMap<K, V> c;
        try {
            c = (Object2ObjectAVLTreeMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.allocatePaths();
        if (this.count != 0) {
            final Entry<K, V> rp = new Entry<K, V>();
            final Entry<K, V> rq = new Entry<K, V>();
            Entry<K, V> p = rp;
            rp.left(this.tree);
            Entry<K, V> q = rq;
            rq.pred(null);
        Block_4:
            while (true) {
                if (!p.pred()) {
                    final Entry<K, V> e = p.left.clone();
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
                    final Entry<K, V> e = p.right.clone();
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
            final Entry<K, V> e = i.nextEntry();
            s.writeObject(e.key);
            s.writeObject(e.value);
        }
    }
    
    private Entry<K, V> readTree(final ObjectInputStream s, final int n, final Entry<K, V> pred, final Entry<K, V> succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry<K, V> top = new Entry<K, V>((K)s.readObject(), (V)s.readObject());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            final Entry<K, V> top = new Entry<K, V>((K)s.readObject(), (V)s.readObject());
            top.right(new Entry<K, V>((K)s.readObject(), (V)s.readObject()));
            top.right.pred(top);
            top.balance(1);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry<K, V> top2 = new Entry<K, V>();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = (K)s.readObject();
        top2.value = (V)s.readObject();
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
            Entry<K, V> e;
            for (e = this.tree; e.left() != null; e = e.left()) {}
            this.firstEntry = e;
            for (e = this.tree; e.right() != null; e = e.right()) {}
            this.lastEntry = e;
        }
    }
    
    private static final class Entry<K, V> extends BasicEntry<K, V> implements Cloneable {
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        private static final int BALANCE_MASK = 255;
        Entry<K, V> left;
        Entry<K, V> right;
        int info;
        
        Entry() {
            super(null, null);
        }
        
        Entry(final K k, final V v) {
            super(k, v);
            this.info = -1073741824;
        }
        
        Entry<K, V> left() {
            return ((this.info & 0x40000000) != 0x0) ? null : this.left;
        }
        
        Entry<K, V> right() {
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
        
        void pred(final Entry<K, V> pred) {
            this.info |= 0x40000000;
            this.left = pred;
        }
        
        void succ(final Entry<K, V> succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }
        
        void left(final Entry<K, V> left) {
            this.info &= 0xBFFFFFFF;
            this.left = left;
        }
        
        void right(final Entry<K, V> right) {
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
        
        Entry<K, V> next() {
            Entry<K, V> next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0x0) {
                while ((next.info & 0x40000000) == 0x0) {
                    next = next.left;
                }
            }
            return next;
        }
        
        Entry<K, V> prev() {
            Entry<K, V> prev = this.left;
            if ((this.info & 0x40000000) == 0x0) {
                while ((prev.info & Integer.MIN_VALUE) == 0x0) {
                    prev = prev.right;
                }
            }
            return prev;
        }
        
        @Override
        public V setValue(final V value) {
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        public Entry<K, V> clone() {
            Entry<K, V> c;
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
            final Map.Entry<K, V> e = (Map.Entry<K, V>)o;
            return Objects.equals(this.key, e.getKey()) && Objects.equals(this.value, e.getValue());
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode() ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append(this.key).append("=>").append(this.value).toString();
        }
    }
    
    private class TreeIterator {
        Entry<K, V> prev;
        Entry<K, V> next;
        Entry<K, V> curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Object2ObjectAVLTreeMap.this.firstEntry;
        }
        
        TreeIterator(final K k) {
            this.index = 0;
            final Entry<K, V> locateKey = Object2ObjectAVLTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Object2ObjectAVLTreeMap.this.compare(this.next.key, k) <= 0) {
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
        
        Entry<K, V> nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Entry<K, V> next = this.next;
            this.prev = next;
            this.curr = next;
            ++this.index;
            this.updateNext();
            return this.curr;
        }
        
        void updatePrevious() {
            this.prev = this.prev.prev();
        }
        
        Entry<K, V> previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final Entry<K, V> prev = this.prev;
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
            final Entry<K, V> curr = this.curr;
            this.prev = curr;
            this.next = curr;
            this.updatePrevious();
            this.updateNext();
            Object2ObjectAVLTreeMap.this.remove(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Object2ObjectMap.Entry<K, V>> {
        EntryIterator() {
        }
        
        EntryIterator(final K k) {
            super(k);
        }
        
        public Object2ObjectMap.Entry<K, V> next() {
            return this.nextEntry();
        }
        
        public Object2ObjectMap.Entry<K, V> previous() {
            return this.previousEntry();
        }
        
        @Override
        public void set(final Object2ObjectMap.Entry<K, V> ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Object2ObjectMap.Entry<K, V> ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements ObjectListIterator<K> {
        public KeyIterator() {
        }
        
        public KeyIterator(final K k) {
            super(k);
        }
        
        public K next() {
            return this.nextEntry().key;
        }
        
        public K previous() {
            return this.previousEntry().key;
        }
    }
    
    private class KeySet extends AbstractObject2ObjectSortedMap.KeySet {
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeyIterator();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements ObjectListIterator<V> {
        public V next() {
            return this.nextEntry().value;
        }
        
        public V previous() {
            return this.previousEntry().value;
        }
    }
    
    private final class Submap extends AbstractObject2ObjectSortedMap<K, V> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        K from;
        K to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Object2ObjectMap.Entry<K, V>> entries;
        protected transient ObjectSortedSet<K> keys;
        protected transient ObjectCollection<V> values;
        final /* synthetic */ Object2ObjectAVLTreeMap this$0;
        
        public Submap(final K from, final boolean bottom, final K to, final boolean top) {
            if (!bottom && !top && Object2ObjectAVLTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append(from).append(") is larger than end key (").append(to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Object2ObjectAVLTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final K k) {
            return (this.bottom || Object2ObjectAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Object2ObjectAVLTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> object2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Object2ObjectMap.Entry<K, V>>() {
                    @Override
                    public ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> iterator(final Object2ObjectMap.Entry<K, V> from) {
                        return new SubmapEntryIterator((K)from.getKey());
                    }
                    
                    public Comparator<? super Object2ObjectMap.Entry<K, V>> comparator() {
                        return Object2ObjectAVLTreeMap.this.object2ObjectEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        final Object2ObjectAVLTreeMap.Entry<K, V> f = (Object2ObjectAVLTreeMap.Entry<K, V>)Object2ObjectAVLTreeMap.this.findKey(e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        final Object2ObjectAVLTreeMap.Entry<K, V> f = (Object2ObjectAVLTreeMap.Entry<K, V>)Object2ObjectAVLTreeMap.this.findKey(e.getKey());
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
                    
                    public Object2ObjectMap.Entry<K, V> first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Object2ObjectMap.Entry<K, V> last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> subSet(final Object2ObjectMap.Entry<K, V> from, final Object2ObjectMap.Entry<K, V> to) {
                        return (ObjectSortedSet<Object2ObjectMap.Entry<K, V>>)Submap.this.subMap(from.getKey(), to.getKey()).object2ObjectEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> headSet(final Object2ObjectMap.Entry<K, V> to) {
                        return (ObjectSortedSet<Object2ObjectMap.Entry<K, V>>)Submap.this.headMap(to.getKey()).object2ObjectEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> tailSet(final Object2ObjectMap.Entry<K, V> from) {
                        return (ObjectSortedSet<Object2ObjectMap.Entry<K, V>>)Submap.this.tailMap(from.getKey()).object2ObjectEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = (ObjectSortedSet<K>)new KeySet();
            }
            return this.keys;
        }
        
        @Override
        public ObjectCollection<V> values() {
            if (this.values == null) {
                this.values = new AbstractObjectCollection<V>() {
                    @Override
                    public ObjectIterator<V> iterator() {
                        return new SubmapValueIterator();
                    }
                    
                    public boolean contains(final Object k) {
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
        
        public boolean containsKey(final Object k) {
            return this.in(k) && Object2ObjectAVLTreeMap.this.containsKey(k);
        }
        
        public boolean containsValue(final Object v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final Object ev = i.nextEntry().value;
                if (Objects.equals(ev, v)) {
                    return true;
                }
            }
            return false;
        }
        
        public V get(final Object k) {
            final K kk = (K)k;
            final Object2ObjectAVLTreeMap.Entry<K, V> e;
            return (V)((this.in(kk) && (e = Object2ObjectAVLTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue);
        }
        
        public V put(final K k, final V v) {
            Object2ObjectAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append(k).append(") out of range [").append(this.bottom ? "-" : String.valueOf(this.from)).append(", ").append(this.top ? "-" : String.valueOf(this.to)).append(")").toString());
            }
            final V oldValue = Object2ObjectAVLTreeMap.this.put(k, v);
            return (V)(Object2ObjectAVLTreeMap.this.modified ? this.defRetValue : oldValue);
        }
        
        public V remove(final Object k) {
            Object2ObjectAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                return (V)this.defRetValue;
            }
            final V oldValue = Object2ObjectAVLTreeMap.this.remove(k);
            return (V)(Object2ObjectAVLTreeMap.this.modified ? oldValue : this.defRetValue);
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
        
        public Comparator<? super K> comparator() {
            return Object2ObjectAVLTreeMap.this.actualComparator;
        }
        
        public Object2ObjectSortedMap<K, V> headMap(final K to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Object2ObjectAVLTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Object2ObjectSortedMap<K, V> tailMap(final K from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Object2ObjectAVLTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Object2ObjectSortedMap<K, V> subMap(K from, K to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Object2ObjectAVLTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Object2ObjectAVLTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Object2ObjectAVLTreeMap.Entry<K, V> firstEntry() {
            if (Object2ObjectAVLTreeMap.this.tree == null) {
                return null;
            }
            Object2ObjectAVLTreeMap.Entry<K, V> e;
            if (this.bottom) {
                e = Object2ObjectAVLTreeMap.this.firstEntry;
            }
            else {
                e = Object2ObjectAVLTreeMap.this.locateKey(this.from);
                if (Object2ObjectAVLTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Object2ObjectAVLTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Object2ObjectAVLTreeMap.Entry<K, V> lastEntry() {
            if (Object2ObjectAVLTreeMap.this.tree == null) {
                return null;
            }
            Object2ObjectAVLTreeMap.Entry<K, V> e;
            if (this.top) {
                e = Object2ObjectAVLTreeMap.this.lastEntry;
            }
            else {
                e = Object2ObjectAVLTreeMap.this.locateKey(this.to);
                if (Object2ObjectAVLTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Object2ObjectAVLTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public K firstKey() {
            final Object2ObjectAVLTreeMap.Entry<K, V> e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public K lastKey() {
            final Object2ObjectAVLTreeMap.Entry<K, V> e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractObject2ObjectSortedMap.KeySet {
            @Override
            public ObjectBidirectionalIterator<K> iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public ObjectBidirectionalIterator<K> iterator(final K from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final K k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Object2ObjectAVLTreeMap this$0 = submap.this$0;
                            final Object2ObjectAVLTreeMap.Entry<K, V> lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Object2ObjectAVLTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Object2ObjectAVLTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Object2ObjectMap.Entry<K, V>> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final K k) {
                super(k);
            }
            
            public Object2ObjectMap.Entry<K, V> next() {
                return this.nextEntry();
            }
            
            public Object2ObjectMap.Entry<K, V> previous() {
                return this.previousEntry();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements ObjectListIterator<K> {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final K from) {
                super(from);
            }
            
            public K next() {
                return this.nextEntry().key;
            }
            
            public K previous() {
                return this.previousEntry().key;
            }
        }
        
        private final class SubmapValueIterator extends SubmapIterator implements ObjectListIterator<V> {
            public V next() {
                return this.nextEntry().value;
            }
            
            public V previous() {
                return this.previousEntry().value;
            }
        }
    }
}

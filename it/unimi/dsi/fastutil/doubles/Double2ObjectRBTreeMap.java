package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import java.util.SortedSet;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;

public class Double2ObjectRBTreeMap<V> extends AbstractDouble2ObjectSortedMap<V> implements Serializable, Cloneable {
    protected transient Entry<V> tree;
    protected int count;
    protected transient Entry<V> firstEntry;
    protected transient Entry<V> lastEntry;
    protected transient ObjectSortedSet<Double2ObjectMap.Entry<V>> entries;
    protected transient DoubleSortedSet keys;
    protected transient ObjectCollection<V> values;
    protected transient boolean modified;
    protected Comparator<? super Double> storedComparator;
    protected transient DoubleComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    private transient Entry<V>[] nodePath;
    
    public Double2ObjectRBTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = DoubleComparators.asDoubleComparator(this.storedComparator);
    }
    
    public Double2ObjectRBTreeMap(final Comparator<? super Double> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Double2ObjectRBTreeMap(final Map<? extends Double, ? extends V> m) {
        this();
        this.putAll(m);
    }
    
    public Double2ObjectRBTreeMap(final SortedMap<Double, V> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends Double, ? extends V>)m);
    }
    
    public Double2ObjectRBTreeMap(final Double2ObjectMap<? extends V> m) {
        this();
        this.putAll((java.util.Map<? extends Double, ? extends V>)m);
    }
    
    public Double2ObjectRBTreeMap(final Double2ObjectSortedMap<V> m) {
        this((Comparator)m.comparator());
        this.putAll((java.util.Map<? extends Double, ? extends V>)m);
    }
    
    public Double2ObjectRBTreeMap(final double[] k, final V[] v, final Comparator<? super Double> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Double2ObjectRBTreeMap(final double[] k, final V[] v) {
        this(k, v, null);
    }
    
    final int compare(final double k1, final double k2) {
        return (this.actualComparator == null) ? Double.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry<V> findKey(final double k) {
        Entry<V> e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry<V> locateKey(final double k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        it/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap$Entry;
        //     4: astore_3        /* e */
        //     5: aload_0         /* this */
        //     6: getfield        it/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap$Entry;
        //     9: astore          last
        //    11: iconst_0       
        //    12: istore          cmp
        //    14: aload_3         /* e */
        //    15: ifnull          56
        //    18: aload_0         /* this */
        //    19: dload_1         /* k */
        //    20: aload_3         /* e */
        //    21: getfield        it/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap$Entry.key:D
        //    24: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap.compare:(DD)I
        //    27: dup            
        //    28: istore          cmp
        //    30: ifeq            56
        //    33: aload_3         /* e */
        //    34: astore          last
        //    36: iload           cmp
        //    38: ifge            48
        //    41: aload_3         /* e */
        //    42: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap$Entry.left:()Lit/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap$Entry;
        //    45: goto            52
        //    48: aload_3         /* e */
        //    49: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap$Entry.right:()Lit/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap$Entry;
        //    52: astore_3        /* e */
        //    53: goto            14
        //    56: iload           cmp
        //    58: ifne            65
        //    61: aload_3         /* e */
        //    62: goto            67
        //    65: aload           last
        //    67: areturn        
        //    Signature:
        //  (D)Lit/unimi/dsi/fastutil/doubles/Double2ObjectRBTreeMap$Entry<TV;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 06 FE 00 0E 07 00 1E 07 00 1E 01 21 43 07 00 1E 03 08 41 07 00 1E
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.resolve(ParameterizedType.java:108)
        //     at com.strobel.assembler.metadata.MetadataHelper.getBaseType(MetadataHelper.java:686)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:813)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2503)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    private void allocatePaths() {
        this.dirPath = new boolean[64];
        this.nodePath = new Entry[64];
    }
    
    public V put(final double k, final V v) {
        final Entry<V> e = this.add(k);
        final V oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry<V> add(final double k) {
        this.modified = false;
        int maxDepth = 0;
        Entry<V> e = null;
        Label_0919: {
            if (this.tree != null) {
                Entry<V> p = this.tree;
                int i = 0;
                int cmp;
                while ((cmp = this.compare(k, p.key)) != 0) {
                    this.nodePath[i] = p;
                    final boolean[] dirPath = this.dirPath;
                    final int n = i++;
                    final boolean b = cmp > 0;
                    dirPath[n] = b;
                    if (b) {
                        if (!p.succ()) {
                            p = p.right;
                            continue;
                        }
                        ++this.count;
                        e = new Entry<V>(k, this.defRetValue);
                        if (p.right == null) {
                            this.lastEntry = e;
                        }
                        e.left = p;
                        e.right = p.right;
                        p.right(e);
                    }
                    else {
                        if (!p.pred()) {
                            p = p.left;
                            continue;
                        }
                        ++this.count;
                        e = new Entry<V>(k, this.defRetValue);
                        if (p.left == null) {
                            this.firstEntry = e;
                        }
                        e.right = p;
                        e.left = p.left;
                        p.left(e);
                    }
                    this.modified = true;
                    maxDepth = i--;
                    while (i > 0 && !this.nodePath[i].black()) {
                        if (!this.dirPath[i - 1]) {
                            Entry<V> y = this.nodePath[i - 1].right;
                            if (!this.nodePath[i - 1].succ() && !y.black()) {
                                this.nodePath[i].black(true);
                                y.black(true);
                                this.nodePath[i - 1].black(false);
                                i -= 2;
                            }
                            else {
                                if (!this.dirPath[i]) {
                                    y = this.nodePath[i];
                                }
                                else {
                                    final Entry<V> x = this.nodePath[i];
                                    y = x.right;
                                    x.right = y.left;
                                    y.left = x;
                                    this.nodePath[i - 1].left = y;
                                    if (y.pred()) {
                                        y.pred(false);
                                        x.succ(y);
                                    }
                                }
                                final Entry<V> x = this.nodePath[i - 1];
                                x.black(false);
                                y.black(true);
                                x.left = y.right;
                                y.right = x;
                                if (i < 2) {
                                    this.tree = y;
                                }
                                else if (this.dirPath[i - 2]) {
                                    this.nodePath[i - 2].right = y;
                                }
                                else {
                                    this.nodePath[i - 2].left = y;
                                }
                                if (y.succ()) {
                                    y.succ(false);
                                    x.pred(y);
                                    break;
                                }
                                break;
                            }
                        }
                        else {
                            Entry<V> y = this.nodePath[i - 1].left;
                            if (!this.nodePath[i - 1].pred() && !y.black()) {
                                this.nodePath[i].black(true);
                                y.black(true);
                                this.nodePath[i - 1].black(false);
                                i -= 2;
                            }
                            else {
                                if (this.dirPath[i]) {
                                    y = this.nodePath[i];
                                }
                                else {
                                    final Entry<V> x = this.nodePath[i];
                                    y = x.left;
                                    x.left = y.right;
                                    y.right = x;
                                    this.nodePath[i - 1].right = y;
                                    if (y.succ()) {
                                        y.succ(false);
                                        x.pred(y);
                                    }
                                }
                                final Entry<V> x = this.nodePath[i - 1];
                                x.black(false);
                                y.black(true);
                                x.right = y.left;
                                y.left = x;
                                if (i < 2) {
                                    this.tree = y;
                                }
                                else if (this.dirPath[i - 2]) {
                                    this.nodePath[i - 2].right = y;
                                }
                                else {
                                    this.nodePath[i - 2].left = y;
                                }
                                if (y.pred()) {
                                    y.pred(false);
                                    x.succ(y);
                                    break;
                                }
                                break;
                            }
                        }
                    }
                    break Label_0919;
                }
                while (i-- != 0) {
                    this.nodePath[i] = null;
                }
                return p;
            }
            ++this.count;
            final Entry<V> tree = new Entry<V>(k, this.defRetValue);
            this.firstEntry = tree;
            this.lastEntry = tree;
            this.tree = tree;
            e = tree;
        }
        this.tree.black(true);
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return e;
    }
    
    public V remove(final double k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry<V> p = this.tree;
        int i = 0;
        final double kk = k;
        int cmp;
        while ((cmp = this.compare(kk, p.key)) != 0) {
            this.dirPath[i] = (cmp > 0);
            this.nodePath[i] = p;
            if (this.dirPath[i++]) {
                if ((p = p.right()) == null) {
                    while (i-- != 0) {
                        this.nodePath[i] = null;
                    }
                    return this.defRetValue;
                }
                continue;
            }
            else {
                if ((p = p.left()) == null) {
                    while (i-- != 0) {
                        this.nodePath[i] = null;
                    }
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
                if (i == 0) {
                    this.tree = p.left;
                }
                else if (this.dirPath[i - 1]) {
                    this.nodePath[i - 1].succ(p.right);
                }
                else {
                    this.nodePath[i - 1].pred(p.left);
                }
            }
            else {
                p.prev().right = p.right;
                if (i == 0) {
                    this.tree = p.left;
                }
                else if (this.dirPath[i - 1]) {
                    this.nodePath[i - 1].right = p.left;
                }
                else {
                    this.nodePath[i - 1].left = p.left;
                }
            }
        }
        else {
            Entry<V> r = p.right;
            if (r.pred()) {
                r.left = p.left;
                r.pred(p.pred());
                if (!r.pred()) {
                    r.prev().right = r;
                }
                if (i == 0) {
                    this.tree = r;
                }
                else if (this.dirPath[i - 1]) {
                    this.nodePath[i - 1].right = r;
                }
                else {
                    this.nodePath[i - 1].left = r;
                }
                final boolean color = r.black();
                r.black(p.black());
                p.black(color);
                this.dirPath[i] = true;
                this.nodePath[i++] = r;
            }
            else {
                final int j = i++;
                Entry<V> s;
                while (true) {
                    this.dirPath[i] = false;
                    this.nodePath[i++] = r;
                    s = r.left;
                    if (s.pred()) {
                        break;
                    }
                    r = s;
                }
                this.dirPath[j] = true;
                this.nodePath[j] = s;
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
                s.right(p.right);
                final boolean color = s.black();
                s.black(p.black());
                p.black(color);
                if (j == 0) {
                    this.tree = s;
                }
                else if (this.dirPath[j - 1]) {
                    this.nodePath[j - 1].right = s;
                }
                else {
                    this.nodePath[j - 1].left = s;
                }
            }
        }
        int maxDepth = i;
        if (p.black()) {
            while (i > 0) {
                if ((this.dirPath[i - 1] && !this.nodePath[i - 1].succ()) || (!this.dirPath[i - 1] && !this.nodePath[i - 1].pred())) {
                    final Entry<V> x = this.dirPath[i - 1] ? this.nodePath[i - 1].right : this.nodePath[i - 1].left;
                    if (!x.black()) {
                        x.black(true);
                        break;
                    }
                }
                if (!this.dirPath[i - 1]) {
                    Entry<V> w = this.nodePath[i - 1].right;
                    if (!w.black()) {
                        w.black(true);
                        this.nodePath[i - 1].black(false);
                        this.nodePath[i - 1].right = w.left;
                        w.left = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        this.nodePath[i] = this.nodePath[i - 1];
                        this.dirPath[i] = false;
                        this.nodePath[i - 1] = w;
                        if (maxDepth == i++) {
                            ++maxDepth;
                        }
                        w = this.nodePath[i - 1].right;
                    }
                    if ((w.pred() || w.left.black()) && (w.succ() || w.right.black())) {
                        w.black(false);
                    }
                    else {
                        if (w.succ() || w.right.black()) {
                            final Entry<V> y = w.left;
                            y.black(true);
                            w.black(false);
                            w.left = y.right;
                            y.right = w;
                            final Entry<V> entry = this.nodePath[i - 1];
                            final Entry<V> right = y;
                            entry.right = right;
                            w = right;
                            if (w.succ()) {
                                w.succ(false);
                                w.right.pred(w);
                            }
                        }
                        w.black(this.nodePath[i - 1].black());
                        this.nodePath[i - 1].black(true);
                        w.right.black(true);
                        this.nodePath[i - 1].right = w.left;
                        w.left = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        if (w.pred()) {
                            w.pred(false);
                            this.nodePath[i - 1].succ(w);
                            break;
                        }
                        break;
                    }
                }
                else {
                    Entry<V> w = this.nodePath[i - 1].left;
                    if (!w.black()) {
                        w.black(true);
                        this.nodePath[i - 1].black(false);
                        this.nodePath[i - 1].left = w.right;
                        w.right = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        this.nodePath[i] = this.nodePath[i - 1];
                        this.dirPath[i] = true;
                        this.nodePath[i - 1] = w;
                        if (maxDepth == i++) {
                            ++maxDepth;
                        }
                        w = this.nodePath[i - 1].left;
                    }
                    if ((w.pred() || w.left.black()) && (w.succ() || w.right.black())) {
                        w.black(false);
                    }
                    else {
                        if (w.pred() || w.left.black()) {
                            final Entry<V> y = w.right;
                            y.black(true);
                            w.black(false);
                            w.right = y.left;
                            y.left = w;
                            final Entry<V> entry2 = this.nodePath[i - 1];
                            final Entry<V> left = y;
                            entry2.left = left;
                            w = left;
                            if (w.pred()) {
                                w.pred(false);
                                w.left.succ(w);
                            }
                        }
                        w.black(this.nodePath[i - 1].black());
                        this.nodePath[i - 1].black(true);
                        w.left.black(true);
                        this.nodePath[i - 1].left = w.right;
                        w.right = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        }
                        else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        }
                        else {
                            this.nodePath[i - 2].left = w;
                        }
                        if (w.succ()) {
                            w.succ(false);
                            this.nodePath[i - 1].pred(w);
                            break;
                        }
                        break;
                    }
                }
                --i;
            }
            if (this.tree != null) {
                this.tree.black(true);
            }
        }
        this.modified = true;
        --this.count;
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return p.value;
    }
    
    public boolean containsValue(final Object v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final Object ev = i.next();
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
        final Entry<V> entry = null;
        this.lastEntry = entry;
        this.firstEntry = entry;
    }
    
    public boolean containsKey(final double k) {
        return this.findKey(k) != null;
    }
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public V get(final double k) {
        final Entry<V> e = this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    public double firstDoubleKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public double lastDoubleKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    public ObjectSortedSet<Double2ObjectMap.Entry<V>> double2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Double2ObjectMap.Entry<V>>() {
                final Comparator<? super Double2ObjectMap.Entry<V>> comparator = (x, y) -> Double2ObjectRBTreeMap.this.actualComparator.compare(x.getDoubleKey(), y.getDoubleKey());
                
                public Comparator<? super Double2ObjectMap.Entry<V>> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> iterator(final Double2ObjectMap.Entry<V> from) {
                    return new EntryIterator(from.getDoubleKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                        return false;
                    }
                    final Entry<V> f = Double2ObjectRBTreeMap.this.findKey((double)e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                        return false;
                    }
                    final Entry<V> f = Double2ObjectRBTreeMap.this.findKey((double)e.getKey());
                    if (f == null || !Objects.equals(f.getValue(), e.getValue())) {
                        return false;
                    }
                    Double2ObjectRBTreeMap.this.remove(f.key);
                    return true;
                }
                
                public int size() {
                    return Double2ObjectRBTreeMap.this.count;
                }
                
                public void clear() {
                    Double2ObjectRBTreeMap.this.clear();
                }
                
                public Double2ObjectMap.Entry<V> first() {
                    return Double2ObjectRBTreeMap.this.firstEntry;
                }
                
                public Double2ObjectMap.Entry<V> last() {
                    return Double2ObjectRBTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Double2ObjectMap.Entry<V>> subSet(final Double2ObjectMap.Entry<V> from, final Double2ObjectMap.Entry<V> to) {
                    return Double2ObjectRBTreeMap.this.subMap(from.getDoubleKey(), to.getDoubleKey()).double2ObjectEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Double2ObjectMap.Entry<V>> headSet(final Double2ObjectMap.Entry<V> to) {
                    return Double2ObjectRBTreeMap.this.headMap(to.getDoubleKey()).double2ObjectEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Double2ObjectMap.Entry<V>> tailSet(final Double2ObjectMap.Entry<V> from) {
                    return Double2ObjectRBTreeMap.this.tailMap(from.getDoubleKey()).double2ObjectEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public DoubleSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
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
                    return Double2ObjectRBTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Double2ObjectRBTreeMap.this.count;
                }
                
                public void clear() {
                    Double2ObjectRBTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public DoubleComparator comparator() {
        return this.actualComparator;
    }
    
    public Double2ObjectSortedMap<V> headMap(final double to) {
        return new Submap(0.0, true, to, false);
    }
    
    public Double2ObjectSortedMap<V> tailMap(final double from) {
        return new Submap(from, false, 0.0, true);
    }
    
    public Double2ObjectSortedMap<V> subMap(final double from, final double to) {
        return new Submap(from, false, to, false);
    }
    
    public Double2ObjectRBTreeMap<V> clone() {
        Double2ObjectRBTreeMap<V> c;
        try {
            c = (Double2ObjectRBTreeMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.allocatePaths();
        if (this.count != 0) {
            final Entry<V> rp = new Entry<V>();
            final Entry<V> rq = new Entry<V>();
            Entry<V> p = rp;
            rp.left(this.tree);
            Entry<V> q = rq;
            rq.pred(null);
        Block_4:
            while (true) {
                if (!p.pred()) {
                    final Entry<V> e = p.left.clone();
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
                    final Entry<V> e = p.right.clone();
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
            final Entry<V> e = i.nextEntry();
            s.writeDouble(e.key);
            s.writeObject(e.value);
        }
    }
    
    private Entry<V> readTree(final ObjectInputStream s, final int n, final Entry<V> pred, final Entry<V> succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry<V> top = new Entry<V>(s.readDouble(), (V)s.readObject());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            final Entry<V> top = new Entry<V>(s.readDouble(), (V)s.readObject());
            top.black(true);
            top.right(new Entry<V>(s.readDouble(), (V)s.readObject()));
            top.right.pred(top);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry<V> top2 = new Entry<V>();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = s.readDouble();
        top2.value = (V)s.readObject();
        top2.black(true);
        top2.right(this.readTree(s, rightN, top2, succ));
        if (n + 2 == (n + 2 & -(n + 2))) {
            top2.right.black(false);
        }
        return top2;
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.setActualComparator();
        this.allocatePaths();
        if (this.count != 0) {
            this.tree = this.readTree(s, this.count, null, null);
            Entry<V> e;
            for (e = this.tree; e.left() != null; e = e.left()) {}
            this.firstEntry = e;
            for (e = this.tree; e.right() != null; e = e.right()) {}
            this.lastEntry = e;
        }
    }
    
    private static final class Entry<V> extends BasicEntry<V> implements Cloneable {
        private static final int BLACK_MASK = 1;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        Entry<V> left;
        Entry<V> right;
        int info;
        
        Entry() {
            super(0.0, null);
        }
        
        Entry(final double k, final V v) {
            super(k, v);
            this.info = -1073741824;
        }
        
        Entry<V> left() {
            return ((this.info & 0x40000000) != 0x0) ? null : this.left;
        }
        
        Entry<V> right() {
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
        
        void pred(final Entry<V> pred) {
            this.info |= 0x40000000;
            this.left = pred;
        }
        
        void succ(final Entry<V> succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }
        
        void left(final Entry<V> left) {
            this.info &= 0xBFFFFFFF;
            this.left = left;
        }
        
        void right(final Entry<V> right) {
            this.info &= Integer.MAX_VALUE;
            this.right = right;
        }
        
        boolean black() {
            return (this.info & 0x1) != 0x0;
        }
        
        void black(final boolean black) {
            if (black) {
                this.info |= 0x1;
            }
            else {
                this.info &= 0xFFFFFFFE;
            }
        }
        
        Entry<V> next() {
            Entry<V> next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0x0) {
                while ((next.info & 0x40000000) == 0x0) {
                    next = next.left;
                }
            }
            return next;
        }
        
        Entry<V> prev() {
            Entry<V> prev = this.left;
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
        
        public Entry<V> clone() {
            Entry<V> c;
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
            final Map.Entry<Double, V> e = (Map.Entry<Double, V>)o;
            return Double.doubleToLongBits(this.key) == Double.doubleToLongBits((double)e.getKey()) && Objects.equals(this.value, e.getValue());
        }
        
        @Override
        public int hashCode() {
            return HashCommon.double2int(this.key) ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append(this.key).append("=>").append(this.value).toString();
        }
    }
    
    private class TreeIterator {
        Entry<V> prev;
        Entry<V> next;
        Entry<V> curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Double2ObjectRBTreeMap.this.firstEntry;
        }
        
        TreeIterator(final double k) {
            this.index = 0;
            final Entry<V> locateKey = Double2ObjectRBTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Double2ObjectRBTreeMap.this.compare(this.next.key, k) <= 0) {
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
        
        Entry<V> nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Entry<V> next = this.next;
            this.prev = next;
            this.curr = next;
            ++this.index;
            this.updateNext();
            return this.curr;
        }
        
        void updatePrevious() {
            this.prev = this.prev.prev();
        }
        
        Entry<V> previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final Entry<V> prev = this.prev;
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
            final Entry<V> curr = this.curr;
            this.prev = curr;
            this.next = curr;
            this.updatePrevious();
            this.updateNext();
            Double2ObjectRBTreeMap.this.remove(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Double2ObjectMap.Entry<V>> {
        EntryIterator() {
        }
        
        EntryIterator(final double k) {
            super(k);
        }
        
        public Double2ObjectMap.Entry<V> next() {
            return this.nextEntry();
        }
        
        public Double2ObjectMap.Entry<V> previous() {
            return this.previousEntry();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements DoubleListIterator {
        public KeyIterator() {
        }
        
        public KeyIterator(final double k) {
            super(k);
        }
        
        public double nextDouble() {
            return this.nextEntry().key;
        }
        
        public double previousDouble() {
            return this.previousEntry().key;
        }
    }
    
    private class KeySet extends AbstractDouble2ObjectSortedMap.KeySet {
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
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
    
    private final class Submap extends AbstractDouble2ObjectSortedMap<V> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        double from;
        double to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Double2ObjectMap.Entry<V>> entries;
        protected transient DoubleSortedSet keys;
        protected transient ObjectCollection<V> values;
        final /* synthetic */ Double2ObjectRBTreeMap this$0;
        
        public Submap(final double from, final boolean bottom, final double to, final boolean top) {
            if (!bottom && !top && Double2ObjectRBTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append(from).append(") is larger than end key (").append(to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Double2ObjectRBTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final double k) {
            return (this.bottom || Double2ObjectRBTreeMap.this.compare(k, this.from) >= 0) && (this.top || Double2ObjectRBTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Double2ObjectMap.Entry<V>> double2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Double2ObjectMap.Entry<V>>() {
                    @Override
                    public ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> iterator(final Double2ObjectMap.Entry<V> from) {
                        return new SubmapEntryIterator(from.getDoubleKey());
                    }
                    
                    public Comparator<? super Double2ObjectMap.Entry<V>> comparator() {
                        return Double2ObjectRBTreeMap.this.double2ObjectEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                            return false;
                        }
                        final Double2ObjectRBTreeMap.Entry<V> f = Double2ObjectRBTreeMap.this.findKey((double)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                            return false;
                        }
                        final Double2ObjectRBTreeMap.Entry<V> f = Double2ObjectRBTreeMap.this.findKey((double)e.getKey());
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
                    
                    public Double2ObjectMap.Entry<V> first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Double2ObjectMap.Entry<V> last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Double2ObjectMap.Entry<V>> subSet(final Double2ObjectMap.Entry<V> from, final Double2ObjectMap.Entry<V> to) {
                        return Submap.this.subMap(from.getDoubleKey(), to.getDoubleKey()).double2ObjectEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Double2ObjectMap.Entry<V>> headSet(final Double2ObjectMap.Entry<V> to) {
                        return Submap.this.headMap(to.getDoubleKey()).double2ObjectEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Double2ObjectMap.Entry<V>> tailSet(final Double2ObjectMap.Entry<V> from) {
                        return Submap.this.tailMap(from.getDoubleKey()).double2ObjectEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public DoubleSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet();
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
        
        public boolean containsKey(final double k) {
            return this.in(k) && Double2ObjectRBTreeMap.this.containsKey(k);
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
        
        public V get(final double k) {
            final double kk = k;
            final Double2ObjectRBTreeMap.Entry<V> e;
            return (V)((this.in(kk) && (e = Double2ObjectRBTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue);
        }
        
        public V put(final double k, final V v) {
            Double2ObjectRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append(k).append(") out of range [").append(this.bottom ? "-" : String.valueOf(this.from)).append(", ").append(this.top ? "-" : String.valueOf(this.to)).append(")").toString());
            }
            final V oldValue = Double2ObjectRBTreeMap.this.put(k, v);
            return (V)(Double2ObjectRBTreeMap.this.modified ? this.defRetValue : oldValue);
        }
        
        public V remove(final double k) {
            Double2ObjectRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                return (V)this.defRetValue;
            }
            final V oldValue = Double2ObjectRBTreeMap.this.remove(k);
            return (V)(Double2ObjectRBTreeMap.this.modified ? oldValue : this.defRetValue);
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
        
        public DoubleComparator comparator() {
            return Double2ObjectRBTreeMap.this.actualComparator;
        }
        
        public Double2ObjectSortedMap<V> headMap(final double to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Double2ObjectRBTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Double2ObjectSortedMap<V> tailMap(final double from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Double2ObjectRBTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Double2ObjectSortedMap<V> subMap(double from, double to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Double2ObjectRBTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Double2ObjectRBTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Double2ObjectRBTreeMap.Entry<V> firstEntry() {
            if (Double2ObjectRBTreeMap.this.tree == null) {
                return null;
            }
            Double2ObjectRBTreeMap.Entry<V> e;
            if (this.bottom) {
                e = Double2ObjectRBTreeMap.this.firstEntry;
            }
            else {
                e = Double2ObjectRBTreeMap.this.locateKey(this.from);
                if (Double2ObjectRBTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Double2ObjectRBTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Double2ObjectRBTreeMap.Entry<V> lastEntry() {
            if (Double2ObjectRBTreeMap.this.tree == null) {
                return null;
            }
            Double2ObjectRBTreeMap.Entry<V> e;
            if (this.top) {
                e = Double2ObjectRBTreeMap.this.lastEntry;
            }
            else {
                e = Double2ObjectRBTreeMap.this.locateKey(this.to);
                if (Double2ObjectRBTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Double2ObjectRBTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public double firstDoubleKey() {
            final Double2ObjectRBTreeMap.Entry<V> e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public double lastDoubleKey() {
            final Double2ObjectRBTreeMap.Entry<V> e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractDouble2ObjectSortedMap.KeySet {
            @Override
            public DoubleBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public DoubleBidirectionalIterator iterator(final double from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final double k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Double2ObjectRBTreeMap this$0 = submap.this$0;
                            final Double2ObjectRBTreeMap.Entry<V> lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Double2ObjectRBTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Double2ObjectRBTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Double2ObjectMap.Entry<V>> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final double k) {
                super(k);
            }
            
            public Double2ObjectMap.Entry<V> next() {
                return this.nextEntry();
            }
            
            public Double2ObjectMap.Entry<V> previous() {
                return this.previousEntry();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements DoubleListIterator {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final double from) {
                super(from);
            }
            
            public double nextDouble() {
                return this.nextEntry().key;
            }
            
            public double previousDouble() {
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

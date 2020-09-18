package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.SortedSet;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;

public class Double2ReferenceAVLTreeMap<V> extends AbstractDouble2ReferenceSortedMap<V> implements Serializable, Cloneable {
    protected transient Entry<V> tree;
    protected int count;
    protected transient Entry<V> firstEntry;
    protected transient Entry<V> lastEntry;
    protected transient ObjectSortedSet<Double2ReferenceMap.Entry<V>> entries;
    protected transient DoubleSortedSet keys;
    protected transient ReferenceCollection<V> values;
    protected transient boolean modified;
    protected Comparator<? super Double> storedComparator;
    protected transient DoubleComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    
    public Double2ReferenceAVLTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = DoubleComparators.asDoubleComparator(this.storedComparator);
    }
    
    public Double2ReferenceAVLTreeMap(final Comparator<? super Double> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Double2ReferenceAVLTreeMap(final Map<? extends Double, ? extends V> m) {
        this();
        this.putAll(m);
    }
    
    public Double2ReferenceAVLTreeMap(final SortedMap<Double, V> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends Double, ? extends V>)m);
    }
    
    public Double2ReferenceAVLTreeMap(final Double2ReferenceMap<? extends V> m) {
        this();
        this.putAll((java.util.Map<? extends Double, ? extends V>)m);
    }
    
    public Double2ReferenceAVLTreeMap(final Double2ReferenceSortedMap<V> m) {
        this((Comparator)m.comparator());
        this.putAll((java.util.Map<? extends Double, ? extends V>)m);
    }
    
    public Double2ReferenceAVLTreeMap(final double[] k, final V[] v, final Comparator<? super Double> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Double2ReferenceAVLTreeMap(final double[] k, final V[] v) {
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
        Entry<V> e = this.tree;
        Entry<V> last = this.tree;
        int cmp;
        for (cmp = 0; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {
            last = e;
        }
        return (cmp == 0) ? e : last;
    }
    
    private void allocatePaths() {
        this.dirPath = new boolean[48];
    }
    
    public V put(final double k, final V v) {
        final Entry<V> e = this.add(k);
        final V oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry<V> add(final double k) {
        this.modified = false;
        Entry<V> e = null;
        if (this.tree != null) {
            Entry<V> p = this.tree;
            Entry<V> q = null;
            Entry<V> y = this.tree;
            Entry<V> z = null;
            Entry<V> w = null;
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
                    e = new Entry<V>(k, this.defRetValue);
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
                    e = new Entry<V>(k, this.defRetValue);
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
                    final Entry<V> x = y.left;
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
                    final Entry<V> x = y.right;
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
        final Entry<V> tree = new Entry<V>(k, this.defRetValue);
        this.firstEntry = tree;
        this.lastEntry = tree;
        this.tree = tree;
        e = tree;
        this.modified = true;
        return e;
    }
    
    private Entry<V> parent(final Entry<V> e) {
        if (e == this.tree) {
            return null;
        }
        Entry<V> y = e;
        Entry<V> x = e;
        while (!y.succ()) {
            if (x.pred()) {
                Entry<V> p = x.left;
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
        Entry<V> p = y.right;
        if (p == null || p.left != e) {
            while (!x.pred()) {
                x = x.left;
            }
            p = x.left;
        }
        return p;
    }
    
    public V remove(final double k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iconst_0       
        //     2: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.modified:Z
        //     5: aload_0         /* this */
        //     6: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //     9: ifnonnull       17
        //    12: aload_0         /* this */
        //    13: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.defRetValue:Ljava/lang/Object;
        //    16: areturn        
        //    17: aload_0         /* this */
        //    18: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //    21: astore          p
        //    23: aconst_null    
        //    24: astore          q
        //    26: iconst_0       
        //    27: istore          dir
        //    29: dload_1         /* k */
        //    30: dstore          kk
        //    32: aload_0         /* this */
        //    33: dload           kk
        //    35: aload           p
        //    37: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.key:D
        //    40: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.compare:(DD)I
        //    43: dup            
        //    44: istore_3        /* cmp */
        //    45: ifne            51
        //    48: goto            106
        //    51: iload_3         /* cmp */
        //    52: ifle            59
        //    55: iconst_1       
        //    56: goto            60
        //    59: iconst_0       
        //    60: dup            
        //    61: istore          dir
        //    63: ifeq            86
        //    66: aload           p
        //    68: astore          q
        //    70: aload           p
        //    72: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:()Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //    75: dup            
        //    76: astore          p
        //    78: ifnonnull       32
        //    81: aload_0         /* this */
        //    82: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.defRetValue:Ljava/lang/Object;
        //    85: areturn        
        //    86: aload           p
        //    88: astore          q
        //    90: aload           p
        //    92: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:()Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //    95: dup            
        //    96: astore          p
        //    98: ifnonnull       32
        //   101: aload_0         /* this */
        //   102: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.defRetValue:Ljava/lang/Object;
        //   105: areturn        
        //   106: aload           p
        //   108: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   111: ifnonnull       123
        //   114: aload_0         /* this */
        //   115: aload           p
        //   117: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.next:()Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   120: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.firstEntry:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   123: aload           p
        //   125: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   128: ifnonnull       140
        //   131: aload_0         /* this */
        //   132: aload           p
        //   134: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   137: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.lastEntry:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   140: aload           p
        //   142: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:()Z
        //   145: ifeq            278
        //   148: aload           p
        //   150: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:()Z
        //   153: ifeq            217
        //   156: aload           q
        //   158: ifnull          192
        //   161: iload           dir
        //   163: ifeq            179
        //   166: aload           q
        //   168: aload           p
        //   170: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   173: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;)V
        //   176: goto            543
        //   179: aload           q
        //   181: aload           p
        //   183: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   186: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;)V
        //   189: goto            543
        //   192: aload_0         /* this */
        //   193: iload           dir
        //   195: ifeq            206
        //   198: aload           p
        //   200: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   203: goto            211
        //   206: aload           p
        //   208: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   211: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   214: goto            543
        //   217: aload           p
        //   219: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   222: aload           p
        //   224: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   227: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   230: aload           q
        //   232: ifnull          266
        //   235: iload           dir
        //   237: ifeq            253
        //   240: aload           q
        //   242: aload           p
        //   244: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   247: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   250: goto            543
        //   253: aload           q
        //   255: aload           p
        //   257: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   260: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   263: goto            543
        //   266: aload_0         /* this */
        //   267: aload           p
        //   269: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   272: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   275: goto            543
        //   278: aload           p
        //   280: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   283: astore          r
        //   285: aload           r
        //   287: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:()Z
        //   290: ifeq            387
        //   293: aload           r
        //   295: aload           p
        //   297: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   300: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   303: aload           r
        //   305: aload           p
        //   307: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:()Z
        //   310: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:(Z)V
        //   313: aload           r
        //   315: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:()Z
        //   318: ifne            331
        //   321: aload           r
        //   323: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   326: aload           r
        //   328: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   331: aload           q
        //   333: ifnull          361
        //   336: iload           dir
        //   338: ifeq            351
        //   341: aload           q
        //   343: aload           r
        //   345: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   348: goto            367
        //   351: aload           q
        //   353: aload           r
        //   355: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   358: goto            367
        //   361: aload_0         /* this */
        //   362: aload           r
        //   364: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   367: aload           r
        //   369: aload           p
        //   371: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   374: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   377: aload           r
        //   379: astore          q
        //   381: iconst_1       
        //   382: istore          dir
        //   384: goto            543
        //   387: aload           r
        //   389: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   392: astore          s
        //   394: aload           s
        //   396: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:()Z
        //   399: ifeq            405
        //   402: goto            412
        //   405: aload           s
        //   407: astore          r
        //   409: goto            387
        //   412: aload           s
        //   414: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:()Z
        //   417: ifeq            430
        //   420: aload           r
        //   422: aload           s
        //   424: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;)V
        //   427: goto            440
        //   430: aload           r
        //   432: aload           s
        //   434: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   437: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   440: aload           s
        //   442: aload           p
        //   444: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   447: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   450: aload           p
        //   452: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:()Z
        //   455: ifne            474
        //   458: aload           p
        //   460: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   463: aload           s
        //   465: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   468: aload           s
        //   470: iconst_0       
        //   471: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:(Z)V
        //   474: aload           s
        //   476: aload           p
        //   478: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   481: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   484: aload           s
        //   486: iconst_0       
        //   487: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:(Z)V
        //   490: aload           q
        //   492: ifnull          520
        //   495: iload           dir
        //   497: ifeq            510
        //   500: aload           q
        //   502: aload           s
        //   504: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   507: goto            526
        //   510: aload           q
        //   512: aload           s
        //   514: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   517: goto            526
        //   520: aload_0         /* this */
        //   521: aload           s
        //   523: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   526: aload           s
        //   528: aload           p
        //   530: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   533: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   536: aload           r
        //   538: astore          q
        //   540: iconst_0       
        //   541: istore          dir
        //   543: aload           q
        //   545: ifnull          1506
        //   548: aload           q
        //   550: astore          y
        //   552: aload_0         /* this */
        //   553: aload           y
        //   555: invokespecial   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.parent:(Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;)Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   558: astore          q
        //   560: iload           dir
        //   562: ifne            1035
        //   565: aload           q
        //   567: ifnull          584
        //   570: aload           q
        //   572: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   575: aload           y
        //   577: if_acmpeq       584
        //   580: iconst_1       
        //   581: goto            585
        //   584: iconst_0       
        //   585: istore          dir
        //   587: aload           y
        //   589: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.incBalance:()V
        //   592: aload           y
        //   594: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   597: iconst_1       
        //   598: if_icmpne       604
        //   601: goto            1506
        //   604: aload           y
        //   606: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   609: iconst_2       
        //   610: if_icmpne       543
        //   613: aload           y
        //   615: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   618: astore          x
        //   620: getstatic       it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.$assertionsDisabled:Z
        //   623: ifne            639
        //   626: aload           x
        //   628: ifnonnull       639
        //   631: new             Ljava/lang/AssertionError;
        //   634: dup            
        //   635: invokespecial   java/lang/AssertionError.<init>:()V
        //   638: athrow         
        //   639: aload           x
        //   641: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   644: iconst_m1      
        //   645: if_icmpne       881
        //   648: getstatic       it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.$assertionsDisabled:Z
        //   651: ifne            671
        //   654: aload           x
        //   656: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   659: iconst_m1      
        //   660: if_icmpeq       671
        //   663: new             Ljava/lang/AssertionError;
        //   666: dup            
        //   667: invokespecial   java/lang/AssertionError.<init>:()V
        //   670: athrow         
        //   671: aload           x
        //   673: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   676: astore          w
        //   678: aload           x
        //   680: aload           w
        //   682: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   685: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   688: aload           w
        //   690: aload           x
        //   692: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   695: aload           y
        //   697: aload           w
        //   699: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   702: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   705: aload           w
        //   707: aload           y
        //   709: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   712: aload           w
        //   714: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   717: iconst_1       
        //   718: if_icmpne       736
        //   721: aload           x
        //   723: iconst_0       
        //   724: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   727: aload           y
        //   729: iconst_m1      
        //   730: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   733: goto            794
        //   736: aload           w
        //   738: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   741: ifne            759
        //   744: aload           x
        //   746: iconst_0       
        //   747: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   750: aload           y
        //   752: iconst_0       
        //   753: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   756: goto            794
        //   759: getstatic       it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.$assertionsDisabled:Z
        //   762: ifne            782
        //   765: aload           w
        //   767: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   770: iconst_m1      
        //   771: if_icmpeq       782
        //   774: new             Ljava/lang/AssertionError;
        //   777: dup            
        //   778: invokespecial   java/lang/AssertionError.<init>:()V
        //   781: athrow         
        //   782: aload           x
        //   784: iconst_1       
        //   785: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   788: aload           y
        //   790: iconst_0       
        //   791: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   794: aload           w
        //   796: iconst_0       
        //   797: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   800: aload           w
        //   802: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:()Z
        //   805: ifeq            821
        //   808: aload           y
        //   810: aload           w
        //   812: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;)V
        //   815: aload           w
        //   817: iconst_0       
        //   818: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:(Z)V
        //   821: aload           w
        //   823: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:()Z
        //   826: ifeq            842
        //   829: aload           x
        //   831: aload           w
        //   833: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;)V
        //   836: aload           w
        //   838: iconst_0       
        //   839: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:(Z)V
        //   842: aload           q
        //   844: ifnull          872
        //   847: iload           dir
        //   849: ifeq            862
        //   852: aload           q
        //   854: aload           w
        //   856: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   859: goto            878
        //   862: aload           q
        //   864: aload           w
        //   866: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   869: goto            878
        //   872: aload_0         /* this */
        //   873: aload           w
        //   875: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   878: goto            1032
        //   881: aload           q
        //   883: ifnull          911
        //   886: iload           dir
        //   888: ifeq            901
        //   891: aload           q
        //   893: aload           x
        //   895: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   898: goto            917
        //   901: aload           q
        //   903: aload           x
        //   905: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   908: goto            917
        //   911: aload_0         /* this */
        //   912: aload           x
        //   914: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   917: aload           x
        //   919: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   922: ifne            957
        //   925: aload           y
        //   927: aload           x
        //   929: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   932: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   935: aload           x
        //   937: aload           y
        //   939: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //   942: aload           x
        //   944: iconst_m1      
        //   945: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   948: aload           y
        //   950: iconst_1       
        //   951: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   954: goto            1506
        //   957: getstatic       it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.$assertionsDisabled:Z
        //   960: ifne            980
        //   963: aload           x
        //   965: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //   968: iconst_1       
        //   969: if_icmpeq       980
        //   972: new             Ljava/lang/AssertionError;
        //   975: dup            
        //   976: invokespecial   java/lang/AssertionError.<init>:()V
        //   979: athrow         
        //   980: aload           x
        //   982: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:()Z
        //   985: ifeq            1003
        //   988: aload           y
        //   990: iconst_1       
        //   991: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:(Z)V
        //   994: aload           x
        //   996: iconst_0       
        //   997: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:(Z)V
        //  1000: goto            1013
        //  1003: aload           y
        //  1005: aload           x
        //  1007: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1010: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1013: aload           x
        //  1015: aload           y
        //  1017: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1020: aload           y
        //  1022: iconst_0       
        //  1023: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1026: aload           x
        //  1028: iconst_0       
        //  1029: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1032: goto            543
        //  1035: aload           q
        //  1037: ifnull          1054
        //  1040: aload           q
        //  1042: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1045: aload           y
        //  1047: if_acmpeq       1054
        //  1050: iconst_1       
        //  1051: goto            1055
        //  1054: iconst_0       
        //  1055: istore          dir
        //  1057: aload           y
        //  1059: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.decBalance:()V
        //  1062: aload           y
        //  1064: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //  1067: iconst_m1      
        //  1068: if_icmpne       1074
        //  1071: goto            1506
        //  1074: aload           y
        //  1076: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //  1079: bipush          -2
        //  1081: if_icmpne       543
        //  1084: aload           y
        //  1086: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1089: astore          x
        //  1091: getstatic       it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.$assertionsDisabled:Z
        //  1094: ifne            1110
        //  1097: aload           x
        //  1099: ifnonnull       1110
        //  1102: new             Ljava/lang/AssertionError;
        //  1105: dup            
        //  1106: invokespecial   java/lang/AssertionError.<init>:()V
        //  1109: athrow         
        //  1110: aload           x
        //  1112: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //  1115: iconst_1       
        //  1116: if_icmpne       1352
        //  1119: getstatic       it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.$assertionsDisabled:Z
        //  1122: ifne            1142
        //  1125: aload           x
        //  1127: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //  1130: iconst_1       
        //  1131: if_icmpeq       1142
        //  1134: new             Ljava/lang/AssertionError;
        //  1137: dup            
        //  1138: invokespecial   java/lang/AssertionError.<init>:()V
        //  1141: athrow         
        //  1142: aload           x
        //  1144: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1147: astore          w
        //  1149: aload           x
        //  1151: aload           w
        //  1153: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1156: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1159: aload           w
        //  1161: aload           x
        //  1163: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1166: aload           y
        //  1168: aload           w
        //  1170: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1173: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1176: aload           w
        //  1178: aload           y
        //  1180: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1183: aload           w
        //  1185: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //  1188: iconst_m1      
        //  1189: if_icmpne       1207
        //  1192: aload           x
        //  1194: iconst_0       
        //  1195: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1198: aload           y
        //  1200: iconst_1       
        //  1201: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1204: goto            1265
        //  1207: aload           w
        //  1209: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //  1212: ifne            1230
        //  1215: aload           x
        //  1217: iconst_0       
        //  1218: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1221: aload           y
        //  1223: iconst_0       
        //  1224: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1227: goto            1265
        //  1230: getstatic       it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.$assertionsDisabled:Z
        //  1233: ifne            1253
        //  1236: aload           w
        //  1238: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //  1241: iconst_1       
        //  1242: if_icmpeq       1253
        //  1245: new             Ljava/lang/AssertionError;
        //  1248: dup            
        //  1249: invokespecial   java/lang/AssertionError.<init>:()V
        //  1252: athrow         
        //  1253: aload           x
        //  1255: iconst_m1      
        //  1256: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1259: aload           y
        //  1261: iconst_0       
        //  1262: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1265: aload           w
        //  1267: iconst_0       
        //  1268: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1271: aload           w
        //  1273: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:()Z
        //  1276: ifeq            1292
        //  1279: aload           x
        //  1281: aload           w
        //  1283: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;)V
        //  1286: aload           w
        //  1288: iconst_0       
        //  1289: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:(Z)V
        //  1292: aload           w
        //  1294: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:()Z
        //  1297: ifeq            1313
        //  1300: aload           y
        //  1302: aload           w
        //  1304: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;)V
        //  1307: aload           w
        //  1309: iconst_0       
        //  1310: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:(Z)V
        //  1313: aload           q
        //  1315: ifnull          1343
        //  1318: iload           dir
        //  1320: ifeq            1333
        //  1323: aload           q
        //  1325: aload           w
        //  1327: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1330: goto            1349
        //  1333: aload           q
        //  1335: aload           w
        //  1337: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1340: goto            1349
        //  1343: aload_0         /* this */
        //  1344: aload           w
        //  1346: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1349: goto            1503
        //  1352: aload           q
        //  1354: ifnull          1382
        //  1357: iload           dir
        //  1359: ifeq            1372
        //  1362: aload           q
        //  1364: aload           x
        //  1366: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1369: goto            1388
        //  1372: aload           q
        //  1374: aload           x
        //  1376: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1379: goto            1388
        //  1382: aload_0         /* this */
        //  1383: aload           x
        //  1385: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1388: aload           x
        //  1390: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //  1393: ifne            1428
        //  1396: aload           y
        //  1398: aload           x
        //  1400: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1403: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1406: aload           x
        //  1408: aload           y
        //  1410: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1413: aload           x
        //  1415: iconst_1       
        //  1416: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1419: aload           y
        //  1421: iconst_m1      
        //  1422: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1425: goto            1506
        //  1428: getstatic       it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.$assertionsDisabled:Z
        //  1431: ifne            1451
        //  1434: aload           x
        //  1436: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:()I
        //  1439: iconst_m1      
        //  1440: if_icmpeq       1451
        //  1443: new             Ljava/lang/AssertionError;
        //  1446: dup            
        //  1447: invokespecial   java/lang/AssertionError.<init>:()V
        //  1450: athrow         
        //  1451: aload           x
        //  1453: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:()Z
        //  1456: ifeq            1474
        //  1459: aload           y
        //  1461: iconst_1       
        //  1462: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.pred:(Z)V
        //  1465: aload           x
        //  1467: iconst_0       
        //  1468: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.succ:(Z)V
        //  1471: goto            1484
        //  1474: aload           y
        //  1476: aload           x
        //  1478: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1481: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1484: aload           x
        //  1486: aload           y
        //  1488: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry;
        //  1491: aload           y
        //  1493: iconst_0       
        //  1494: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1497: aload           x
        //  1499: iconst_0       
        //  1500: invokevirtual   it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.balance:(I)V
        //  1503: goto            543
        //  1506: aload_0         /* this */
        //  1507: iconst_1       
        //  1508: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.modified:Z
        //  1511: aload_0         /* this */
        //  1512: dup            
        //  1513: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.count:I
        //  1516: iconst_1       
        //  1517: isub           
        //  1518: putfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap.count:I
        //  1521: aload           p
        //  1523: getfield        it/unimi/dsi/fastutil/doubles/Double2ReferenceAVLTreeMap$Entry.value:Ljava/lang/Object;
        //  1526: areturn        
        //    Signature:
        //  (D)TV;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 4F 11 FF 00 0E 00 07 07 00 02 03 00 07 00 1E 07 00 1E 01 03 00 00 FF 00 12 00 07 07 00 02 03 01 07 00 1E 07 00 1E 01 03 00 00 07 40 01 19 13 10 10 26 0C 4D 07 00 02 FF 00 04 00 07 07 00 02 03 01 07 00 1E 07 00 1E 01 03 00 02 07 00 02 07 00 1E 05 23 0C 0B FC 00 34 07 00 1E 13 09 05 13 FC 00 11 07 00 1E 06 11 09 21 23 09 05 F9 00 10 FC 00 28 07 00 1E 40 01 12 FC 00 22 07 00 1E 1F FC 00 40 07 00 1E 16 16 0B 1A 14 13 09 FA 00 05 02 13 09 05 27 16 16 09 FA 00 12 02 12 40 01 12 FC 00 23 07 00 1E 1F FC 00 40 07 00 1E 16 16 0B 1A 14 13 09 FA 00 05 02 13 09 05 27 16 16 09 FA 00 12 FA 00 02
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
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2365)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.assembler.metadata.TypeReference.equals(TypeReference.java:118)
        //     at com.strobel.core.Comparer.equals(Comparer.java:31)
        //     at com.strobel.assembler.ir.FrameValue.equals(FrameValue.java:72)
        //     at com.strobel.core.Comparer.equals(Comparer.java:31)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:338)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:254)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2206)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    public boolean containsValue(final Object v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final V ev = i.next();
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
    
    public ObjectSortedSet<Double2ReferenceMap.Entry<V>> double2ReferenceEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Double2ReferenceMap.Entry<V>>() {
                final Comparator<? super Double2ReferenceMap.Entry<V>> comparator = (x, y) -> Double2ReferenceAVLTreeMap.this.actualComparator.compare(x.getDoubleKey(), y.getDoubleKey());
                
                public Comparator<? super Double2ReferenceMap.Entry<V>> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> iterator(final Double2ReferenceMap.Entry<V> from) {
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
                    final Entry<V> f = Double2ReferenceAVLTreeMap.this.findKey((double)e.getKey());
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
                    final Entry<V> f = Double2ReferenceAVLTreeMap.this.findKey((double)e.getKey());
                    if (f == null || f.getValue() != e.getValue()) {
                        return false;
                    }
                    Double2ReferenceAVLTreeMap.this.remove(f.key);
                    return true;
                }
                
                public int size() {
                    return Double2ReferenceAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Double2ReferenceAVLTreeMap.this.clear();
                }
                
                public Double2ReferenceMap.Entry<V> first() {
                    return Double2ReferenceAVLTreeMap.this.firstEntry;
                }
                
                public Double2ReferenceMap.Entry<V> last() {
                    return Double2ReferenceAVLTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Double2ReferenceMap.Entry<V>> subSet(final Double2ReferenceMap.Entry<V> from, final Double2ReferenceMap.Entry<V> to) {
                    return Double2ReferenceAVLTreeMap.this.subMap(from.getDoubleKey(), to.getDoubleKey()).double2ReferenceEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Double2ReferenceMap.Entry<V>> headSet(final Double2ReferenceMap.Entry<V> to) {
                    return Double2ReferenceAVLTreeMap.this.headMap(to.getDoubleKey()).double2ReferenceEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Double2ReferenceMap.Entry<V>> tailSet(final Double2ReferenceMap.Entry<V> from) {
                    return Double2ReferenceAVLTreeMap.this.tailMap(from.getDoubleKey()).double2ReferenceEntrySet();
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
    public ReferenceCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractReferenceCollection<V>() {
                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }
                
                public boolean contains(final Object k) {
                    return Double2ReferenceAVLTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Double2ReferenceAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Double2ReferenceAVLTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public DoubleComparator comparator() {
        return this.actualComparator;
    }
    
    public Double2ReferenceSortedMap<V> headMap(final double to) {
        return new Submap(0.0, true, to, false);
    }
    
    public Double2ReferenceSortedMap<V> tailMap(final double from) {
        return new Submap(from, false, 0.0, true);
    }
    
    public Double2ReferenceSortedMap<V> subMap(final double from, final double to) {
        return new Submap(from, false, to, false);
    }
    
    public Double2ReferenceAVLTreeMap<V> clone() {
        Double2ReferenceAVLTreeMap<V> c;
        try {
            c = (Double2ReferenceAVLTreeMap)super.clone();
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
            return top;
        }
        if (n == 2) {
            final Entry<V> top = new Entry<V>(s.readDouble(), (V)s.readObject());
            top.right(new Entry<V>(s.readDouble(), (V)s.readObject()));
            top.right.pred(top);
            top.balance(1);
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
            Entry<V> e;
            for (e = this.tree; e.left() != null; e = e.left()) {}
            this.firstEntry = e;
            for (e = this.tree; e.right() != null; e = e.right()) {}
            this.lastEntry = e;
        }
    }
    
    private static final class Entry<V> extends BasicEntry<V> implements Cloneable {
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        private static final int BALANCE_MASK = 255;
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
            return Double.doubleToLongBits(this.key) == Double.doubleToLongBits((double)e.getKey()) && this.value == e.getValue();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.double2int(this.key) ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
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
            this.next = Double2ReferenceAVLTreeMap.this.firstEntry;
        }
        
        TreeIterator(final double k) {
            this.index = 0;
            final Entry<V> locateKey = Double2ReferenceAVLTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Double2ReferenceAVLTreeMap.this.compare(this.next.key, k) <= 0) {
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
            Double2ReferenceAVLTreeMap.this.remove(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Double2ReferenceMap.Entry<V>> {
        EntryIterator() {
        }
        
        EntryIterator(final double k) {
            super(k);
        }
        
        public Double2ReferenceMap.Entry<V> next() {
            return this.nextEntry();
        }
        
        public Double2ReferenceMap.Entry<V> previous() {
            return this.previousEntry();
        }
        
        @Override
        public void set(final Double2ReferenceMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Double2ReferenceMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
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
    
    private class KeySet extends AbstractDouble2ReferenceSortedMap.KeySet {
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
    
    private final class Submap extends AbstractDouble2ReferenceSortedMap<V> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        double from;
        double to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Double2ReferenceMap.Entry<V>> entries;
        protected transient DoubleSortedSet keys;
        protected transient ReferenceCollection<V> values;
        final /* synthetic */ Double2ReferenceAVLTreeMap this$0;
        
        public Submap(final double from, final boolean bottom, final double to, final boolean top) {
            if (!bottom && !top && Double2ReferenceAVLTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append(from).append(") is larger than end key (").append(to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Double2ReferenceAVLTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final double k) {
            return (this.bottom || Double2ReferenceAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Double2ReferenceAVLTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Double2ReferenceMap.Entry<V>> double2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Double2ReferenceMap.Entry<V>>() {
                    @Override
                    public ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> iterator(final Double2ReferenceMap.Entry<V> from) {
                        return new SubmapEntryIterator(from.getDoubleKey());
                    }
                    
                    public Comparator<? super Double2ReferenceMap.Entry<V>> comparator() {
                        return Double2ReferenceAVLTreeMap.this.double2ReferenceEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                            return false;
                        }
                        final Double2ReferenceAVLTreeMap.Entry<V> f = Double2ReferenceAVLTreeMap.this.findKey((double)e.getKey());
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
                        final Double2ReferenceAVLTreeMap.Entry<V> f = Double2ReferenceAVLTreeMap.this.findKey((double)e.getKey());
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
                    
                    public Double2ReferenceMap.Entry<V> first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Double2ReferenceMap.Entry<V> last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Double2ReferenceMap.Entry<V>> subSet(final Double2ReferenceMap.Entry<V> from, final Double2ReferenceMap.Entry<V> to) {
                        return Submap.this.subMap(from.getDoubleKey(), to.getDoubleKey()).double2ReferenceEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Double2ReferenceMap.Entry<V>> headSet(final Double2ReferenceMap.Entry<V> to) {
                        return Submap.this.headMap(to.getDoubleKey()).double2ReferenceEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Double2ReferenceMap.Entry<V>> tailSet(final Double2ReferenceMap.Entry<V> from) {
                        return Submap.this.tailMap(from.getDoubleKey()).double2ReferenceEntrySet();
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
        public ReferenceCollection<V> values() {
            if (this.values == null) {
                this.values = new AbstractReferenceCollection<V>() {
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
            return this.in(k) && Double2ReferenceAVLTreeMap.this.containsKey(k);
        }
        
        public boolean containsValue(final Object v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final Object ev = i.nextEntry().value;
                if (ev == v) {
                    return true;
                }
            }
            return false;
        }
        
        public V get(final double k) {
            final double kk = k;
            final Double2ReferenceAVLTreeMap.Entry<V> e;
            return (V)((this.in(kk) && (e = Double2ReferenceAVLTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue);
        }
        
        public V put(final double k, final V v) {
            Double2ReferenceAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append(k).append(") out of range [").append(this.bottom ? "-" : String.valueOf(this.from)).append(", ").append(this.top ? "-" : String.valueOf(this.to)).append(")").toString());
            }
            final V oldValue = Double2ReferenceAVLTreeMap.this.put(k, v);
            return (V)(Double2ReferenceAVLTreeMap.this.modified ? this.defRetValue : oldValue);
        }
        
        public V remove(final double k) {
            Double2ReferenceAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                return (V)this.defRetValue;
            }
            final V oldValue = Double2ReferenceAVLTreeMap.this.remove(k);
            return (V)(Double2ReferenceAVLTreeMap.this.modified ? oldValue : this.defRetValue);
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
            return Double2ReferenceAVLTreeMap.this.actualComparator;
        }
        
        public Double2ReferenceSortedMap<V> headMap(final double to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Double2ReferenceAVLTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Double2ReferenceSortedMap<V> tailMap(final double from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Double2ReferenceAVLTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Double2ReferenceSortedMap<V> subMap(double from, double to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Double2ReferenceAVLTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Double2ReferenceAVLTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Double2ReferenceAVLTreeMap.Entry<V> firstEntry() {
            if (Double2ReferenceAVLTreeMap.this.tree == null) {
                return null;
            }
            Double2ReferenceAVLTreeMap.Entry<V> e;
            if (this.bottom) {
                e = Double2ReferenceAVLTreeMap.this.firstEntry;
            }
            else {
                e = Double2ReferenceAVLTreeMap.this.locateKey(this.from);
                if (Double2ReferenceAVLTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Double2ReferenceAVLTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Double2ReferenceAVLTreeMap.Entry<V> lastEntry() {
            if (Double2ReferenceAVLTreeMap.this.tree == null) {
                return null;
            }
            Double2ReferenceAVLTreeMap.Entry<V> e;
            if (this.top) {
                e = Double2ReferenceAVLTreeMap.this.lastEntry;
            }
            else {
                e = Double2ReferenceAVLTreeMap.this.locateKey(this.to);
                if (Double2ReferenceAVLTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Double2ReferenceAVLTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public double firstDoubleKey() {
            final Double2ReferenceAVLTreeMap.Entry<V> e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public double lastDoubleKey() {
            final Double2ReferenceAVLTreeMap.Entry<V> e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractDouble2ReferenceSortedMap.KeySet {
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
                            final Double2ReferenceAVLTreeMap this$0 = submap.this$0;
                            final Double2ReferenceAVLTreeMap.Entry<V> lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Double2ReferenceAVLTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Double2ReferenceAVLTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Double2ReferenceMap.Entry<V>> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final double k) {
                super(k);
            }
            
            public Double2ReferenceMap.Entry<V> next() {
                return this.nextEntry();
            }
            
            public Double2ReferenceMap.Entry<V> previous() {
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

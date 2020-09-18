package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.io.Serializable;

public class Object2DoubleRBTreeMap<K> extends AbstractObject2DoubleSortedMap<K> implements Serializable, Cloneable {
    protected transient Entry<K> tree;
    protected int count;
    protected transient Entry<K> firstEntry;
    protected transient Entry<K> lastEntry;
    protected transient ObjectSortedSet<Object2DoubleMap.Entry<K>> entries;
    protected transient ObjectSortedSet<K> keys;
    protected transient DoubleCollection values;
    protected transient boolean modified;
    protected Comparator<? super K> storedComparator;
    protected transient Comparator<? super K> actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    private transient Entry<K>[] nodePath;
    
    public Object2DoubleRBTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = this.storedComparator;
    }
    
    public Object2DoubleRBTreeMap(final Comparator<? super K> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Object2DoubleRBTreeMap(final Map<? extends K, ? extends Double> m) {
        this();
        this.putAll(m);
    }
    
    public Object2DoubleRBTreeMap(final SortedMap<K, Double> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends K, ? extends Double>)m);
    }
    
    public Object2DoubleRBTreeMap(final Object2DoubleMap<? extends K> m) {
        this();
        this.putAll((java.util.Map<? extends K, ? extends Double>)m);
    }
    
    public Object2DoubleRBTreeMap(final Object2DoubleSortedMap<K> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends K, ? extends Double>)m);
    }
    
    public Object2DoubleRBTreeMap(final K[] k, final double[] v, final Comparator<? super K> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2DoubleRBTreeMap(final K[] k, final double[] v) {
        this(k, v, null);
    }
    
    final int compare(final K k1, final K k2) {
        return (this.actualComparator == null) ? ((Comparable)k1).compareTo(k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry<K> findKey(final K k) {
        Entry<K> e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry<K> locateKey(final K k) {
        Entry<K> e = this.tree;
        Entry<K> last = this.tree;
        int cmp;
        for (cmp = 0; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {
            last = e;
        }
        return (cmp == 0) ? e : last;
    }
    
    private void allocatePaths() {
        this.dirPath = new boolean[64];
        this.nodePath = new Entry[64];
    }
    
    public double addTo(final K k, final double incr) {
        final Entry<K> e = this.add(k);
        final double oldValue = e.value;
        final Entry<K> entry = e;
        entry.value += incr;
        return oldValue;
    }
    
    public double put(final K k, final double v) {
        final Entry<K> e = this.add(k);
        final double oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry<K> add(final K k) {
        this.modified = false;
        int maxDepth = 0;
        Entry<K> e = null;
        Label_0908: {
            if (this.tree != null) {
                Entry<K> p = this.tree;
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
                        e = new Entry<K>(k, this.defRetValue);
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
                        e = new Entry<K>(k, this.defRetValue);
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
                            Entry<K> y = this.nodePath[i - 1].right;
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
                                    final Entry<K> x = this.nodePath[i];
                                    y = x.right;
                                    x.right = y.left;
                                    y.left = x;
                                    this.nodePath[i - 1].left = y;
                                    if (y.pred()) {
                                        y.pred(false);
                                        x.succ(y);
                                    }
                                }
                                final Entry<K> x = this.nodePath[i - 1];
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
                            Entry<K> y = this.nodePath[i - 1].left;
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
                                    final Entry<K> x = this.nodePath[i];
                                    y = x.left;
                                    x.left = y.right;
                                    y.right = x;
                                    this.nodePath[i - 1].right = y;
                                    if (y.succ()) {
                                        y.succ(false);
                                        x.pred(y);
                                    }
                                }
                                final Entry<K> x = this.nodePath[i - 1];
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
                    break Label_0908;
                }
                while (i-- != 0) {
                    this.nodePath[i] = null;
                }
                return p;
            }
            ++this.count;
            final Entry<K> tree = new Entry<K>(k, this.defRetValue);
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
    
    public double removeDouble(final Object k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iconst_0       
        //     2: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.modified:Z
        //     5: aload_0         /* this */
        //     6: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //     9: ifnonnull       17
        //    12: aload_0         /* this */
        //    13: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.defRetValue:D
        //    16: dreturn        
        //    17: aload_0         /* this */
        //    18: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //    21: astore_2        /* p */
        //    22: iconst_0       
        //    23: istore          i
        //    25: aload_1         /* k */
        //    26: astore          kk
        //    28: aload_0         /* this */
        //    29: aload           kk
        //    31: aload_2         /* p */
        //    32: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.key:Ljava/lang/Object;
        //    35: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.compare:(Ljava/lang/Object;Ljava/lang/Object;)I
        //    38: dup            
        //    39: istore_3        /* cmp */
        //    40: ifne            46
        //    43: goto            149
        //    46: aload_0         /* this */
        //    47: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //    50: iload           i
        //    52: iload_3         /* cmp */
        //    53: ifle            60
        //    56: iconst_1       
        //    57: goto            61
        //    60: iconst_0       
        //    61: bastore        
        //    62: aload_0         /* this */
        //    63: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //    66: iload           i
        //    68: aload_2         /* p */
        //    69: aastore        
        //    70: aload_0         /* this */
        //    71: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //    74: iload           i
        //    76: iinc            i, 1
        //    79: baload         
        //    80: ifeq            116
        //    83: aload_2         /* p */
        //    84: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:()Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //    87: dup            
        //    88: astore_2        /* p */
        //    89: ifnonnull       28
        //    92: iload           i
        //    94: iinc            i, -1
        //    97: ifeq            111
        //   100: aload_0         /* this */
        //   101: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   104: iload           i
        //   106: aconst_null    
        //   107: aastore        
        //   108: goto            92
        //   111: aload_0         /* this */
        //   112: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.defRetValue:D
        //   115: dreturn        
        //   116: aload_2         /* p */
        //   117: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:()Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   120: dup            
        //   121: astore_2        /* p */
        //   122: ifnonnull       28
        //   125: iload           i
        //   127: iinc            i, -1
        //   130: ifeq            144
        //   133: aload_0         /* this */
        //   134: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   137: iload           i
        //   139: aconst_null    
        //   140: aastore        
        //   141: goto            125
        //   144: aload_0         /* this */
        //   145: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.defRetValue:D
        //   148: dreturn        
        //   149: aload_2         /* p */
        //   150: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   153: ifnonnull       164
        //   156: aload_0         /* this */
        //   157: aload_2         /* p */
        //   158: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.next:()Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   161: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.firstEntry:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   164: aload_2         /* p */
        //   165: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   168: ifnonnull       179
        //   171: aload_0         /* this */
        //   172: aload_2         /* p */
        //   173: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   176: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.lastEntry:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   179: aload_2         /* p */
        //   180: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:()Z
        //   183: ifeq            336
        //   186: aload_2         /* p */
        //   187: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //   190: ifeq            259
        //   193: iload           i
        //   195: ifne            209
        //   198: aload_0         /* this */
        //   199: aload_2         /* p */
        //   200: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   203: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   206: goto            704
        //   209: aload_0         /* this */
        //   210: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   213: iload           i
        //   215: iconst_1       
        //   216: isub           
        //   217: baload         
        //   218: ifeq            240
        //   221: aload_0         /* this */
        //   222: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   225: iload           i
        //   227: iconst_1       
        //   228: isub           
        //   229: aaload         
        //   230: aload_2         /* p */
        //   231: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   234: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;)V
        //   237: goto            704
        //   240: aload_0         /* this */
        //   241: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   244: iload           i
        //   246: iconst_1       
        //   247: isub           
        //   248: aaload         
        //   249: aload_2         /* p */
        //   250: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   253: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;)V
        //   256: goto            704
        //   259: aload_2         /* p */
        //   260: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   263: aload_2         /* p */
        //   264: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   267: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   270: iload           i
        //   272: ifne            286
        //   275: aload_0         /* this */
        //   276: aload_2         /* p */
        //   277: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   280: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   283: goto            704
        //   286: aload_0         /* this */
        //   287: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   290: iload           i
        //   292: iconst_1       
        //   293: isub           
        //   294: baload         
        //   295: ifeq            317
        //   298: aload_0         /* this */
        //   299: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   302: iload           i
        //   304: iconst_1       
        //   305: isub           
        //   306: aaload         
        //   307: aload_2         /* p */
        //   308: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   311: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   314: goto            704
        //   317: aload_0         /* this */
        //   318: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   321: iload           i
        //   323: iconst_1       
        //   324: isub           
        //   325: aaload         
        //   326: aload_2         /* p */
        //   327: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   330: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   333: goto            704
        //   336: aload_2         /* p */
        //   337: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   340: astore          r
        //   342: aload           r
        //   344: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //   347: ifeq            488
        //   350: aload           r
        //   352: aload_2         /* p */
        //   353: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   356: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   359: aload           r
        //   361: aload_2         /* p */
        //   362: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //   365: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:(Z)V
        //   368: aload           r
        //   370: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //   373: ifne            386
        //   376: aload           r
        //   378: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   381: aload           r
        //   383: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   386: iload           i
        //   388: ifne            400
        //   391: aload_0         /* this */
        //   392: aload           r
        //   394: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   397: goto            443
        //   400: aload_0         /* this */
        //   401: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   404: iload           i
        //   406: iconst_1       
        //   407: isub           
        //   408: baload         
        //   409: ifeq            429
        //   412: aload_0         /* this */
        //   413: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   416: iload           i
        //   418: iconst_1       
        //   419: isub           
        //   420: aaload         
        //   421: aload           r
        //   423: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   426: goto            443
        //   429: aload_0         /* this */
        //   430: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   433: iload           i
        //   435: iconst_1       
        //   436: isub           
        //   437: aaload         
        //   438: aload           r
        //   440: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   443: aload           r
        //   445: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //   448: istore          color
        //   450: aload           r
        //   452: aload_2         /* p */
        //   453: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //   456: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //   459: aload_2         /* p */
        //   460: iload           color
        //   462: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //   465: aload_0         /* this */
        //   466: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   469: iload           i
        //   471: iconst_1       
        //   472: bastore        
        //   473: aload_0         /* this */
        //   474: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   477: iload           i
        //   479: iinc            i, 1
        //   482: aload           r
        //   484: aastore        
        //   485: goto            704
        //   488: iload           i
        //   490: iinc            i, 1
        //   493: istore          j
        //   495: aload_0         /* this */
        //   496: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   499: iload           i
        //   501: iconst_0       
        //   502: bastore        
        //   503: aload_0         /* this */
        //   504: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   507: iload           i
        //   509: iinc            i, 1
        //   512: aload           r
        //   514: aastore        
        //   515: aload           r
        //   517: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   520: astore          s
        //   522: aload           s
        //   524: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //   527: ifeq            533
        //   530: goto            540
        //   533: aload           s
        //   535: astore          r
        //   537: goto            495
        //   540: aload_0         /* this */
        //   541: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   544: iload           j
        //   546: iconst_1       
        //   547: bastore        
        //   548: aload_0         /* this */
        //   549: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   552: iload           j
        //   554: aload           s
        //   556: aastore        
        //   557: aload           s
        //   559: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:()Z
        //   562: ifeq            575
        //   565: aload           r
        //   567: aload           s
        //   569: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;)V
        //   572: goto            585
        //   575: aload           r
        //   577: aload           s
        //   579: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   582: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   585: aload           s
        //   587: aload_2         /* p */
        //   588: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   591: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   594: aload_2         /* p */
        //   595: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //   598: ifne            616
        //   601: aload_2         /* p */
        //   602: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   605: aload           s
        //   607: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   610: aload           s
        //   612: iconst_0       
        //   613: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:(Z)V
        //   616: aload           s
        //   618: aload_2         /* p */
        //   619: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   622: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:(Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;)V
        //   625: aload           s
        //   627: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //   630: istore          color
        //   632: aload           s
        //   634: aload_2         /* p */
        //   635: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //   638: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //   641: aload_2         /* p */
        //   642: iload           color
        //   644: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //   647: iload           j
        //   649: ifne            661
        //   652: aload_0         /* this */
        //   653: aload           s
        //   655: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   658: goto            704
        //   661: aload_0         /* this */
        //   662: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   665: iload           j
        //   667: iconst_1       
        //   668: isub           
        //   669: baload         
        //   670: ifeq            690
        //   673: aload_0         /* this */
        //   674: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   677: iload           j
        //   679: iconst_1       
        //   680: isub           
        //   681: aaload         
        //   682: aload           s
        //   684: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   687: goto            704
        //   690: aload_0         /* this */
        //   691: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   694: iload           j
        //   696: iconst_1       
        //   697: isub           
        //   698: aaload         
        //   699: aload           s
        //   701: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   704: iload           i
        //   706: istore          maxDepth
        //   708: aload_2         /* p */
        //   709: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //   712: ifeq            1856
        //   715: iload           i
        //   717: ifle            1841
        //   720: aload_0         /* this */
        //   721: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   724: iload           i
        //   726: iconst_1       
        //   727: isub           
        //   728: baload         
        //   729: ifeq            747
        //   732: aload_0         /* this */
        //   733: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   736: iload           i
        //   738: iconst_1       
        //   739: isub           
        //   740: aaload         
        //   741: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:()Z
        //   744: ifeq            774
        //   747: aload_0         /* this */
        //   748: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   751: iload           i
        //   753: iconst_1       
        //   754: isub           
        //   755: baload         
        //   756: ifne            832
        //   759: aload_0         /* this */
        //   760: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   763: iload           i
        //   765: iconst_1       
        //   766: isub           
        //   767: aaload         
        //   768: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //   771: ifne            832
        //   774: aload_0         /* this */
        //   775: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   778: iload           i
        //   780: iconst_1       
        //   781: isub           
        //   782: baload         
        //   783: ifeq            801
        //   786: aload_0         /* this */
        //   787: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   790: iload           i
        //   792: iconst_1       
        //   793: isub           
        //   794: aaload         
        //   795: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   798: goto            813
        //   801: aload_0         /* this */
        //   802: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   805: iload           i
        //   807: iconst_1       
        //   808: isub           
        //   809: aaload         
        //   810: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   813: astore          x
        //   815: aload           x
        //   817: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //   820: ifne            832
        //   823: aload           x
        //   825: iconst_1       
        //   826: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //   829: goto            1841
        //   832: aload_0         /* this */
        //   833: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   836: iload           i
        //   838: iconst_1       
        //   839: isub           
        //   840: baload         
        //   841: ifne            1341
        //   844: aload_0         /* this */
        //   845: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   848: iload           i
        //   850: iconst_1       
        //   851: isub           
        //   852: aaload         
        //   853: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   856: astore          w
        //   858: aload           w
        //   860: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //   863: ifne            1036
        //   866: aload           w
        //   868: iconst_1       
        //   869: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //   872: aload_0         /* this */
        //   873: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   876: iload           i
        //   878: iconst_1       
        //   879: isub           
        //   880: aaload         
        //   881: iconst_0       
        //   882: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //   885: aload_0         /* this */
        //   886: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   889: iload           i
        //   891: iconst_1       
        //   892: isub           
        //   893: aaload         
        //   894: aload           w
        //   896: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   899: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   902: aload           w
        //   904: aload_0         /* this */
        //   905: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   908: iload           i
        //   910: iconst_1       
        //   911: isub           
        //   912: aaload         
        //   913: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   916: iload           i
        //   918: iconst_2       
        //   919: if_icmpge       931
        //   922: aload_0         /* this */
        //   923: aload           w
        //   925: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   928: goto            974
        //   931: aload_0         /* this */
        //   932: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   935: iload           i
        //   937: iconst_2       
        //   938: isub           
        //   939: baload         
        //   940: ifeq            960
        //   943: aload_0         /* this */
        //   944: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   947: iload           i
        //   949: iconst_2       
        //   950: isub           
        //   951: aaload         
        //   952: aload           w
        //   954: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   957: goto            974
        //   960: aload_0         /* this */
        //   961: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   964: iload           i
        //   966: iconst_2       
        //   967: isub           
        //   968: aaload         
        //   969: aload           w
        //   971: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   974: aload_0         /* this */
        //   975: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   978: iload           i
        //   980: aload_0         /* this */
        //   981: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //   984: iload           i
        //   986: iconst_1       
        //   987: isub           
        //   988: aaload         
        //   989: aastore        
        //   990: aload_0         /* this */
        //   991: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //   994: iload           i
        //   996: iconst_0       
        //   997: bastore        
        //   998: aload_0         /* this */
        //   999: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1002: iload           i
        //  1004: iconst_1       
        //  1005: isub           
        //  1006: aload           w
        //  1008: aastore        
        //  1009: iload           maxDepth
        //  1011: iload           i
        //  1013: iinc            i, 1
        //  1016: if_icmpne       1022
        //  1019: iinc            maxDepth, 1
        //  1022: aload_0         /* this */
        //  1023: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1026: iload           i
        //  1028: iconst_1       
        //  1029: isub           
        //  1030: aaload         
        //  1031: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1034: astore          w
        //  1036: aload           w
        //  1038: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //  1041: ifne            1055
        //  1044: aload           w
        //  1046: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1049: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //  1052: ifeq            1083
        //  1055: aload           w
        //  1057: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:()Z
        //  1060: ifne            1074
        //  1063: aload           w
        //  1065: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1068: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //  1071: ifeq            1083
        //  1074: aload           w
        //  1076: iconst_0       
        //  1077: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1080: goto            1338
        //  1083: aload           w
        //  1085: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:()Z
        //  1088: ifne            1102
        //  1091: aload           w
        //  1093: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1096: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //  1099: ifeq            1179
        //  1102: aload           w
        //  1104: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1107: astore          y
        //  1109: aload           y
        //  1111: iconst_1       
        //  1112: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1115: aload           w
        //  1117: iconst_0       
        //  1118: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1121: aload           w
        //  1123: aload           y
        //  1125: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1128: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1131: aload           y
        //  1133: aload           w
        //  1135: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1138: aload_0         /* this */
        //  1139: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1142: iload           i
        //  1144: iconst_1       
        //  1145: isub           
        //  1146: aaload         
        //  1147: aload           y
        //  1149: dup_x1         
        //  1150: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1153: astore          w
        //  1155: aload           w
        //  1157: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:()Z
        //  1160: ifeq            1179
        //  1163: aload           w
        //  1165: iconst_0       
        //  1166: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:(Z)V
        //  1169: aload           w
        //  1171: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1174: aload           w
        //  1176: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;)V
        //  1179: aload           w
        //  1181: aload_0         /* this */
        //  1182: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1185: iload           i
        //  1187: iconst_1       
        //  1188: isub           
        //  1189: aaload         
        //  1190: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //  1193: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1196: aload_0         /* this */
        //  1197: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1200: iload           i
        //  1202: iconst_1       
        //  1203: isub           
        //  1204: aaload         
        //  1205: iconst_1       
        //  1206: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1209: aload           w
        //  1211: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1214: iconst_1       
        //  1215: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1218: aload_0         /* this */
        //  1219: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1222: iload           i
        //  1224: iconst_1       
        //  1225: isub           
        //  1226: aaload         
        //  1227: aload           w
        //  1229: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1232: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1235: aload           w
        //  1237: aload_0         /* this */
        //  1238: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1241: iload           i
        //  1243: iconst_1       
        //  1244: isub           
        //  1245: aaload         
        //  1246: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1249: iload           i
        //  1251: iconst_2       
        //  1252: if_icmpge       1264
        //  1255: aload_0         /* this */
        //  1256: aload           w
        //  1258: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1261: goto            1307
        //  1264: aload_0         /* this */
        //  1265: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //  1268: iload           i
        //  1270: iconst_2       
        //  1271: isub           
        //  1272: baload         
        //  1273: ifeq            1293
        //  1276: aload_0         /* this */
        //  1277: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1280: iload           i
        //  1282: iconst_2       
        //  1283: isub           
        //  1284: aaload         
        //  1285: aload           w
        //  1287: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1290: goto            1307
        //  1293: aload_0         /* this */
        //  1294: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1297: iload           i
        //  1299: iconst_2       
        //  1300: isub           
        //  1301: aaload         
        //  1302: aload           w
        //  1304: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1307: aload           w
        //  1309: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //  1312: ifeq            1841
        //  1315: aload           w
        //  1317: iconst_0       
        //  1318: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:(Z)V
        //  1321: aload_0         /* this */
        //  1322: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1325: iload           i
        //  1327: iconst_1       
        //  1328: isub           
        //  1329: aaload         
        //  1330: aload           w
        //  1332: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;)V
        //  1335: goto            1841
        //  1338: goto            1835
        //  1341: aload_0         /* this */
        //  1342: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1345: iload           i
        //  1347: iconst_1       
        //  1348: isub           
        //  1349: aaload         
        //  1350: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1353: astore          w
        //  1355: aload           w
        //  1357: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //  1360: ifne            1533
        //  1363: aload           w
        //  1365: iconst_1       
        //  1366: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1369: aload_0         /* this */
        //  1370: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1373: iload           i
        //  1375: iconst_1       
        //  1376: isub           
        //  1377: aaload         
        //  1378: iconst_0       
        //  1379: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1382: aload_0         /* this */
        //  1383: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1386: iload           i
        //  1388: iconst_1       
        //  1389: isub           
        //  1390: aaload         
        //  1391: aload           w
        //  1393: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1396: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1399: aload           w
        //  1401: aload_0         /* this */
        //  1402: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1405: iload           i
        //  1407: iconst_1       
        //  1408: isub           
        //  1409: aaload         
        //  1410: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1413: iload           i
        //  1415: iconst_2       
        //  1416: if_icmpge       1428
        //  1419: aload_0         /* this */
        //  1420: aload           w
        //  1422: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1425: goto            1471
        //  1428: aload_0         /* this */
        //  1429: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //  1432: iload           i
        //  1434: iconst_2       
        //  1435: isub           
        //  1436: baload         
        //  1437: ifeq            1457
        //  1440: aload_0         /* this */
        //  1441: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1444: iload           i
        //  1446: iconst_2       
        //  1447: isub           
        //  1448: aaload         
        //  1449: aload           w
        //  1451: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1454: goto            1471
        //  1457: aload_0         /* this */
        //  1458: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1461: iload           i
        //  1463: iconst_2       
        //  1464: isub           
        //  1465: aaload         
        //  1466: aload           w
        //  1468: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1471: aload_0         /* this */
        //  1472: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1475: iload           i
        //  1477: aload_0         /* this */
        //  1478: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1481: iload           i
        //  1483: iconst_1       
        //  1484: isub           
        //  1485: aaload         
        //  1486: aastore        
        //  1487: aload_0         /* this */
        //  1488: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //  1491: iload           i
        //  1493: iconst_1       
        //  1494: bastore        
        //  1495: aload_0         /* this */
        //  1496: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1499: iload           i
        //  1501: iconst_1       
        //  1502: isub           
        //  1503: aload           w
        //  1505: aastore        
        //  1506: iload           maxDepth
        //  1508: iload           i
        //  1510: iinc            i, 1
        //  1513: if_icmpne       1519
        //  1516: iinc            maxDepth, 1
        //  1519: aload_0         /* this */
        //  1520: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1523: iload           i
        //  1525: iconst_1       
        //  1526: isub           
        //  1527: aaload         
        //  1528: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1531: astore          w
        //  1533: aload           w
        //  1535: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //  1538: ifne            1552
        //  1541: aload           w
        //  1543: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1546: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //  1549: ifeq            1580
        //  1552: aload           w
        //  1554: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:()Z
        //  1557: ifne            1571
        //  1560: aload           w
        //  1562: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1565: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //  1568: ifeq            1580
        //  1571: aload           w
        //  1573: iconst_0       
        //  1574: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1577: goto            1835
        //  1580: aload           w
        //  1582: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //  1585: ifne            1599
        //  1588: aload           w
        //  1590: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1593: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //  1596: ifeq            1676
        //  1599: aload           w
        //  1601: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1604: astore          y
        //  1606: aload           y
        //  1608: iconst_1       
        //  1609: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1612: aload           w
        //  1614: iconst_0       
        //  1615: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1618: aload           w
        //  1620: aload           y
        //  1622: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1625: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1628: aload           y
        //  1630: aload           w
        //  1632: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1635: aload_0         /* this */
        //  1636: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1639: iload           i
        //  1641: iconst_1       
        //  1642: isub           
        //  1643: aaload         
        //  1644: aload           y
        //  1646: dup_x1         
        //  1647: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1650: astore          w
        //  1652: aload           w
        //  1654: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:()Z
        //  1657: ifeq            1676
        //  1660: aload           w
        //  1662: iconst_0       
        //  1663: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:(Z)V
        //  1666: aload           w
        //  1668: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1671: aload           w
        //  1673: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;)V
        //  1676: aload           w
        //  1678: aload_0         /* this */
        //  1679: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1682: iload           i
        //  1684: iconst_1       
        //  1685: isub           
        //  1686: aaload         
        //  1687: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:()Z
        //  1690: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1693: aload_0         /* this */
        //  1694: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1697: iload           i
        //  1699: iconst_1       
        //  1700: isub           
        //  1701: aaload         
        //  1702: iconst_1       
        //  1703: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1706: aload           w
        //  1708: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1711: iconst_1       
        //  1712: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1715: aload_0         /* this */
        //  1716: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1719: iload           i
        //  1721: iconst_1       
        //  1722: isub           
        //  1723: aaload         
        //  1724: aload           w
        //  1726: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1729: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1732: aload           w
        //  1734: aload_0         /* this */
        //  1735: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1738: iload           i
        //  1740: iconst_1       
        //  1741: isub           
        //  1742: aaload         
        //  1743: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1746: iload           i
        //  1748: iconst_2       
        //  1749: if_icmpge       1761
        //  1752: aload_0         /* this */
        //  1753: aload           w
        //  1755: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1758: goto            1804
        //  1761: aload_0         /* this */
        //  1762: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.dirPath:[Z
        //  1765: iload           i
        //  1767: iconst_2       
        //  1768: isub           
        //  1769: baload         
        //  1770: ifeq            1790
        //  1773: aload_0         /* this */
        //  1774: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1777: iload           i
        //  1779: iconst_2       
        //  1780: isub           
        //  1781: aaload         
        //  1782: aload           w
        //  1784: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1787: goto            1804
        //  1790: aload_0         /* this */
        //  1791: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1794: iload           i
        //  1796: iconst_2       
        //  1797: isub           
        //  1798: aaload         
        //  1799: aload           w
        //  1801: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1804: aload           w
        //  1806: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:()Z
        //  1809: ifeq            1841
        //  1812: aload           w
        //  1814: iconst_0       
        //  1815: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.succ:(Z)V
        //  1818: aload_0         /* this */
        //  1819: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1822: iload           i
        //  1824: iconst_1       
        //  1825: isub           
        //  1826: aaload         
        //  1827: aload           w
        //  1829: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;)V
        //  1832: goto            1841
        //  1835: iinc            i, -1
        //  1838: goto            715
        //  1841: aload_0         /* this */
        //  1842: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1845: ifnull          1856
        //  1848: aload_0         /* this */
        //  1849: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1852: iconst_1       
        //  1853: invokevirtual   it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.black:(Z)V
        //  1856: aload_0         /* this */
        //  1857: iconst_1       
        //  1858: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.modified:Z
        //  1861: aload_0         /* this */
        //  1862: dup            
        //  1863: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.count:I
        //  1866: iconst_1       
        //  1867: isub           
        //  1868: putfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.count:I
        //  1871: iload           maxDepth
        //  1873: iinc            maxDepth, -1
        //  1876: ifeq            1890
        //  1879: aload_0         /* this */
        //  1880: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry;
        //  1883: iload           maxDepth
        //  1885: aconst_null    
        //  1886: aastore        
        //  1887: goto            1871
        //  1890: aload_2         /* p */
        //  1891: getfield        it/unimi/dsi/fastutil/objects/Object2DoubleRBTreeMap$Entry.value:D
        //  1894: dreturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 48 11 FF 00 0A 00 06 07 00 02 07 00 E7 07 00 1E 00 01 07 00 E7 00 00 FF 00 11 00 06 07 00 02 07 00 E7 07 00 1E 01 01 07 00 E7 00 00 FF 00 0D 00 06 07 00 02 07 00 E7 07 00 1E 01 01 07 00 E7 00 02 07 00 E8 01 FF 00 00 00 06 07 00 02 07 00 E7 07 00 1E 01 01 07 00 E7 00 03 07 00 E8 01 01 1E 12 04 08 12 04 0E 0E 1D 1E 12 1A 1E 12 FD 00 31 00 07 00 1E 0D 1C 0D 2C FD 00 06 00 01 FF 00 25 00 0A 07 00 02 07 00 E7 07 00 1E 01 01 07 00 E7 00 07 00 1E 07 00 1E 01 00 00 06 22 09 1E FF 00 2C 00 0A 07 00 02 07 00 E7 07 00 1E 01 01 07 00 E7 01 07 00 1E 07 00 1E 01 00 00 1C FF 00 0D 00 06 07 00 02 07 00 E7 07 00 1E 01 01 07 00 E7 00 00 FC 00 0A 01 1F 1A 1A 4B 07 00 1E 12 FC 00 62 07 00 1E 1C 0D 2F 0D 12 12 08 12 FB 00 4C FB 00 54 1C 0D FA 00 1E 02 FC 00 56 07 00 1E 1C 0D 2F 0D 12 12 08 12 FB 00 4C FB 00 54 1C 0D FA 00 1E 05 0E 0E 12
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1067)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
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
    
    public boolean containsValue(final double v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final double ev = i.nextDouble();
            if (Double.doubleToLongBits(ev) == Double.doubleToLongBits(v)) {
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
        final Entry<K> entry = null;
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
    
    public double getDouble(final Object k) {
        final Entry<K> e = (Entry<K>)this.findKey(k);
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
    
    public ObjectSortedSet<Object2DoubleMap.Entry<K>> object2DoubleEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Object2DoubleMap.Entry<K>>() {
                final Comparator<? super Object2DoubleMap.Entry<K>> comparator = (x, y) -> Object2DoubleRBTreeMap.this.actualComparator.compare(x.getKey(), y.getKey());
                
                public Comparator<? super Object2DoubleMap.Entry<K>> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Object2DoubleMap.Entry<K>> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Object2DoubleMap.Entry<K>> iterator(final Object2DoubleMap.Entry<K> from) {
                    return new EntryIterator((K)from.getKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                        return false;
                    }
                    final Entry<K> f = (Entry<K>)Object2DoubleRBTreeMap.this.findKey(e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                        return false;
                    }
                    final Entry<K> f = (Entry<K>)Object2DoubleRBTreeMap.this.findKey(e.getKey());
                    if (f == null || Double.doubleToLongBits(f.getDoubleValue()) != Double.doubleToLongBits((double)e.getValue())) {
                        return false;
                    }
                    Object2DoubleRBTreeMap.this.removeDouble(f.key);
                    return true;
                }
                
                public int size() {
                    return Object2DoubleRBTreeMap.this.count;
                }
                
                public void clear() {
                    Object2DoubleRBTreeMap.this.clear();
                }
                
                public Object2DoubleMap.Entry<K> first() {
                    return Object2DoubleRBTreeMap.this.firstEntry;
                }
                
                public Object2DoubleMap.Entry<K> last() {
                    return Object2DoubleRBTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Object2DoubleMap.Entry<K>> subSet(final Object2DoubleMap.Entry<K> from, final Object2DoubleMap.Entry<K> to) {
                    return (ObjectSortedSet<Object2DoubleMap.Entry<K>>)Object2DoubleRBTreeMap.this.subMap(from.getKey(), to.getKey()).object2DoubleEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Object2DoubleMap.Entry<K>> headSet(final Object2DoubleMap.Entry<K> to) {
                    return (ObjectSortedSet<Object2DoubleMap.Entry<K>>)Object2DoubleRBTreeMap.this.headMap(to.getKey()).object2DoubleEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Object2DoubleMap.Entry<K>> tailSet(final Object2DoubleMap.Entry<K> from) {
                    return (ObjectSortedSet<Object2DoubleMap.Entry<K>>)Object2DoubleRBTreeMap.this.tailMap(from.getKey()).object2DoubleEntrySet();
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
    public DoubleCollection values() {
        if (this.values == null) {
            this.values = new AbstractDoubleCollection() {
                @Override
                public DoubleIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public boolean contains(final double k) {
                    return Object2DoubleRBTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Object2DoubleRBTreeMap.this.count;
                }
                
                public void clear() {
                    Object2DoubleRBTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public Comparator<? super K> comparator() {
        return this.actualComparator;
    }
    
    public Object2DoubleSortedMap<K> headMap(final K to) {
        return new Submap(null, true, to, false);
    }
    
    public Object2DoubleSortedMap<K> tailMap(final K from) {
        return new Submap(from, false, null, true);
    }
    
    public Object2DoubleSortedMap<K> subMap(final K from, final K to) {
        return new Submap(from, false, to, false);
    }
    
    public Object2DoubleRBTreeMap<K> clone() {
        Object2DoubleRBTreeMap<K> c;
        try {
            c = (Object2DoubleRBTreeMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.allocatePaths();
        if (this.count != 0) {
            final Entry<K> rp = new Entry<K>();
            final Entry<K> rq = new Entry<K>();
            Entry<K> p = rp;
            rp.left(this.tree);
            Entry<K> q = rq;
            rq.pred(null);
        Block_4:
            while (true) {
                if (!p.pred()) {
                    final Entry<K> e = p.left.clone();
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
                    final Entry<K> e = p.right.clone();
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
            final Entry<K> e = i.nextEntry();
            s.writeObject(e.key);
            s.writeDouble(e.value);
        }
    }
    
    private Entry<K> readTree(final ObjectInputStream s, final int n, final Entry<K> pred, final Entry<K> succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry<K> top = new Entry<K>((K)s.readObject(), s.readDouble());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            final Entry<K> top = new Entry<K>((K)s.readObject(), s.readDouble());
            top.black(true);
            top.right(new Entry<K>((K)s.readObject(), s.readDouble()));
            top.right.pred(top);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        final int rightN = n / 2;
        final int leftN = n - rightN - 1;
        final Entry<K> top2 = new Entry<K>();
        top2.left(this.readTree(s, leftN, pred, top2));
        top2.key = (K)s.readObject();
        top2.value = s.readDouble();
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
            Entry<K> e;
            for (e = this.tree; e.left() != null; e = e.left()) {}
            this.firstEntry = e;
            for (e = this.tree; e.right() != null; e = e.right()) {}
            this.lastEntry = e;
        }
    }
    
    private static final class Entry<K> extends BasicEntry<K> implements Cloneable {
        private static final int BLACK_MASK = 1;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        Entry<K> left;
        Entry<K> right;
        int info;
        
        Entry() {
            super(null, 0.0);
        }
        
        Entry(final K k, final double v) {
            super(k, v);
            this.info = -1073741824;
        }
        
        Entry<K> left() {
            return ((this.info & 0x40000000) != 0x0) ? null : this.left;
        }
        
        Entry<K> right() {
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
        
        void pred(final Entry<K> pred) {
            this.info |= 0x40000000;
            this.left = pred;
        }
        
        void succ(final Entry<K> succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }
        
        void left(final Entry<K> left) {
            this.info &= 0xBFFFFFFF;
            this.left = left;
        }
        
        void right(final Entry<K> right) {
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
        
        Entry<K> next() {
            Entry<K> next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0x0) {
                while ((next.info & 0x40000000) == 0x0) {
                    next = next.left;
                }
            }
            return next;
        }
        
        Entry<K> prev() {
            Entry<K> prev = this.left;
            if ((this.info & 0x40000000) == 0x0) {
                while ((prev.info & Integer.MIN_VALUE) == 0x0) {
                    prev = prev.right;
                }
            }
            return prev;
        }
        
        @Override
        public double setValue(final double value) {
            final double oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        public Entry<K> clone() {
            Entry<K> c;
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
            final Map.Entry<K, Double> e = (Map.Entry<K, Double>)o;
            return Objects.equals(this.key, e.getKey()) && Double.doubleToLongBits(this.value) == Double.doubleToLongBits((double)e.getValue());
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode() ^ HashCommon.double2int(this.value);
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append(this.key).append("=>").append(this.value).toString();
        }
    }
    
    private class TreeIterator {
        Entry<K> prev;
        Entry<K> next;
        Entry<K> curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Object2DoubleRBTreeMap.this.firstEntry;
        }
        
        TreeIterator(final K k) {
            this.index = 0;
            final Entry<K> locateKey = Object2DoubleRBTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Object2DoubleRBTreeMap.this.compare(this.next.key, k) <= 0) {
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
        
        Entry<K> nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Entry<K> next = this.next;
            this.prev = next;
            this.curr = next;
            ++this.index;
            this.updateNext();
            return this.curr;
        }
        
        void updatePrevious() {
            this.prev = this.prev.prev();
        }
        
        Entry<K> previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final Entry<K> prev = this.prev;
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
            final Entry<K> curr = this.curr;
            this.prev = curr;
            this.next = curr;
            this.updatePrevious();
            this.updateNext();
            Object2DoubleRBTreeMap.this.removeDouble(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Object2DoubleMap.Entry<K>> {
        EntryIterator() {
        }
        
        EntryIterator(final K k) {
            super(k);
        }
        
        public Object2DoubleMap.Entry<K> next() {
            return this.nextEntry();
        }
        
        public Object2DoubleMap.Entry<K> previous() {
            return this.previousEntry();
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
    
    private class KeySet extends AbstractObject2DoubleSortedMap.KeySet {
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeyIterator();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements DoubleListIterator {
        public double nextDouble() {
            return this.nextEntry().value;
        }
        
        public double previousDouble() {
            return this.previousEntry().value;
        }
    }
    
    private final class Submap extends AbstractObject2DoubleSortedMap<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        K from;
        K to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Object2DoubleMap.Entry<K>> entries;
        protected transient ObjectSortedSet<K> keys;
        protected transient DoubleCollection values;
        final /* synthetic */ Object2DoubleRBTreeMap this$0;
        
        public Submap(final K from, final boolean bottom, final K to, final boolean top) {
            if (!bottom && !top && Object2DoubleRBTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append(from).append(") is larger than end key (").append(to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Object2DoubleRBTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final K k) {
            return (this.bottom || Object2DoubleRBTreeMap.this.compare(k, this.from) >= 0) && (this.top || Object2DoubleRBTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Object2DoubleMap.Entry<K>> object2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Object2DoubleMap.Entry<K>>() {
                    @Override
                    public ObjectBidirectionalIterator<Object2DoubleMap.Entry<K>> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Object2DoubleMap.Entry<K>> iterator(final Object2DoubleMap.Entry<K> from) {
                        return new SubmapEntryIterator((K)from.getKey());
                    }
                    
                    public Comparator<? super Object2DoubleMap.Entry<K>> comparator() {
                        return Object2DoubleRBTreeMap.this.object2DoubleEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                            return false;
                        }
                        final Object2DoubleRBTreeMap.Entry<K> f = (Object2DoubleRBTreeMap.Entry<K>)Object2DoubleRBTreeMap.this.findKey(e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                            return false;
                        }
                        final Object2DoubleRBTreeMap.Entry<K> f = (Object2DoubleRBTreeMap.Entry<K>)Object2DoubleRBTreeMap.this.findKey(e.getKey());
                        if (f != null && Submap.this.in(f.key)) {
                            Submap.this.removeDouble(f.key);
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
                    
                    public Object2DoubleMap.Entry<K> first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Object2DoubleMap.Entry<K> last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2DoubleMap.Entry<K>> subSet(final Object2DoubleMap.Entry<K> from, final Object2DoubleMap.Entry<K> to) {
                        return (ObjectSortedSet<Object2DoubleMap.Entry<K>>)Submap.this.subMap(from.getKey(), to.getKey()).object2DoubleEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2DoubleMap.Entry<K>> headSet(final Object2DoubleMap.Entry<K> to) {
                        return (ObjectSortedSet<Object2DoubleMap.Entry<K>>)Submap.this.headMap(to.getKey()).object2DoubleEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2DoubleMap.Entry<K>> tailSet(final Object2DoubleMap.Entry<K> from) {
                        return (ObjectSortedSet<Object2DoubleMap.Entry<K>>)Submap.this.tailMap(from.getKey()).object2DoubleEntrySet();
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
        public DoubleCollection values() {
            if (this.values == null) {
                this.values = new AbstractDoubleCollection() {
                    @Override
                    public DoubleIterator iterator() {
                        return new SubmapValueIterator();
                    }
                    
                    @Override
                    public boolean contains(final double k) {
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
            return this.in(k) && Object2DoubleRBTreeMap.this.containsKey(k);
        }
        
        public boolean containsValue(final double v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final double ev = i.nextEntry().value;
                if (Double.doubleToLongBits(ev) == Double.doubleToLongBits(v)) {
                    return true;
                }
            }
            return false;
        }
        
        public double getDouble(final Object k) {
            final K kk = (K)k;
            final Object2DoubleRBTreeMap.Entry<K> e;
            return (this.in(kk) && (e = Object2DoubleRBTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
        }
        
        public double put(final K k, final double v) {
            Object2DoubleRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append(k).append(") out of range [").append(this.bottom ? "-" : String.valueOf(this.from)).append(", ").append(this.top ? "-" : String.valueOf(this.to)).append(")").toString());
            }
            final double oldValue = Object2DoubleRBTreeMap.this.put(k, v);
            return Object2DoubleRBTreeMap.this.modified ? this.defRetValue : oldValue;
        }
        
        public double removeDouble(final Object k) {
            Object2DoubleRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            final double oldValue = Object2DoubleRBTreeMap.this.removeDouble(k);
            return Object2DoubleRBTreeMap.this.modified ? oldValue : this.defRetValue;
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
            return Object2DoubleRBTreeMap.this.actualComparator;
        }
        
        public Object2DoubleSortedMap<K> headMap(final K to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Object2DoubleRBTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Object2DoubleSortedMap<K> tailMap(final K from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Object2DoubleRBTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Object2DoubleSortedMap<K> subMap(K from, K to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Object2DoubleRBTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Object2DoubleRBTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Object2DoubleRBTreeMap.Entry<K> firstEntry() {
            if (Object2DoubleRBTreeMap.this.tree == null) {
                return null;
            }
            Object2DoubleRBTreeMap.Entry<K> e;
            if (this.bottom) {
                e = Object2DoubleRBTreeMap.this.firstEntry;
            }
            else {
                e = Object2DoubleRBTreeMap.this.locateKey(this.from);
                if (Object2DoubleRBTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Object2DoubleRBTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Object2DoubleRBTreeMap.Entry<K> lastEntry() {
            if (Object2DoubleRBTreeMap.this.tree == null) {
                return null;
            }
            Object2DoubleRBTreeMap.Entry<K> e;
            if (this.top) {
                e = Object2DoubleRBTreeMap.this.lastEntry;
            }
            else {
                e = Object2DoubleRBTreeMap.this.locateKey(this.to);
                if (Object2DoubleRBTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Object2DoubleRBTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public K firstKey() {
            final Object2DoubleRBTreeMap.Entry<K> e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public K lastKey() {
            final Object2DoubleRBTreeMap.Entry<K> e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractObject2DoubleSortedMap.KeySet {
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
                            final Object2DoubleRBTreeMap this$0 = submap.this$0;
                            final Object2DoubleRBTreeMap.Entry<K> lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Object2DoubleRBTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Object2DoubleRBTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Object2DoubleMap.Entry<K>> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final K k) {
                super(k);
            }
            
            public Object2DoubleMap.Entry<K> next() {
                return this.nextEntry();
            }
            
            public Object2DoubleMap.Entry<K> previous() {
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
        
        private final class SubmapValueIterator extends SubmapIterator implements DoubleListIterator {
            public double nextDouble() {
                return this.nextEntry().value;
            }
            
            public double previousDouble() {
                return this.previousEntry().value;
            }
        }
    }
}

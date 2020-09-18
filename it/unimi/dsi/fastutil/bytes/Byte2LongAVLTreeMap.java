package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.longs.LongListIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.SortedSet;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.Serializable;

public class Byte2LongAVLTreeMap extends AbstractByte2LongSortedMap implements Serializable, Cloneable {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Byte2LongMap.Entry> entries;
    protected transient ByteSortedSet keys;
    protected transient LongCollection values;
    protected transient boolean modified;
    protected Comparator<? super Byte> storedComparator;
    protected transient ByteComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    
    public Byte2LongAVLTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = ByteComparators.asByteComparator(this.storedComparator);
    }
    
    public Byte2LongAVLTreeMap(final Comparator<? super Byte> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Byte2LongAVLTreeMap(final Map<? extends Byte, ? extends Long> m) {
        this();
        this.putAll(m);
    }
    
    public Byte2LongAVLTreeMap(final SortedMap<Byte, Long> m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Byte2LongAVLTreeMap(final Byte2LongMap m) {
        this();
        this.putAll(m);
    }
    
    public Byte2LongAVLTreeMap(final Byte2LongSortedMap m) {
        this(m.comparator());
        this.putAll(m);
    }
    
    public Byte2LongAVLTreeMap(final byte[] k, final long[] v, final Comparator<? super Byte> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Byte2LongAVLTreeMap(final byte[] k, final long[] v) {
        this(k, v, null);
    }
    
    final int compare(final byte k1, final byte k2) {
        return (this.actualComparator == null) ? Byte.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry findKey(final byte k) {
        Entry e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry locateKey(final byte k) {
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
    
    public long addTo(final byte k, final long incr) {
        final Entry e = this.add(k);
        final long oldValue = e.value;
        final Entry entry = e;
        entry.value += incr;
        return oldValue;
    }
    
    public long put(final byte k, final long v) {
        final Entry e = this.add(k);
        final long oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry add(final byte k) {
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
    
    public long remove(final byte k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iconst_0       
        //     2: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.modified:Z
        //     5: aload_0         /* this */
        //     6: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.tree:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //     9: ifnonnull       17
        //    12: aload_0         /* this */
        //    13: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.defRetValue:J
        //    16: lreturn        
        //    17: aload_0         /* this */
        //    18: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.tree:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //    21: astore_3        /* p */
        //    22: aconst_null    
        //    23: astore          q
        //    25: iconst_0       
        //    26: istore          dir
        //    28: iload_1         /* k */
        //    29: istore          kk
        //    31: aload_0         /* this */
        //    32: iload           kk
        //    34: aload_3         /* p */
        //    35: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.key:B
        //    38: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.compare:(BB)I
        //    41: dup            
        //    42: istore_2        /* cmp */
        //    43: ifne            49
        //    46: goto            98
        //    49: iload_2         /* cmp */
        //    50: ifle            57
        //    53: iconst_1       
        //    54: goto            58
        //    57: iconst_0       
        //    58: dup            
        //    59: istore          dir
        //    61: ifeq            81
        //    64: aload_3         /* p */
        //    65: astore          q
        //    67: aload_3         /* p */
        //    68: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:()Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //    71: dup            
        //    72: astore_3        /* p */
        //    73: ifnonnull       31
        //    76: aload_0         /* this */
        //    77: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.defRetValue:J
        //    80: lreturn        
        //    81: aload_3         /* p */
        //    82: astore          q
        //    84: aload_3         /* p */
        //    85: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:()Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //    88: dup            
        //    89: astore_3        /* p */
        //    90: ifnonnull       31
        //    93: aload_0         /* this */
        //    94: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.defRetValue:J
        //    97: lreturn        
        //    98: aload_3         /* p */
        //    99: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   102: ifnonnull       113
        //   105: aload_0         /* this */
        //   106: aload_3         /* p */
        //   107: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.next:()Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   110: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.firstEntry:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   113: aload_3         /* p */
        //   114: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   117: ifnonnull       128
        //   120: aload_0         /* this */
        //   121: aload_3         /* p */
        //   122: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   125: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.lastEntry:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   128: aload_3         /* p */
        //   129: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:()Z
        //   132: ifeq            255
        //   135: aload_3         /* p */
        //   136: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:()Z
        //   139: ifeq            199
        //   142: aload           q
        //   144: ifnull          176
        //   147: iload           dir
        //   149: ifeq            164
        //   152: aload           q
        //   154: aload_3         /* p */
        //   155: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   158: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;)V
        //   161: goto            511
        //   164: aload           q
        //   166: aload_3         /* p */
        //   167: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   170: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;)V
        //   173: goto            511
        //   176: aload_0         /* this */
        //   177: iload           dir
        //   179: ifeq            189
        //   182: aload_3         /* p */
        //   183: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   186: goto            193
        //   189: aload_3         /* p */
        //   190: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   193: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.tree:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   196: goto            511
        //   199: aload_3         /* p */
        //   200: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   203: aload_3         /* p */
        //   204: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   207: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   210: aload           q
        //   212: ifnull          244
        //   215: iload           dir
        //   217: ifeq            232
        //   220: aload           q
        //   222: aload_3         /* p */
        //   223: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   226: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   229: goto            511
        //   232: aload           q
        //   234: aload_3         /* p */
        //   235: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   238: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   241: goto            511
        //   244: aload_0         /* this */
        //   245: aload_3         /* p */
        //   246: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   249: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.tree:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   252: goto            511
        //   255: aload_3         /* p */
        //   256: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   259: astore          r
        //   261: aload           r
        //   263: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:()Z
        //   266: ifeq            360
        //   269: aload           r
        //   271: aload_3         /* p */
        //   272: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   275: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   278: aload           r
        //   280: aload_3         /* p */
        //   281: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:()Z
        //   284: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:(Z)V
        //   287: aload           r
        //   289: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:()Z
        //   292: ifne            305
        //   295: aload           r
        //   297: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   300: aload           r
        //   302: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   305: aload           q
        //   307: ifnull          335
        //   310: iload           dir
        //   312: ifeq            325
        //   315: aload           q
        //   317: aload           r
        //   319: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   322: goto            341
        //   325: aload           q
        //   327: aload           r
        //   329: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   332: goto            341
        //   335: aload_0         /* this */
        //   336: aload           r
        //   338: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.tree:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   341: aload           r
        //   343: aload_3         /* p */
        //   344: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   347: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   350: aload           r
        //   352: astore          q
        //   354: iconst_1       
        //   355: istore          dir
        //   357: goto            511
        //   360: aload           r
        //   362: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   365: astore          s
        //   367: aload           s
        //   369: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:()Z
        //   372: ifeq            378
        //   375: goto            385
        //   378: aload           s
        //   380: astore          r
        //   382: goto            360
        //   385: aload           s
        //   387: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:()Z
        //   390: ifeq            403
        //   393: aload           r
        //   395: aload           s
        //   397: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;)V
        //   400: goto            413
        //   403: aload           r
        //   405: aload           s
        //   407: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   410: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   413: aload           s
        //   415: aload_3         /* p */
        //   416: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   419: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   422: aload_3         /* p */
        //   423: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:()Z
        //   426: ifne            444
        //   429: aload_3         /* p */
        //   430: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.prev:()Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   433: aload           s
        //   435: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   438: aload           s
        //   440: iconst_0       
        //   441: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:(Z)V
        //   444: aload           s
        //   446: aload_3         /* p */
        //   447: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   450: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   453: aload           s
        //   455: iconst_0       
        //   456: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:(Z)V
        //   459: aload           q
        //   461: ifnull          489
        //   464: iload           dir
        //   466: ifeq            479
        //   469: aload           q
        //   471: aload           s
        //   473: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   476: goto            495
        //   479: aload           q
        //   481: aload           s
        //   483: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   486: goto            495
        //   489: aload_0         /* this */
        //   490: aload           s
        //   492: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.tree:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   495: aload           s
        //   497: aload_3         /* p */
        //   498: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   501: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   504: aload           r
        //   506: astore          q
        //   508: iconst_0       
        //   509: istore          dir
        //   511: aload           q
        //   513: ifnull          1474
        //   516: aload           q
        //   518: astore          y
        //   520: aload_0         /* this */
        //   521: aload           y
        //   523: invokespecial   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.parent:(Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;)Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   526: astore          q
        //   528: iload           dir
        //   530: ifne            1003
        //   533: aload           q
        //   535: ifnull          552
        //   538: aload           q
        //   540: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   543: aload           y
        //   545: if_acmpeq       552
        //   548: iconst_1       
        //   549: goto            553
        //   552: iconst_0       
        //   553: istore          dir
        //   555: aload           y
        //   557: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.incBalance:()V
        //   560: aload           y
        //   562: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   565: iconst_1       
        //   566: if_icmpne       572
        //   569: goto            1474
        //   572: aload           y
        //   574: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   577: iconst_2       
        //   578: if_icmpne       511
        //   581: aload           y
        //   583: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   586: astore          x
        //   588: getstatic       it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.$assertionsDisabled:Z
        //   591: ifne            607
        //   594: aload           x
        //   596: ifnonnull       607
        //   599: new             Ljava/lang/AssertionError;
        //   602: dup            
        //   603: invokespecial   java/lang/AssertionError.<init>:()V
        //   606: athrow         
        //   607: aload           x
        //   609: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   612: iconst_m1      
        //   613: if_icmpne       849
        //   616: getstatic       it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.$assertionsDisabled:Z
        //   619: ifne            639
        //   622: aload           x
        //   624: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   627: iconst_m1      
        //   628: if_icmpeq       639
        //   631: new             Ljava/lang/AssertionError;
        //   634: dup            
        //   635: invokespecial   java/lang/AssertionError.<init>:()V
        //   638: athrow         
        //   639: aload           x
        //   641: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   644: astore          w
        //   646: aload           x
        //   648: aload           w
        //   650: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   653: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   656: aload           w
        //   658: aload           x
        //   660: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   663: aload           y
        //   665: aload           w
        //   667: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   670: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   673: aload           w
        //   675: aload           y
        //   677: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   680: aload           w
        //   682: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   685: iconst_1       
        //   686: if_icmpne       704
        //   689: aload           x
        //   691: iconst_0       
        //   692: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   695: aload           y
        //   697: iconst_m1      
        //   698: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   701: goto            762
        //   704: aload           w
        //   706: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   709: ifne            727
        //   712: aload           x
        //   714: iconst_0       
        //   715: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   718: aload           y
        //   720: iconst_0       
        //   721: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   724: goto            762
        //   727: getstatic       it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.$assertionsDisabled:Z
        //   730: ifne            750
        //   733: aload           w
        //   735: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   738: iconst_m1      
        //   739: if_icmpeq       750
        //   742: new             Ljava/lang/AssertionError;
        //   745: dup            
        //   746: invokespecial   java/lang/AssertionError.<init>:()V
        //   749: athrow         
        //   750: aload           x
        //   752: iconst_1       
        //   753: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   756: aload           y
        //   758: iconst_0       
        //   759: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   762: aload           w
        //   764: iconst_0       
        //   765: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   768: aload           w
        //   770: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:()Z
        //   773: ifeq            789
        //   776: aload           y
        //   778: aload           w
        //   780: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;)V
        //   783: aload           w
        //   785: iconst_0       
        //   786: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:(Z)V
        //   789: aload           w
        //   791: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:()Z
        //   794: ifeq            810
        //   797: aload           x
        //   799: aload           w
        //   801: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;)V
        //   804: aload           w
        //   806: iconst_0       
        //   807: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:(Z)V
        //   810: aload           q
        //   812: ifnull          840
        //   815: iload           dir
        //   817: ifeq            830
        //   820: aload           q
        //   822: aload           w
        //   824: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   827: goto            846
        //   830: aload           q
        //   832: aload           w
        //   834: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   837: goto            846
        //   840: aload_0         /* this */
        //   841: aload           w
        //   843: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.tree:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   846: goto            1000
        //   849: aload           q
        //   851: ifnull          879
        //   854: iload           dir
        //   856: ifeq            869
        //   859: aload           q
        //   861: aload           x
        //   863: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   866: goto            885
        //   869: aload           q
        //   871: aload           x
        //   873: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   876: goto            885
        //   879: aload_0         /* this */
        //   880: aload           x
        //   882: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.tree:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   885: aload           x
        //   887: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   890: ifne            925
        //   893: aload           y
        //   895: aload           x
        //   897: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   900: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   903: aload           x
        //   905: aload           y
        //   907: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   910: aload           x
        //   912: iconst_m1      
        //   913: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   916: aload           y
        //   918: iconst_1       
        //   919: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   922: goto            1474
        //   925: getstatic       it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.$assertionsDisabled:Z
        //   928: ifne            948
        //   931: aload           x
        //   933: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //   936: iconst_1       
        //   937: if_icmpeq       948
        //   940: new             Ljava/lang/AssertionError;
        //   943: dup            
        //   944: invokespecial   java/lang/AssertionError.<init>:()V
        //   947: athrow         
        //   948: aload           x
        //   950: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:()Z
        //   953: ifeq            971
        //   956: aload           y
        //   958: iconst_1       
        //   959: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:(Z)V
        //   962: aload           x
        //   964: iconst_0       
        //   965: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:(Z)V
        //   968: goto            981
        //   971: aload           y
        //   973: aload           x
        //   975: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   978: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   981: aload           x
        //   983: aload           y
        //   985: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //   988: aload           y
        //   990: iconst_0       
        //   991: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //   994: aload           x
        //   996: iconst_0       
        //   997: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1000: goto            511
        //  1003: aload           q
        //  1005: ifnull          1022
        //  1008: aload           q
        //  1010: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1013: aload           y
        //  1015: if_acmpeq       1022
        //  1018: iconst_1       
        //  1019: goto            1023
        //  1022: iconst_0       
        //  1023: istore          dir
        //  1025: aload           y
        //  1027: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.decBalance:()V
        //  1030: aload           y
        //  1032: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //  1035: iconst_m1      
        //  1036: if_icmpne       1042
        //  1039: goto            1474
        //  1042: aload           y
        //  1044: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //  1047: bipush          -2
        //  1049: if_icmpne       511
        //  1052: aload           y
        //  1054: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1057: astore          x
        //  1059: getstatic       it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.$assertionsDisabled:Z
        //  1062: ifne            1078
        //  1065: aload           x
        //  1067: ifnonnull       1078
        //  1070: new             Ljava/lang/AssertionError;
        //  1073: dup            
        //  1074: invokespecial   java/lang/AssertionError.<init>:()V
        //  1077: athrow         
        //  1078: aload           x
        //  1080: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //  1083: iconst_1       
        //  1084: if_icmpne       1320
        //  1087: getstatic       it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.$assertionsDisabled:Z
        //  1090: ifne            1110
        //  1093: aload           x
        //  1095: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //  1098: iconst_1       
        //  1099: if_icmpeq       1110
        //  1102: new             Ljava/lang/AssertionError;
        //  1105: dup            
        //  1106: invokespecial   java/lang/AssertionError.<init>:()V
        //  1109: athrow         
        //  1110: aload           x
        //  1112: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1115: astore          w
        //  1117: aload           x
        //  1119: aload           w
        //  1121: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1124: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1127: aload           w
        //  1129: aload           x
        //  1131: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1134: aload           y
        //  1136: aload           w
        //  1138: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1141: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1144: aload           w
        //  1146: aload           y
        //  1148: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1151: aload           w
        //  1153: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //  1156: iconst_m1      
        //  1157: if_icmpne       1175
        //  1160: aload           x
        //  1162: iconst_0       
        //  1163: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1166: aload           y
        //  1168: iconst_1       
        //  1169: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1172: goto            1233
        //  1175: aload           w
        //  1177: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //  1180: ifne            1198
        //  1183: aload           x
        //  1185: iconst_0       
        //  1186: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1189: aload           y
        //  1191: iconst_0       
        //  1192: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1195: goto            1233
        //  1198: getstatic       it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.$assertionsDisabled:Z
        //  1201: ifne            1221
        //  1204: aload           w
        //  1206: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //  1209: iconst_1       
        //  1210: if_icmpeq       1221
        //  1213: new             Ljava/lang/AssertionError;
        //  1216: dup            
        //  1217: invokespecial   java/lang/AssertionError.<init>:()V
        //  1220: athrow         
        //  1221: aload           x
        //  1223: iconst_m1      
        //  1224: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1227: aload           y
        //  1229: iconst_0       
        //  1230: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1233: aload           w
        //  1235: iconst_0       
        //  1236: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1239: aload           w
        //  1241: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:()Z
        //  1244: ifeq            1260
        //  1247: aload           x
        //  1249: aload           w
        //  1251: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;)V
        //  1254: aload           w
        //  1256: iconst_0       
        //  1257: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:(Z)V
        //  1260: aload           w
        //  1262: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:()Z
        //  1265: ifeq            1281
        //  1268: aload           y
        //  1270: aload           w
        //  1272: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;)V
        //  1275: aload           w
        //  1277: iconst_0       
        //  1278: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:(Z)V
        //  1281: aload           q
        //  1283: ifnull          1311
        //  1286: iload           dir
        //  1288: ifeq            1301
        //  1291: aload           q
        //  1293: aload           w
        //  1295: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1298: goto            1317
        //  1301: aload           q
        //  1303: aload           w
        //  1305: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1308: goto            1317
        //  1311: aload_0         /* this */
        //  1312: aload           w
        //  1314: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.tree:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1317: goto            1471
        //  1320: aload           q
        //  1322: ifnull          1350
        //  1325: iload           dir
        //  1327: ifeq            1340
        //  1330: aload           q
        //  1332: aload           x
        //  1334: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1337: goto            1356
        //  1340: aload           q
        //  1342: aload           x
        //  1344: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1347: goto            1356
        //  1350: aload_0         /* this */
        //  1351: aload           x
        //  1353: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.tree:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1356: aload           x
        //  1358: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //  1361: ifne            1396
        //  1364: aload           y
        //  1366: aload           x
        //  1368: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1371: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1374: aload           x
        //  1376: aload           y
        //  1378: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1381: aload           x
        //  1383: iconst_1       
        //  1384: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1387: aload           y
        //  1389: iconst_m1      
        //  1390: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1393: goto            1474
        //  1396: getstatic       it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.$assertionsDisabled:Z
        //  1399: ifne            1419
        //  1402: aload           x
        //  1404: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:()I
        //  1407: iconst_m1      
        //  1408: if_icmpeq       1419
        //  1411: new             Ljava/lang/AssertionError;
        //  1414: dup            
        //  1415: invokespecial   java/lang/AssertionError.<init>:()V
        //  1418: athrow         
        //  1419: aload           x
        //  1421: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:()Z
        //  1424: ifeq            1442
        //  1427: aload           y
        //  1429: iconst_1       
        //  1430: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.pred:(Z)V
        //  1433: aload           x
        //  1435: iconst_0       
        //  1436: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.succ:(Z)V
        //  1439: goto            1452
        //  1442: aload           y
        //  1444: aload           x
        //  1446: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1449: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1452: aload           x
        //  1454: aload           y
        //  1456: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry;
        //  1459: aload           y
        //  1461: iconst_0       
        //  1462: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1465: aload           x
        //  1467: iconst_0       
        //  1468: invokevirtual   it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.balance:(I)V
        //  1471: goto            511
        //  1474: aload_0         /* this */
        //  1475: iconst_1       
        //  1476: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.modified:Z
        //  1479: aload_0         /* this */
        //  1480: dup            
        //  1481: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.count:I
        //  1484: iconst_1       
        //  1485: isub           
        //  1486: putfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap.count:I
        //  1489: aload_3         /* p */
        //  1490: getfield        it/unimi/dsi/fastutil/bytes/Byte2LongAVLTreeMap$Entry.value:J
        //  1493: lreturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 4F 11 FF 00 0D 00 07 07 00 02 01 00 07 00 1D 07 00 1D 01 01 00 00 FF 00 11 00 07 07 00 02 01 01 07 00 1D 07 00 1D 01 01 00 00 07 40 01 16 10 0E 0E 23 0B 4C 07 00 02 FF 00 03 00 07 07 00 02 01 01 07 00 1D 07 00 1D 01 01 00 02 07 00 02 07 00 1D 05 20 0B 0A FC 00 31 07 00 1D 13 09 05 12 FC 00 11 07 00 1D 06 11 09 1E 22 09 05 F9 00 0F FC 00 28 07 00 1D 40 01 12 FC 00 22 07 00 1D 1F FC 00 40 07 00 1D 16 16 0B 1A 14 13 09 FA 00 05 02 13 09 05 27 16 16 09 FA 00 12 02 12 40 01 12 FC 00 23 07 00 1D 1F FC 00 40 07 00 1D 16 16 0B 1A 14 13 09 FA 00 05 02 13 09 05 27 16 16 09 FA 00 12 FA 00 02
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public boolean containsValue(final long v) {
        final ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            final long ev = i.nextLong();
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
    
    public boolean containsKey(final byte k) {
        return this.findKey(k) != null;
    }
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public long get(final byte k) {
        final Entry e = this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    public byte firstByteKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public byte lastByteKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    public ObjectSortedSet<Byte2LongMap.Entry> byte2LongEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Byte2LongMap.Entry>() {
                final Comparator<? super Byte2LongMap.Entry> comparator = (x, y) -> Byte2LongAVLTreeMap.this.actualComparator.compare(x.getByteKey(), y.getByteKey());
                
                public Comparator<? super Byte2LongMap.Entry> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Byte2LongMap.Entry> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Byte2LongMap.Entry> iterator(final Byte2LongMap.Entry from) {
                    return new EntryIterator(from.getByteKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                        return false;
                    }
                    final Entry f = Byte2LongAVLTreeMap.this.findKey((byte)e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                        return false;
                    }
                    final Entry f = Byte2LongAVLTreeMap.this.findKey((byte)e.getKey());
                    if (f == null || f.getLongValue() != (long)e.getValue()) {
                        return false;
                    }
                    Byte2LongAVLTreeMap.this.remove(f.key);
                    return true;
                }
                
                public int size() {
                    return Byte2LongAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Byte2LongAVLTreeMap.this.clear();
                }
                
                public Byte2LongMap.Entry first() {
                    return Byte2LongAVLTreeMap.this.firstEntry;
                }
                
                public Byte2LongMap.Entry last() {
                    return Byte2LongAVLTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Byte2LongMap.Entry> subSet(final Byte2LongMap.Entry from, final Byte2LongMap.Entry to) {
                    return Byte2LongAVLTreeMap.this.subMap(from.getByteKey(), to.getByteKey()).byte2LongEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Byte2LongMap.Entry> headSet(final Byte2LongMap.Entry to) {
                    return Byte2LongAVLTreeMap.this.headMap(to.getByteKey()).byte2LongEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Byte2LongMap.Entry> tailSet(final Byte2LongMap.Entry from) {
                    return Byte2LongAVLTreeMap.this.tailMap(from.getByteKey()).byte2LongEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public ByteSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }
    
    @Override
    public LongCollection values() {
        if (this.values == null) {
            this.values = new AbstractLongCollection() {
                @Override
                public LongIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public boolean contains(final long k) {
                    return Byte2LongAVLTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Byte2LongAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Byte2LongAVLTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public ByteComparator comparator() {
        return this.actualComparator;
    }
    
    public Byte2LongSortedMap headMap(final byte to) {
        return new Submap((byte)0, true, to, false);
    }
    
    public Byte2LongSortedMap tailMap(final byte from) {
        return new Submap(from, false, (byte)0, true);
    }
    
    public Byte2LongSortedMap subMap(final byte from, final byte to) {
        return new Submap(from, false, to, false);
    }
    
    public Byte2LongAVLTreeMap clone() {
        Byte2LongAVLTreeMap c;
        try {
            c = (Byte2LongAVLTreeMap)super.clone();
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
            s.writeByte((int)e.key);
            s.writeLong(e.value);
        }
    }
    
    private Entry readTree(final ObjectInputStream s, final int n, final Entry pred, final Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry top = new Entry(s.readByte(), s.readLong());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            final Entry top = new Entry(s.readByte(), s.readLong());
            top.right(new Entry(s.readByte(), s.readLong()));
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
        top2.key = s.readByte();
        top2.value = s.readLong();
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
            super((byte)0, 0L);
        }
        
        Entry(final byte k, final long v) {
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
        public long setValue(final long value) {
            final long oldValue = this.value;
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
            final Map.Entry<Byte, Long> e = (Map.Entry<Byte, Long>)o;
            return this.key == (byte)e.getKey() && this.value == (long)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key ^ HashCommon.long2int(this.value);
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append((int)this.key).append("=>").append(this.value).toString();
        }
    }
    
    private class TreeIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        
        TreeIterator() {
            this.index = 0;
            this.next = Byte2LongAVLTreeMap.this.firstEntry;
        }
        
        TreeIterator(final byte k) {
            this.index = 0;
            final Entry locateKey = Byte2LongAVLTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Byte2LongAVLTreeMap.this.compare(this.next.key, k) <= 0) {
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
            Byte2LongAVLTreeMap.this.remove(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Byte2LongMap.Entry> {
        EntryIterator() {
        }
        
        EntryIterator(final byte k) {
            super(k);
        }
        
        public Byte2LongMap.Entry next() {
            return this.nextEntry();
        }
        
        public Byte2LongMap.Entry previous() {
            return this.previousEntry();
        }
        
        @Override
        public void set(final Byte2LongMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Byte2LongMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements ByteListIterator {
        public KeyIterator() {
        }
        
        public KeyIterator(final byte k) {
            super(k);
        }
        
        public byte nextByte() {
            return this.nextEntry().key;
        }
        
        public byte previousByte() {
            return this.previousEntry().key;
        }
    }
    
    private class KeySet extends AbstractByte2LongSortedMap.KeySet {
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeyIterator(from);
        }
    }
    
    private final class ValueIterator extends TreeIterator implements LongListIterator {
        public long nextLong() {
            return this.nextEntry().value;
        }
        
        public long previousLong() {
            return this.previousEntry().value;
        }
    }
    
    private final class Submap extends AbstractByte2LongSortedMap implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        byte from;
        byte to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Byte2LongMap.Entry> entries;
        protected transient ByteSortedSet keys;
        protected transient LongCollection values;
        final /* synthetic */ Byte2LongAVLTreeMap this$0;
        
        public Submap(final byte from, final boolean bottom, final byte to, final boolean top) {
            if (!bottom && !top && Byte2LongAVLTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append((int)from).append(") is larger than end key (").append((int)to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Byte2LongAVLTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final byte k) {
            return (this.bottom || Byte2LongAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Byte2LongAVLTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Byte2LongMap.Entry> byte2LongEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Byte2LongMap.Entry>() {
                    @Override
                    public ObjectBidirectionalIterator<Byte2LongMap.Entry> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Byte2LongMap.Entry> iterator(final Byte2LongMap.Entry from) {
                        return new SubmapEntryIterator(from.getByteKey());
                    }
                    
                    public Comparator<? super Byte2LongMap.Entry> comparator() {
                        return Byte2LongAVLTreeMap.this.byte2LongEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                            return false;
                        }
                        final Byte2LongAVLTreeMap.Entry f = Byte2LongAVLTreeMap.this.findKey((byte)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Byte)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                            return false;
                        }
                        final Byte2LongAVLTreeMap.Entry f = Byte2LongAVLTreeMap.this.findKey((byte)e.getKey());
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
                    
                    public Byte2LongMap.Entry first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Byte2LongMap.Entry last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Byte2LongMap.Entry> subSet(final Byte2LongMap.Entry from, final Byte2LongMap.Entry to) {
                        return Submap.this.subMap(from.getByteKey(), to.getByteKey()).byte2LongEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Byte2LongMap.Entry> headSet(final Byte2LongMap.Entry to) {
                        return Submap.this.headMap(to.getByteKey()).byte2LongEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Byte2LongMap.Entry> tailSet(final Byte2LongMap.Entry from) {
                        return Submap.this.tailMap(from.getByteKey()).byte2LongEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet();
            }
            return this.keys;
        }
        
        @Override
        public LongCollection values() {
            if (this.values == null) {
                this.values = new AbstractLongCollection() {
                    @Override
                    public LongIterator iterator() {
                        return new SubmapValueIterator();
                    }
                    
                    @Override
                    public boolean contains(final long k) {
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
        
        public boolean containsKey(final byte k) {
            return this.in(k) && Byte2LongAVLTreeMap.this.containsKey(k);
        }
        
        public boolean containsValue(final long v) {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                final long ev = i.nextEntry().value;
                if (ev == v) {
                    return true;
                }
            }
            return false;
        }
        
        public long get(final byte k) {
            final byte kk = k;
            final Byte2LongAVLTreeMap.Entry e;
            return (this.in(kk) && (e = Byte2LongAVLTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
        }
        
        public long put(final byte k, final long v) {
            Byte2LongAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append((int)k).append(") out of range [").append(this.bottom ? "-" : String.valueOf((int)this.from)).append(", ").append(this.top ? "-" : String.valueOf((int)this.to)).append(")").toString());
            }
            final long oldValue = Byte2LongAVLTreeMap.this.put(k, v);
            return Byte2LongAVLTreeMap.this.modified ? this.defRetValue : oldValue;
        }
        
        public long remove(final byte k) {
            Byte2LongAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            final long oldValue = Byte2LongAVLTreeMap.this.remove(k);
            return Byte2LongAVLTreeMap.this.modified ? oldValue : this.defRetValue;
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
        
        public ByteComparator comparator() {
            return Byte2LongAVLTreeMap.this.actualComparator;
        }
        
        public Byte2LongSortedMap headMap(final byte to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Byte2LongAVLTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Byte2LongSortedMap tailMap(final byte from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Byte2LongAVLTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Byte2LongSortedMap subMap(byte from, byte to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Byte2LongAVLTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Byte2LongAVLTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Byte2LongAVLTreeMap.Entry firstEntry() {
            if (Byte2LongAVLTreeMap.this.tree == null) {
                return null;
            }
            Byte2LongAVLTreeMap.Entry e;
            if (this.bottom) {
                e = Byte2LongAVLTreeMap.this.firstEntry;
            }
            else {
                e = Byte2LongAVLTreeMap.this.locateKey(this.from);
                if (Byte2LongAVLTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Byte2LongAVLTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Byte2LongAVLTreeMap.Entry lastEntry() {
            if (Byte2LongAVLTreeMap.this.tree == null) {
                return null;
            }
            Byte2LongAVLTreeMap.Entry e;
            if (this.top) {
                e = Byte2LongAVLTreeMap.this.lastEntry;
            }
            else {
                e = Byte2LongAVLTreeMap.this.locateKey(this.to);
                if (Byte2LongAVLTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Byte2LongAVLTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public byte firstByteKey() {
            final Byte2LongAVLTreeMap.Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public byte lastByteKey() {
            final Byte2LongAVLTreeMap.Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractByte2LongSortedMap.KeySet {
            @Override
            public ByteBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public ByteBidirectionalIterator iterator(final byte from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final byte k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Byte2LongAVLTreeMap this$0 = submap.this$0;
                            final Byte2LongAVLTreeMap.Entry lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Byte2LongAVLTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Byte2LongAVLTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Byte2LongMap.Entry> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final byte k) {
                super(k);
            }
            
            public Byte2LongMap.Entry next() {
                return this.nextEntry();
            }
            
            public Byte2LongMap.Entry previous() {
                return this.previousEntry();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements ByteListIterator {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final byte from) {
                super(from);
            }
            
            public byte nextByte() {
                return this.nextEntry().key;
            }
            
            public byte previousByte() {
                return this.previousEntry().key;
            }
        }
        
        private final class SubmapValueIterator extends SubmapIterator implements LongListIterator {
            public long nextLong() {
                return this.nextEntry().value;
            }
            
            public long previousLong() {
                return this.previousEntry().value;
            }
        }
    }
}

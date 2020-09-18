package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.longs.LongListIterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map;
import java.util.Comparator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.io.Serializable;

public class Object2LongRBTreeMap<K> extends AbstractObject2LongSortedMap<K> implements Serializable, Cloneable {
    protected transient Entry<K> tree;
    protected int count;
    protected transient Entry<K> firstEntry;
    protected transient Entry<K> lastEntry;
    protected transient ObjectSortedSet<Object2LongMap.Entry<K>> entries;
    protected transient ObjectSortedSet<K> keys;
    protected transient LongCollection values;
    protected transient boolean modified;
    protected Comparator<? super K> storedComparator;
    protected transient Comparator<? super K> actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    private transient Entry<K>[] nodePath;
    
    public Object2LongRBTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = this.storedComparator;
    }
    
    public Object2LongRBTreeMap(final Comparator<? super K> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Object2LongRBTreeMap(final Map<? extends K, ? extends Long> m) {
        this();
        this.putAll(m);
    }
    
    public Object2LongRBTreeMap(final SortedMap<K, Long> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends K, ? extends Long>)m);
    }
    
    public Object2LongRBTreeMap(final Object2LongMap<? extends K> m) {
        this();
        this.putAll((java.util.Map<? extends K, ? extends Long>)m);
    }
    
    public Object2LongRBTreeMap(final Object2LongSortedMap<K> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends K, ? extends Long>)m);
    }
    
    public Object2LongRBTreeMap(final K[] k, final long[] v, final Comparator<? super K> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Object2LongRBTreeMap(final K[] k, final long[] v) {
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
    
    public long addTo(final K k, final long incr) {
        final Entry<K> e = this.add(k);
        final long oldValue = e.value;
        final Entry<K> entry = e;
        entry.value += incr;
        return oldValue;
    }
    
    public long put(final K k, final long v) {
        final Entry<K> e = this.add(k);
        final long oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry<K> add(final K k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iconst_0       
        //     2: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.modified:Z
        //     5: iconst_0       
        //     6: istore_2        /* maxDepth */
        //     7: aload_0         /* this */
        //     8: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //    11: ifnonnull       55
        //    14: aload_0         /* this */
        //    15: dup            
        //    16: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.count:I
        //    19: iconst_1       
        //    20: iadd           
        //    21: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.count:I
        //    24: aload_0         /* this */
        //    25: aload_0         /* this */
        //    26: aload_0         /* this */
        //    27: new             Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //    30: dup            
        //    31: aload_1         /* k */
        //    32: aload_0         /* this */
        //    33: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.defRetValue:J
        //    36: invokespecial   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.<init>:(Ljava/lang/Object;J)V
        //    39: dup_x1         
        //    40: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.firstEntry:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //    43: dup_x1         
        //    44: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.lastEntry:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //    47: dup_x1         
        //    48: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //    51: astore_3        /* e */
        //    52: goto            908
        //    55: aload_0         /* this */
        //    56: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //    59: astore          p
        //    61: iconst_0       
        //    62: istore          i
        //    64: aload_0         /* this */
        //    65: aload_1         /* k */
        //    66: aload           p
        //    68: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.key:Ljava/lang/Object;
        //    71: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.compare:(Ljava/lang/Object;Ljava/lang/Object;)I
        //    74: dup            
        //    75: istore          cmp
        //    77: ifne            102
        //    80: iload           i
        //    82: iinc            i, -1
        //    85: ifeq            99
        //    88: aload_0         /* this */
        //    89: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //    92: iload           i
        //    94: aconst_null    
        //    95: aastore        
        //    96: goto            80
        //    99: aload           p
        //   101: areturn        
        //   102: aload_0         /* this */
        //   103: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   106: iload           i
        //   108: aload           p
        //   110: aastore        
        //   111: aload_0         /* this */
        //   112: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.dirPath:[Z
        //   115: iload           i
        //   117: iinc            i, 1
        //   120: iload           cmp
        //   122: ifle            129
        //   125: iconst_1       
        //   126: goto            130
        //   129: iconst_0       
        //   130: dup_x2         
        //   131: bastore        
        //   132: ifeq            213
        //   135: aload           p
        //   137: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.succ:()Z
        //   140: ifeq            203
        //   143: aload_0         /* this */
        //   144: dup            
        //   145: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.count:I
        //   148: iconst_1       
        //   149: iadd           
        //   150: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.count:I
        //   153: new             Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   156: dup            
        //   157: aload_1         /* k */
        //   158: aload_0         /* this */
        //   159: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.defRetValue:J
        //   162: invokespecial   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.<init>:(Ljava/lang/Object;J)V
        //   165: astore_3        /* e */
        //   166: aload           p
        //   168: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   171: ifnonnull       179
        //   174: aload_0         /* this */
        //   175: aload_3         /* e */
        //   176: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.lastEntry:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   179: aload_3         /* e */
        //   180: aload           p
        //   182: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   185: aload_3         /* e */
        //   186: aload           p
        //   188: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   191: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   194: aload           p
        //   196: aload_3         /* e */
        //   197: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:(Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;)V
        //   200: goto            291
        //   203: aload           p
        //   205: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   208: astore          p
        //   210: goto            64
        //   213: aload           p
        //   215: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.pred:()Z
        //   218: ifeq            281
        //   221: aload_0         /* this */
        //   222: dup            
        //   223: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.count:I
        //   226: iconst_1       
        //   227: iadd           
        //   228: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.count:I
        //   231: new             Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   234: dup            
        //   235: aload_1         /* k */
        //   236: aload_0         /* this */
        //   237: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.defRetValue:J
        //   240: invokespecial   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.<init>:(Ljava/lang/Object;J)V
        //   243: astore_3        /* e */
        //   244: aload           p
        //   246: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   249: ifnonnull       257
        //   252: aload_0         /* this */
        //   253: aload_3         /* e */
        //   254: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.firstEntry:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   257: aload_3         /* e */
        //   258: aload           p
        //   260: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   263: aload_3         /* e */
        //   264: aload           p
        //   266: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   269: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   272: aload           p
        //   274: aload_3         /* e */
        //   275: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:(Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;)V
        //   278: goto            291
        //   281: aload           p
        //   283: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   286: astore          p
        //   288: goto            64
        //   291: aload_0         /* this */
        //   292: iconst_1       
        //   293: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.modified:Z
        //   296: iload           i
        //   298: iinc            i, -1
        //   301: istore_2        /* maxDepth */
        //   302: iload           i
        //   304: ifle            908
        //   307: aload_0         /* this */
        //   308: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   311: iload           i
        //   313: aaload         
        //   314: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:()Z
        //   317: ifne            908
        //   320: aload_0         /* this */
        //   321: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.dirPath:[Z
        //   324: iload           i
        //   326: iconst_1       
        //   327: isub           
        //   328: baload         
        //   329: ifne            620
        //   332: aload_0         /* this */
        //   333: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   336: iload           i
        //   338: iconst_1       
        //   339: isub           
        //   340: aaload         
        //   341: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   344: astore          y
        //   346: aload_0         /* this */
        //   347: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   350: iload           i
        //   352: iconst_1       
        //   353: isub           
        //   354: aaload         
        //   355: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.succ:()Z
        //   358: ifne            405
        //   361: aload           y
        //   363: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:()Z
        //   366: ifne            405
        //   369: aload_0         /* this */
        //   370: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   373: iload           i
        //   375: aaload         
        //   376: iconst_1       
        //   377: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   380: aload           y
        //   382: iconst_1       
        //   383: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   386: aload_0         /* this */
        //   387: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   390: iload           i
        //   392: iconst_1       
        //   393: isub           
        //   394: aaload         
        //   395: iconst_0       
        //   396: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   399: iinc            i, -2
        //   402: goto            617
        //   405: aload_0         /* this */
        //   406: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.dirPath:[Z
        //   409: iload           i
        //   411: baload         
        //   412: ifne            427
        //   415: aload_0         /* this */
        //   416: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   419: iload           i
        //   421: aaload         
        //   422: astore          y
        //   424: goto            495
        //   427: aload_0         /* this */
        //   428: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   431: iload           i
        //   433: aaload         
        //   434: astore          x
        //   436: aload           x
        //   438: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   441: astore          y
        //   443: aload           x
        //   445: aload           y
        //   447: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   450: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   453: aload           y
        //   455: aload           x
        //   457: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   460: aload_0         /* this */
        //   461: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   464: iload           i
        //   466: iconst_1       
        //   467: isub           
        //   468: aaload         
        //   469: aload           y
        //   471: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   474: aload           y
        //   476: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.pred:()Z
        //   479: ifeq            495
        //   482: aload           y
        //   484: iconst_0       
        //   485: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.pred:(Z)V
        //   488: aload           x
        //   490: aload           y
        //   492: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;)V
        //   495: aload_0         /* this */
        //   496: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   499: iload           i
        //   501: iconst_1       
        //   502: isub           
        //   503: aaload         
        //   504: astore          x
        //   506: aload           x
        //   508: iconst_0       
        //   509: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   512: aload           y
        //   514: iconst_1       
        //   515: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   518: aload           x
        //   520: aload           y
        //   522: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   525: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   528: aload           y
        //   530: aload           x
        //   532: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   535: iload           i
        //   537: iconst_2       
        //   538: if_icmpge       550
        //   541: aload_0         /* this */
        //   542: aload           y
        //   544: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   547: goto            593
        //   550: aload_0         /* this */
        //   551: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.dirPath:[Z
        //   554: iload           i
        //   556: iconst_2       
        //   557: isub           
        //   558: baload         
        //   559: ifeq            579
        //   562: aload_0         /* this */
        //   563: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   566: iload           i
        //   568: iconst_2       
        //   569: isub           
        //   570: aaload         
        //   571: aload           y
        //   573: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   576: goto            593
        //   579: aload_0         /* this */
        //   580: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   583: iload           i
        //   585: iconst_2       
        //   586: isub           
        //   587: aaload         
        //   588: aload           y
        //   590: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   593: aload           y
        //   595: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.succ:()Z
        //   598: ifeq            908
        //   601: aload           y
        //   603: iconst_0       
        //   604: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.succ:(Z)V
        //   607: aload           x
        //   609: aload           y
        //   611: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;)V
        //   614: goto            908
        //   617: goto            302
        //   620: aload_0         /* this */
        //   621: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   624: iload           i
        //   626: iconst_1       
        //   627: isub           
        //   628: aaload         
        //   629: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   632: astore          y
        //   634: aload_0         /* this */
        //   635: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   638: iload           i
        //   640: iconst_1       
        //   641: isub           
        //   642: aaload         
        //   643: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.pred:()Z
        //   646: ifne            693
        //   649: aload           y
        //   651: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:()Z
        //   654: ifne            693
        //   657: aload_0         /* this */
        //   658: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   661: iload           i
        //   663: aaload         
        //   664: iconst_1       
        //   665: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   668: aload           y
        //   670: iconst_1       
        //   671: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   674: aload_0         /* this */
        //   675: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   678: iload           i
        //   680: iconst_1       
        //   681: isub           
        //   682: aaload         
        //   683: iconst_0       
        //   684: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   687: iinc            i, -2
        //   690: goto            905
        //   693: aload_0         /* this */
        //   694: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.dirPath:[Z
        //   697: iload           i
        //   699: baload         
        //   700: ifeq            715
        //   703: aload_0         /* this */
        //   704: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   707: iload           i
        //   709: aaload         
        //   710: astore          y
        //   712: goto            783
        //   715: aload_0         /* this */
        //   716: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   719: iload           i
        //   721: aaload         
        //   722: astore          x
        //   724: aload           x
        //   726: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   729: astore          y
        //   731: aload           x
        //   733: aload           y
        //   735: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   738: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   741: aload           y
        //   743: aload           x
        //   745: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   748: aload_0         /* this */
        //   749: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   752: iload           i
        //   754: iconst_1       
        //   755: isub           
        //   756: aaload         
        //   757: aload           y
        //   759: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   762: aload           y
        //   764: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.succ:()Z
        //   767: ifeq            783
        //   770: aload           y
        //   772: iconst_0       
        //   773: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.succ:(Z)V
        //   776: aload           x
        //   778: aload           y
        //   780: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;)V
        //   783: aload_0         /* this */
        //   784: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   787: iload           i
        //   789: iconst_1       
        //   790: isub           
        //   791: aaload         
        //   792: astore          x
        //   794: aload           x
        //   796: iconst_0       
        //   797: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   800: aload           y
        //   802: iconst_1       
        //   803: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   806: aload           x
        //   808: aload           y
        //   810: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   813: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   816: aload           y
        //   818: aload           x
        //   820: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   823: iload           i
        //   825: iconst_2       
        //   826: if_icmpge       838
        //   829: aload_0         /* this */
        //   830: aload           y
        //   832: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   835: goto            881
        //   838: aload_0         /* this */
        //   839: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.dirPath:[Z
        //   842: iload           i
        //   844: iconst_2       
        //   845: isub           
        //   846: baload         
        //   847: ifeq            867
        //   850: aload_0         /* this */
        //   851: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   854: iload           i
        //   856: iconst_2       
        //   857: isub           
        //   858: aaload         
        //   859: aload           y
        //   861: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.right:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   864: goto            881
        //   867: aload_0         /* this */
        //   868: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   871: iload           i
        //   873: iconst_2       
        //   874: isub           
        //   875: aaload         
        //   876: aload           y
        //   878: putfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.left:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   881: aload           y
        //   883: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.pred:()Z
        //   886: ifeq            908
        //   889: aload           y
        //   891: iconst_0       
        //   892: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.pred:(Z)V
        //   895: aload           x
        //   897: aload           y
        //   899: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;)V
        //   902: goto            908
        //   905: goto            302
        //   908: aload_0         /* this */
        //   909: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.tree:Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   912: iconst_1       
        //   913: invokevirtual   it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry.black:(Z)V
        //   916: iload_2         /* maxDepth */
        //   917: iinc            maxDepth, -1
        //   920: ifeq            933
        //   923: aload_0         /* this */
        //   924: getfield        it/unimi/dsi/fastutil/objects/Object2LongRBTreeMap.nodePath:[Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry;
        //   927: iload_2         /* maxDepth */
        //   928: aconst_null    
        //   929: aastore        
        //   930: goto            916
        //   933: aload_3         /* e */
        //   934: areturn        
        //    Signature:
        //  (TK;)Lit/unimi/dsi/fastutil/objects/Object2LongRBTreeMap$Entry<TK;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 20 FC 00 37 01 FF 00 08 00 07 07 00 02 07 00 E6 01 00 07 00 1E 00 01 00 00 FF 00 0F 00 07 07 00 02 07 00 E6 01 00 07 00 1E 01 01 00 00 12 02 FF 00 1A 00 07 07 00 02 07 00 E6 01 00 07 00 1E 01 01 00 02 07 00 E7 01 FF 00 00 00 07 07 00 02 07 00 E6 01 00 07 00 1E 01 01 00 03 07 00 E7 01 01 FF 00 30 00 07 07 00 02 07 00 E6 01 07 00 1E 07 00 1E 01 01 00 00 FF 00 17 00 07 07 00 02 07 00 E6 01 00 07 00 1E 01 01 00 00 09 FF 00 2B 00 07 07 00 02 07 00 E6 01 07 00 1E 07 00 1E 01 01 00 00 FF 00 17 00 07 07 00 02 07 00 E6 01 00 07 00 1E 01 01 00 00 FF 00 09 00 07 07 00 02 07 00 E6 01 07 00 1E 07 00 1E 01 01 00 00 0A FC 00 66 07 00 1E 15 FB 00 43 FC 00 36 07 00 1E 1C 0D F9 00 17 02 FC 00 48 07 00 1E 15 FB 00 43 FC 00 36 07 00 1E 1C 0D F9 00 17 F8 00 02 07 10
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 5
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1063)
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
    
    public long removeLong(final Object k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry<K> p = this.tree;
        int i = 0;
        final K kk = (K)k;
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
            Entry<K> r = p.right;
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
                Entry<K> s;
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
                    final Entry<K> x = this.dirPath[i - 1] ? this.nodePath[i - 1].right : this.nodePath[i - 1].left;
                    if (!x.black()) {
                        x.black(true);
                        break;
                    }
                }
                if (!this.dirPath[i - 1]) {
                    Entry<K> w = this.nodePath[i - 1].right;
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
                            final Entry<K> y = w.left;
                            y.black(true);
                            w.black(false);
                            w.left = y.right;
                            y.right = w;
                            final Entry<K> entry = this.nodePath[i - 1];
                            final Entry<K> right = y;
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
                    Entry<K> w = this.nodePath[i - 1].left;
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
                            final Entry<K> y = w.right;
                            y.black(true);
                            w.black(false);
                            w.right = y.left;
                            y.left = w;
                            final Entry<K> entry2 = this.nodePath[i - 1];
                            final Entry<K> left = y;
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
    
    public long getLong(final Object k) {
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
    
    public ObjectSortedSet<Object2LongMap.Entry<K>> object2LongEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Object2LongMap.Entry<K>>() {
                final Comparator<? super Object2LongMap.Entry<K>> comparator = (x, y) -> Object2LongRBTreeMap.this.actualComparator.compare(x.getKey(), y.getKey());
                
                public Comparator<? super Object2LongMap.Entry<K>> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Object2LongMap.Entry<K>> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Object2LongMap.Entry<K>> iterator(final Object2LongMap.Entry<K> from) {
                    return new EntryIterator((K)from.getKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                        return false;
                    }
                    final Entry<K> f = (Entry<K>)Object2LongRBTreeMap.this.findKey(e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                        return false;
                    }
                    final Entry<K> f = (Entry<K>)Object2LongRBTreeMap.this.findKey(e.getKey());
                    if (f == null || f.getLongValue() != (long)e.getValue()) {
                        return false;
                    }
                    Object2LongRBTreeMap.this.removeLong(f.key);
                    return true;
                }
                
                public int size() {
                    return Object2LongRBTreeMap.this.count;
                }
                
                public void clear() {
                    Object2LongRBTreeMap.this.clear();
                }
                
                public Object2LongMap.Entry<K> first() {
                    return Object2LongRBTreeMap.this.firstEntry;
                }
                
                public Object2LongMap.Entry<K> last() {
                    return Object2LongRBTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Object2LongMap.Entry<K>> subSet(final Object2LongMap.Entry<K> from, final Object2LongMap.Entry<K> to) {
                    return (ObjectSortedSet<Object2LongMap.Entry<K>>)Object2LongRBTreeMap.this.subMap(from.getKey(), to.getKey()).object2LongEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Object2LongMap.Entry<K>> headSet(final Object2LongMap.Entry<K> to) {
                    return (ObjectSortedSet<Object2LongMap.Entry<K>>)Object2LongRBTreeMap.this.headMap(to.getKey()).object2LongEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Object2LongMap.Entry<K>> tailSet(final Object2LongMap.Entry<K> from) {
                    return (ObjectSortedSet<Object2LongMap.Entry<K>>)Object2LongRBTreeMap.this.tailMap(from.getKey()).object2LongEntrySet();
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
    public LongCollection values() {
        if (this.values == null) {
            this.values = new AbstractLongCollection() {
                @Override
                public LongIterator iterator() {
                    return new ValueIterator();
                }
                
                @Override
                public boolean contains(final long k) {
                    return Object2LongRBTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Object2LongRBTreeMap.this.count;
                }
                
                public void clear() {
                    Object2LongRBTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public Comparator<? super K> comparator() {
        return this.actualComparator;
    }
    
    public Object2LongSortedMap<K> headMap(final K to) {
        return new Submap(null, true, to, false);
    }
    
    public Object2LongSortedMap<K> tailMap(final K from) {
        return new Submap(from, false, null, true);
    }
    
    public Object2LongSortedMap<K> subMap(final K from, final K to) {
        return new Submap(from, false, to, false);
    }
    
    public Object2LongRBTreeMap<K> clone() {
        Object2LongRBTreeMap<K> c;
        try {
            c = (Object2LongRBTreeMap)super.clone();
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
            s.writeLong(e.value);
        }
    }
    
    private Entry<K> readTree(final ObjectInputStream s, final int n, final Entry<K> pred, final Entry<K> succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry<K> top = new Entry<K>((K)s.readObject(), s.readLong());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            final Entry<K> top = new Entry<K>((K)s.readObject(), s.readLong());
            top.black(true);
            top.right(new Entry<K>((K)s.readObject(), s.readLong()));
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
        top2.value = s.readLong();
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
            super(null, 0L);
        }
        
        Entry(final K k, final long v) {
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
        public long setValue(final long value) {
            final long oldValue = this.value;
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
            final Map.Entry<K, Long> e = (Map.Entry<K, Long>)o;
            return Objects.equals(this.key, e.getKey()) && this.value == (long)e.getValue();
        }
        
        @Override
        public int hashCode() {
            return this.key.hashCode() ^ HashCommon.long2int(this.value);
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
            this.next = Object2LongRBTreeMap.this.firstEntry;
        }
        
        TreeIterator(final K k) {
            this.index = 0;
            final Entry<K> locateKey = Object2LongRBTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Object2LongRBTreeMap.this.compare(this.next.key, k) <= 0) {
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
            Object2LongRBTreeMap.this.removeLong(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Object2LongMap.Entry<K>> {
        EntryIterator() {
        }
        
        EntryIterator(final K k) {
            super(k);
        }
        
        public Object2LongMap.Entry<K> next() {
            return this.nextEntry();
        }
        
        public Object2LongMap.Entry<K> previous() {
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
    
    private class KeySet extends AbstractObject2LongSortedMap.KeySet {
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeyIterator();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
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
    
    private final class Submap extends AbstractObject2LongSortedMap<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        K from;
        K to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Object2LongMap.Entry<K>> entries;
        protected transient ObjectSortedSet<K> keys;
        protected transient LongCollection values;
        final /* synthetic */ Object2LongRBTreeMap this$0;
        
        public Submap(final K from, final boolean bottom, final K to, final boolean top) {
            if (!bottom && !top && Object2LongRBTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append(from).append(") is larger than end key (").append(to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Object2LongRBTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final K k) {
            return (this.bottom || Object2LongRBTreeMap.this.compare(k, this.from) >= 0) && (this.top || Object2LongRBTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Object2LongMap.Entry<K>> object2LongEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Object2LongMap.Entry<K>>() {
                    @Override
                    public ObjectBidirectionalIterator<Object2LongMap.Entry<K>> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Object2LongMap.Entry<K>> iterator(final Object2LongMap.Entry<K> from) {
                        return new SubmapEntryIterator((K)from.getKey());
                    }
                    
                    public Comparator<? super Object2LongMap.Entry<K>> comparator() {
                        return Object2LongRBTreeMap.this.object2LongEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                            return false;
                        }
                        final Object2LongRBTreeMap.Entry<K> f = (Object2LongRBTreeMap.Entry<K>)Object2LongRBTreeMap.this.findKey(e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getValue() == null || !(e.getValue() instanceof Long)) {
                            return false;
                        }
                        final Object2LongRBTreeMap.Entry<K> f = (Object2LongRBTreeMap.Entry<K>)Object2LongRBTreeMap.this.findKey(e.getKey());
                        if (f != null && Submap.this.in(f.key)) {
                            Submap.this.removeLong(f.key);
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
                    
                    public Object2LongMap.Entry<K> first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Object2LongMap.Entry<K> last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2LongMap.Entry<K>> subSet(final Object2LongMap.Entry<K> from, final Object2LongMap.Entry<K> to) {
                        return (ObjectSortedSet<Object2LongMap.Entry<K>>)Submap.this.subMap(from.getKey(), to.getKey()).object2LongEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2LongMap.Entry<K>> headSet(final Object2LongMap.Entry<K> to) {
                        return (ObjectSortedSet<Object2LongMap.Entry<K>>)Submap.this.headMap(to.getKey()).object2LongEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Object2LongMap.Entry<K>> tailSet(final Object2LongMap.Entry<K> from) {
                        return (ObjectSortedSet<Object2LongMap.Entry<K>>)Submap.this.tailMap(from.getKey()).object2LongEntrySet();
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
        
        public boolean containsKey(final Object k) {
            return this.in(k) && Object2LongRBTreeMap.this.containsKey(k);
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
        
        public long getLong(final Object k) {
            final K kk = (K)k;
            final Object2LongRBTreeMap.Entry<K> e;
            return (this.in(kk) && (e = Object2LongRBTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue;
        }
        
        public long put(final K k, final long v) {
            Object2LongRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append(k).append(") out of range [").append(this.bottom ? "-" : String.valueOf(this.from)).append(", ").append(this.top ? "-" : String.valueOf(this.to)).append(")").toString());
            }
            final long oldValue = Object2LongRBTreeMap.this.put(k, v);
            return Object2LongRBTreeMap.this.modified ? this.defRetValue : oldValue;
        }
        
        public long removeLong(final Object k) {
            Object2LongRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            final long oldValue = Object2LongRBTreeMap.this.removeLong(k);
            return Object2LongRBTreeMap.this.modified ? oldValue : this.defRetValue;
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
            return Object2LongRBTreeMap.this.actualComparator;
        }
        
        public Object2LongSortedMap<K> headMap(final K to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Object2LongRBTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Object2LongSortedMap<K> tailMap(final K from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Object2LongRBTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Object2LongSortedMap<K> subMap(K from, K to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Object2LongRBTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Object2LongRBTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Object2LongRBTreeMap.Entry<K> firstEntry() {
            if (Object2LongRBTreeMap.this.tree == null) {
                return null;
            }
            Object2LongRBTreeMap.Entry<K> e;
            if (this.bottom) {
                e = Object2LongRBTreeMap.this.firstEntry;
            }
            else {
                e = Object2LongRBTreeMap.this.locateKey(this.from);
                if (Object2LongRBTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Object2LongRBTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Object2LongRBTreeMap.Entry<K> lastEntry() {
            if (Object2LongRBTreeMap.this.tree == null) {
                return null;
            }
            Object2LongRBTreeMap.Entry<K> e;
            if (this.top) {
                e = Object2LongRBTreeMap.this.lastEntry;
            }
            else {
                e = Object2LongRBTreeMap.this.locateKey(this.to);
                if (Object2LongRBTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Object2LongRBTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public K firstKey() {
            final Object2LongRBTreeMap.Entry<K> e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public K lastKey() {
            final Object2LongRBTreeMap.Entry<K> e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractObject2LongSortedMap.KeySet {
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
                            final Object2LongRBTreeMap this$0 = submap.this$0;
                            final Object2LongRBTreeMap.Entry<K> lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Object2LongRBTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Object2LongRBTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Object2LongMap.Entry<K>> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final K k) {
                super(k);
            }
            
            public Object2LongMap.Entry<K> next() {
                return this.nextEntry();
            }
            
            public Object2LongMap.Entry<K> previous() {
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

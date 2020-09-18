package it.unimi.dsi.fastutil.longs;

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

public class Long2ReferenceAVLTreeMap<V> extends AbstractLong2ReferenceSortedMap<V> implements Serializable, Cloneable {
    protected transient Entry<V> tree;
    protected int count;
    protected transient Entry<V> firstEntry;
    protected transient Entry<V> lastEntry;
    protected transient ObjectSortedSet<Long2ReferenceMap.Entry<V>> entries;
    protected transient LongSortedSet keys;
    protected transient ReferenceCollection<V> values;
    protected transient boolean modified;
    protected Comparator<? super Long> storedComparator;
    protected transient LongComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    
    public Long2ReferenceAVLTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = LongComparators.asLongComparator(this.storedComparator);
    }
    
    public Long2ReferenceAVLTreeMap(final Comparator<? super Long> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public Long2ReferenceAVLTreeMap(final Map<? extends Long, ? extends V> m) {
        this();
        this.putAll(m);
    }
    
    public Long2ReferenceAVLTreeMap(final SortedMap<Long, V> m) {
        this(m.comparator());
        this.putAll((java.util.Map<? extends Long, ? extends V>)m);
    }
    
    public Long2ReferenceAVLTreeMap(final Long2ReferenceMap<? extends V> m) {
        this();
        this.putAll((java.util.Map<? extends Long, ? extends V>)m);
    }
    
    public Long2ReferenceAVLTreeMap(final Long2ReferenceSortedMap<V> m) {
        this((Comparator)m.comparator());
        this.putAll((java.util.Map<? extends Long, ? extends V>)m);
    }
    
    public Long2ReferenceAVLTreeMap(final long[] k, final V[] v, final Comparator<? super Long> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The key array and the value array have different lengths (").append(k.length).append(" and ").append(v.length).append(")").toString());
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }
    
    public Long2ReferenceAVLTreeMap(final long[] k, final V[] v) {
        this(k, v, null);
    }
    
    final int compare(final long k1, final long k2) {
        return (this.actualComparator == null) ? Long.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    final Entry<V> findKey(final long k) {
        Entry<V> e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry<V> locateKey(final long k) {
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
    
    public V put(final long k, final V v) {
        final Entry<V> e = this.add(k);
        final V oldValue = e.value;
        e.value = v;
        return oldValue;
    }
    
    private Entry<V> add(final long k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iconst_0       
        //     2: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.modified:Z
        //     5: aconst_null    
        //     6: astore_3        /* e */
        //     7: aload_0         /* this */
        //     8: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //    11: ifnonnull       60
        //    14: aload_0         /* this */
        //    15: dup            
        //    16: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.count:I
        //    19: iconst_1       
        //    20: iadd           
        //    21: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.count:I
        //    24: aload_0         /* this */
        //    25: aload_0         /* this */
        //    26: aload_0         /* this */
        //    27: new             Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //    30: dup            
        //    31: lload_1         /* k */
        //    32: aload_0         /* this */
        //    33: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.defRetValue:Ljava/lang/Object;
        //    36: invokespecial   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.<init>:(JLjava/lang/Object;)V
        //    39: dup_x1         
        //    40: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.firstEntry:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //    43: dup_x1         
        //    44: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.lastEntry:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //    47: dup_x1         
        //    48: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //    51: astore_3        /* e */
        //    52: aload_0         /* this */
        //    53: iconst_1       
        //    54: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.modified:Z
        //    57: goto            949
        //    60: aload_0         /* this */
        //    61: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //    64: astore          p
        //    66: aconst_null    
        //    67: astore          q
        //    69: aload_0         /* this */
        //    70: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //    73: astore          y
        //    75: aconst_null    
        //    76: astore          z
        //    78: aconst_null    
        //    79: astore          w
        //    81: iconst_0       
        //    82: istore          i
        //    84: aload_0         /* this */
        //    85: lload_1         /* k */
        //    86: aload           p
        //    88: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.key:J
        //    91: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.compare:(JJ)I
        //    94: dup            
        //    95: istore          cmp
        //    97: ifne            103
        //   100: aload           p
        //   102: areturn        
        //   103: aload           p
        //   105: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   108: ifeq            122
        //   111: iconst_0       
        //   112: istore          i
        //   114: aload           q
        //   116: astore          z
        //   118: aload           p
        //   120: astore          y
        //   122: aload_0         /* this */
        //   123: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.dirPath:[Z
        //   126: iload           i
        //   128: iinc            i, 1
        //   131: iload           cmp
        //   133: ifle            140
        //   136: iconst_1       
        //   137: goto            141
        //   140: iconst_0       
        //   141: dup_x2         
        //   142: bastore        
        //   143: ifeq            233
        //   146: aload           p
        //   148: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.succ:()Z
        //   151: ifeq            219
        //   154: aload_0         /* this */
        //   155: dup            
        //   156: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.count:I
        //   159: iconst_1       
        //   160: iadd           
        //   161: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.count:I
        //   164: new             Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   167: dup            
        //   168: lload_1         /* k */
        //   169: aload_0         /* this */
        //   170: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.defRetValue:Ljava/lang/Object;
        //   173: invokespecial   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.<init>:(JLjava/lang/Object;)V
        //   176: astore_3        /* e */
        //   177: aload_0         /* this */
        //   178: iconst_1       
        //   179: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.modified:Z
        //   182: aload           p
        //   184: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   187: ifnonnull       195
        //   190: aload_0         /* this */
        //   191: aload_3         /* e */
        //   192: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.lastEntry:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   195: aload_3         /* e */
        //   196: aload           p
        //   198: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   201: aload_3         /* e */
        //   202: aload           p
        //   204: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   207: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   210: aload           p
        //   212: aload_3         /* e */
        //   213: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:(Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;)V
        //   216: goto            320
        //   219: aload           p
        //   221: astore          q
        //   223: aload           p
        //   225: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   228: astore          p
        //   230: goto            84
        //   233: aload           p
        //   235: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.pred:()Z
        //   238: ifeq            306
        //   241: aload_0         /* this */
        //   242: dup            
        //   243: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.count:I
        //   246: iconst_1       
        //   247: iadd           
        //   248: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.count:I
        //   251: new             Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   254: dup            
        //   255: lload_1         /* k */
        //   256: aload_0         /* this */
        //   257: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.defRetValue:Ljava/lang/Object;
        //   260: invokespecial   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.<init>:(JLjava/lang/Object;)V
        //   263: astore_3        /* e */
        //   264: aload_0         /* this */
        //   265: iconst_1       
        //   266: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.modified:Z
        //   269: aload           p
        //   271: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   274: ifnonnull       282
        //   277: aload_0         /* this */
        //   278: aload_3         /* e */
        //   279: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.firstEntry:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   282: aload_3         /* e */
        //   283: aload           p
        //   285: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   288: aload_3         /* e */
        //   289: aload           p
        //   291: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   294: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   297: aload           p
        //   299: aload_3         /* e */
        //   300: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:(Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;)V
        //   303: goto            320
        //   306: aload           p
        //   308: astore          q
        //   310: aload           p
        //   312: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   315: astore          p
        //   317: goto            84
        //   320: aload           y
        //   322: astore          p
        //   324: iconst_0       
        //   325: istore          i
        //   327: aload           p
        //   329: aload_3         /* e */
        //   330: if_acmpeq       387
        //   333: aload_0         /* this */
        //   334: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.dirPath:[Z
        //   337: iload           i
        //   339: baload         
        //   340: ifeq            351
        //   343: aload           p
        //   345: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.incBalance:()V
        //   348: goto            356
        //   351: aload           p
        //   353: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.decBalance:()V
        //   356: aload_0         /* this */
        //   357: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.dirPath:[Z
        //   360: iload           i
        //   362: iinc            i, 1
        //   365: baload         
        //   366: ifeq            377
        //   369: aload           p
        //   371: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   374: goto            382
        //   377: aload           p
        //   379: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   382: astore          p
        //   384: goto            327
        //   387: aload           y
        //   389: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   392: bipush          -2
        //   394: if_icmpne       647
        //   397: aload           y
        //   399: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   402: astore          x
        //   404: aload           x
        //   406: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   409: iconst_m1      
        //   410: if_icmpne       473
        //   413: aload           x
        //   415: astore          w
        //   417: aload           x
        //   419: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.succ:()Z
        //   422: ifeq            441
        //   425: aload           x
        //   427: iconst_0       
        //   428: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.succ:(Z)V
        //   431: aload           y
        //   433: aload           x
        //   435: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;)V
        //   438: goto            451
        //   441: aload           y
        //   443: aload           x
        //   445: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   448: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   451: aload           x
        //   453: aload           y
        //   455: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   458: aload           x
        //   460: iconst_0       
        //   461: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   464: aload           y
        //   466: iconst_0       
        //   467: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   470: goto            644
        //   473: getstatic       it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.$assertionsDisabled:Z
        //   476: ifne            496
        //   479: aload           x
        //   481: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   484: iconst_1       
        //   485: if_icmpeq       496
        //   488: new             Ljava/lang/AssertionError;
        //   491: dup            
        //   492: invokespecial   java/lang/AssertionError.<init>:()V
        //   495: athrow         
        //   496: aload           x
        //   498: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   501: astore          w
        //   503: aload           x
        //   505: aload           w
        //   507: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   510: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   513: aload           w
        //   515: aload           x
        //   517: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   520: aload           y
        //   522: aload           w
        //   524: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   527: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   530: aload           w
        //   532: aload           y
        //   534: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   537: aload           w
        //   539: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   542: iconst_m1      
        //   543: if_icmpne       561
        //   546: aload           x
        //   548: iconst_0       
        //   549: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   552: aload           y
        //   554: iconst_1       
        //   555: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   558: goto            596
        //   561: aload           w
        //   563: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   566: ifne            584
        //   569: aload           x
        //   571: iconst_0       
        //   572: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   575: aload           y
        //   577: iconst_0       
        //   578: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   581: goto            596
        //   584: aload           x
        //   586: iconst_m1      
        //   587: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   590: aload           y
        //   592: iconst_0       
        //   593: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   596: aload           w
        //   598: iconst_0       
        //   599: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   602: aload           w
        //   604: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.pred:()Z
        //   607: ifeq            623
        //   610: aload           x
        //   612: aload           w
        //   614: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;)V
        //   617: aload           w
        //   619: iconst_0       
        //   620: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.pred:(Z)V
        //   623: aload           w
        //   625: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.succ:()Z
        //   628: ifeq            644
        //   631: aload           y
        //   633: aload           w
        //   635: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;)V
        //   638: aload           w
        //   640: iconst_0       
        //   641: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.succ:(Z)V
        //   644: goto            908
        //   647: aload           y
        //   649: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   652: iconst_2       
        //   653: if_icmpne       906
        //   656: aload           y
        //   658: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   661: astore          x
        //   663: aload           x
        //   665: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   668: iconst_1       
        //   669: if_icmpne       732
        //   672: aload           x
        //   674: astore          w
        //   676: aload           x
        //   678: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.pred:()Z
        //   681: ifeq            700
        //   684: aload           x
        //   686: iconst_0       
        //   687: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.pred:(Z)V
        //   690: aload           y
        //   692: aload           x
        //   694: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;)V
        //   697: goto            710
        //   700: aload           y
        //   702: aload           x
        //   704: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   707: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   710: aload           x
        //   712: aload           y
        //   714: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   717: aload           x
        //   719: iconst_0       
        //   720: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   723: aload           y
        //   725: iconst_0       
        //   726: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   729: goto            903
        //   732: getstatic       it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.$assertionsDisabled:Z
        //   735: ifne            755
        //   738: aload           x
        //   740: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   743: iconst_m1      
        //   744: if_icmpeq       755
        //   747: new             Ljava/lang/AssertionError;
        //   750: dup            
        //   751: invokespecial   java/lang/AssertionError.<init>:()V
        //   754: athrow         
        //   755: aload           x
        //   757: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   760: astore          w
        //   762: aload           x
        //   764: aload           w
        //   766: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   769: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   772: aload           w
        //   774: aload           x
        //   776: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   779: aload           y
        //   781: aload           w
        //   783: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   786: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   789: aload           w
        //   791: aload           y
        //   793: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   796: aload           w
        //   798: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   801: iconst_1       
        //   802: if_icmpne       820
        //   805: aload           x
        //   807: iconst_0       
        //   808: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   811: aload           y
        //   813: iconst_m1      
        //   814: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   817: goto            855
        //   820: aload           w
        //   822: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:()I
        //   825: ifne            843
        //   828: aload           x
        //   830: iconst_0       
        //   831: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   834: aload           y
        //   836: iconst_0       
        //   837: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   840: goto            855
        //   843: aload           x
        //   845: iconst_1       
        //   846: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   849: aload           y
        //   851: iconst_0       
        //   852: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   855: aload           w
        //   857: iconst_0       
        //   858: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.balance:(I)V
        //   861: aload           w
        //   863: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.pred:()Z
        //   866: ifeq            882
        //   869: aload           y
        //   871: aload           w
        //   873: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.succ:(Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;)V
        //   876: aload           w
        //   878: iconst_0       
        //   879: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.pred:(Z)V
        //   882: aload           w
        //   884: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.succ:()Z
        //   887: ifeq            903
        //   890: aload           x
        //   892: aload           w
        //   894: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.pred:(Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;)V
        //   897: aload           w
        //   899: iconst_0       
        //   900: invokevirtual   it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.succ:(Z)V
        //   903: goto            908
        //   906: aload_3         /* e */
        //   907: areturn        
        //   908: aload           z
        //   910: ifnonnull       922
        //   913: aload_0         /* this */
        //   914: aload           w
        //   916: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap.tree:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   919: goto            949
        //   922: aload           z
        //   924: getfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   927: aload           y
        //   929: if_acmpne       942
        //   932: aload           z
        //   934: aload           w
        //   936: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.left:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   939: goto            949
        //   942: aload           z
        //   944: aload           w
        //   946: putfield        it/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry.right:Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry;
        //   949: aload_3         /* e */
        //   950: areturn        
        //    Signature:
        //  (J)Lit/unimi/dsi/fastutil/longs/Long2ReferenceAVLTreeMap$Entry<TV;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 2A FC 00 3C 07 00 1E FF 00 17 00 0A 07 00 02 04 07 00 1E 07 00 1E 07 00 1E 07 00 1E 07 00 1E 07 00 1E 00 01 00 00 FF 00 12 00 0A 07 00 02 04 07 00 1E 07 00 1E 07 00 1E 07 00 1E 07 00 1E 07 00 1E 01 01 00 00 12 FF 00 11 00 0A 07 00 02 04 07 00 1E 07 00 1E 07 00 1E 07 00 1E 07 00 1E 07 00 1E 01 01 00 02 07 00 EB 01 FF 00 00 00 0A 07 00 02 04 07 00 1E 07 00 1E 07 00 1E 07 00 1E 07 00 1E 07 00 1E 01 01 00 03 07 00 EB 01 01 35 17 0D 30 17 0D 06 17 04 14 44 07 00 1E 04 FC 00 35 07 00 1E 09 15 16 FB 00 40 16 0B 1A FA 00 14 02 FC 00 34 07 00 1E 09 15 16 FB 00 40 16 0B 1A FA 00 14 02 01 0D 13 FF 00 06 00 03 07 00 02 04 07 00 1E 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1047)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryArguments(TypeAnalysis.java:2796)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2195)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1531)
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
    
    public V remove(final long k) {
        this.modified = false;
        if (this.tree == null) {
            return this.defRetValue;
        }
        Entry<V> p = this.tree;
        Entry<V> q = null;
        boolean dir = false;
        final long kk = k;
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
            Entry<V> r = p.right;
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
                Entry<V> s;
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
            final Entry<V> y = q;
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
                final Entry<V> x = y.right;
                assert x != null;
                if (x.balance() == -1) {
                    assert x.balance() == -1;
                    final Entry<V> w = x.left;
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
                final Entry<V> x = y.left;
                assert x != null;
                if (x.balance() == 1) {
                    assert x.balance() == 1;
                    final Entry<V> w = x.right;
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
    
    public boolean containsKey(final long k) {
        return this.findKey(k) != null;
    }
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public V get(final long k) {
        final Entry<V> e = this.findKey(k);
        return (e == null) ? this.defRetValue : e.value;
    }
    
    public long firstLongKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public long lastLongKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    public ObjectSortedSet<Long2ReferenceMap.Entry<V>> long2ReferenceEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Long2ReferenceMap.Entry<V>>() {
                final Comparator<? super Long2ReferenceMap.Entry<V>> comparator = (x, y) -> Long2ReferenceAVLTreeMap.this.actualComparator.compare(x.getLongKey(), y.getLongKey());
                
                public Comparator<? super Long2ReferenceMap.Entry<V>> comparator() {
                    return this.comparator;
                }
                
                @Override
                public ObjectBidirectionalIterator<Long2ReferenceMap.Entry<V>> iterator() {
                    return new EntryIterator();
                }
                
                @Override
                public ObjectBidirectionalIterator<Long2ReferenceMap.Entry<V>> iterator(final Long2ReferenceMap.Entry<V> from) {
                    return new EntryIterator(from.getLongKey());
                }
                
                public boolean contains(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                        return false;
                    }
                    final Entry<V> f = Long2ReferenceAVLTreeMap.this.findKey((long)e.getKey());
                    return e.equals(f);
                }
                
                public boolean remove(final Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    final Map.Entry<?, ?> e = o;
                    if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                        return false;
                    }
                    final Entry<V> f = Long2ReferenceAVLTreeMap.this.findKey((long)e.getKey());
                    if (f == null || f.getValue() != e.getValue()) {
                        return false;
                    }
                    Long2ReferenceAVLTreeMap.this.remove(f.key);
                    return true;
                }
                
                public int size() {
                    return Long2ReferenceAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Long2ReferenceAVLTreeMap.this.clear();
                }
                
                public Long2ReferenceMap.Entry<V> first() {
                    return Long2ReferenceAVLTreeMap.this.firstEntry;
                }
                
                public Long2ReferenceMap.Entry<V> last() {
                    return Long2ReferenceAVLTreeMap.this.lastEntry;
                }
                
                @Override
                public ObjectSortedSet<Long2ReferenceMap.Entry<V>> subSet(final Long2ReferenceMap.Entry<V> from, final Long2ReferenceMap.Entry<V> to) {
                    return Long2ReferenceAVLTreeMap.this.subMap(from.getLongKey(), to.getLongKey()).long2ReferenceEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Long2ReferenceMap.Entry<V>> headSet(final Long2ReferenceMap.Entry<V> to) {
                    return Long2ReferenceAVLTreeMap.this.headMap(to.getLongKey()).long2ReferenceEntrySet();
                }
                
                @Override
                public ObjectSortedSet<Long2ReferenceMap.Entry<V>> tailSet(final Long2ReferenceMap.Entry<V> from) {
                    return Long2ReferenceAVLTreeMap.this.tailMap(from.getLongKey()).long2ReferenceEntrySet();
                }
            };
        }
        return this.entries;
    }
    
    @Override
    public LongSortedSet keySet() {
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
                    return Long2ReferenceAVLTreeMap.this.containsValue(k);
                }
                
                public int size() {
                    return Long2ReferenceAVLTreeMap.this.count;
                }
                
                public void clear() {
                    Long2ReferenceAVLTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }
    
    public LongComparator comparator() {
        return this.actualComparator;
    }
    
    public Long2ReferenceSortedMap<V> headMap(final long to) {
        return new Submap(0L, true, to, false);
    }
    
    public Long2ReferenceSortedMap<V> tailMap(final long from) {
        return new Submap(from, false, 0L, true);
    }
    
    public Long2ReferenceSortedMap<V> subMap(final long from, final long to) {
        return new Submap(from, false, to, false);
    }
    
    public Long2ReferenceAVLTreeMap<V> clone() {
        Long2ReferenceAVLTreeMap<V> c;
        try {
            c = (Long2ReferenceAVLTreeMap)super.clone();
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
            s.writeLong(e.key);
            s.writeObject(e.value);
        }
    }
    
    private Entry<V> readTree(final ObjectInputStream s, final int n, final Entry<V> pred, final Entry<V> succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry<V> top = new Entry<V>(s.readLong(), (V)s.readObject());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            final Entry<V> top = new Entry<V>(s.readLong(), (V)s.readObject());
            top.right(new Entry<V>(s.readLong(), (V)s.readObject()));
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
        top2.key = s.readLong();
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
            super(0L, null);
        }
        
        Entry(final long k, final V v) {
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
            final Map.Entry<Long, V> e = (Map.Entry<Long, V>)o;
            return this.key == (long)e.getKey() && this.value == e.getValue();
        }
        
        @Override
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
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
            this.next = Long2ReferenceAVLTreeMap.this.firstEntry;
        }
        
        TreeIterator(final long k) {
            this.index = 0;
            final Entry<V> locateKey = Long2ReferenceAVLTreeMap.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (Long2ReferenceAVLTreeMap.this.compare(this.next.key, k) <= 0) {
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
            Long2ReferenceAVLTreeMap.this.remove(this.curr.key);
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
    
    private class EntryIterator extends TreeIterator implements ObjectListIterator<Long2ReferenceMap.Entry<V>> {
        EntryIterator() {
        }
        
        EntryIterator(final long k) {
            super(k);
        }
        
        public Long2ReferenceMap.Entry<V> next() {
            return this.nextEntry();
        }
        
        public Long2ReferenceMap.Entry<V> previous() {
            return this.previousEntry();
        }
        
        @Override
        public void set(final Long2ReferenceMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final Long2ReferenceMap.Entry<V> ok) {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class KeyIterator extends TreeIterator implements LongListIterator {
        public KeyIterator() {
        }
        
        public KeyIterator(final long k) {
            super(k);
        }
        
        public long nextLong() {
            return this.nextEntry().key;
        }
        
        public long previousLong() {
            return this.previousEntry().key;
        }
    }
    
    private class KeySet extends AbstractLong2ReferenceSortedMap.KeySet {
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeyIterator();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
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
    
    private final class Submap extends AbstractLong2ReferenceSortedMap<V> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        long from;
        long to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Long2ReferenceMap.Entry<V>> entries;
        protected transient LongSortedSet keys;
        protected transient ReferenceCollection<V> values;
        final /* synthetic */ Long2ReferenceAVLTreeMap this$0;
        
        public Submap(final long from, final boolean bottom, final long to, final boolean top) {
            if (!bottom && !top && Long2ReferenceAVLTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start key (").append(from).append(") is larger than end key (").append(to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Long2ReferenceAVLTreeMap.this.defRetValue;
        }
        
        public void clear() {
            final SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }
        
        final boolean in(final long k) {
            return (this.bottom || Long2ReferenceAVLTreeMap.this.compare(k, this.from) >= 0) && (this.top || Long2ReferenceAVLTreeMap.this.compare(k, this.to) < 0);
        }
        
        public ObjectSortedSet<Long2ReferenceMap.Entry<V>> long2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Long2ReferenceMap.Entry<V>>() {
                    @Override
                    public ObjectBidirectionalIterator<Long2ReferenceMap.Entry<V>> iterator() {
                        return new SubmapEntryIterator();
                    }
                    
                    @Override
                    public ObjectBidirectionalIterator<Long2ReferenceMap.Entry<V>> iterator(final Long2ReferenceMap.Entry<V> from) {
                        return new SubmapEntryIterator(from.getLongKey());
                    }
                    
                    public Comparator<? super Long2ReferenceMap.Entry<V>> comparator() {
                        return Long2ReferenceAVLTreeMap.this.long2ReferenceEntrySet().comparator();
                    }
                    
                    public boolean contains(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                            return false;
                        }
                        final Long2ReferenceAVLTreeMap.Entry<V> f = Long2ReferenceAVLTreeMap.this.findKey((long)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }
                    
                    public boolean remove(final Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        final Map.Entry<?, ?> e = o;
                        if (e.getKey() == null || !(e.getKey() instanceof Long)) {
                            return false;
                        }
                        final Long2ReferenceAVLTreeMap.Entry<V> f = Long2ReferenceAVLTreeMap.this.findKey((long)e.getKey());
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
                    
                    public Long2ReferenceMap.Entry<V> first() {
                        return Submap.this.firstEntry();
                    }
                    
                    public Long2ReferenceMap.Entry<V> last() {
                        return Submap.this.lastEntry();
                    }
                    
                    @Override
                    public ObjectSortedSet<Long2ReferenceMap.Entry<V>> subSet(final Long2ReferenceMap.Entry<V> from, final Long2ReferenceMap.Entry<V> to) {
                        return Submap.this.subMap(from.getLongKey(), to.getLongKey()).long2ReferenceEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Long2ReferenceMap.Entry<V>> headSet(final Long2ReferenceMap.Entry<V> to) {
                        return Submap.this.headMap(to.getLongKey()).long2ReferenceEntrySet();
                    }
                    
                    @Override
                    public ObjectSortedSet<Long2ReferenceMap.Entry<V>> tailSet(final Long2ReferenceMap.Entry<V> from) {
                        return Submap.this.tailMap(from.getLongKey()).long2ReferenceEntrySet();
                    }
                };
            }
            return this.entries;
        }
        
        @Override
        public LongSortedSet keySet() {
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
        
        public boolean containsKey(final long k) {
            return this.in(k) && Long2ReferenceAVLTreeMap.this.containsKey(k);
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
        
        public V get(final long k) {
            final long kk = k;
            final Long2ReferenceAVLTreeMap.Entry<V> e;
            return (V)((this.in(kk) && (e = Long2ReferenceAVLTreeMap.this.findKey(kk)) != null) ? e.value : this.defRetValue);
        }
        
        public V put(final long k, final V v) {
            Long2ReferenceAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Key (").append(k).append(") out of range [").append(this.bottom ? "-" : String.valueOf(this.from)).append(", ").append(this.top ? "-" : String.valueOf(this.to)).append(")").toString());
            }
            final V oldValue = Long2ReferenceAVLTreeMap.this.put(k, v);
            return (V)(Long2ReferenceAVLTreeMap.this.modified ? this.defRetValue : oldValue);
        }
        
        public V remove(final long k) {
            Long2ReferenceAVLTreeMap.this.modified = false;
            if (!this.in(k)) {
                return (V)this.defRetValue;
            }
            final V oldValue = Long2ReferenceAVLTreeMap.this.remove(k);
            return (V)(Long2ReferenceAVLTreeMap.this.modified ? oldValue : this.defRetValue);
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
        
        public LongComparator comparator() {
            return Long2ReferenceAVLTreeMap.this.actualComparator;
        }
        
        public Long2ReferenceSortedMap<V> headMap(final long to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return (Long2ReferenceAVLTreeMap.this.compare(to, this.to) < 0) ? new Submap(this.from, this.bottom, to, false) : this;
        }
        
        public Long2ReferenceSortedMap<V> tailMap(final long from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return (Long2ReferenceAVLTreeMap.this.compare(from, this.from) > 0) ? new Submap(from, false, this.to, this.top) : this;
        }
        
        public Long2ReferenceSortedMap<V> subMap(long from, long to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                to = ((Long2ReferenceAVLTreeMap.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((Long2ReferenceAVLTreeMap.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }
        
        public Long2ReferenceAVLTreeMap.Entry<V> firstEntry() {
            if (Long2ReferenceAVLTreeMap.this.tree == null) {
                return null;
            }
            Long2ReferenceAVLTreeMap.Entry<V> e;
            if (this.bottom) {
                e = Long2ReferenceAVLTreeMap.this.firstEntry;
            }
            else {
                e = Long2ReferenceAVLTreeMap.this.locateKey(this.from);
                if (Long2ReferenceAVLTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && Long2ReferenceAVLTreeMap.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Long2ReferenceAVLTreeMap.Entry<V> lastEntry() {
            if (Long2ReferenceAVLTreeMap.this.tree == null) {
                return null;
            }
            Long2ReferenceAVLTreeMap.Entry<V> e;
            if (this.top) {
                e = Long2ReferenceAVLTreeMap.this.lastEntry;
            }
            else {
                e = Long2ReferenceAVLTreeMap.this.locateKey(this.to);
                if (Long2ReferenceAVLTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && Long2ReferenceAVLTreeMap.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public long firstLongKey() {
            final Long2ReferenceAVLTreeMap.Entry<V> e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public long lastLongKey() {
            final Long2ReferenceAVLTreeMap.Entry<V> e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private class KeySet extends AbstractLong2ReferenceSortedMap.KeySet {
            @Override
            public LongBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }
            
            @Override
            public LongBidirectionalIterator iterator(final long from) {
                return new SubmapKeyIterator(from);
            }
        }
        
        private class SubmapIterator extends TreeIterator {
            SubmapIterator() {
                Submap.this.this$0.super();
                this.next = Submap.this.firstEntry();
            }
            
            SubmapIterator(final Submap submap, final long k) {
                this(submap);
                if (this.next != null) {
                    if (!submap.bottom && submap.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!submap.top) {
                            final Long2ReferenceAVLTreeMap this$0 = submap.this$0;
                            final Long2ReferenceAVLTreeMap.Entry<V> lastEntry = submap.lastEntry();
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
                if (!Submap.this.bottom && this.prev != null && Long2ReferenceAVLTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Long2ReferenceAVLTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
        
        private class SubmapEntryIterator extends SubmapIterator implements ObjectListIterator<Long2ReferenceMap.Entry<V>> {
            SubmapEntryIterator() {
            }
            
            SubmapEntryIterator(final long k) {
                super(k);
            }
            
            public Long2ReferenceMap.Entry<V> next() {
                return this.nextEntry();
            }
            
            public Long2ReferenceMap.Entry<V> previous() {
                return this.previousEntry();
            }
        }
        
        private final class SubmapKeyIterator extends SubmapIterator implements LongListIterator {
            public SubmapKeyIterator() {
            }
            
            public SubmapKeyIterator(final long from) {
                super(from);
            }
            
            public long nextLong() {
                return this.nextEntry().key;
            }
            
            public long previousLong() {
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

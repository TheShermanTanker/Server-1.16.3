package it.unimi.dsi.fastutil.ints;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Collection;
import java.util.Comparator;
import java.io.Serializable;

public class IntAVLTreeSet extends AbstractIntSortedSet implements Serializable, Cloneable, IntSortedSet {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected Comparator<? super Integer> storedComparator;
    protected transient IntComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353130L;
    private transient boolean[] dirPath;
    
    public IntAVLTreeSet() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }
    
    private void setActualComparator() {
        this.actualComparator = IntComparators.asIntComparator(this.storedComparator);
    }
    
    public IntAVLTreeSet(final Comparator<? super Integer> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }
    
    public IntAVLTreeSet(final Collection<? extends Integer> c) {
        this();
        this.addAll((Collection)c);
    }
    
    public IntAVLTreeSet(final SortedSet<Integer> s) {
        this(s.comparator());
        this.addAll((Collection)s);
    }
    
    public IntAVLTreeSet(final IntCollection c) {
        this();
        this.addAll(c);
    }
    
    public IntAVLTreeSet(final IntSortedSet s) {
        this(s.comparator());
        this.addAll(s);
    }
    
    public IntAVLTreeSet(final IntIterator i) {
        this.allocatePaths();
        while (i.hasNext()) {
            this.add(i.nextInt());
        }
    }
    
    public IntAVLTreeSet(final Iterator<?> i) {
        this(IntIterators.asIntIterator(i));
    }
    
    public IntAVLTreeSet(final int[] a, final int offset, final int length, final Comparator<? super Integer> c) {
        this(c);
        IntArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }
    
    public IntAVLTreeSet(final int[] a, final int offset, final int length) {
        this(a, offset, length, null);
    }
    
    public IntAVLTreeSet(final int[] a) {
        this();
        int i = a.length;
        while (i-- != 0) {
            this.add(a[i]);
        }
    }
    
    public IntAVLTreeSet(final int[] a, final Comparator<? super Integer> c) {
        this(c);
        int i = a.length;
        while (i-- != 0) {
            this.add(a[i]);
        }
    }
    
    final int compare(final int k1, final int k2) {
        return (this.actualComparator == null) ? Integer.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }
    
    private Entry findKey(final int k) {
        Entry e;
        int cmp;
        for (e = this.tree; e != null && (cmp = this.compare(k, e.key)) != 0; e = ((cmp < 0) ? e.left() : e.right())) {}
        return e;
    }
    
    final Entry locateKey(final int k) {
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
    
    public boolean add(final int k) {
        if (this.tree != null) {
            Entry p = this.tree;
            Entry q = null;
            Entry y = this.tree;
            Entry z = null;
            Entry e = null;
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
                    e = new Entry(k);
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
                    e = new Entry(k);
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
                        return true;
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
                    return true;
                }
                if (z.left == y) {
                    z.left = w;
                    return true;
                }
                z.right = w;
                return true;
            }
            return false;
        }
        ++this.count;
        final Entry tree = new Entry(k);
        this.firstEntry = tree;
        this.lastEntry = tree;
        this.tree = tree;
        return true;
    }
    
    private Entry parent(final Entry e) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_0         /* this */
        //     2: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //     5: if_acmpne       10
        //     8: aconst_null    
        //     9: areturn        
        //    10: aload_1         /* e */
        //    11: dup            
        //    12: astore_3        /* y */
        //    13: astore_2        /* x */
        //    14: aload_3         /* y */
        //    15: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:()Z
        //    18: ifeq            65
        //    21: aload_3         /* y */
        //    22: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    25: astore          p
        //    27: aload           p
        //    29: ifnull          41
        //    32: aload           p
        //    34: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    37: aload_1         /* e */
        //    38: if_acmpeq       62
        //    41: aload_2         /* x */
        //    42: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //    45: ifne            56
        //    48: aload_2         /* x */
        //    49: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    52: astore_2        /* x */
        //    53: goto            41
        //    56: aload_2         /* x */
        //    57: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    60: astore          p
        //    62: aload           p
        //    64: areturn        
        //    65: aload_2         /* x */
        //    66: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //    69: ifeq            116
        //    72: aload_2         /* x */
        //    73: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    76: astore          p
        //    78: aload           p
        //    80: ifnull          92
        //    83: aload           p
        //    85: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    88: aload_1         /* e */
        //    89: if_acmpeq       113
        //    92: aload_3         /* y */
        //    93: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:()Z
        //    96: ifne            107
        //    99: aload_3         /* y */
        //   100: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   103: astore_3        /* y */
        //   104: goto            92
        //   107: aload_3         /* y */
        //   108: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   111: astore          p
        //   113: aload           p
        //   115: areturn        
        //   116: aload_2         /* x */
        //   117: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   120: astore_2        /* x */
        //   121: aload_3         /* y */
        //   122: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   125: astore_3        /* y */
        //   126: goto            14
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  e     
        //    StackMapTable: 00 0A 0A FD 00 03 07 00 13 07 00 13 FC 00 1A 07 00 13 0E 05 FA 00 02 FC 00 1A 07 00 13 0E 05 FA 00 02
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2470)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:830)
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
    
    public boolean remove(final int k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //     4: ifnonnull       9
        //     7: iconst_0       
        //     8: ireturn        
        //     9: aload_0         /* this */
        //    10: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    13: astore_3        /* p */
        //    14: aconst_null    
        //    15: astore          q
        //    17: iconst_0       
        //    18: istore          dir
        //    20: iload_1         /* k */
        //    21: istore          kk
        //    23: aload_0         /* this */
        //    24: iload           kk
        //    26: aload_3         /* p */
        //    27: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.key:I
        //    30: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet.compare:(II)I
        //    33: dup            
        //    34: istore_2        /* cmp */
        //    35: ifne            41
        //    38: goto            84
        //    41: iload_2         /* cmp */
        //    42: ifle            49
        //    45: iconst_1       
        //    46: goto            50
        //    49: iconst_0       
        //    50: dup            
        //    51: istore          dir
        //    53: ifeq            70
        //    56: aload_3         /* p */
        //    57: astore          q
        //    59: aload_3         /* p */
        //    60: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:()Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    63: dup            
        //    64: astore_3        /* p */
        //    65: ifnonnull       23
        //    68: iconst_0       
        //    69: ireturn        
        //    70: aload_3         /* p */
        //    71: astore          q
        //    73: aload_3         /* p */
        //    74: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:()Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    77: dup            
        //    78: astore_3        /* p */
        //    79: ifnonnull       23
        //    82: iconst_0       
        //    83: ireturn        
        //    84: aload_3         /* p */
        //    85: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    88: ifnonnull       99
        //    91: aload_0         /* this */
        //    92: aload_3         /* p */
        //    93: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.next:()Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    96: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.firstEntry:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    99: aload_3         /* p */
        //   100: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   103: ifnonnull       114
        //   106: aload_0         /* this */
        //   107: aload_3         /* p */
        //   108: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.prev:()Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   111: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.lastEntry:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   114: aload_3         /* p */
        //   115: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:()Z
        //   118: ifeq            241
        //   121: aload_3         /* p */
        //   122: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //   125: ifeq            185
        //   128: aload           q
        //   130: ifnull          162
        //   133: iload           dir
        //   135: ifeq            150
        //   138: aload           q
        //   140: aload_3         /* p */
        //   141: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   144: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:(Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;)V
        //   147: goto            497
        //   150: aload           q
        //   152: aload_3         /* p */
        //   153: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   156: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:(Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;)V
        //   159: goto            497
        //   162: aload_0         /* this */
        //   163: iload           dir
        //   165: ifeq            175
        //   168: aload_3         /* p */
        //   169: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   172: goto            179
        //   175: aload_3         /* p */
        //   176: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   179: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   182: goto            497
        //   185: aload_3         /* p */
        //   186: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.prev:()Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   189: aload_3         /* p */
        //   190: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   193: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   196: aload           q
        //   198: ifnull          230
        //   201: iload           dir
        //   203: ifeq            218
        //   206: aload           q
        //   208: aload_3         /* p */
        //   209: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   212: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   215: goto            497
        //   218: aload           q
        //   220: aload_3         /* p */
        //   221: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   224: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   227: goto            497
        //   230: aload_0         /* this */
        //   231: aload_3         /* p */
        //   232: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   235: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   238: goto            497
        //   241: aload_3         /* p */
        //   242: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   245: astore          r
        //   247: aload           r
        //   249: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //   252: ifeq            346
        //   255: aload           r
        //   257: aload_3         /* p */
        //   258: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   261: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   264: aload           r
        //   266: aload_3         /* p */
        //   267: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //   270: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:(Z)V
        //   273: aload           r
        //   275: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //   278: ifne            291
        //   281: aload           r
        //   283: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.prev:()Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   286: aload           r
        //   288: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   291: aload           q
        //   293: ifnull          321
        //   296: iload           dir
        //   298: ifeq            311
        //   301: aload           q
        //   303: aload           r
        //   305: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   308: goto            327
        //   311: aload           q
        //   313: aload           r
        //   315: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   318: goto            327
        //   321: aload_0         /* this */
        //   322: aload           r
        //   324: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   327: aload           r
        //   329: aload_3         /* p */
        //   330: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   333: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   336: aload           r
        //   338: astore          q
        //   340: iconst_1       
        //   341: istore          dir
        //   343: goto            497
        //   346: aload           r
        //   348: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   351: astore          s
        //   353: aload           s
        //   355: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //   358: ifeq            364
        //   361: goto            371
        //   364: aload           s
        //   366: astore          r
        //   368: goto            346
        //   371: aload           s
        //   373: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:()Z
        //   376: ifeq            389
        //   379: aload           r
        //   381: aload           s
        //   383: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:(Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;)V
        //   386: goto            399
        //   389: aload           r
        //   391: aload           s
        //   393: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   396: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   399: aload           s
        //   401: aload_3         /* p */
        //   402: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   405: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   408: aload_3         /* p */
        //   409: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //   412: ifne            430
        //   415: aload_3         /* p */
        //   416: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.prev:()Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   419: aload           s
        //   421: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   424: aload           s
        //   426: iconst_0       
        //   427: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:(Z)V
        //   430: aload           s
        //   432: aload_3         /* p */
        //   433: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   436: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   439: aload           s
        //   441: iconst_0       
        //   442: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:(Z)V
        //   445: aload           q
        //   447: ifnull          475
        //   450: iload           dir
        //   452: ifeq            465
        //   455: aload           q
        //   457: aload           s
        //   459: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   462: goto            481
        //   465: aload           q
        //   467: aload           s
        //   469: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   472: goto            481
        //   475: aload_0         /* this */
        //   476: aload           s
        //   478: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   481: aload           s
        //   483: aload_3         /* p */
        //   484: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   487: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   490: aload           r
        //   492: astore          q
        //   494: iconst_0       
        //   495: istore          dir
        //   497: aload           q
        //   499: ifnull          1460
        //   502: aload           q
        //   504: astore          y
        //   506: aload_0         /* this */
        //   507: aload           y
        //   509: invokespecial   it/unimi/dsi/fastutil/ints/IntAVLTreeSet.parent:(Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;)Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   512: astore          q
        //   514: iload           dir
        //   516: ifne            989
        //   519: aload           q
        //   521: ifnull          538
        //   524: aload           q
        //   526: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   529: aload           y
        //   531: if_acmpeq       538
        //   534: iconst_1       
        //   535: goto            539
        //   538: iconst_0       
        //   539: istore          dir
        //   541: aload           y
        //   543: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.incBalance:()V
        //   546: aload           y
        //   548: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   551: iconst_1       
        //   552: if_icmpne       558
        //   555: goto            1460
        //   558: aload           y
        //   560: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   563: iconst_2       
        //   564: if_icmpne       497
        //   567: aload           y
        //   569: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   572: astore          x
        //   574: getstatic       it/unimi/dsi/fastutil/ints/IntAVLTreeSet.$assertionsDisabled:Z
        //   577: ifne            593
        //   580: aload           x
        //   582: ifnonnull       593
        //   585: new             Ljava/lang/AssertionError;
        //   588: dup            
        //   589: invokespecial   java/lang/AssertionError.<init>:()V
        //   592: athrow         
        //   593: aload           x
        //   595: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   598: iconst_m1      
        //   599: if_icmpne       835
        //   602: getstatic       it/unimi/dsi/fastutil/ints/IntAVLTreeSet.$assertionsDisabled:Z
        //   605: ifne            625
        //   608: aload           x
        //   610: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   613: iconst_m1      
        //   614: if_icmpeq       625
        //   617: new             Ljava/lang/AssertionError;
        //   620: dup            
        //   621: invokespecial   java/lang/AssertionError.<init>:()V
        //   624: athrow         
        //   625: aload           x
        //   627: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   630: astore          w
        //   632: aload           x
        //   634: aload           w
        //   636: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   639: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   642: aload           w
        //   644: aload           x
        //   646: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   649: aload           y
        //   651: aload           w
        //   653: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   656: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   659: aload           w
        //   661: aload           y
        //   663: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   666: aload           w
        //   668: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   671: iconst_1       
        //   672: if_icmpne       690
        //   675: aload           x
        //   677: iconst_0       
        //   678: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   681: aload           y
        //   683: iconst_m1      
        //   684: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   687: goto            748
        //   690: aload           w
        //   692: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   695: ifne            713
        //   698: aload           x
        //   700: iconst_0       
        //   701: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   704: aload           y
        //   706: iconst_0       
        //   707: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   710: goto            748
        //   713: getstatic       it/unimi/dsi/fastutil/ints/IntAVLTreeSet.$assertionsDisabled:Z
        //   716: ifne            736
        //   719: aload           w
        //   721: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   724: iconst_m1      
        //   725: if_icmpeq       736
        //   728: new             Ljava/lang/AssertionError;
        //   731: dup            
        //   732: invokespecial   java/lang/AssertionError.<init>:()V
        //   735: athrow         
        //   736: aload           x
        //   738: iconst_1       
        //   739: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   742: aload           y
        //   744: iconst_0       
        //   745: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   748: aload           w
        //   750: iconst_0       
        //   751: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   754: aload           w
        //   756: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //   759: ifeq            775
        //   762: aload           y
        //   764: aload           w
        //   766: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:(Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;)V
        //   769: aload           w
        //   771: iconst_0       
        //   772: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:(Z)V
        //   775: aload           w
        //   777: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:()Z
        //   780: ifeq            796
        //   783: aload           x
        //   785: aload           w
        //   787: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:(Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;)V
        //   790: aload           w
        //   792: iconst_0       
        //   793: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:(Z)V
        //   796: aload           q
        //   798: ifnull          826
        //   801: iload           dir
        //   803: ifeq            816
        //   806: aload           q
        //   808: aload           w
        //   810: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   813: goto            832
        //   816: aload           q
        //   818: aload           w
        //   820: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   823: goto            832
        //   826: aload_0         /* this */
        //   827: aload           w
        //   829: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   832: goto            986
        //   835: aload           q
        //   837: ifnull          865
        //   840: iload           dir
        //   842: ifeq            855
        //   845: aload           q
        //   847: aload           x
        //   849: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   852: goto            871
        //   855: aload           q
        //   857: aload           x
        //   859: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   862: goto            871
        //   865: aload_0         /* this */
        //   866: aload           x
        //   868: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   871: aload           x
        //   873: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   876: ifne            911
        //   879: aload           y
        //   881: aload           x
        //   883: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   886: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   889: aload           x
        //   891: aload           y
        //   893: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   896: aload           x
        //   898: iconst_m1      
        //   899: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   902: aload           y
        //   904: iconst_1       
        //   905: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   908: goto            1460
        //   911: getstatic       it/unimi/dsi/fastutil/ints/IntAVLTreeSet.$assertionsDisabled:Z
        //   914: ifne            934
        //   917: aload           x
        //   919: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //   922: iconst_1       
        //   923: if_icmpeq       934
        //   926: new             Ljava/lang/AssertionError;
        //   929: dup            
        //   930: invokespecial   java/lang/AssertionError.<init>:()V
        //   933: athrow         
        //   934: aload           x
        //   936: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //   939: ifeq            957
        //   942: aload           y
        //   944: iconst_1       
        //   945: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:(Z)V
        //   948: aload           x
        //   950: iconst_0       
        //   951: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:(Z)V
        //   954: goto            967
        //   957: aload           y
        //   959: aload           x
        //   961: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   964: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   967: aload           x
        //   969: aload           y
        //   971: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   974: aload           y
        //   976: iconst_0       
        //   977: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   980: aload           x
        //   982: iconst_0       
        //   983: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //   986: goto            497
        //   989: aload           q
        //   991: ifnull          1008
        //   994: aload           q
        //   996: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //   999: aload           y
        //  1001: if_acmpeq       1008
        //  1004: iconst_1       
        //  1005: goto            1009
        //  1008: iconst_0       
        //  1009: istore          dir
        //  1011: aload           y
        //  1013: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.decBalance:()V
        //  1016: aload           y
        //  1018: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //  1021: iconst_m1      
        //  1022: if_icmpne       1028
        //  1025: goto            1460
        //  1028: aload           y
        //  1030: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //  1033: bipush          -2
        //  1035: if_icmpne       497
        //  1038: aload           y
        //  1040: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1043: astore          x
        //  1045: getstatic       it/unimi/dsi/fastutil/ints/IntAVLTreeSet.$assertionsDisabled:Z
        //  1048: ifne            1064
        //  1051: aload           x
        //  1053: ifnonnull       1064
        //  1056: new             Ljava/lang/AssertionError;
        //  1059: dup            
        //  1060: invokespecial   java/lang/AssertionError.<init>:()V
        //  1063: athrow         
        //  1064: aload           x
        //  1066: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //  1069: iconst_1       
        //  1070: if_icmpne       1306
        //  1073: getstatic       it/unimi/dsi/fastutil/ints/IntAVLTreeSet.$assertionsDisabled:Z
        //  1076: ifne            1096
        //  1079: aload           x
        //  1081: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //  1084: iconst_1       
        //  1085: if_icmpeq       1096
        //  1088: new             Ljava/lang/AssertionError;
        //  1091: dup            
        //  1092: invokespecial   java/lang/AssertionError.<init>:()V
        //  1095: athrow         
        //  1096: aload           x
        //  1098: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1101: astore          w
        //  1103: aload           x
        //  1105: aload           w
        //  1107: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1110: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1113: aload           w
        //  1115: aload           x
        //  1117: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1120: aload           y
        //  1122: aload           w
        //  1124: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1127: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1130: aload           w
        //  1132: aload           y
        //  1134: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1137: aload           w
        //  1139: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //  1142: iconst_m1      
        //  1143: if_icmpne       1161
        //  1146: aload           x
        //  1148: iconst_0       
        //  1149: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1152: aload           y
        //  1154: iconst_1       
        //  1155: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1158: goto            1219
        //  1161: aload           w
        //  1163: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //  1166: ifne            1184
        //  1169: aload           x
        //  1171: iconst_0       
        //  1172: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1175: aload           y
        //  1177: iconst_0       
        //  1178: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1181: goto            1219
        //  1184: getstatic       it/unimi/dsi/fastutil/ints/IntAVLTreeSet.$assertionsDisabled:Z
        //  1187: ifne            1207
        //  1190: aload           w
        //  1192: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //  1195: iconst_1       
        //  1196: if_icmpeq       1207
        //  1199: new             Ljava/lang/AssertionError;
        //  1202: dup            
        //  1203: invokespecial   java/lang/AssertionError.<init>:()V
        //  1206: athrow         
        //  1207: aload           x
        //  1209: iconst_m1      
        //  1210: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1213: aload           y
        //  1215: iconst_0       
        //  1216: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1219: aload           w
        //  1221: iconst_0       
        //  1222: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1225: aload           w
        //  1227: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:()Z
        //  1230: ifeq            1246
        //  1233: aload           x
        //  1235: aload           w
        //  1237: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:(Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;)V
        //  1240: aload           w
        //  1242: iconst_0       
        //  1243: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:(Z)V
        //  1246: aload           w
        //  1248: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:()Z
        //  1251: ifeq            1267
        //  1254: aload           y
        //  1256: aload           w
        //  1258: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:(Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;)V
        //  1261: aload           w
        //  1263: iconst_0       
        //  1264: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:(Z)V
        //  1267: aload           q
        //  1269: ifnull          1297
        //  1272: iload           dir
        //  1274: ifeq            1287
        //  1277: aload           q
        //  1279: aload           w
        //  1281: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1284: goto            1303
        //  1287: aload           q
        //  1289: aload           w
        //  1291: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1294: goto            1303
        //  1297: aload_0         /* this */
        //  1298: aload           w
        //  1300: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1303: goto            1457
        //  1306: aload           q
        //  1308: ifnull          1336
        //  1311: iload           dir
        //  1313: ifeq            1326
        //  1316: aload           q
        //  1318: aload           x
        //  1320: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1323: goto            1342
        //  1326: aload           q
        //  1328: aload           x
        //  1330: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1333: goto            1342
        //  1336: aload_0         /* this */
        //  1337: aload           x
        //  1339: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1342: aload           x
        //  1344: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //  1347: ifne            1382
        //  1350: aload           y
        //  1352: aload           x
        //  1354: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1357: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1360: aload           x
        //  1362: aload           y
        //  1364: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1367: aload           x
        //  1369: iconst_1       
        //  1370: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1373: aload           y
        //  1375: iconst_m1      
        //  1376: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1379: goto            1460
        //  1382: getstatic       it/unimi/dsi/fastutil/ints/IntAVLTreeSet.$assertionsDisabled:Z
        //  1385: ifne            1405
        //  1388: aload           x
        //  1390: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:()I
        //  1393: iconst_m1      
        //  1394: if_icmpeq       1405
        //  1397: new             Ljava/lang/AssertionError;
        //  1400: dup            
        //  1401: invokespecial   java/lang/AssertionError.<init>:()V
        //  1404: athrow         
        //  1405: aload           x
        //  1407: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:()Z
        //  1410: ifeq            1428
        //  1413: aload           y
        //  1415: iconst_1       
        //  1416: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.pred:(Z)V
        //  1419: aload           x
        //  1421: iconst_0       
        //  1422: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.succ:(Z)V
        //  1425: goto            1438
        //  1428: aload           y
        //  1430: aload           x
        //  1432: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1435: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.left:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1438: aload           x
        //  1440: aload           y
        //  1442: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.right:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //  1445: aload           y
        //  1447: iconst_0       
        //  1448: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1451: aload           x
        //  1453: iconst_0       
        //  1454: invokevirtual   it/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry.balance:(I)V
        //  1457: goto            497
        //  1460: aload_0         /* this */
        //  1461: dup            
        //  1462: getfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.count:I
        //  1465: iconst_1       
        //  1466: isub           
        //  1467: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.count:I
        //  1470: iconst_1       
        //  1471: ireturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 4F 09 FF 00 0D 00 07 07 00 02 01 00 07 00 13 07 00 13 01 01 00 00 FF 00 11 00 07 07 00 02 01 01 07 00 13 07 00 13 01 01 00 00 07 40 01 13 0D 0E 0E 23 0B 4C 07 00 02 FF 00 03 00 07 07 00 02 01 01 07 00 13 07 00 13 01 01 00 02 07 00 02 07 00 13 05 20 0B 0A FC 00 31 07 00 13 13 09 05 12 FC 00 11 07 00 13 06 11 09 1E 22 09 05 F9 00 0F FC 00 28 07 00 13 40 01 12 FC 00 22 07 00 13 1F FC 00 40 07 00 13 16 16 0B 1A 14 13 09 FA 00 05 02 13 09 05 27 16 16 09 FA 00 12 02 12 40 01 12 FC 00 23 07 00 13 1F FC 00 40 07 00 13 16 16 0B 1A 14 13 09 FA 00 05 02 13 09 05 27 16 16 09 FA 00 12 FA 00 02
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2470)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    public boolean contains(final int k) {
        return this.findKey(k) != null;
    }
    
    public void clear() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: iconst_0       
        //     2: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.count:I
        //     5: aload_0         /* this */
        //     6: aconst_null    
        //     7: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.tree:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    10: aload_0         /* this */
        //    11: aload_0         /* this */
        //    12: aconst_null    
        //    13: dup_x1         
        //    14: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.lastEntry:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    17: putfield        it/unimi/dsi/fastutil/ints/IntAVLTreeSet.firstEntry:Lit/unimi/dsi/fastutil/ints/IntAVLTreeSet$Entry;
        //    20: return         
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
    
    public int size() {
        return this.count;
    }
    
    public boolean isEmpty() {
        return this.count == 0;
    }
    
    public int firstInt() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }
    
    public int lastInt() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }
    
    @Override
    public IntBidirectionalIterator iterator() {
        return new SetIterator();
    }
    
    public IntBidirectionalIterator iterator(final int from) {
        return new SetIterator(from);
    }
    
    public IntComparator comparator() {
        return this.actualComparator;
    }
    
    public IntSortedSet headSet(final int to) {
        return new Subset(0, true, to, false);
    }
    
    public IntSortedSet tailSet(final int from) {
        return new Subset(from, false, 0, true);
    }
    
    public IntSortedSet subSet(final int from, final int to) {
        return new Subset(from, false, to, false);
    }
    
    public Object clone() {
        IntAVLTreeSet c;
        try {
            c = (IntAVLTreeSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
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
        final SetIterator i = new SetIterator();
        s.defaultWriteObject();
        while (n-- != 0) {
            s.writeInt(i.nextInt());
        }
    }
    
    private Entry readTree(final ObjectInputStream s, final int n, final Entry pred, final Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            final Entry top = new Entry(s.readInt());
            top.pred(pred);
            top.succ(succ);
            return top;
        }
        if (n == 2) {
            final Entry top = new Entry(s.readInt());
            top.right(new Entry(s.readInt()));
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
        top2.key = s.readInt();
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
    
    private static final class Entry implements Cloneable {
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 1073741824;
        private static final int BALANCE_MASK = 255;
        int key;
        Entry left;
        Entry right;
        int info;
        
        Entry() {
        }
        
        Entry(final int k) {
            this.key = k;
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
        
        public Entry clone() {
            Entry c;
            try {
                c = (Entry)super.clone();
            }
            catch (CloneNotSupportedException cantHappen) {
                throw new InternalError();
            }
            c.key = this.key;
            c.info = this.info;
            return c;
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            final Entry e = (Entry)o;
            return this.key == e.key;
        }
        
        public int hashCode() {
            return this.key;
        }
        
        public String toString() {
            return String.valueOf(this.key);
        }
    }
    
    private class SetIterator implements IntListIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index;
        
        SetIterator() {
            this.index = 0;
            this.next = IntAVLTreeSet.this.firstEntry;
        }
        
        SetIterator(final int k) {
            this.index = 0;
            final Entry locateKey = IntAVLTreeSet.this.locateKey(k);
            this.next = locateKey;
            if (locateKey != null) {
                if (IntAVLTreeSet.this.compare(this.next.key, k) <= 0) {
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
        
        public int nextInt() {
            return this.nextEntry().key;
        }
        
        public int previousInt() {
            return this.previousEntry().key;
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
            IntAVLTreeSet.this.remove(this.curr.key);
            this.curr = null;
        }
    }
    
    private final class Subset extends AbstractIntSortedSet implements Serializable, IntSortedSet {
        private static final long serialVersionUID = -7046029254386353129L;
        int from;
        int to;
        boolean bottom;
        boolean top;
        final /* synthetic */ IntAVLTreeSet this$0;
        
        public Subset(final int from, final boolean bottom, final int to, final boolean top) {
            if (!bottom && !top && IntAVLTreeSet.this.compare(from, to) > 0) {
                throw new IllegalArgumentException(new StringBuilder().append("Start element (").append(from).append(") is larger than end element (").append(to).append(")").toString());
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
        }
        
        public void clear() {
            final SubsetIterator i = new SubsetIterator();
            while (i.hasNext()) {
                i.nextInt();
                i.remove();
            }
        }
        
        final boolean in(final int k) {
            return (this.bottom || IntAVLTreeSet.this.compare(k, this.from) >= 0) && (this.top || IntAVLTreeSet.this.compare(k, this.to) < 0);
        }
        
        public boolean contains(final int k) {
            return this.in(k) && IntAVLTreeSet.this.contains(k);
        }
        
        public boolean add(final int k) {
            if (!this.in(k)) {
                throw new IllegalArgumentException(new StringBuilder().append("Element (").append(k).append(") out of range [").append(this.bottom ? "-" : String.valueOf(this.from)).append(", ").append(this.top ? "-" : String.valueOf(this.to)).append(")").toString());
            }
            return IntAVLTreeSet.this.add(k);
        }
        
        public boolean remove(final int k) {
            return this.in(k) && IntAVLTreeSet.this.remove(k);
        }
        
        public int size() {
            final SubsetIterator i = new SubsetIterator();
            int n = 0;
            while (i.hasNext()) {
                ++n;
                i.nextInt();
            }
            return n;
        }
        
        public boolean isEmpty() {
            return !new SubsetIterator().hasNext();
        }
        
        public IntComparator comparator() {
            return IntAVLTreeSet.this.actualComparator;
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new SubsetIterator();
        }
        
        public IntBidirectionalIterator iterator(final int from) {
            return new SubsetIterator(from);
        }
        
        public IntSortedSet headSet(final int to) {
            if (this.top) {
                return new Subset(this.from, this.bottom, to, false);
            }
            return (IntAVLTreeSet.this.compare(to, this.to) < 0) ? new Subset(this.from, this.bottom, to, false) : this;
        }
        
        public IntSortedSet tailSet(final int from) {
            if (this.bottom) {
                return new Subset(from, false, this.to, this.top);
            }
            return (IntAVLTreeSet.this.compare(from, this.from) > 0) ? new Subset(from, false, this.to, this.top) : this;
        }
        
        public IntSortedSet subSet(int from, int to) {
            if (this.top && this.bottom) {
                return new Subset(from, false, to, false);
            }
            if (!this.top) {
                to = ((IntAVLTreeSet.this.compare(to, this.to) < 0) ? to : this.to);
            }
            if (!this.bottom) {
                from = ((IntAVLTreeSet.this.compare(from, this.from) > 0) ? from : this.from);
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Subset(from, false, to, false);
        }
        
        public Entry firstEntry() {
            if (IntAVLTreeSet.this.tree == null) {
                return null;
            }
            Entry e;
            if (this.bottom) {
                e = IntAVLTreeSet.this.firstEntry;
            }
            else {
                e = IntAVLTreeSet.this.locateKey(this.from);
                if (IntAVLTreeSet.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || (!this.top && IntAVLTreeSet.this.compare(e.key, this.to) >= 0)) {
                return null;
            }
            return e;
        }
        
        public Entry lastEntry() {
            if (IntAVLTreeSet.this.tree == null) {
                return null;
            }
            Entry e;
            if (this.top) {
                e = IntAVLTreeSet.this.lastEntry;
            }
            else {
                e = IntAVLTreeSet.this.locateKey(this.to);
                if (IntAVLTreeSet.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || (!this.bottom && IntAVLTreeSet.this.compare(e.key, this.from) < 0)) {
                return null;
            }
            return e;
        }
        
        public int firstInt() {
            final Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        public int lastInt() {
            final Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }
        
        private final class SubsetIterator extends SetIterator {
            SubsetIterator() {
                Subset.this.this$0.super();
                this.next = Subset.this.firstEntry();
            }
            
            SubsetIterator(final Subset subset, final int k) {
                this(subset);
                if (this.next != null) {
                    if (!subset.bottom && subset.this$0.compare(k, this.next.key) < 0) {
                        this.prev = null;
                    }
                    else {
                        if (!subset.top) {
                            final IntAVLTreeSet this$0 = subset.this$0;
                            final Entry lastEntry = subset.lastEntry();
                            this.prev = lastEntry;
                            if (this$0.compare(k, lastEntry.key) >= 0) {
                                this.next = null;
                                return;
                            }
                        }
                        this.next = subset.this$0.locateKey(k);
                        if (subset.this$0.compare(this.next.key, k) <= 0) {
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
                if (!Subset.this.bottom && this.prev != null && IntAVLTreeSet.this.compare(this.prev.key, Subset.this.from) < 0) {
                    this.prev = null;
                }
            }
            
            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Subset.this.top && this.next != null && IntAVLTreeSet.this.compare(this.next.key, Subset.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
    }
}

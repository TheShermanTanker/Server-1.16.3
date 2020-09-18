package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Comparator;

public final class ObjectSortedSets {
    public static final EmptySet EMPTY_SET;
    
    private ObjectSortedSets() {
    }
    
    public static <K> ObjectSet<K> emptySet() {
        return (ObjectSet<K>)ObjectSortedSets.EMPTY_SET;
    }
    
    public static <K> ObjectSortedSet<K> singleton(final K element) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0         /* element */
        //     5: aconst_null    
        //     6: invokespecial   it/unimi/dsi/fastutil/objects/ObjectSortedSets$Singleton.<init>:(Ljava/lang/Object;Lit/unimi/dsi/fastutil/objects/ObjectSortedSets$1;)V
        //     9: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;>(TK;)Lit/unimi/dsi/fastutil/objects/ObjectSortedSet<TK;>;
        //    MethodParameters:
        //  Name     Flags  
        //  -------  -----
        //  element  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.resolve(CoreMetadataFactory.java:744)
        //     at com.strobel.assembler.metadata.MetadataHelper.getBaseType(MetadataHelper.java:686)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:813)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2503)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
        //     at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
        //     at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
        //     at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
        //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
        //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.base/java.lang.Thread.run(Thread.java:832)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static <K> ObjectSortedSet<K> singleton(final K element, final Comparator<? super K> comparator) {
        return new Singleton<K>(element, comparator);
    }
    
    public static <K> ObjectSortedSet<K> synchronize(final ObjectSortedSet<K> s) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0         /* s */
        //     5: invokespecial   it/unimi/dsi/fastutil/objects/ObjectSortedSets$SynchronizedSortedSet.<init>:(Lit/unimi/dsi/fastutil/objects/ObjectSortedSet;)V
        //     8: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;>(Lit/unimi/dsi/fastutil/objects/ObjectSortedSet<TK;>;)Lit/unimi/dsi/fastutil/objects/ObjectSortedSet<TK;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  s     
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2370)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1007)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
        //     at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
        //     at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
        //     at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
        //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
        //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.base/java.lang.Thread.run(Thread.java:832)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static <K> ObjectSortedSet<K> synchronize(final ObjectSortedSet<K> s, final Object sync) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0         /* s */
        //     5: aload_1         /* sync */
        //     6: invokespecial   it/unimi/dsi/fastutil/objects/ObjectSortedSets$SynchronizedSortedSet.<init>:(Lit/unimi/dsi/fastutil/objects/ObjectSortedSet;Ljava/lang/Object;)V
        //     9: areturn        
        //    Signature:
        //  <K:Ljava/lang/Object;>(Lit/unimi/dsi/fastutil/objects/ObjectSortedSet<TK;>;Ljava/lang/Object;)Lit/unimi/dsi/fastutil/objects/ObjectSortedSet<TK;>;
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  s     
        //  sync  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2370)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:575)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
        //     at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
        //     at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
        //     at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
        //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
        //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.base/java.lang.Thread.run(Thread.java:832)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static <K> ObjectSortedSet<K> unmodifiable(final ObjectSortedSet<K> s) {
        return new UnmodifiableSortedSet<K>(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet<K> extends ObjectSets.EmptySet<K> implements ObjectSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return (ObjectBidirectionalIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K from) {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K to) {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        public K first() {
            throw new NoSuchElementException();
        }
        
        public K last() {
            throw new NoSuchElementException();
        }
        
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public Object clone() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return ObjectSortedSets.EMPTY_SET;
        }
    }
    
    public static class EmptySet<K> extends ObjectSets.EmptySet<K> implements ObjectSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return (ObjectBidirectionalIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K from) {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K to) {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        public K first() {
            throw new NoSuchElementException();
        }
        
        public K last() {
            throw new NoSuchElementException();
        }
        
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public Object clone() {
            return ObjectSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return ObjectSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton<K> extends ObjectSets.Singleton<K> implements ObjectSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        final Comparator<? super K> comparator;
        
        protected Singleton(final K element, final Comparator<? super K> comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final K element) {
            this(element, (java.util.Comparator<? super Object>)null);
        }
        
        final int compare(final K k1, final K k2) {
            return (this.comparator == null) ? ((Comparable)k1).compareTo(k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            final ObjectBidirectionalIterator<K> i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        public Comparator<? super K> comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        public K first() {
            return this.element;
        }
        
        public K last() {
            return this.element;
        }
    }
    
    public static class Singleton<K> extends ObjectSets.Singleton<K> implements ObjectSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        final Comparator<? super K> comparator;
        
        protected Singleton(final K element, final Comparator<? super K> comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final K element) {
            this(element, (java.util.Comparator<? super Object>)null);
        }
        
        final int compare(final K k1, final K k2) {
            return (this.comparator == null) ? ((Comparable)k1).compareTo(k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            final ObjectBidirectionalIterator<K> i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        public Comparator<? super K> comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        public K first() {
            return this.element;
        }
        
        public K last() {
            return this.element;
        }
    }
    
    public static class SynchronizedSortedSet<K> extends ObjectSets.SynchronizedSet<K> implements ObjectSortedSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectSortedSet<K> sortedSet;
        
        protected SynchronizedSortedSet(final ObjectSortedSet<K> s, final Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }
        
        protected SynchronizedSortedSet(final ObjectSortedSet<K> s) {
            super(s);
            this.sortedSet = s;
        }
        
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.sortedSet.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return new SynchronizedSortedSet((ObjectSortedSet<Object>)this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return new SynchronizedSortedSet((ObjectSortedSet<Object>)this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return new SynchronizedSortedSet((ObjectSortedSet<Object>)this.sortedSet.tailSet(from), this.sync);
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return this.sortedSet.iterator(from);
        }
        
        public K first() {
            synchronized (this.sync) {
                return (K)this.sortedSet.first();
            }
        }
        
        public K last() {
            synchronized (this.sync) {
                return (K)this.sortedSet.last();
            }
        }
    }
    
    public static class SynchronizedSortedSet<K> extends ObjectSets.SynchronizedSet<K> implements ObjectSortedSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectSortedSet<K> sortedSet;
        
        protected SynchronizedSortedSet(final ObjectSortedSet<K> s, final Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }
        
        protected SynchronizedSortedSet(final ObjectSortedSet<K> s) {
            super(s);
            this.sortedSet = s;
        }
        
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.sortedSet.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return new SynchronizedSortedSet<K>(this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return new SynchronizedSortedSet<K>(this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return new SynchronizedSortedSet<K>(this.sortedSet.tailSet(from), this.sync);
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return this.sortedSet.iterator(from);
        }
        
        public K first() {
            synchronized (this.sync) {
                return (K)this.sortedSet.first();
            }
        }
        
        public K last() {
            synchronized (this.sync) {
                return (K)this.sortedSet.last();
            }
        }
    }
    
    public static class UnmodifiableSortedSet<K> extends ObjectSets.UnmodifiableSet<K> implements ObjectSortedSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectSortedSet<K> sortedSet;
        
        protected UnmodifiableSortedSet(final ObjectSortedSet<K> s) {
            super(s);
            this.sortedSet = s;
        }
        
        public Comparator<? super K> comparator() {
            return this.sortedSet.comparator();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return new UnmodifiableSortedSet((ObjectSortedSet<Object>)this.sortedSet.subSet(from, to));
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return new UnmodifiableSortedSet((ObjectSortedSet<Object>)this.sortedSet.headSet(to));
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return new UnmodifiableSortedSet((ObjectSortedSet<Object>)this.sortedSet.tailSet(from));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return ObjectIterators.<K>unmodifiable(this.sortedSet.iterator());
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return ObjectIterators.<K>unmodifiable(this.sortedSet.iterator(from));
        }
        
        public K first() {
            return (K)this.sortedSet.first();
        }
        
        public K last() {
            return (K)this.sortedSet.last();
        }
    }
}

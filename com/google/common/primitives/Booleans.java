package com.google.common.primitives;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.RandomAccess;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Booleans {
    private Booleans() {
    }
    
    @Beta
    public static Comparator<Boolean> trueFirst() {
        return (Comparator<Boolean>)BooleanComparator.TRUE_FIRST;
    }
    
    @Beta
    public static Comparator<Boolean> falseFirst() {
        return (Comparator<Boolean>)BooleanComparator.FALSE_FIRST;
    }
    
    public static int hashCode(final boolean value) {
        return value ? 1231 : 1237;
    }
    
    public static int compare(final boolean a, final boolean b) {
        return (a == b) ? 0 : (a ? 1 : -1);
    }
    
    public static boolean contains(final boolean[] array, final boolean target) {
        for (final boolean value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }
    
    public static int indexOf(final boolean[] array, final boolean target) {
        return indexOf(array, target, 0, array.length);
    }
    
    private static int indexOf(final boolean[] array, final boolean target, final int start, final int end) {
        for (int i = start; i < end; ++i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(final boolean[] array, final boolean[] target) {
        Preconditions.<boolean[]>checkNotNull(array, "array");
        Preconditions.<boolean[]>checkNotNull(target, "target");
        if (target.length == 0) {
            return 0;
        }
        int i = 0;
    Label_0023:
        while (i < array.length - target.length + 1) {
            for (int j = 0; j < target.length; ++j) {
                if (array[i + j] != target[j]) {
                    ++i;
                    continue Label_0023;
                }
            }
            return i;
        }
        return -1;
    }
    
    public static int lastIndexOf(final boolean[] array, final boolean target) {
        return lastIndexOf(array, target, 0, array.length);
    }
    
    private static int lastIndexOf(final boolean[] array, final boolean target, final int start, final int end) {
        for (int i = end - 1; i >= start; --i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean[] concat(final boolean[]... arrays) {
        int length = 0;
        for (final boolean[] array : arrays) {
            length += array.length;
        }
        final boolean[] result = new boolean[length];
        int pos = 0;
        for (final boolean[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }
    
    public static boolean[] ensureCapacity(final boolean[] array, final int minLength, final int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }
    
    public static String join(final String separator, final boolean... array) {
        Preconditions.<String>checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(array.length * 7);
        builder.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }
    
    public static Comparator<boolean[]> lexicographicalComparator() {
        return (Comparator<boolean[]>)LexicographicalComparator.INSTANCE;
    }
    
    public static boolean[] toArray(final Collection<Boolean> collection) {
        if (collection instanceof BooleanArrayAsList) {
            return ((BooleanArrayAsList)collection).toBooleanArray();
        }
        final Object[] boxedArray = collection.toArray();
        final int len = boxedArray.length;
        final boolean[] array = new boolean[len];
        for (int i = 0; i < len; ++i) {
            array[i] = Preconditions.<Boolean>checkNotNull(boxedArray[i]);
        }
        return array;
    }
    
    public static List<Boolean> asList(final boolean... backingArray) {
        if (backingArray.length == 0) {
            return (List<Boolean>)Collections.emptyList();
        }
        return (List<Boolean>)new BooleanArrayAsList(backingArray);
    }
    
    @Beta
    public static int countTrue(final boolean... values) {
        int count = 0;
        for (final boolean value : values) {
            if (value) {
                ++count;
            }
        }
        return count;
    }
    
    private enum BooleanComparator implements Comparator<Boolean> {
        TRUE_FIRST(1, "Booleans.trueFirst()"), 
        FALSE_FIRST(-1, "Booleans.falseFirst()");
        
        private final int trueValue;
        private final String toString;
        
        private BooleanComparator(final int trueValue, final String toString) {
            this.trueValue = trueValue;
            this.toString = toString;
        }
        
        public int compare(final Boolean a, final Boolean b) {
            final int aVal = a ? this.trueValue : 0;
            final int bVal = b ? this.trueValue : 0;
            return bVal - aVal;
        }
        
        public String toString() {
            return this.toString;
        }
    }
    
    private enum LexicographicalComparator implements Comparator<boolean[]> {
        INSTANCE;
        
        public int compare(final boolean[] left, final boolean[] right) {
            for (int minLength = Math.min(left.length, right.length), i = 0; i < minLength; ++i) {
                final int result = Booleans.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
        
        public String toString() {
            return "Booleans.lexicographicalComparator()";
        }
    }
    
    @GwtCompatible
    private static class BooleanArrayAsList extends AbstractList<Boolean> implements RandomAccess, Serializable {
        final boolean[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;
        
        BooleanArrayAsList(final boolean[] array) {
            this(array, 0, array.length);
        }
        
        BooleanArrayAsList(final boolean[] array, final int start, final int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }
        
        public int size() {
            return this.end - this.start;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public Boolean get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }
        
        public boolean contains(final Object target) {
            return target instanceof Boolean && indexOf(this.array, (boolean)target, this.start, this.end) != -1;
        }
        
        public int indexOf(final Object target) {
            if (target instanceof Boolean) {
                final int i = indexOf(this.array, (boolean)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        public int lastIndexOf(final Object target) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: instanceof      Ljava/lang/Boolean;
            //     4: ifeq            41
            //     7: aload_0         /* this */
            //     8: getfield        com/google/common/primitives/Booleans$BooleanArrayAsList.array:[Z
            //    11: aload_1         /* target */
            //    12: checkcast       Ljava/lang/Boolean;
            //    15: invokevirtual   java/lang/Boolean.booleanValue:()Z
            //    18: aload_0         /* this */
            //    19: getfield        com/google/common/primitives/Booleans$BooleanArrayAsList.start:I
            //    22: aload_0         /* this */
            //    23: getfield        com/google/common/primitives/Booleans$BooleanArrayAsList.end:I
            //    26: invokestatic    com/google/common/primitives/Booleans.access$100:([ZZII)I
            //    29: istore_2        /* i */
            //    30: iload_2         /* i */
            //    31: iflt            41
            //    34: iload_2         /* i */
            //    35: aload_0         /* this */
            //    36: getfield        com/google/common/primitives/Booleans$BooleanArrayAsList.start:I
            //    39: isub           
            //    40: ireturn        
            //    41: iconst_m1      
            //    42: ireturn        
            //    MethodParameters:
            //  Name    Flags  
            //  ------  -----
            //  target  
            //    StackMapTable: 00 01 29
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1506)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        public Boolean set(final int index, final Boolean element) {
            Preconditions.checkElementIndex(index, this.size());
            final boolean oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.<Boolean>checkNotNull(element);
            return oldValue;
        }
        
        public List<Boolean> subList(final int fromIndex, final int toIndex) {
            final int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return (List<Boolean>)Collections.emptyList();
            }
            return (List<Boolean>)new BooleanArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }
        
        public boolean equals(@Nullable final Object object) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: aload_0         /* this */
            //     2: if_acmpne       7
            //     5: iconst_1       
            //     6: ireturn        
            //     7: aload_1         /* object */
            //     8: instanceof      Lcom/google/common/primitives/Booleans$BooleanArrayAsList;
            //    11: ifeq            80
            //    14: aload_1         /* object */
            //    15: checkcast       Lcom/google/common/primitives/Booleans$BooleanArrayAsList;
            //    18: astore_2        /* that */
            //    19: aload_0         /* this */
            //    20: invokevirtual   com/google/common/primitives/Booleans$BooleanArrayAsList.size:()I
            //    23: istore_3        /* size */
            //    24: aload_2         /* that */
            //    25: invokevirtual   com/google/common/primitives/Booleans$BooleanArrayAsList.size:()I
            //    28: iload_3         /* size */
            //    29: if_icmpeq       34
            //    32: iconst_0       
            //    33: ireturn        
            //    34: iconst_0       
            //    35: istore          i
            //    37: iload           i
            //    39: iload_3         /* size */
            //    40: if_icmpge       78
            //    43: aload_0         /* this */
            //    44: getfield        com/google/common/primitives/Booleans$BooleanArrayAsList.array:[Z
            //    47: aload_0         /* this */
            //    48: getfield        com/google/common/primitives/Booleans$BooleanArrayAsList.start:I
            //    51: iload           i
            //    53: iadd           
            //    54: baload         
            //    55: aload_2         /* that */
            //    56: getfield        com/google/common/primitives/Booleans$BooleanArrayAsList.array:[Z
            //    59: aload_2         /* that */
            //    60: getfield        com/google/common/primitives/Booleans$BooleanArrayAsList.start:I
            //    63: iload           i
            //    65: iadd           
            //    66: baload         
            //    67: if_icmpeq       72
            //    70: iconst_0       
            //    71: ireturn        
            //    72: iinc            i, 1
            //    75: goto            37
            //    78: iconst_1       
            //    79: ireturn        
            //    80: aload_0         /* this */
            //    81: aload_1         /* object */
            //    82: invokespecial   java/util/AbstractList.equals:(Ljava/lang/Object;)Z
            //    85: ireturn        
            //    MethodParameters:
            //  Name    Flags  
            //  ------  -----
            //  object  
            //    StackMapTable: 00 06 07 FD 00 1A 07 00 02 01 FC 00 02 01 22 FA 00 05 F9 00 01
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:715)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1496)
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        public int hashCode() {
            int result = 1;
            for (int i = this.start; i < this.end; ++i) {
                result = 31 * result + Booleans.hashCode(this.array[i]);
            }
            return result;
        }
        
        public String toString() {
            final StringBuilder builder = new StringBuilder(this.size() * 7);
            builder.append(this.array[this.start] ? "[true" : "[false");
            for (int i = this.start + 1; i < this.end; ++i) {
                builder.append(this.array[i] ? ", true" : ", false");
            }
            return builder.append(']').toString();
        }
        
        boolean[] toBooleanArray() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }
    }
}

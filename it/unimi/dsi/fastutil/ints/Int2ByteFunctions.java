package it.unimi.dsi.fastutil.ints;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import java.util.Objects;
import java.util.function.Function;

public final class Int2ByteFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Int2ByteFunctions() {
    }
    
    public static Int2ByteFunction singleton(final int key, final byte value) {
        return new Singleton(key, value);
    }
    
    public static Int2ByteFunction singleton(final Integer key, final Byte value) {
        return new Singleton(key, value);
    }
    
    public static Int2ByteFunction synchronize(final Int2ByteFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Int2ByteFunction synchronize(final Int2ByteFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Int2ByteFunction unmodifiable(final Int2ByteFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Int2ByteFunction primitive(final Function<? super Integer, ? extends Byte> f) {
        Objects.requireNonNull(f);
        if (f instanceof Int2ByteFunction) {
            return (Int2ByteFunction)f;
        }
        if (f instanceof IntUnaryOperator) {
            return key -> SafeMath.safeIntToByte(((IntUnaryOperator)f).applyAsInt(key));
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractInt2ByteFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public byte get(final int k) {
            return 0;
        }
        
        public boolean containsKey(final int k) {
            return false;
        }
        
        @Override
        public byte defaultReturnValue() {
            return 0;
        }
        
        @Override
        public void defaultReturnValue(final byte defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Int2ByteFunctions.EMPTY_FUNCTION;
        }
        
        public int hashCode() {
            return 0;
        }
        
        public boolean equals(final Object o) {
            return o instanceof Function && ((Function)o).size() == 0;
        }
        
        public String toString() {
            return "{}";
        }
        
        private Object readResolve() {
            return Int2ByteFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractInt2ByteFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int key;
        protected final byte value;
        
        protected Singleton(final int key, final byte value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final int k) {
            return this.key == k;
        }
        
        public byte get(final int k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Int2ByteFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ByteFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Int2ByteFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Int2ByteFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public int applyAsInt(final int operand) {
            synchronized (this.sync) {
                return this.function.applyAsInt(operand);
            }
        }
        
        @Deprecated
        public Byte apply(final Integer key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public byte defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final byte defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        public boolean containsKey(final int k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        @Deprecated
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        public byte put(final int k, final byte v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public byte get(final int k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public byte remove(final int k) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        it/unimi/dsi/fastutil/ints/Int2ByteFunctions$SynchronizedFunction.sync:Ljava/lang/Object;
            //     4: dup            
            //     5: astore_2       
            //     6: monitorenter   
            //     7: aload_0         /* this */
            //     8: getfield        it/unimi/dsi/fastutil/ints/Int2ByteFunctions$SynchronizedFunction.function:Lit/unimi/dsi/fastutil/ints/Int2ByteFunction;
            //    11: iload_1         /* k */
            //    12: invokeinterface it/unimi/dsi/fastutil/ints/Int2ByteFunction.remove:(I)B
            //    17: aload_2        
            //    18: monitorexit    
            //    19: ireturn        
            //    20: astore_3       
            //    21: aload_2        
            //    22: monitorexit    
            //    23: aload_3        
            //    24: athrow         
            //    MethodParameters:
            //  Name  Flags  
            //  ----  -----
            //  k     
            //    StackMapTable: 00 01 FF 00 14 00 03 07 00 02 01 07 00 04 00 01 07 00 2B
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  7      19     20     25     Any
            //  20     23     20     25     Any
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
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1036)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:710)
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
        
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        @Deprecated
        public Byte put(final Integer k, final Byte v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Byte get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Byte remove(final Object k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        public int hashCode() {
            synchronized (this.sync) {
                return this.function.hashCode();
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.function.equals(o);
            }
        }
        
        public String toString() {
            synchronized (this.sync) {
                return this.function.toString();
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }
    
    public static class UnmodifiableFunction extends AbstractInt2ByteFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ByteFunction function;
        
        protected UnmodifiableFunction(final Int2ByteFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public byte defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final byte defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final int k) {
            return this.function.containsKey(k);
        }
        
        public byte put(final int k, final byte v) {
            throw new UnsupportedOperationException();
        }
        
        public byte get(final int k) {
            return this.function.get(k);
        }
        
        public byte remove(final int k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Byte put(final Integer k, final Byte v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Byte get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Byte remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public int hashCode() {
            return this.function.hashCode();
        }
        
        public boolean equals(final Object o) {
            return o == this || this.function.equals(o);
        }
        
        public String toString() {
            return this.function.toString();
        }
    }
    
    public static class PrimitiveFunction implements Int2ByteFunction {
        protected final java.util.function.Function<? super Integer, ? extends Byte> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Integer, ? extends Byte> function) {
            this.function = function;
        }
        
        public boolean containsKey(final int key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public byte get(final int key) {
            final Byte v = (Byte)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Byte get(final Object key) {
            if (key == null) {
                return null;
            }
            return (Byte)this.function.apply(key);
        }
        
        @Deprecated
        public Byte put(final Integer key, final Byte value) {
            throw new UnsupportedOperationException();
        }
    }
}

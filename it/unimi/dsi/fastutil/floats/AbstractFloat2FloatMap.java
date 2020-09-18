package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractFloat2FloatMap extends AbstractFloat2FloatFunction implements Float2FloatMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractFloat2FloatMap() {
    }
    
    @Override
    public boolean containsValue(final float v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final float k) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   it/unimi/dsi/fastutil/floats/AbstractFloat2FloatMap.float2FloatEntrySet:()Lit/unimi/dsi/fastutil/objects/ObjectSet;
        //     4: invokeinterface it/unimi/dsi/fastutil/objects/ObjectSet.iterator:()Lit/unimi/dsi/fastutil/objects/ObjectIterator;
        //     9: astore_2        /* i */
        //    10: aload_2         /* i */
        //    11: invokeinterface it/unimi/dsi/fastutil/objects/ObjectIterator.hasNext:()Z
        //    16: ifeq            40
        //    19: aload_2         /* i */
        //    20: invokeinterface it/unimi/dsi/fastutil/objects/ObjectIterator.next:()Ljava/lang/Object;
        //    25: checkcast       Lit/unimi/dsi/fastutil/floats/Float2FloatMap$Entry;
        //    28: invokeinterface it/unimi/dsi/fastutil/floats/Float2FloatMap$Entry.getFloatKey:()F
        //    33: fload_1         /* k */
        //    34: fcmpl          
        //    35: ifne            10
        //    38: iconst_1       
        //    39: ireturn        
        //    40: iconst_0       
        //    41: ireturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  k     
        //    StackMapTable: 00 02 FC 00 0A 07 00 3F 1D
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2104)
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
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public FloatSet keySet() {
        return new AbstractFloatSet() {
            @Override
            public boolean contains(final float k) {
                return AbstractFloat2FloatMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractFloat2FloatMap.this.size();
            }
            
            public void clear() {
                AbstractFloat2FloatMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final ObjectIterator<Entry> i = Float2FloatMaps.fastIterator(AbstractFloat2FloatMap.this);
                    
                    public float nextFloat() {
                        return ((Entry)this.i.next()).getFloatKey();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                    
                    public void remove() {
                        this.i.remove();
                    }
                };
            }
        };
    }
    
    @Override
    public FloatCollection values() {
        return new AbstractFloatCollection() {
            @Override
            public boolean contains(final float k) {
                return AbstractFloat2FloatMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractFloat2FloatMap.this.size();
            }
            
            public void clear() {
                AbstractFloat2FloatMap.this.clear();
            }
            
            @Override
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final ObjectIterator<Entry> i = Float2FloatMaps.fastIterator(AbstractFloat2FloatMap.this);
                    
                    public float nextFloat() {
                        return ((Entry)this.i.next()).getFloatValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends Float, ? extends Float> m) {
        if (m instanceof Float2FloatMap) {
            final ObjectIterator<Entry> i = Float2FloatMaps.fastIterator((Float2FloatMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getFloatKey(), e.getFloatValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Float, ? extends Float>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Float, ? extends Float> e2 = j.next();
                this.put((Float)e2.getKey(), (Float)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Float2FloatMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Entry)i.next()).hashCode();
        }
        return h;
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        final Map<?, ?> m = o;
        return m.size() == this.size() && this.float2FloatEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Float2FloatMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final Entry e = (Entry)i.next();
            s.append(String.valueOf(e.getFloatKey()));
            s.append("=>");
            s.append(String.valueOf(e.getFloatValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected float key;
        protected float value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Float key, final Float value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final float key, final float value) {
            this.key = key;
            this.value = value;
        }
        
        public float getFloatKey() {
            return this.key;
        }
        
        public float getFloatValue() {
            return this.value;
        }
        
        public float setValue(final float value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return Float.floatToIntBits(this.key) == Float.floatToIntBits(e.getFloatKey()) && Float.floatToIntBits(this.value) == Float.floatToIntBits(e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Float)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Float && Float.floatToIntBits(this.key) == Float.floatToIntBits((float)key) && Float.floatToIntBits(this.value) == Float.floatToIntBits((float)value);
        }
        
        public int hashCode() {
            return HashCommon.float2int(this.key) ^ HashCommon.float2int(this.value);
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Float2FloatMap map;
        
        public BasicEntrySet(final Float2FloatMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final float k = e.getFloatKey();
                return this.map.containsKey(k) && Float.floatToIntBits(this.map.get(k)) == Float.floatToIntBits(e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Float)) {
                return false;
            }
            final float i = (float)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Float && this.map.containsKey(i) && Float.floatToIntBits(this.map.get(i)) == Float.floatToIntBits((float)value);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getFloatKey(), e.getFloatValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Float)) {
                return false;
            }
            final float k = (float)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Float)) {
                return false;
            }
            final float v = (float)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}

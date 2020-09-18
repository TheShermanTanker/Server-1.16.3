package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractLong2ByteMap extends AbstractLong2ByteFunction implements Long2ByteMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractLong2ByteMap() {
    }
    
    @Override
    public boolean containsValue(final byte v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final long k) {
        final ObjectIterator<Entry> i = this.long2ByteEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getLongKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public LongSet keySet() {
        return new AbstractLongSet() {
            @Override
            public boolean contains(final long k) {
                return AbstractLong2ByteMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractLong2ByteMap.this.size();
            }
            
            public void clear() {
                AbstractLong2ByteMap.this.clear();
            }
            
            @Override
            public LongIterator iterator() {
                return new LongIterator() {
                    private final ObjectIterator<Entry> i = Long2ByteMaps.fastIterator(AbstractLong2ByteMap.this);
                    
                    public long nextLong() {
                        return ((Entry)this.i.next()).getLongKey();
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
    public ByteCollection values() {
        return new AbstractByteCollection() {
            @Override
            public boolean contains(final byte k) {
                return AbstractLong2ByteMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractLong2ByteMap.this.size();
            }
            
            public void clear() {
                AbstractLong2ByteMap.this.clear();
            }
            
            @Override
            public ByteIterator iterator() {
                return new ByteIterator() {
                    private final ObjectIterator<Entry> i = Long2ByteMaps.fastIterator(AbstractLong2ByteMap.this);
                    
                    public byte nextByte() {
                        return ((Entry)this.i.next()).getByteValue();
                    }
                    
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }
    
    public void putAll(final Map<? extends Long, ? extends Byte> m) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: instanceof      Lit/unimi/dsi/fastutil/longs/Long2ByteMap;
        //     4: ifeq            57
        //     7: aload_1         /* m */
        //     8: checkcast       Lit/unimi/dsi/fastutil/longs/Long2ByteMap;
        //    11: invokestatic    it/unimi/dsi/fastutil/longs/Long2ByteMaps.fastIterator:(Lit/unimi/dsi/fastutil/longs/Long2ByteMap;)Lit/unimi/dsi/fastutil/objects/ObjectIterator;
        //    14: astore_2        /* i */
        //    15: aload_2         /* i */
        //    16: invokeinterface it/unimi/dsi/fastutil/objects/ObjectIterator.hasNext:()Z
        //    21: ifeq            54
        //    24: aload_2         /* i */
        //    25: invokeinterface it/unimi/dsi/fastutil/objects/ObjectIterator.next:()Ljava/lang/Object;
        //    30: checkcast       Lit/unimi/dsi/fastutil/longs/Long2ByteMap$Entry;
        //    33: astore_3        /* e */
        //    34: aload_0         /* this */
        //    35: aload_3         /* e */
        //    36: invokeinterface it/unimi/dsi/fastutil/longs/Long2ByteMap$Entry.getLongKey:()J
        //    41: aload_3         /* e */
        //    42: invokeinterface it/unimi/dsi/fastutil/longs/Long2ByteMap$Entry.getByteValue:()B
        //    47: invokevirtual   it/unimi/dsi/fastutil/longs/AbstractLong2ByteMap.put:(JB)B
        //    50: pop            
        //    51: goto            15
        //    54: goto            122
        //    57: aload_1         /* m */
        //    58: invokeinterface java/util/Map.size:()I
        //    63: istore_2        /* n */
        //    64: aload_1         /* m */
        //    65: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //    70: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    75: astore_3        /* i */
        //    76: iload_2         /* n */
        //    77: iinc            n, -1
        //    80: ifeq            122
        //    83: aload_3         /* i */
        //    84: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    89: checkcast       Ljava/util/Map$Entry;
        //    92: astore          e
        //    94: aload_0         /* this */
        //    95: aload           e
        //    97: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   102: checkcast       Ljava/lang/Long;
        //   105: aload           e
        //   107: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   112: checkcast       Ljava/lang/Byte;
        //   115: invokevirtual   it/unimi/dsi/fastutil/longs/AbstractLong2ByteMap.put:(Ljava/lang/Long;Ljava/lang/Byte;)Ljava/lang/Byte;
        //   118: pop            
        //   119: goto            76
        //   122: return         
        //    Signature:
        //  (Ljava/util/Map<+Ljava/lang/Long;+Ljava/lang/Byte;>;)V
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  m     
        //    StackMapTable: 00 05 FC 00 0F 07 00 40 FA 00 26 02 FD 00 12 01 07 00 78 F9 00 2D
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2611)
        //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2608)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getInterfaces(MetadataHelper.java:702)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2027)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:840)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2476)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Long2ByteMaps.fastIterator(this);
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
        return m.size() == this.size() && this.long2ByteEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Long2ByteMaps.fastIterator(this);
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
            s.append(String.valueOf(e.getLongKey()));
            s.append("=>");
            s.append(String.valueOf((int)e.getByteValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected long key;
        protected byte value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Long key, final Byte value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final long key, final byte value) {
            this.key = key;
            this.value = value;
        }
        
        public long getLongKey() {
            return this.key;
        }
        
        public byte getByteValue() {
            return this.value;
        }
        
        public byte setValue(final byte value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.key == e.getLongKey() && this.value == e.getByteValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Byte && this.key == (long)key && this.value == (byte)value;
        }
        
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ this.value;
        }
        
        public String toString() {
            return new StringBuilder().append(this.key).append("->").append((int)this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Long2ByteMap map;
        
        public BasicEntrySet(final Long2ByteMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final long k = e.getLongKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getByteValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            final long i = (long)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Byte && this.map.containsKey(i) && this.map.get(i) == (byte)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getLongKey(), e.getByteValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            final long k = (long)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Byte)) {
                return false;
            }
            final byte v = (byte)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}

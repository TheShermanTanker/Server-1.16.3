package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;

public abstract class AbstractShort2CharMap extends AbstractShort2CharFunction implements Short2CharMap, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    
    protected AbstractShort2CharMap() {
    }
    
    @Override
    public boolean containsValue(final char v) {
        return this.values().contains(v);
    }
    
    @Override
    public boolean containsKey(final short k) {
        final ObjectIterator<Entry> i = this.short2CharEntrySet().iterator();
        while (i.hasNext()) {
            if (((Entry)i.next()).getShortKey() == k) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public ShortSet keySet() {
        return new AbstractShortSet() {
            @Override
            public boolean contains(final short k) {
                return AbstractShort2CharMap.this.containsKey(k);
            }
            
            public int size() {
                return AbstractShort2CharMap.this.size();
            }
            
            public void clear() {
                AbstractShort2CharMap.this.clear();
            }
            
            @Override
            public ShortIterator iterator() {
                return new ShortIterator() {
                    private final ObjectIterator<Entry> i = Short2CharMaps.fastIterator(AbstractShort2CharMap.this);
                    
                    public short nextShort() {
                        return ((Entry)this.i.next()).getShortKey();
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
    public CharCollection values() {
        return new AbstractCharCollection() {
            @Override
            public boolean contains(final char k) {
                return AbstractShort2CharMap.this.containsValue(k);
            }
            
            public int size() {
                return AbstractShort2CharMap.this.size();
            }
            
            public void clear() {
                AbstractShort2CharMap.this.clear();
            }
            
            @Override
            public CharIterator iterator() {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     3: dup            
                //     4: aload_0         /* this */
                //     5: invokespecial   it/unimi/dsi/fastutil/shorts/AbstractShort2CharMap$2$1.<init>:(Lit/unimi/dsi/fastutil/shorts/AbstractShort2CharMap$2;)V
                //     8: areturn        
                // 
                // The error that occurred was:
                // 
                // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
                //     at java.base/java.util.Vector.get(Vector.java:749)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
                //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
                //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
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
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1164)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        };
    }
    
    public void putAll(final Map<? extends Short, ? extends Character> m) {
        if (m instanceof Short2CharMap) {
            final ObjectIterator<Entry> i = Short2CharMaps.fastIterator((Short2CharMap)m);
            while (i.hasNext()) {
                final Entry e = (Entry)i.next();
                this.put(e.getShortKey(), e.getCharValue());
            }
        }
        else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends Short, ? extends Character>> j = m.entrySet().iterator();
            while (n-- != 0) {
                final Map.Entry<? extends Short, ? extends Character> e2 = j.next();
                this.put((Short)e2.getKey(), (Character)e2.getValue());
            }
        }
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<Entry> i = Short2CharMaps.fastIterator(this);
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
        return m.size() == this.size() && this.short2CharEntrySet().containsAll((Collection)m.entrySet());
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry> i = Short2CharMaps.fastIterator(this);
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
            s.append(String.valueOf((int)e.getShortKey()));
            s.append("=>");
            s.append(String.valueOf(e.getCharValue()));
        }
        s.append("}");
        return s.toString();
    }
    
    public static class BasicEntry implements Entry {
        protected short key;
        protected char value;
        
        public BasicEntry() {
        }
        
        public BasicEntry(final Short key, final Character value) {
            this.key = key;
            this.value = value;
        }
        
        public BasicEntry(final short key, final char value) {
            this.key = key;
            this.value = value;
        }
        
        public short getShortKey() {
            return this.key;
        }
        
        public char getCharValue() {
            return this.value;
        }
        
        public char setValue(final char value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.key == e.getShortKey() && this.value == e.getCharValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final Object value = e2.getValue();
            return value != null && value instanceof Character && this.key == (short)key && this.value == (char)value;
        }
        
        public int hashCode() {
            return this.key ^ this.value;
        }
        
        public String toString() {
            return new StringBuilder().append((int)this.key).append("->").append(this.value).toString();
        }
    }
    
    public abstract static class BasicEntrySet extends AbstractObjectSet<Entry> {
        protected final Short2CharMap map;
        
        public BasicEntrySet(final Short2CharMap map) {
            this.map = map;
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                final short k = e.getShortKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getCharValue();
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final short i = (short)key;
            final Object value = e2.getValue();
            return value != null && value instanceof Character && this.map.containsKey(i) && this.map.get(i) == (char)value;
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Entry) {
                final Entry e = (Entry)o;
                return this.map.remove(e.getShortKey(), e.getCharValue());
            }
            final Map.Entry<?, ?> e2 = o;
            final Object key = e2.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            final short k = (short)key;
            final Object value = e2.getValue();
            if (value == null || !(value instanceof Character)) {
                return false;
            }
            final char v = (char)value;
            return this.map.remove(k, v);
        }
        
        public int size() {
            return this.map.size();
        }
    }
}

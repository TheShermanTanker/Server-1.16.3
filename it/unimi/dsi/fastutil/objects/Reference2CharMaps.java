package it.unimi.dsi.fastutil.objects;

import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.chars.CharCollections;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.chars.CharSets;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

public final class Reference2CharMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Reference2CharMaps() {
    }
    
    public static <K> ObjectIterator<Reference2CharMap.Entry<K>> fastIterator(final Reference2CharMap<K> map) {
        final ObjectSet<Reference2CharMap.Entry<K>> entries = map.reference2CharEntrySet();
        return (entries instanceof Reference2CharMap.FastEntrySet) ? ((Reference2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> void fastForEach(final Reference2CharMap<K> map, final Consumer<? super Reference2CharMap.Entry<K>> consumer) {
        final ObjectSet<Reference2CharMap.Entry<K>> entries = map.reference2CharEntrySet();
        if (entries instanceof Reference2CharMap.FastEntrySet) {
            ((Reference2CharMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <K> ObjectIterable<Reference2CharMap.Entry<K>> fastIterable(final Reference2CharMap<K> map) {
        final ObjectSet<Reference2CharMap.Entry<K>> entries = map.reference2CharEntrySet();
        return (entries instanceof Reference2CharMap.FastEntrySet) ? new ObjectIterable<Reference2CharMap.Entry<K>>() {
            public ObjectIterator<Reference2CharMap.Entry<K>> iterator() {
                return ((Reference2CharMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Reference2CharMap.Entry<K>> consumer) {
                ((Reference2CharMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K> Reference2CharMap<K> emptyMap() {
        return (Reference2CharMap<K>)Reference2CharMaps.EMPTY_MAP;
    }
    
    public static <K> Reference2CharMap<K> singleton(final K key, final char value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2CharMap<K> singleton(final K key, final Character value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2CharMap<K> synchronize(final Reference2CharMap<K> m) {
        return new SynchronizedMap<K>(m);
    }
    
    public static <K> Reference2CharMap<K> synchronize(final Reference2CharMap<K> m, final Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }
    
    public static <K> Reference2CharMap<K> unmodifiable(final Reference2CharMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Reference2CharFunctions.EmptyFunction<K> implements Reference2CharMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final char v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends K, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2CharEntrySet() {
            return (ObjectSet<Entry<K>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            return (ReferenceSet<K>)ReferenceSets.EMPTY_SET;
        }
        
        @Override
        public CharCollection values() {
            return CharSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Reference2CharMaps.EMPTY_MAP;
        }
        
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Map && ((Map)o).isEmpty();
        }
        
        @Override
        public String toString() {
            return "{}";
        }
    }
    
    public static class Singleton<K> extends Reference2CharFunctions.Singleton<K> implements Reference2CharMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient CharCollection values;
        
        protected Singleton(final K key, final char value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final char v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (char)ov == this.value;
        }
        
        public void putAll(final Map<? extends K, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractReference2CharMap.BasicEntry<K>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Character>> entrySet() {
            return (ObjectSet<Map.Entry<K, Character>>)this.reference2CharEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.<K>singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public CharCollection values() {
            if (this.values == null) {
                this.values = CharSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return System.identityHashCode(this.key) ^ this.value;
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Map)) {
                return false;
            }
            final Map<?, ?> m = o;
            return m.size() == 1 && ((Map.Entry)m.entrySet().iterator().next()).equals(this.entrySet().iterator().next());
        }
        
        public String toString() {
            return new StringBuilder().append("{").append(this.key).append("=>").append(this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap<K> extends Reference2CharFunctions.SynchronizedFunction<K> implements Reference2CharMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2CharMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient CharCollection values;
        
        protected SynchronizedMap(final Reference2CharMap<K> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Reference2CharMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final char v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            synchronized (this.sync) {
                return this.map.containsValue(ov);
            }
        }
        
        public void putAll(final Map<? extends K, ? extends Character> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2CharEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<K>>synchronize(this.map.reference2CharEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Character>> entrySet() {
            return (ObjectSet<Map.Entry<K, Character>>)this.reference2CharEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = ReferenceSets.<K>synchronize(this.map.keySet(), this.sync);
                }
                return this.keys;
            }
        }
        
        @Override
        public CharCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return CharCollections.synchronize(this.map.values(), this.sync);
                }
                return this.values;
            }
        }
        
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.map.isEmpty();
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.map.hashCode();
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.map.equals(o);
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
        
        @Override
        public char getOrDefault(final Object key, final char defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super K, ? super Character> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Character, ? extends Character> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public char putIfAbsent(final K key, final char value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final Object key, final char value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public char replace(final K key, final char value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final K key, final char oldValue, final char newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public char computeCharIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeCharIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public char computeCharIfAbsentPartial(final K key, final Reference2CharFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeCharIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public char computeCharIfPresent(final K key, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeCharIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public char computeChar(final K key, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeChar(key, remappingFunction);
            }
        }
        
        @Override
        public char mergeChar(final K key, final char value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.mergeChar(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character getOrDefault(final Object key, final Character defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Character replace(final K key, final Character value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Character oldValue, final Character newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Character putIfAbsent(final K key, final Character value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        public Character computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Character> mappingFunction) {
            synchronized (this.sync) {
                return (Character)this.map.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
            }
        }
        
        public Character computeIfPresent(final K key, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return (Character)this.map.computeIfPresent(key, (BiFunction)remappingFunction);
            }
        }
        
        public Character compute(final K key, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return (Character)this.map.compute(key, (BiFunction)remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character merge(final K key, final Character value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<K> extends Reference2CharFunctions.UnmodifiableFunction<K> implements Reference2CharMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2CharMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient CharCollection values;
        
        protected UnmodifiableMap(final Reference2CharMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final char v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends K, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<K>>unmodifiable(this.map.reference2CharEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Character>> entrySet() {
            return (ObjectSet<Map.Entry<K, Character>>)this.reference2CharEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.<K>unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public CharCollection values() {
            if (this.values == null) {
                return CharCollections.unmodifiable(this.map.values());
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public int hashCode() {
            return this.map.hashCode();
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || this.map.equals(o);
        }
        
        @Override
        public char getOrDefault(final Object key, final char defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super K, ? super Character> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Character, ? extends Character> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char putIfAbsent(final K key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char replace(final K key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final K key, final char oldValue, final char newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeCharIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeCharIfAbsentPartial(final K key, final Reference2CharFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeCharIfPresent(final K key, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeChar(final K key, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char mergeChar(final K key, final char value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character getOrDefault(final Object key, final Character defaultValue) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        it/unimi/dsi/fastutil/objects/Reference2CharMaps$UnmodifiableMap.map:Lit/unimi/dsi/fastutil/objects/Reference2CharMap;
            //     4: aload_1         /* key */
            //     5: aload_2         /* defaultValue */
            //     6: invokeinterface it/unimi/dsi/fastutil/objects/Reference2CharMap.getOrDefault:(Ljava/lang/Object;Ljava/lang/Character;)Ljava/lang/Character;
            //    11: areturn        
            //    MethodParameters:
            //  Name          Flags  
            //  ------------  -----
            //  key           
            //  defaultValue  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:201)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:25)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameters(TypeSubstitutionVisitor.java:364)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:279)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2566)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character replace(final K key, final Character value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Character oldValue, final Character newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character putIfAbsent(final K key, final Character value) {
            throw new UnsupportedOperationException();
        }
        
        public Character computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Character> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Character computeIfPresent(final K key, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Character compute(final K key, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character merge(final K key, final Character value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}

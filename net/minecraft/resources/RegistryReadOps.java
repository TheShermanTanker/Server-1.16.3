package net.minecraft.resources;

import java.util.stream.Collectors;
import com.mojang.serialization.Encoder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.io.Reader;
import net.minecraft.server.packs.resources.Resource;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonIOException;
import java.io.IOException;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import java.util.OptionalInt;
import com.mojang.serialization.Decoder;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Collection;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.WritableRegistry;
import java.util.function.Supplier;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.util.IdentityHashMap;
import com.google.common.collect.Maps;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.serialization.DynamicOps;
import com.google.gson.JsonElement;
import net.minecraft.core.Registry;
import java.util.Map;
import net.minecraft.core.RegistryAccess;
import org.apache.logging.log4j.Logger;

public class RegistryReadOps<T> extends DelegatingOps<T> {
    private static final Logger LOGGER;
    private final ResourceAccess resources;
    private final RegistryAccess.RegistryHolder registryHolder;
    private final Map<ResourceKey<? extends Registry<?>>, ReadCache<?>> readCache;
    private final RegistryReadOps<JsonElement> jsonOps;
    
    public static <T> RegistryReadOps<T> create(final DynamicOps<T> dynamicOps, final ResourceManager acf, final RegistryAccess.RegistryHolder b) {
        return RegistryReadOps.<T>create(dynamicOps, ResourceAccess.forResourceManager(acf), b);
    }
    
    public static <T> RegistryReadOps<T> create(final DynamicOps<T> dynamicOps, final ResourceAccess b, final RegistryAccess.RegistryHolder b) {
        final RegistryReadOps<T> vh4 = new RegistryReadOps<T>(dynamicOps, b, b, Maps.<ResourceKey<? extends Registry<?>>, ReadCache<?>>newIdentityHashMap());
        RegistryAccess.load(b, vh4);
        return vh4;
    }
    
    private RegistryReadOps(final DynamicOps<T> dynamicOps, final ResourceAccess b, final RegistryAccess.RegistryHolder b, final IdentityHashMap<ResourceKey<? extends Registry<?>>, ReadCache<?>> identityHashMap) {
        super(dynamicOps);
        this.resources = b;
        this.registryHolder = b;
        this.readCache = (Map<ResourceKey<? extends Registry<?>>, ReadCache<?>>)identityHashMap;
        this.jsonOps = ((dynamicOps == JsonOps.INSTANCE) ? this : new RegistryReadOps<JsonElement>((DynamicOps<Object>)JsonOps.INSTANCE, b, b, identityHashMap));
    }
    
    protected <E> DataResult<Pair<Supplier<E>, T>> decodeElement(final T object, final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec, final boolean boolean4) {
        final Optional<WritableRegistry<E>> optional6 = this.registryHolder.<E>registry(vj);
        if (!optional6.isPresent()) {
            return DataResult.<Pair<Supplier<E>, T>>error(new StringBuilder().append("Unknown registry: ").append(vj).toString());
        }
        final WritableRegistry<E> gs7 = (WritableRegistry<E>)optional6.get();
        final DataResult<Pair<ResourceLocation, T>> dataResult8 = ResourceLocation.CODEC.<T>decode(this.delegate, object);
        if (dataResult8.result().isPresent()) {
            final Pair<ResourceLocation, T> pair9 = (Pair<ResourceLocation, T>)dataResult8.result().get();
            final ResourceLocation vk10 = pair9.getFirst();
            return this.<E>readAndRegisterElement(vj, gs7, codec, vk10).<Pair<Supplier<E>, T>>map((java.util.function.Function<? super java.util.function.Supplier<E>, ? extends Pair<Supplier<E>, T>>)(supplier -> Pair.<Supplier, Object>of(supplier, pair9.getSecond())));
        }
        if (!boolean4) {
            return DataResult.<Pair<Supplier<E>, T>>error("Inline definitions not allowed here");
        }
        return codec.<T>decode((DynamicOps<T>)this, object).<Pair<Supplier<E>, T>>map((java.util.function.Function<? super Pair<Object, T>, ? extends Pair<Supplier<E>, T>>)(pair -> pair.mapFirst(object -> () -> object)));
    }
    
    public <E> DataResult<MappedRegistry<E>> decodeElements(final MappedRegistry<E> gi, final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec) {
        final Collection<ResourceLocation> collection5 = this.resources.listResources(vj);
        DataResult<MappedRegistry<E>> dataResult6 = DataResult.<MappedRegistry<E>>success(gi, Lifecycle.stable());
        final String string7 = vj.location().getPath() + "/";
        for (final ResourceLocation vk9 : collection5) {
            final String string8 = vk9.getPath();
            if (!string8.endsWith(".json")) {
                RegistryReadOps.LOGGER.warn("Skipping resource {} since it is not a json file", vk9);
            }
            else if (!string8.startsWith(string7)) {
                RegistryReadOps.LOGGER.warn("Skipping resource {} since it does not have a registry name prefix", vk9);
            }
            else {
                final String string9 = string8.substring(string7.length(), string8.length() - ".json".length());
                final ResourceLocation vk10 = new ResourceLocation(vk9.getNamespace(), string9);
                dataResult6 = dataResult6.<MappedRegistry<E>>flatMap((java.util.function.Function<? super MappedRegistry<E>, ? extends DataResult<MappedRegistry<E>>>)(gi -> this.readAndRegisterElement(vj, gi, (Codec<Object>)codec, vk10).map((java.util.function.Function<? super java.util.function.Supplier<Object>, ?>)(supplier -> gi))));
            }
        }
        return dataResult6.setPartial(gi);
    }
    
    private <E> DataResult<Supplier<E>> readAndRegisterElement(final ResourceKey<? extends Registry<E>> vj, final WritableRegistry<E> gs, final Codec<E> codec, final ResourceLocation vk) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload           vk
        //     3: invokestatic    net/minecraft/resources/ResourceKey.create:(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/resources/ResourceKey;
        //     6: astore          vj6
        //     8: aload_0         /* this */
        //     9: aload_1         /* vj */
        //    10: invokespecial   net/minecraft/resources/RegistryReadOps.readCache:(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/resources/RegistryReadOps$ReadCache;
        //    13: astore          a7
        //    15: aload           a7
        //    17: invokestatic    net/minecraft/resources/RegistryReadOps$ReadCache.access$000:(Lnet/minecraft/resources/RegistryReadOps$ReadCache;)Ljava/util/Map;
        //    20: aload           vj6
        //    22: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    27: checkcast       Lcom/mojang/serialization/DataResult;
        //    30: astore          dataResult8
        //    32: aload           dataResult8
        //    34: ifnull          40
        //    37: aload           dataResult8
        //    39: areturn        
        //    40: aload_2         /* gs */
        //    41: aload           vj6
        //    43: invokedynamic   BootstrapMethod #3, get:(Lnet/minecraft/core/WritableRegistry;Lnet/minecraft/resources/ResourceKey;)Lcom/google/common/base/Supplier;
        //    48: invokestatic    com/google/common/base/Suppliers.memoize:(Lcom/google/common/base/Supplier;)Lcom/google/common/base/Supplier;
        //    51: astore          supplier9
        //    53: aload           a7
        //    55: invokestatic    net/minecraft/resources/RegistryReadOps$ReadCache.access$000:(Lnet/minecraft/resources/RegistryReadOps$ReadCache;)Ljava/util/Map;
        //    58: aload           vj6
        //    60: aload           supplier9
        //    62: invokestatic    com/mojang/serialization/DataResult.success:(Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
        //    65: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    70: pop            
        //    71: aload_0         /* this */
        //    72: getfield        net/minecraft/resources/RegistryReadOps.resources:Lnet/minecraft/resources/RegistryReadOps$ResourceAccess;
        //    75: aload_0         /* this */
        //    76: getfield        net/minecraft/resources/RegistryReadOps.jsonOps:Lnet/minecraft/resources/RegistryReadOps;
        //    79: aload_1         /* vj */
        //    80: aload           vj6
        //    82: aload_3         /* codec */
        //    83: invokeinterface net/minecraft/resources/RegistryReadOps$ResourceAccess.parseElement:(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceKey;Lcom/mojang/serialization/Decoder;)Lcom/mojang/serialization/DataResult;
        //    88: astore          dataResult10
        //    90: aload           dataResult10
        //    92: invokevirtual   com/mojang/serialization/DataResult.result:()Ljava/util/Optional;
        //    95: astore          optional11
        //    97: aload           optional11
        //    99: invokevirtual   java/util/Optional.isPresent:()Z
        //   102: ifeq            140
        //   105: aload           optional11
        //   107: invokevirtual   java/util/Optional.get:()Ljava/lang/Object;
        //   110: checkcast       Lcom/mojang/datafixers/util/Pair;
        //   113: astore          pair12
        //   115: aload_2         /* gs */
        //   116: aload           pair12
        //   118: invokevirtual   com/mojang/datafixers/util/Pair.getSecond:()Ljava/lang/Object;
        //   121: checkcast       Ljava/util/OptionalInt;
        //   124: aload           vj6
        //   126: aload           pair12
        //   128: invokevirtual   com/mojang/datafixers/util/Pair.getFirst:()Ljava/lang/Object;
        //   131: aload           dataResult10
        //   133: invokevirtual   com/mojang/serialization/DataResult.lifecycle:()Lcom/mojang/serialization/Lifecycle;
        //   136: invokevirtual   net/minecraft/core/WritableRegistry.registerOrOverride:(Ljava/util/OptionalInt;Lnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;)Ljava/lang/Object;
        //   139: pop            
        //   140: aload           optional11
        //   142: invokevirtual   java/util/Optional.isPresent:()Z
        //   145: ifne            176
        //   148: aload_2         /* gs */
        //   149: aload           vj6
        //   151: invokevirtual   net/minecraft/core/WritableRegistry.get:(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;
        //   154: ifnull          176
        //   157: aload_2         /* gs */
        //   158: aload           vj6
        //   160: invokedynamic   BootstrapMethod #4, get:(Lnet/minecraft/core/WritableRegistry;Lnet/minecraft/resources/ResourceKey;)Ljava/util/function/Supplier;
        //   165: invokestatic    com/mojang/serialization/Lifecycle.stable:()Lcom/mojang/serialization/Lifecycle;
        //   168: invokestatic    com/mojang/serialization/DataResult.success:(Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;)Lcom/mojang/serialization/DataResult;
        //   171: astore          dataResult12
        //   173: goto            191
        //   176: aload           dataResult10
        //   178: aload_2         /* gs */
        //   179: aload           vj6
        //   181: invokedynamic   BootstrapMethod #5, apply:(Lnet/minecraft/core/WritableRegistry;Lnet/minecraft/resources/ResourceKey;)Ljava/util/function/Function;
        //   186: invokevirtual   com/mojang/serialization/DataResult.map:(Ljava/util/function/Function;)Lcom/mojang/serialization/DataResult;
        //   189: astore          dataResult12
        //   191: aload           a7
        //   193: invokestatic    net/minecraft/resources/RegistryReadOps$ReadCache.access$000:(Lnet/minecraft/resources/RegistryReadOps$ReadCache;)Ljava/util/Map;
        //   196: aload           vj6
        //   198: aload           dataResult12
        //   200: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   205: pop            
        //   206: aload           dataResult12
        //   208: areturn        
        //    Signature:
        //  <E:Ljava/lang/Object;>(Lnet/minecraft/resources/ResourceKey<+Lnet/minecraft/core/Registry<TE;>;>;Lnet/minecraft/core/WritableRegistry<TE;>;Lcom/mojang/serialization/Codec<TE;>;Lnet/minecraft/resources/ResourceLocation;)Lcom/mojang/serialization/DataResult<Ljava/util/function/Supplier<TE;>;>;
        //    MethodParameters:
        //  Name   Flags  
        //  -----  -----
        //  vj     
        //  gs     
        //  codec  
        //  vk     
        //    StackMapTable: 00 04 FF 00 28 00 07 07 00 02 07 00 BC 07 00 8C 07 00 97 00 07 00 BC 07 00 0D 00 00 FF 00 63 00 0B 00 00 07 00 8C 00 00 07 00 BC 07 00 0D 00 00 07 00 82 07 00 6A 00 00 FA 00 23 FF 00 0E 00 0C 00 00 00 00 00 07 00 BC 07 00 0D 00 00 00 00 07 00 82 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
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
    
    private <E> ReadCache<E> readCache(final ResourceKey<? extends Registry<E>> vj) {
        return (ReadCache<E>)this.readCache.computeIfAbsent(vj, vj -> new ReadCache());
    }
    
    protected <E> DataResult<Registry<E>> registry(final ResourceKey<? extends Registry<E>> vj) {
        return (DataResult<Registry<E>>)this.registryHolder.registry(vj).map(gs -> DataResult.<WritableRegistry>success(gs, gs.elementsLifecycle())).orElseGet(() -> DataResult.error(new StringBuilder().append("Unknown registry: ").append(vj).toString()));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static final class ReadCache<E> {
        private final Map<ResourceKey<E>, DataResult<Supplier<E>>> values;
        
        private ReadCache() {
            this.values = Maps.newIdentityHashMap();
        }
    }
    
    public interface ResourceAccess {
        Collection<ResourceLocation> listResources(final ResourceKey<? extends Registry<?>> vj);
        
         <E> DataResult<Pair<E, OptionalInt>> parseElement(final DynamicOps<JsonElement> dynamicOps, final ResourceKey<? extends Registry<E>> vj2, final ResourceKey<E> vj3, final Decoder<E> decoder);
        
        default ResourceAccess forResourceManager(final ResourceManager acf) {
            return new ResourceAccess() {
                public Collection<ResourceLocation> listResources(final ResourceKey<? extends Registry<?>> vj) {
                    return acf.listResources(vj.location().getPath(), (Predicate<String>)(string -> string.endsWith(".json")));
                }
                
                public <E> DataResult<Pair<E, OptionalInt>> parseElement(final DynamicOps<JsonElement> dynamicOps, final ResourceKey<? extends Registry<E>> vj2, final ResourceKey<E> vj3, final Decoder<E> decoder) {
                    final ResourceLocation vk6 = vj3.location();
                    final ResourceLocation vk7 = new ResourceLocation(vk6.getNamespace(), vj2.location().getPath() + "/" + vk6.getPath() + ".json");
                    try (final Resource ace8 = acf.getResource(vk7);
                         final Reader reader10 = (Reader)new InputStreamReader(ace8.getInputStream(), StandardCharsets.UTF_8)) {
                        final JsonParser jsonParser12 = new JsonParser();
                        final JsonElement jsonElement13 = jsonParser12.parse(reader10);
                        return decoder.<JsonElement>parse(dynamicOps, jsonElement13).<Pair<E, OptionalInt>>map((java.util.function.Function<? super E, ? extends Pair<E, OptionalInt>>)(object -> Pair.<Object, OptionalInt>of(object, OptionalInt.empty())));
                    }
                    catch (IOException | JsonIOException | JsonSyntaxException ex2) {
                        final Exception ex;
                        final Exception exception8 = ex;
                        return DataResult.<Pair<E, OptionalInt>>error(new StringBuilder().append("Failed to parse ").append(vk7).append(" file: ").append(exception8.getMessage()).toString());
                    }
                }
                
                public String toString() {
                    return new StringBuilder().append("ResourceAccess[").append(acf).append("]").toString();
                }
            };
        }
        
        public static final class MemoryMap implements ResourceAccess {
            private final Map<ResourceKey<?>, JsonElement> data;
            private final Object2IntMap<ResourceKey<?>> ids;
            private final Map<ResourceKey<?>, Lifecycle> lifecycles;
            
            public MemoryMap() {
                this.data = Maps.newIdentityHashMap();
                this.ids = new Object2IntOpenCustomHashMap<ResourceKey<?>>(Util.<ResourceKey<?>>identityStrategy());
                this.lifecycles = Maps.newIdentityHashMap();
            }
            
            public <E> void add(final RegistryAccess.RegistryHolder b, final ResourceKey<E> vj, final Encoder<E> encoder, final int integer, final E object, final Lifecycle lifecycle) {
                final DataResult<JsonElement> dataResult8 = encoder.<JsonElement>encodeStart(RegistryWriteOps.create((DynamicOps<Object>)JsonOps.INSTANCE, b), object);
                final Optional<DataResult.PartialResult<JsonElement>> optional9 = dataResult8.error();
                if (optional9.isPresent()) {
                    RegistryReadOps.LOGGER.error("Error adding element: {}", ((DataResult.PartialResult)optional9.get()).message());
                    return;
                }
                this.data.put(vj, dataResult8.result().get());
                this.ids.put(vj, integer);
                this.lifecycles.put(vj, lifecycle);
            }
            
            public Collection<ResourceLocation> listResources(final ResourceKey<? extends Registry<?>> vj) {
                return (Collection<ResourceLocation>)this.data.keySet().stream().filter(vj2 -> vj2.isFor(vj)).map(vj2 -> new ResourceLocation(vj2.location().getNamespace(), vj.location().getPath() + "/" + vj2.location().getPath() + ".json")).collect(Collectors.toList());
            }
            
            public <E> DataResult<Pair<E, OptionalInt>> parseElement(final DynamicOps<JsonElement> dynamicOps, final ResourceKey<? extends Registry<E>> vj2, final ResourceKey<E> vj3, final Decoder<E> decoder) {
                final JsonElement jsonElement6 = (JsonElement)this.data.get(vj3);
                if (jsonElement6 == null) {
                    return DataResult.<Pair<E, OptionalInt>>error(new StringBuilder().append("Unknown element: ").append(vj3).toString());
                }
                return decoder.<JsonElement>parse(dynamicOps, jsonElement6).setLifecycle((Lifecycle)this.lifecycles.get(vj3)).<Pair<E, OptionalInt>>map((java.util.function.Function<? super E, ? extends Pair<E, OptionalInt>>)(object -> Pair.<Object, OptionalInt>of(object, OptionalInt.of(this.ids.getInt(vj3)))));
            }
        }
    }
}

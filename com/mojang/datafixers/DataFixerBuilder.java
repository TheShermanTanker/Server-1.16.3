package com.mojang.datafixers;

import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.types.Type;
import java.util.Iterator;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import java.util.concurrent.CompletableFuture;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import java.util.List;
import com.mojang.datafixers.schemas.Schema;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import org.apache.logging.log4j.Logger;

public class DataFixerBuilder {
    private static final Logger LOGGER;
    private final int dataVersion;
    private final Int2ObjectSortedMap<Schema> schemas;
    private final List<DataFix> globalList;
    private final IntSortedSet fixerVersions;
    
    public DataFixerBuilder(final int dataVersion) {
        this.schemas = new Int2ObjectAVLTreeMap<Schema>();
        this.globalList = Lists.newArrayList();
        this.fixerVersions = new IntAVLTreeSet();
        this.dataVersion = dataVersion;
    }
    
    public Schema addSchema(final int version, final BiFunction<Integer, Schema, Schema> factory) {
        return this.addSchema(version, 0, factory);
    }
    
    public Schema addSchema(final int version, final int subVersion, final BiFunction<Integer, Schema, Schema> factory) {
        final int key = DataFixUtils.makeKey(version, subVersion);
        final Schema parent = this.schemas.isEmpty() ? null : this.schemas.get(DataFixerUpper.getLowestSchemaSameVersion(this.schemas, key - 1));
        final Schema schema = (Schema)factory.apply(DataFixUtils.makeKey(version, subVersion), parent);
        this.addSchema(schema);
        return schema;
    }
    
    public void addSchema(final Schema schema) {
        this.schemas.put(schema.getVersionKey(), schema);
    }
    
    public void addFixer(final DataFix fix) {
        final int version = DataFixUtils.getVersion(fix.getVersionKey());
        if (version > this.dataVersion) {
            DataFixerBuilder.LOGGER.warn("Ignored fix registered for version: {} as the DataVersion of the game is: {}", version, this.dataVersion);
            return;
        }
        this.globalList.add(fix);
        this.fixerVersions.add(fix.getVersionKey());
    }
    
    public DataFixer build(final Executor executor) {
        final DataFixerUpper fixerUpper = new DataFixerUpper(new Int2ObjectAVLTreeMap<Schema>(this.schemas), (List<DataFix>)new ArrayList((Collection)this.globalList), new IntAVLTreeSet(this.fixerVersions));
        final IntBidirectionalIterator iterator = fixerUpper.fixerVersions().iterator();
        while (iterator.hasNext()) {
            final int versionKey = iterator.nextInt();
            final Schema schema = this.schemas.get(versionKey);
            for (final String typeName : schema.types()) {
                CompletableFuture.runAsync(() -> {
                    final Type<?> dataType = schema.getType(() -> typeName);
                    final TypeRewriteRule rule = fixerUpper.getRule(DataFixUtils.getVersion(versionKey), this.dataVersion);
                    dataType.rewrite(rule, DataFixerUpper.OPTIMIZATION_RULE);
                }, executor).exceptionally(e -> {
                    DataFixerBuilder.LOGGER.error("Unable to build datafixers", e);
                    Runtime.getRuntime().exit(1);
                    return null;
                });
            }
        }
        return fixerUpper;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

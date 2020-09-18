package com.mojang.datafixers;

import org.apache.logging.log4j.LogManager;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.mojang.serialization.DataResult;
import com.mojang.datafixers.types.Type;
import java.util.function.Consumer;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import java.util.List;
import com.mojang.datafixers.schemas.Schema;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import com.mojang.datafixers.functions.PointFreeRule;
import org.apache.logging.log4j.Logger;

public class DataFixerUpper implements DataFixer {
    public static boolean ERRORS_ARE_FATAL;
    private static final Logger LOGGER;
    protected static final PointFreeRule OPTIMIZATION_RULE;
    private final Int2ObjectSortedMap<Schema> schemas;
    private final List<DataFix> globalList;
    private final IntSortedSet fixerVersions;
    private final Long2ObjectMap<TypeRewriteRule> rules;
    
    protected DataFixerUpper(final Int2ObjectSortedMap<Schema> schemas, final List<DataFix> globalList, final IntSortedSet fixerVersions) {
        this.rules = Long2ObjectMaps.<TypeRewriteRule>synchronize(new Long2ObjectOpenHashMap<TypeRewriteRule>());
        this.schemas = schemas;
        this.globalList = globalList;
        this.fixerVersions = fixerVersions;
    }
    
    public <T> Dynamic<T> update(final DSL.TypeReference type, final Dynamic<T> input, final int version, final int newVersion) {
        if (version < newVersion) {
            final Type<?> dataType = this.getType(type, version);
            final DataResult<T> read = dataType.<T>readAndWrite(input.getOps(), this.getType(type, newVersion), this.getRule(version, newVersion), DataFixerUpper.OPTIMIZATION_RULE, input.getValue());
            final T result = (T)read.resultOrPartial((Consumer<String>)DataFixerUpper.LOGGER::error).orElse(input.getValue());
            return new Dynamic<T>(input.getOps(), result);
        }
        return input;
    }
    
    public Schema getSchema(final int key) {
        return this.schemas.get(getLowestSchemaSameVersion(this.schemas, key));
    }
    
    protected Type<?> getType(final DSL.TypeReference type, final int version) {
        return this.getSchema(DataFixUtils.makeKey(version)).getType(type);
    }
    
    protected static int getLowestSchemaSameVersion(final Int2ObjectSortedMap<Schema> schemas, final int versionKey) {
        if (versionKey < schemas.firstIntKey()) {
            return schemas.firstIntKey();
        }
        return schemas.subMap(0, versionKey + 1).lastIntKey();
    }
    
    private int getLowestFixSameVersion(final int versionKey) {
        if (versionKey < this.fixerVersions.firstInt()) {
            return this.fixerVersions.firstInt() - 1;
        }
        return this.fixerVersions.subSet(0, versionKey + 1).lastInt();
    }
    
    protected TypeRewriteRule getRule(final int version, final int dataVersion) {
        if (version >= dataVersion) {
            return TypeRewriteRule.nop();
        }
        final int expandedVersion = this.getLowestFixSameVersion(DataFixUtils.makeKey(version));
        final int expandedDataVersion = DataFixUtils.makeKey(dataVersion);
        final long key = (long)expandedVersion << 32 | (long)expandedDataVersion;
        return (TypeRewriteRule)this.rules.computeIfAbsent(key, k -> {
            final List<TypeRewriteRule> rules = Lists.newArrayList();
            for (final DataFix fix : this.globalList) {
                final int fixVersion = fix.getVersionKey();
                if (fixVersion > expandedVersion && fixVersion <= expandedDataVersion) {
                    final TypeRewriteRule fixRule = fix.getRule();
                    if (fixRule == TypeRewriteRule.nop()) {
                        continue;
                    }
                    rules.add(fixRule);
                }
            }
            return TypeRewriteRule.seq(rules);
        });
    }
    
    protected IntSortedSet fixerVersions() {
        return this.fixerVersions;
    }
    
    static {
        DataFixerUpper.ERRORS_ARE_FATAL = false;
        LOGGER = LogManager.getLogger();
        OPTIMIZATION_RULE = DataFixUtils.<PointFreeRule>make((java.util.function.Supplier<PointFreeRule>)(() -> {
            final PointFreeRule opSimple = PointFreeRule.orElse(PointFreeRule.orElse(PointFreeRule.CataFuseSame.INSTANCE, PointFreeRule.orElse(PointFreeRule.CataFuseDifferent.INSTANCE, PointFreeRule.LensAppId.INSTANCE)), PointFreeRule.orElse(PointFreeRule.LensComp.INSTANCE, PointFreeRule.orElse(PointFreeRule.AppNest.INSTANCE, PointFreeRule.LensCompFunc.INSTANCE)));
            final PointFreeRule opLeft = PointFreeRule.many(PointFreeRule.once(PointFreeRule.orElse(opSimple, PointFreeRule.CompAssocLeft.INSTANCE)));
            final PointFreeRule opComp = PointFreeRule.many(PointFreeRule.once(PointFreeRule.orElse(PointFreeRule.SortInj.INSTANCE, PointFreeRule.SortProj.INSTANCE)));
            final PointFreeRule opRight = PointFreeRule.many(PointFreeRule.once(PointFreeRule.orElse(opSimple, PointFreeRule.CompAssocRight.INSTANCE)));
            return PointFreeRule.seq(ImmutableList.of(() -> opLeft, () -> opComp, () -> opRight, () -> opLeft, (() -> opRight)));
        }));
    }
}

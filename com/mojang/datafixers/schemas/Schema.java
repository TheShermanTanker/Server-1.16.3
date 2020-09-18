package com.mojang.datafixers.schemas;

import java.util.function.Function;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.templates.RecursivePoint;
import java.util.Set;
import java.util.Iterator;
import com.mojang.datafixers.types.families.TypeFamily;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.List;
import com.mojang.datafixers.types.families.RecursiveTypeFamily;
import com.mojang.datafixers.DSL;
import com.google.common.collect.Lists;
import com.mojang.datafixers.DataFixUtils;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

public class Schema {
    protected final Object2IntMap<String> RECURSIVE_TYPES;
    private final Map<String, Supplier<TypeTemplate>> TYPE_TEMPLATES;
    private final Map<String, Type<?>> TYPES;
    private final int versionKey;
    private final String name;
    protected final Schema parent;
    
    public Schema(final int versionKey, final Schema parent) {
        this.RECURSIVE_TYPES = new Object2IntOpenHashMap<String>();
        this.TYPE_TEMPLATES = Maps.newHashMap();
        this.versionKey = versionKey;
        final int subVersion = DataFixUtils.getSubVersion(versionKey);
        this.name = new StringBuilder().append("V").append(DataFixUtils.getVersion(versionKey)).append((subVersion == 0) ? "" : new StringBuilder().append(".").append(subVersion).toString()).toString();
        this.parent = parent;
        this.registerTypes(this, this.registerEntities(this), this.registerBlockEntities(this));
        this.TYPES = this.buildTypes();
    }
    
    protected Map<String, Type<?>> buildTypes() {
        final Map<String, Type<?>> types = Maps.newHashMap();
        final List<TypeTemplate> templates = Lists.newArrayList();
        for (final Object2IntMap.Entry<String> entry : this.RECURSIVE_TYPES.object2IntEntrySet()) {
            templates.add(DSL.check((String)entry.getKey(), entry.getIntValue(), this.getTemplate((String)entry.getKey())));
        }
        final TypeTemplate choice = (TypeTemplate)templates.stream().reduce(DSL::or).get();
        final TypeFamily family = new RecursiveTypeFamily(this.name, choice);
        for (final String name : this.TYPE_TEMPLATES.keySet()) {
            final int recurseId = (int)this.RECURSIVE_TYPES.getOrDefault(name, (-1));
            Type<?> type;
            if (recurseId != -1) {
                type = family.apply(recurseId);
            }
            else {
                type = this.getTemplate(name).apply(family).apply(-1);
            }
            types.put(name, type);
        }
        return types;
    }
    
    public Set<String> types() {
        return (Set<String>)this.TYPES.keySet();
    }
    
    public Type<?> getTypeRaw(final DSL.TypeReference type) {
        final String name = type.typeName();
        return this.TYPES.computeIfAbsent(name, key -> {
            throw new IllegalArgumentException("Unknown type: " + name);
        });
    }
    
    public Type<?> getType(final DSL.TypeReference type) {
        final String name = type.typeName();
        final Type<?> type2 = this.TYPES.computeIfAbsent(name, key -> {
            throw new IllegalArgumentException("Unknown type: " + name);
        });
        if (type2 instanceof RecursivePoint.RecursivePointType) {
            return type2.findCheckedType(-1).orElseThrow(() -> new IllegalStateException("Could not find choice type in the recursive type"));
        }
        return type2;
    }
    
    public TypeTemplate resolveTemplate(final String name) {
        return (TypeTemplate)((Supplier)this.TYPE_TEMPLATES.getOrDefault(name, (() -> {
            throw new IllegalArgumentException("Unknown type: " + name);
        }))).get();
    }
    
    public TypeTemplate id(final String name) {
        final int id = (int)this.RECURSIVE_TYPES.getOrDefault(name, (-1));
        if (id != -1) {
            return DSL.id(id);
        }
        return this.getTemplate(name);
    }
    
    protected TypeTemplate getTemplate(final String name) {
        return DSL.named(name, this.resolveTemplate(name));
    }
    
    public Type<?> getChoiceType(final DSL.TypeReference type, final String choiceName) {
        final TaggedChoice.TaggedChoiceType<?> choiceType = this.findChoiceType(type);
        if (!choiceType.types().containsKey(choiceName)) {
            throw new IllegalArgumentException("Data fixer not registered for: " + choiceName + " in " + type.typeName());
        }
        return choiceType.types().get(choiceName);
    }
    
    public TaggedChoice.TaggedChoiceType<?> findChoiceType(final DSL.TypeReference type) {
        return this.getType(type).findChoiceType("id", -1).orElseThrow(() -> new IllegalArgumentException("Not a choice type"));
    }
    
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> entityTypes, final Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        this.parent.registerTypes(schema, entityTypes, blockEntityTypes);
    }
    
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        return this.parent.registerEntities(schema);
    }
    
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        return this.parent.registerBlockEntities(schema);
    }
    
    public void registerSimple(final Map<String, Supplier<TypeTemplate>> map, final String name) {
        this.register(map, name, (Supplier<TypeTemplate>)DSL::remainder);
    }
    
    public void register(final Map<String, Supplier<TypeTemplate>> map, final String name, final Function<String, TypeTemplate> template) {
        this.register(map, name, (Supplier<TypeTemplate>)(() -> (TypeTemplate)template.apply(name)));
    }
    
    public void register(final Map<String, Supplier<TypeTemplate>> map, final String name, final Supplier<TypeTemplate> template) {
        map.put(name, template);
    }
    
    public void registerType(final boolean recursive, final DSL.TypeReference type, final Supplier<TypeTemplate> template) {
        this.TYPE_TEMPLATES.put(type.typeName(), template);
        if (recursive && !this.RECURSIVE_TYPES.containsKey(type.typeName())) {
            this.RECURSIVE_TYPES.put(type.typeName(), this.RECURSIVE_TYPES.size());
        }
    }
    
    public int getVersionKey() {
        return this.versionKey;
    }
    
    public Schema getParent() {
        return this.parent;
    }
}

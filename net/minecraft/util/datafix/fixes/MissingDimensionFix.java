package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.templates.CompoundList;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.FieldFinder;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.types.templates.TaggedChoice;
import java.util.Map;
import java.util.List;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.util.Unit;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class MissingDimensionFix extends DataFix {
    public MissingDimensionFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    private static <A> Type<Pair<A, Dynamic<?>>> fields(final String string, final Type<A> type) {
        return DSL.<A, Dynamic<?>>and(DSL.<A>field(string, type), DSL.remainderType());
    }
    
    private static <A> Type<Pair<Either<A, Unit>, Dynamic<?>>> optionalFields(final String string, final Type<A> type) {
        return DSL.<Either<A, Unit>, Dynamic<?>>and(DSL.<A>optional(DSL.<A>field(string, type)), DSL.remainderType());
    }
    
    private static <A1, A2> Type<Pair<Either<A1, Unit>, Pair<Either<A2, Unit>, Dynamic<?>>>> optionalFields(final String string1, final Type<A1> type2, final String string3, final Type<A2> type4) {
        return DSL.<Either<A1, Unit>, Either<A2, Unit>, Dynamic<?>>and(DSL.<A1>optional(DSL.<A1>field(string1, type2)), DSL.<A2>optional(DSL.<A2>field(string3, type4)), DSL.remainderType());
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Schema schema2 = this.getInputSchema();
        final TaggedChoice.TaggedChoiceType<String> taggedChoiceType3 = new TaggedChoice.TaggedChoiceType<String>("type", DSL.string(), (java.util.Map<String, Type<?>>)ImmutableMap.<String, Type<Dynamic<?>>>of("minecraft:debug", DSL.remainderType(), "minecraft:flat", MissingDimensionFix.optionalFields("settings", MissingDimensionFix.optionalFields("biome", schema2.getType(References.BIOME), "layers", DSL.list(MissingDimensionFix.optionalFields("block", schema2.getType(References.BLOCK_NAME))))), "minecraft:noise", MissingDimensionFix.optionalFields("biome_source", DSL.<String>taggedChoiceType("type", DSL.string(), (java.util.Map<String, ? extends Type<?>>)ImmutableMap.<String, Type<List<A>>>of("minecraft:fixed", MissingDimensionFix.fields("biome", schema2.getType(References.BIOME)), "minecraft:multi_noise", DSL.list(MissingDimensionFix.fields("biome", schema2.getType(References.BIOME))), "minecraft:checkerboard", MissingDimensionFix.fields("biomes", DSL.list(schema2.getType(References.BIOME))), "minecraft:vanilla_layered", (Type<List<A>>)DSL.remainderType(), "minecraft:the_end", (Type<List<A>>)DSL.remainderType())), "settings", DSL.<String, Pair<Either<?, Unit>, Pair<Either<?, Unit>, Dynamic<?>>>>or(DSL.string(), MissingDimensionFix.optionalFields("default_block", schema2.getType(References.BLOCK_NAME), "default_fluid", schema2.getType(References.BLOCK_NAME))))));
        final CompoundList.CompoundListType<String, ?> compoundListType4 = DSL.compoundList(NamespacedSchema.namespacedString(), MissingDimensionFix.<String>fields("generator", (Type<String>)taggedChoiceType3));
        final Type<?> type5 = DSL.<Object, Dynamic<?>>and((Type<Object>)compoundListType4, DSL.remainderType());
        final Type<?> type6 = schema2.getType(References.WORLD_GEN_SETTINGS);
        final FieldFinder<?> fieldFinder7 = new FieldFinder<>("dimensions", type5);
        if (!type6.findFieldType("dimensions").equals(type5)) {
            throw new IllegalStateException();
        }
        final OpticFinder<? extends List<? extends Pair<String, ?>>> opticFinder8 = compoundListType4.finder();
        return this.fixTypeEverywhereTyped("MissingDimensionFix", type6, (Function<Typed<?>, Typed<?>>)(typed -> typed.updateTyped(fieldFinder7, typed4 -> typed4.updateTyped(opticFinder8, typed3 -> {
            if (!(typed3.getValue() instanceof List)) {
                throw new IllegalStateException("List exptected");
            }
            if (typed3.getValue().isEmpty()) {
                final Dynamic<?> dynamic5 = typed.<Dynamic<?>>get(DSL.remainderFinder());
                final Dynamic<?> dynamic6 = this.recreateSettings(dynamic5);
                return DataFixUtils.<Typed>orElse((java.util.Optional<? extends Typed>)compoundListType4.readTyped(dynamic6).result().map(Pair::getFirst), typed3);
            }
            return typed3;
        }))));
    }
    
    private <T> Dynamic<T> recreateSettings(final Dynamic<T> dynamic) {
        final long long3 = dynamic.get("seed").asLong(0L);
        return new Dynamic<T>(dynamic.getOps(), WorldGenSettingsFix.<T>vanillaLevels(dynamic, long3, WorldGenSettingsFix.defaultOverworld((Dynamic<T>)dynamic, long3), false));
    }
}

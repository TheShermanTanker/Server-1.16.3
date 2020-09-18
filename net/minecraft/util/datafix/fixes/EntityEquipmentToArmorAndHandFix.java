package net.minecraft.util.datafix.fixes;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import com.google.common.collect.Lists;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Unit;
import java.util.List;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class EntityEquipmentToArmorAndHandFix extends DataFix {
    public EntityEquipmentToArmorAndHandFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        return this.cap(this.getInputSchema().getTypeRaw(References.ITEM_STACK));
    }
    
    private <IS> TypeRewriteRule cap(final Type<IS> type) {
        final Type<Pair<Either<List<IS>, Unit>, Dynamic<?>>> type2 = DSL.<Either<List<IS>, Unit>, Dynamic<?>>and(DSL.optional(DSL.field("Equipment", DSL.<IS>list(type))), DSL.remainderType());
        final Type<Pair<Either<List<IS>, Unit>, Pair<Either<List<IS>, Unit>, Dynamic<?>>>> type3 = DSL.<Either<List<IS>, Unit>, Either<List<IS>, Unit>, Dynamic<?>>and(DSL.optional(DSL.field("ArmorItems", DSL.<IS>list(type))), DSL.optional(DSL.field("HandItems", DSL.<IS>list(type))), DSL.remainderType());
        final OpticFinder<Pair<Either<List<IS>, Unit>, Dynamic<?>>> opticFinder5 = DSL.<Pair<Either<List<IS>, Unit>, Dynamic<?>>>typeFinder(type2);
        final OpticFinder<List<IS>> opticFinder6 = DSL.<List<IS>>fieldFinder("Equipment", DSL.list(type));
        return this.fixTypeEverywhereTyped("EntityEquipmentToArmorAndHandFix", this.getInputSchema().getType(References.ENTITY), this.getOutputSchema().getType(References.ENTITY), (Function<Typed<?>, Typed<?>>)(typed -> {
            Either<List<Object>, Unit> either6 = Either.<List<Object>, Unit>right(DSL.unit());
            Either<List<Object>, Unit> either7 = Either.<List<Object>, Unit>right(DSL.unit());
            Dynamic<?> dynamic8 = typed.<Dynamic<?>>getOrCreate(DSL.remainderFinder());
            final Optional<List<Object>> optional9 = typed.<List<Object>>getOptional(opticFinder6);
            if (optional9.isPresent()) {
                final List<Object> list10 = (List<Object>)optional9.get();
                final Object object11 = ((Pair)type.read(dynamic8.emptyMap()).result().orElseThrow(() -> new IllegalStateException("Could not parse newly created empty itemstack."))).getFirst();
                if (!list10.isEmpty()) {
                    either6 = Either.<List<Object>, Unit>left(Lists.newArrayList(list10.get(0), object11));
                }
                if (list10.size() > 1) {
                    final List<Object> list11 = Lists.newArrayList(object11, object11, object11, object11);
                    for (int integer13 = 1; integer13 < Math.min(list10.size(), 5); ++integer13) {
                        list11.set(integer13 - 1, list10.get(integer13));
                    }
                    either7 = Either.<List<Object>, Unit>left(list11);
                }
            }
            final Dynamic<?> dynamic9 = dynamic8;
            final Optional<? extends Stream<? extends Dynamic<?>>> optional10 = dynamic8.get("DropChances").asStreamOpt().result();
            if (optional10.isPresent()) {
                final Iterator<? extends Dynamic<?>> iterator12 = Stream.concat((Stream)optional10.get(), Stream.generate(() -> dynamic9.createInt(0))).iterator();
                final float float13 = ((Dynamic)iterator12.next()).asFloat(0.0f);
                if (!dynamic8.get("HandDropChances").result().isPresent()) {
                    final Dynamic<?> dynamic10 = dynamic8.createList(Stream.of((Object[])new Float[] { float13, 0.0f }).map(dynamic8::createFloat));
                    dynamic8 = dynamic8.set("HandDropChances", dynamic10);
                }
                if (!dynamic8.get("ArmorDropChances").result().isPresent()) {
                    final Dynamic<?> dynamic10 = dynamic8.createList(Stream.of((Object[])new Float[] { ((Dynamic)iterator12.next()).asFloat(0.0f), ((Dynamic)iterator12.next()).asFloat(0.0f), ((Dynamic)iterator12.next()).asFloat(0.0f), ((Dynamic)iterator12.next()).asFloat(0.0f) }).map(dynamic8::createFloat));
                    dynamic8 = dynamic8.set("ArmorDropChances", dynamic10);
                }
                dynamic8 = dynamic8.remove("DropChances");
            }
            return typed.<Object, Pair<Either<List<Object>, Unit>, Pair<Either<List<Object>, Unit>, Dynamic<?>>>>set(opticFinder5, type3, Pair.<Either<List<Object>, Unit>, Pair<Either<List<Object>, Unit>, Dynamic<?>>>of(either6, Pair.<Either<List<Object>, Unit>, Dynamic<?>>of(either7, dynamic8)));
        }));
    }
}

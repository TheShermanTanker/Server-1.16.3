package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.DataFixUtils;
import java.util.Optional;
import com.mojang.datafixers.OpticFinder;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import java.util.Objects;
import java.util.List;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class EntityRidingToPassengersFix extends DataFix {
    public EntityRidingToPassengersFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Schema schema2 = this.getInputSchema();
        final Schema schema3 = this.getOutputSchema();
        final Type<?> type4 = schema2.getTypeRaw(References.ENTITY_TREE);
        final Type<?> type5 = schema3.getTypeRaw(References.ENTITY_TREE);
        final Type<?> type6 = schema2.getTypeRaw(References.ENTITY);
        return this.cap(schema2, schema3, type4, type5, type6);
    }
    
    private <OldEntityTree, NewEntityTree, Entity> TypeRewriteRule cap(final Schema schema1, final Schema schema2, final Type<OldEntityTree> type3, final Type<NewEntityTree> type4, final Type<Entity> type5) {
        final Type<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>> type6 = DSL.<Pair<Either<OldEntityTree, Unit>, Entity>>named(References.ENTITY_TREE.typeName(), DSL.<Either<OldEntityTree, Unit>, Entity>and(DSL.<OldEntityTree>optional(DSL.<OldEntityTree>field("Riding", type3)), type5));
        final Type<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> type7 = DSL.<Pair<Either<List<NewEntityTree>, Unit>, Entity>>named(References.ENTITY_TREE.typeName(), DSL.<Either<List<NewEntityTree>, Unit>, Entity>and(DSL.optional(DSL.field("Passengers", DSL.<NewEntityTree>list(type4))), type5));
        final Type<?> type8 = schema1.getType(References.ENTITY_TREE);
        final Type<?> type9 = schema2.getType(References.ENTITY_TREE);
        if (!Objects.equals(type8, type6)) {
            throw new IllegalStateException("Old entity type is not what was expected.");
        }
        if (!type9.equals(type7, true, true)) {
            throw new IllegalStateException("New entity type is not what was expected.");
        }
        final OpticFinder<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>> opticFinder11 = DSL.<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>>typeFinder(type6);
        final OpticFinder<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> opticFinder12 = DSL.<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>>typeFinder(type7);
        final OpticFinder<NewEntityTree> opticFinder13 = DSL.<NewEntityTree>typeFinder(type4);
        final Type<?> type10 = schema1.getType(References.PLAYER);
        final Type<?> type11 = schema2.getType(References.PLAYER);
        return TypeRewriteRule.seq(this.<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>, Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>>fixTypeEverywhere("EntityRidingToPassengerFix", type6, type7, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>, Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>>>)(dynamicOps -> pair -> {
            Optional<Pair<String, Pair<Either<List<Object>, Unit>, Object>>> optional8 = (Optional<Pair<String, Pair<Either<List<Object>, Unit>, Object>>>)Optional.empty();
            Pair<String, Pair<Either<Object, Unit>, Object>> pair2 = (Pair<String, Pair<Either<Object, Unit>, Object>>)pair;
            while (true) {
                final Either<List<Object>, Unit> either10 = DataFixUtils.<Either<List<Object>, Unit>>orElse((java.util.Optional<? extends Either<List<Object>, Unit>>)optional8.map(pair -> {
                    final Typed<Object> typed6 = (Typed<Object>)type4.pointTyped(dynamicOps).orElseThrow(() -> new IllegalStateException("Could not create new entity tree"));
                    final Object object7 = typed6.<Pair>set(opticFinder12, pair).getOptional((OpticFinder<Object>)opticFinder13).orElseThrow(() -> new IllegalStateException("Should always have an entity tree here"));
                    return Either.<ImmutableList<Object>, Object>left(ImmutableList.of(object7));
                }), Either.<List<Object>, Unit>right(DSL.unit()));
                optional8 = (Optional<Pair<String, Pair<Either<List<Object>, Unit>, Object>>>)Optional.of(Pair.<String, Pair<Either<List<Object>, Unit>, Object>>of(References.ENTITY_TREE.typeName(), Pair.<Either<List<Object>, Unit>, Object>of(either10, pair2.getSecond().getSecond())));
                final Optional<Object> optional9 = pair2.getSecond().getFirst().left();
                if (!optional9.isPresent()) {
                    break;
                }
                pair2 = (Pair<String, Pair<Either<Object, Unit>, Object>>)new Typed<>(type3, dynamicOps, optional9.get()).getOptional((OpticFinder<Object>)opticFinder11).orElseThrow(() -> new IllegalStateException("Should always have an entity here"));
            }
            return (Pair)optional8.orElseThrow(() -> new IllegalStateException("Should always have an entity tree here"));
        })), this.writeAndRead("player RootVehicle injecter", type10, type11));
    }
}

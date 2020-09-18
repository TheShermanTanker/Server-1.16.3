package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.OpticFinder;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class EntityHorseSaddleFix extends NamedEntityFix {
    public EntityHorseSaddleFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "EntityHorseSaddleFix", References.ENTITY, "EntityHorse");
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        final OpticFinder<Pair<String, String>> opticFinder3 = DSL.<Pair<String, String>>fieldFinder("id", DSL.<String>named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        final Type<?> type4 = this.getInputSchema().getTypeRaw(References.ITEM_STACK);
        final OpticFinder<?> opticFinder4 = DSL.fieldFinder("SaddleItem", type4);
        final Optional<? extends Typed<?>> optional6 = typed.getOptionalTyped(opticFinder4);
        final Dynamic<?> dynamic7 = typed.<Dynamic<?>>get(DSL.remainderFinder());
        if (!optional6.isPresent() && dynamic7.get("Saddle").asBoolean(false)) {
            Typed<?> typed2 = type4.pointTyped(typed.getOps()).orElseThrow(IllegalStateException::new);
            typed2 = typed2.<Pair<String, String>>set(opticFinder3, Pair.<String, String>of(References.ITEM_NAME.typeName(), "minecraft:saddle"));
            Dynamic<?> dynamic8 = dynamic7.emptyMap();
            dynamic8 = dynamic8.set("Count", dynamic8.createByte((byte)1));
            dynamic8 = dynamic8.set("Damage", dynamic8.createShort((short)0));
            typed2 = typed2.<Dynamic<?>>set(DSL.remainderFinder(), dynamic8);
            dynamic7.remove("Saddle");
            return typed.set(opticFinder4, typed2).<Dynamic<?>>set(DSL.remainderFinder(), dynamic7);
        }
        return typed;
    }
}

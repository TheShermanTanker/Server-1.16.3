package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ItemWaterPotionFix extends DataFix {
    public ItemWaterPotionFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.ITEM_STACK);
        final OpticFinder<Pair<String, String>> opticFinder3 = DSL.<Pair<String, String>>fieldFinder("id", DSL.<String>named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        final OpticFinder<?> opticFinder4 = type2.findField("tag");
        return this.fixTypeEverywhereTyped("ItemWaterPotionFix", type2, (Function<Typed<?>, Typed<?>>)(typed -> {
            final Optional<Pair<String, String>> optional4 = typed.<Pair<String, String>>getOptional(opticFinder3);
            if (optional4.isPresent()) {
                final String string5 = ((Pair)optional4.get()).getSecond();
                if ("minecraft:potion".equals(string5) || "minecraft:splash_potion".equals(string5) || "minecraft:lingering_potion".equals(string5) || "minecraft:tipped_arrow".equals(string5)) {
                    final Typed<?> typed2 = typed.getOrCreateTyped(opticFinder4);
                    Dynamic<?> dynamic7 = typed2.<Dynamic<?>>get(DSL.remainderFinder());
                    if (!dynamic7.get("Potion").asString().result().isPresent()) {
                        dynamic7 = dynamic7.set("Potion", dynamic7.createString("minecraft:water"));
                    }
                    return typed.set(opticFinder4, typed2.<Dynamic<?>>set(DSL.remainderFinder(), dynamic7));
                }
            }
            return typed;
        }));
    }
}

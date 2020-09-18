package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class SwimStatsRenameFix extends DataFix {
    public SwimStatsRenameFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getOutputSchema().getType(References.STATS);
        final Type<?> type3 = this.getInputSchema().getType(References.STATS);
        final OpticFinder<?> opticFinder4 = type3.findField("stats");
        final OpticFinder<?> opticFinder5 = opticFinder4.type().findField("minecraft:custom");
        final OpticFinder<String> opticFinder6 = NamespacedSchema.namespacedString().finder();
        return this.fixTypeEverywhereTyped("SwimStatsRenameFix", type3, type2, (Function<Typed<?>, Typed<?>>)(typed -> typed.updateTyped(opticFinder4, typed -> typed.updateTyped(opticFinder5, typed -> typed.update(opticFinder6, string -> {
            if (string.equals("minecraft:swim_one_cm")) {
                return "minecraft:walk_on_water_one_cm";
            }
            if (string.equals("minecraft:dive_one_cm")) {
                return "minecraft:walk_under_water_one_cm";
            }
            return string;
        })))));
    }
}

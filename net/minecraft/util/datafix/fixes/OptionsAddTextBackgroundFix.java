package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class OptionsAddTextBackgroundFix extends DataFix {
    public OptionsAddTextBackgroundFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("OptionsAddTextBackgroundFix", this.getInputSchema().getType(References.OPTIONS), (Function<Typed<?>, Typed<?>>)(typed -> typed.<Dynamic<?>>update(DSL.remainderFinder(), dynamic -> DataFixUtils.<Dynamic>orElse(dynamic.get("chatOpacity").asString().map(string -> dynamic.set("textBackgroundOpacity", (Dynamic)dynamic.createDouble(this.calculateBackground(string)))).result(), dynamic))));
    }
    
    private double calculateBackground(final String string) {
        try {
            final double double3 = 0.9 * Double.parseDouble(string) + 0.1;
            return double3 / 2.0;
        }
        catch (NumberFormatException numberFormatException3) {
            return 0.5;
        }
    }
}

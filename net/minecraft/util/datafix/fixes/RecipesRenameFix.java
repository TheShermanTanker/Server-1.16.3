package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.function.Function;
import com.mojang.datafixers.DataFix;

public class RecipesRenameFix extends DataFix {
    private final String name;
    private final Function<String, String> renamer;
    
    public RecipesRenameFix(final Schema schema, final boolean boolean2, final String string, final Function<String, String> function) {
        super(schema, boolean2);
        this.name = string;
        this.renamer = function;
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, String>> type2 = DSL.<String>named(References.RECIPE.typeName(), NamespacedSchema.namespacedString());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.RECIPE))) {
            throw new IllegalStateException("Recipe type is not what was expected.");
        }
        return this.<Pair<String, String>>fixTypeEverywhere(this.name, type2, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Pair<String, String>, Pair<String, String>>>)(dynamicOps -> pair -> pair.mapSecond(this.renamer)));
    }
}

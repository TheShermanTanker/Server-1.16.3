package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public abstract class ItemRenameFix extends DataFix {
    private final String name;
    
    public ItemRenameFix(final Schema schema, final String string) {
        super(schema, false);
        this.name = string;
    }
    
    public TypeRewriteRule makeRule() {
        final Type<Pair<String, String>> type2 = DSL.<String>named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString());
        if (!Objects.equals(this.getInputSchema().getType(References.ITEM_NAME), type2)) {
            throw new IllegalStateException("item name type is not what was expected.");
        }
        return this.<Pair<String, String>>fixTypeEverywhere(this.name, type2, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Pair<String, String>, Pair<String, String>>>)(dynamicOps -> pair -> pair.mapSecond(this::fixItem)));
    }
    
    protected abstract String fixItem(final String string);
    
    public static DataFix create(final Schema schema, final String string, final Function<String, String> function) {
        return new ItemRenameFix(schema, string) {
            @Override
            protected String fixItem(final String string) {
                return (String)function.apply(string);
            }
        };
    }
}

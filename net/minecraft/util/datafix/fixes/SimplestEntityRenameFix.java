package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import com.mojang.datafixers.types.Type;
import java.util.Objects;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public abstract class SimplestEntityRenameFix extends DataFix {
    private final String name;
    
    public SimplestEntityRenameFix(final String string, final Schema schema, final boolean boolean3) {
        super(schema, boolean3);
        this.name = string;
    }
    
    public TypeRewriteRule makeRule() {
        final TaggedChoice.TaggedChoiceType<String> taggedChoiceType2 = (TaggedChoice.TaggedChoiceType<String>)this.getInputSchema().findChoiceType(References.ENTITY);
        final TaggedChoice.TaggedChoiceType<String> taggedChoiceType3 = (TaggedChoice.TaggedChoiceType<String>)this.getOutputSchema().findChoiceType(References.ENTITY);
        final Type<Pair<String, String>> type4 = DSL.<String>named(References.ENTITY_NAME.typeName(), NamespacedSchema.namespacedString());
        if (!Objects.equals(this.getOutputSchema().getType(References.ENTITY_NAME), type4)) {
            throw new IllegalStateException("Entity name type is not what was expected.");
        }
        return TypeRewriteRule.seq(this.<String, String>fixTypeEverywhere(this.name, (Type<String>)taggedChoiceType2, (Type<String>)taggedChoiceType3, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<String, String>>)(dynamicOps -> pair -> pair.mapFirst(string -> {
            final String string2 = this.rename(string);
            final Type<?> type6 = taggedChoiceType2.types().get(string);
            final Type<?> type7 = taggedChoiceType3.types().get(string2);
            if (!type7.equals(type6, true, true)) {
                throw new IllegalStateException(String.format("Dynamic type check failed: %s not equal to %s", new Object[] { type7, type6 }));
            }
            return string2;
        }))), this.<Pair<String, String>>fixTypeEverywhere(this.name + " for entity name", type4, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Pair<String, String>, Pair<String, String>>>)(dynamicOps -> pair -> pair.mapSecond(this::rename))));
    }
    
    protected abstract String rename(final String string);
}

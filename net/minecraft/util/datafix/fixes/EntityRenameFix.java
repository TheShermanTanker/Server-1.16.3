package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public abstract class EntityRenameFix extends DataFix {
    protected final String name;
    
    public EntityRenameFix(final String string, final Schema schema, final boolean boolean3) {
        super(schema, boolean3);
        this.name = string;
    }
    
    public TypeRewriteRule makeRule() {
        final TaggedChoice.TaggedChoiceType<String> taggedChoiceType2 = (TaggedChoice.TaggedChoiceType<String>)this.getInputSchema().findChoiceType(References.ENTITY);
        final TaggedChoice.TaggedChoiceType<String> taggedChoiceType3 = (TaggedChoice.TaggedChoiceType<String>)this.getOutputSchema().findChoiceType(References.ENTITY);
        return this.<String, String>fixTypeEverywhere(this.name, (Type<String>)taggedChoiceType2, (Type<String>)taggedChoiceType3, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<String, String>>)(dynamicOps -> pair -> {
            final String string6 = pair.getFirst();
            final Type<?> type7 = taggedChoiceType2.types().get(string6);
            final Pair<String, Typed<?>> pair2 = this.fix(string6, this.getEntity(pair.getSecond(), dynamicOps, type7));
            final Type<?> type8 = taggedChoiceType3.types().get(pair2.getFirst());
            if (!type8.equals(pair2.getSecond().getType(), true, true)) {
                throw new IllegalStateException(String.format("Dynamic type check failed: %s not equal to %s", new Object[] { type8, pair2.getSecond().getType() }));
            }
            return Pair.of(pair2.getFirst(), pair2.getSecond().getValue());
        }));
    }
    
    private <A> Typed<A> getEntity(final Object object, final DynamicOps<?> dynamicOps, final Type<A> type) {
        return new Typed<A>(type, dynamicOps, (A)object);
    }
    
    protected abstract Pair<String, Typed<?>> fix(final String string, final Typed<?> typed);
}

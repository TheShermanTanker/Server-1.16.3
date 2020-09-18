package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.DataResult;
import java.util.function.Function;
import com.mojang.datafixers.types.Type;
import java.util.Objects;
import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class EntityHorseSplitFix extends EntityRenameFix {
    public EntityHorseSplitFix(final Schema schema, final boolean boolean2) {
        super("EntityHorseSplitFix", schema, boolean2);
    }
    
    @Override
    protected Pair<String, Typed<?>> fix(final String string, final Typed<?> typed) {
        final Dynamic<?> dynamic4 = typed.<Dynamic<?>>get(DSL.remainderFinder());
        if (Objects.equals("EntityHorse", string)) {
            final int integer6 = dynamic4.get("Type").asInt(0);
            String string2 = null;
            switch (integer6) {
                default: {
                    string2 = "Horse";
                    break;
                }
                case 1: {
                    string2 = "Donkey";
                    break;
                }
                case 2: {
                    string2 = "Mule";
                    break;
                }
                case 3: {
                    string2 = "ZombieHorse";
                    break;
                }
                case 4: {
                    string2 = "SkeletonHorse";
                    break;
                }
            }
            dynamic4.remove("Type");
            final Type<?> type7 = this.getOutputSchema().findChoiceType(References.ENTITY).types().get(string2);
            return Pair.<String, Typed<?>>of(string2, ((Pair)typed.write().flatMap((java.util.function.Function<? super Dynamic<?>, ? extends DataResult<Object>>)type7::readTyped).result().orElseThrow(() -> new IllegalStateException("Could not parse the new horse"))).getFirst());
        }
        return Pair.<String, Typed<?>>of(string, typed);
    }
}

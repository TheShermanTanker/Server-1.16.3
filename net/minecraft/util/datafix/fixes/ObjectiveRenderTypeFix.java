package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ObjectiveRenderTypeFix extends DataFix {
    public ObjectiveRenderTypeFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    private static ObjectiveCriteria.RenderType getRenderType(final String string) {
        return string.equals("health") ? ObjectiveCriteria.RenderType.HEARTS : ObjectiveCriteria.RenderType.INTEGER;
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, Dynamic<?>>> type2 = DSL.<Dynamic<?>>named(References.OBJECTIVE.typeName(), DSL.remainderType());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.OBJECTIVE))) {
            throw new IllegalStateException("Objective type is not what was expected.");
        }
        return this.<Pair<String, Dynamic<?>>>fixTypeEverywhere("ObjectiveRenderTypeFix", type2, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Pair<String, Dynamic<?>>, Pair<String, Dynamic<?>>>>)(dynamicOps -> pair -> pair.mapSecond(dynamic -> {
            final Optional<String> optional2 = (Optional<String>)dynamic.get("RenderType").asString().result();
            if (!optional2.isPresent()) {
                final String string3 = dynamic.get("CriteriaName").asString("");
                final ObjectiveCriteria.RenderType a4 = getRenderType(string3);
                return dynamic.set("RenderType", dynamic.createString(a4.getId()));
            }
            return dynamic;
        })));
    }
}

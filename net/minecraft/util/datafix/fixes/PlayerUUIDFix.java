package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.OpticFinder;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

public class PlayerUUIDFix extends AbstractUUIDFix {
    public PlayerUUIDFix(final Schema schema) {
        super(schema, References.PLAYER);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("PlayerUUIDFix", this.getInputSchema().getType(this.typeReference), (Function<Typed<?>, Typed<?>>)(typed -> {
            final OpticFinder<?> opticFinder2 = typed.getType().findField("RootVehicle");
            return typed.updateTyped(opticFinder2, opticFinder2.type(), typed -> typed.<Dynamic<?>>update(DSL.remainderFinder(), dynamic -> (Dynamic)AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "Attach", "Attach").orElse(dynamic))).<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)(dynamic -> EntityUUIDFix.updateEntityUUID(EntityUUIDFix.updateLivingEntity(dynamic))));
        }));
    }
}

package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class EntityCustomNameToComponentFix extends DataFix {
    public EntityCustomNameToComponentFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final OpticFinder<String> opticFinder2 = DSL.<String>fieldFinder("id", NamespacedSchema.namespacedString());
        return this.fixTypeEverywhereTyped("EntityCustomNameToComponentFix", this.getInputSchema().getType(References.ENTITY), (Function<Typed<?>, Typed<?>>)(typed -> typed.<Dynamic<?>>update(DSL.remainderFinder(), dynamic -> {
            final Optional<String> optional4 = typed.<String>getOptional(opticFinder2);
            if (optional4.isPresent() && Objects.equals(optional4.get(), "minecraft:commandblock_minecart")) {
                return dynamic;
            }
            return fixTagCustomName(dynamic);
        })));
    }
    
    public static Dynamic<?> fixTagCustomName(final Dynamic<?> dynamic) {
        final String string2 = dynamic.get("CustomName").asString("");
        if (string2.isEmpty()) {
            return dynamic.remove("CustomName");
        }
        return dynamic.set("CustomName", dynamic.createString(Component.Serializer.toJson(new TextComponent(string2))));
    }
}

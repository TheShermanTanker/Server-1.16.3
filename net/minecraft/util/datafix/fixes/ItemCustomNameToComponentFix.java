package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import java.util.Optional;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ItemCustomNameToComponentFix extends DataFix {
    public ItemCustomNameToComponentFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    private Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        final Optional<? extends Dynamic<?>> optional3 = dynamic.get("display").result();
        if (optional3.isPresent()) {
            Dynamic<?> dynamic2 = optional3.get();
            final Optional<String> optional4 = dynamic2.get("Name").asString().result();
            if (optional4.isPresent()) {
                dynamic2 = dynamic2.set("Name", dynamic2.createString(Component.Serializer.toJson(new TextComponent((String)optional4.get()))));
            }
            else {
                final Optional<String> optional5 = dynamic2.get("LocName").asString().result();
                if (optional5.isPresent()) {
                    dynamic2 = dynamic2.set("Name", dynamic2.createString(Component.Serializer.toJson(new TranslatableComponent((String)optional5.get()))));
                    dynamic2 = dynamic2.remove("LocName");
                }
            }
            return dynamic.set("display", dynamic2);
        }
        return dynamic;
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.ITEM_STACK);
        final OpticFinder<?> opticFinder3 = type2.findField("tag");
        return this.fixTypeEverywhereTyped("ItemCustomNameToComponentFix", type2, (Function<Typed<?>, Typed<?>>)(typed -> typed.updateTyped(opticFinder3, typed -> typed.<Dynamic<?>>update(DSL.remainderFinder(), this::fixTag))));
    }
}

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

public class OptionsRenameFieldFix extends DataFix {
    private final String fixName;
    private final String fieldFrom;
    private final String fieldTo;
    
    public OptionsRenameFieldFix(final Schema schema, final boolean boolean2, final String string3, final String string4, final String string5) {
        super(schema, boolean2);
        this.fixName = string3;
        this.fieldFrom = string4;
        this.fieldTo = string5;
    }
    
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped(this.fixName, this.getInputSchema().getType(References.OPTIONS), (Function<Typed<?>, Typed<?>>)(typed -> typed.<Dynamic<?>>update(DSL.remainderFinder(), dynamic -> DataFixUtils.<Dynamic>orElse((java.util.Optional<? extends Dynamic>)dynamic.get(this.fieldFrom).result().map(dynamic2 -> dynamic.set(this.fieldTo, dynamic2).remove(this.fieldFrom)), dynamic))));
    }
}

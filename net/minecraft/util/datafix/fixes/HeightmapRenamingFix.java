package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import java.util.Optional;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class HeightmapRenamingFix extends DataFix {
    public HeightmapRenamingFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.CHUNK);
        final OpticFinder<?> opticFinder3 = type2.findField("Level");
        return this.fixTypeEverywhereTyped("HeightmapRenamingFix", type2, (Function<Typed<?>, Typed<?>>)(typed -> typed.updateTyped(opticFinder3, typed -> typed.<Dynamic<?>>update(DSL.remainderFinder(), this::fix))));
    }
    
    private Dynamic<?> fix(final Dynamic<?> dynamic) {
        final Optional<? extends Dynamic<?>> optional3 = dynamic.get("Heightmaps").result();
        if (!optional3.isPresent()) {
            return dynamic;
        }
        Dynamic<?> dynamic2 = optional3.get();
        final Optional<? extends Dynamic<?>> optional4 = dynamic2.get("LIQUID").result();
        if (optional4.isPresent()) {
            dynamic2 = dynamic2.remove("LIQUID");
            dynamic2 = dynamic2.set("WORLD_SURFACE_WG", optional4.get());
        }
        final Optional<? extends Dynamic<?>> optional5 = dynamic2.get("SOLID").result();
        if (optional5.isPresent()) {
            dynamic2 = dynamic2.remove("SOLID");
            dynamic2 = dynamic2.set("OCEAN_FLOOR_WG", optional5.get());
            dynamic2 = dynamic2.set("OCEAN_FLOOR", optional5.get());
        }
        final Optional<? extends Dynamic<?>> optional6 = dynamic2.get("LIGHT").result();
        if (optional6.isPresent()) {
            dynamic2 = dynamic2.remove("LIGHT");
            dynamic2 = dynamic2.set("LIGHT_BLOCKING", optional6.get());
        }
        final Optional<? extends Dynamic<?>> optional7 = dynamic2.get("RAIN").result();
        if (optional7.isPresent()) {
            dynamic2 = dynamic2.remove("RAIN");
            dynamic2 = dynamic2.set("MOTION_BLOCKING", optional7.get());
            dynamic2 = dynamic2.set("MOTION_BLOCKING_NO_LEAVES", optional7.get());
        }
        return dynamic.set("Heightmaps", dynamic2);
    }
}

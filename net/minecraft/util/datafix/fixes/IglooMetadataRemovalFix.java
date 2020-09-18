package net.minecraft.util.datafix.fixes;

import java.util.stream.Stream;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class IglooMetadataRemovalFix extends DataFix {
    public IglooMetadataRemovalFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.STRUCTURE_FEATURE);
        final Type<?> type3 = this.getOutputSchema().getType(References.STRUCTURE_FEATURE);
        return this.writeFixAndRead("IglooMetadataRemovalFix", type2, type3, (Function<Dynamic<?>, Dynamic<?>>)IglooMetadataRemovalFix::fixTag);
    }
    
    private static <T> Dynamic<T> fixTag(final Dynamic<T> dynamic) {
        final boolean boolean2 = (boolean)dynamic.get("Children").asStreamOpt().map((java.util.function.Function<? super java.util.stream.Stream<Dynamic<T>>, ?>)(stream -> stream.allMatch(IglooMetadataRemovalFix::isIglooPiece))).result().orElse(false);
        if (boolean2) {
            return dynamic.set("id", dynamic.createString("Igloo")).remove("Children");
        }
        return dynamic.update("Children", (Function<Dynamic<?>, Dynamic<?>>)IglooMetadataRemovalFix::removeIglooPieces);
    }
    
    private static <T> Dynamic<T> removeIglooPieces(final Dynamic<T> dynamic) {
        return (Dynamic<T>)dynamic.asStreamOpt().map((java.util.function.Function<? super java.util.stream.Stream<Dynamic<T>>, ?>)(stream -> stream.filter(dynamic -> !isIglooPiece(dynamic)))).map((java.util.function.Function<? super Object, ?>)dynamic::createList).result().orElse(dynamic);
    }
    
    private static boolean isIglooPiece(final Dynamic<?> dynamic) {
        return dynamic.get("id").asString("").equals("Iglu");
    }
}

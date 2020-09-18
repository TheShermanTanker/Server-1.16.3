package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import com.mojang.datafixers.util.Either;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class BlockNameFlatteningFix extends DataFix {
    public BlockNameFlatteningFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.BLOCK_NAME);
        final Type<?> type3 = this.getOutputSchema().getType(References.BLOCK_NAME);
        final Type<Pair<String, Either<Integer, String>>> type4 = DSL.<Either<Integer, String>>named(References.BLOCK_NAME.typeName(), DSL.<Integer, String>or(DSL.intType(), NamespacedSchema.namespacedString()));
        final Type<Pair<String, String>> type5 = DSL.<String>named(References.BLOCK_NAME.typeName(), NamespacedSchema.namespacedString());
        if (!Objects.equals(type2, type4) || !Objects.equals(type3, type5)) {
            throw new IllegalStateException("Expected and actual types don't match.");
        }
        return this.<Pair<String, Either<Integer, String>>, Pair<String, String>>fixTypeEverywhere("BlockNameFlatteningFix", type4, type5, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Pair<String, Either<Integer, String>>, Pair<String, String>>>)(dynamicOps -> pair -> pair.mapSecond(either -> either.<String>map(BlockStateData::upgradeBlock, string -> BlockStateData.upgradeBlock(NamespacedSchema.ensureNamespaced(string))))));
    }
}

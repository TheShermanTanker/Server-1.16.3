package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.util.Pair;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFix;

public class ChunkStructuresTemplateRenameFix extends DataFix {
    private static final ImmutableMap<String, Pair<String, ImmutableMap<String, String>>> RENAMES;
    
    public ChunkStructuresTemplateRenameFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.STRUCTURE_FEATURE);
        final OpticFinder<?> opticFinder3 = type2.findField("Children");
        return this.fixTypeEverywhereTyped("ChunkStructuresTemplateRenameFix", type2, (Function<Typed<?>, Typed<?>>)(typed -> typed.updateTyped(opticFinder3, typed2 -> typed2.<Dynamic<?>>update(DSL.remainderFinder(), dynamic -> this.fixTag(typed.<Dynamic<?>>get(DSL.remainderFinder()), dynamic)))));
    }
    
    private Dynamic<?> fixTag(final Dynamic<?> dynamic1, Dynamic<?> dynamic2) {
        final String string4 = dynamic1.get("id").asString("");
        if (ChunkStructuresTemplateRenameFix.RENAMES.containsKey(string4)) {
            final Pair<String, ImmutableMap<String, String>> pair5 = ChunkStructuresTemplateRenameFix.RENAMES.get(string4);
            if (pair5.getFirst().equals(dynamic2.get("id").asString(""))) {
                final String string5 = dynamic2.get("Template").asString("");
                dynamic2 = dynamic2.set("Template", dynamic2.createString(pair5.getSecond().getOrDefault(string5, string5)));
            }
        }
        return dynamic2;
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "EndCity"
        //     5: ldc             "ECP"
        //     7: invokestatic    com/google/common/collect/ImmutableMap.builder:()Lcom/google/common/collect/ImmutableMap$Builder;
        //    10: ldc             "second_floor"
        //    12: ldc             "second_floor_1"
        //    14: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    17: ldc             "third_floor"
        //    19: ldc             "third_floor_1"
        //    21: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    24: ldc             "third_floor_c"
        //    26: ldc             "third_floor_2"
        //    28: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    31: invokevirtual   com/google/common/collect/ImmutableMap$Builder.build:()Lcom/google/common/collect/ImmutableMap;
        //    34: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    37: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    40: ldc             "Mansion"
        //    42: ldc             "WMP"
        //    44: invokestatic    com/google/common/collect/ImmutableMap.builder:()Lcom/google/common/collect/ImmutableMap$Builder;
        //    47: ldc             "carpet_south"
        //    49: ldc             "carpet_south_1"
        //    51: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    54: ldc             "carpet_west"
        //    56: ldc             "carpet_west_1"
        //    58: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    61: ldc             "indoors_door"
        //    63: ldc             "indoors_door_1"
        //    65: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    68: ldc             "indoors_wall"
        //    70: ldc             "indoors_wall_1"
        //    72: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    75: invokevirtual   com/google/common/collect/ImmutableMap$Builder.build:()Lcom/google/common/collect/ImmutableMap;
        //    78: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    81: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    84: ldc             "Igloo"
        //    86: ldc             "Iglu"
        //    88: invokestatic    com/google/common/collect/ImmutableMap.builder:()Lcom/google/common/collect/ImmutableMap$Builder;
        //    91: ldc_w           "minecraft:igloo/igloo_bottom"
        //    94: ldc_w           "minecraft:igloo/bottom"
        //    97: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   100: ldc_w           "minecraft:igloo/igloo_middle"
        //   103: ldc_w           "minecraft:igloo/middle"
        //   106: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   109: ldc_w           "minecraft:igloo/igloo_top"
        //   112: ldc_w           "minecraft:igloo/top"
        //   115: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   118: invokevirtual   com/google/common/collect/ImmutableMap$Builder.build:()Lcom/google/common/collect/ImmutableMap;
        //   121: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   124: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   127: ldc_w           "Ocean_Ruin"
        //   130: ldc_w           "ORP"
        //   133: invokestatic    com/google/common/collect/ImmutableMap.builder:()Lcom/google/common/collect/ImmutableMap$Builder;
        //   136: ldc_w           "minecraft:ruin/big_ruin1_brick"
        //   139: ldc_w           "minecraft:underwater_ruin/big_brick_1"
        //   142: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   145: ldc_w           "minecraft:ruin/big_ruin2_brick"
        //   148: ldc_w           "minecraft:underwater_ruin/big_brick_2"
        //   151: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   154: ldc_w           "minecraft:ruin/big_ruin3_brick"
        //   157: ldc_w           "minecraft:underwater_ruin/big_brick_3"
        //   160: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   163: ldc_w           "minecraft:ruin/big_ruin8_brick"
        //   166: ldc_w           "minecraft:underwater_ruin/big_brick_8"
        //   169: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   172: ldc_w           "minecraft:ruin/big_ruin1_cracked"
        //   175: ldc_w           "minecraft:underwater_ruin/big_cracked_1"
        //   178: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   181: ldc_w           "minecraft:ruin/big_ruin2_cracked"
        //   184: ldc_w           "minecraft:underwater_ruin/big_cracked_2"
        //   187: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   190: ldc_w           "minecraft:ruin/big_ruin3_cracked"
        //   193: ldc_w           "minecraft:underwater_ruin/big_cracked_3"
        //   196: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   199: ldc_w           "minecraft:ruin/big_ruin8_cracked"
        //   202: ldc_w           "minecraft:underwater_ruin/big_cracked_8"
        //   205: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   208: ldc_w           "minecraft:ruin/big_ruin1_mossy"
        //   211: ldc_w           "minecraft:underwater_ruin/big_mossy_1"
        //   214: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   217: ldc_w           "minecraft:ruin/big_ruin2_mossy"
        //   220: ldc_w           "minecraft:underwater_ruin/big_mossy_2"
        //   223: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   226: ldc_w           "minecraft:ruin/big_ruin3_mossy"
        //   229: ldc_w           "minecraft:underwater_ruin/big_mossy_3"
        //   232: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   235: ldc_w           "minecraft:ruin/big_ruin8_mossy"
        //   238: ldc_w           "minecraft:underwater_ruin/big_mossy_8"
        //   241: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   244: ldc_w           "minecraft:ruin/big_ruin_warm4"
        //   247: ldc_w           "minecraft:underwater_ruin/big_warm_4"
        //   250: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   253: ldc_w           "minecraft:ruin/big_ruin_warm5"
        //   256: ldc_w           "minecraft:underwater_ruin/big_warm_5"
        //   259: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   262: ldc_w           "minecraft:ruin/big_ruin_warm6"
        //   265: ldc_w           "minecraft:underwater_ruin/big_warm_6"
        //   268: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   271: ldc_w           "minecraft:ruin/big_ruin_warm7"
        //   274: ldc_w           "minecraft:underwater_ruin/big_warm_7"
        //   277: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   280: ldc_w           "minecraft:ruin/ruin1_brick"
        //   283: ldc_w           "minecraft:underwater_ruin/brick_1"
        //   286: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   289: ldc_w           "minecraft:ruin/ruin2_brick"
        //   292: ldc_w           "minecraft:underwater_ruin/brick_2"
        //   295: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   298: ldc_w           "minecraft:ruin/ruin3_brick"
        //   301: ldc_w           "minecraft:underwater_ruin/brick_3"
        //   304: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   307: ldc_w           "minecraft:ruin/ruin4_brick"
        //   310: ldc_w           "minecraft:underwater_ruin/brick_4"
        //   313: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   316: ldc_w           "minecraft:ruin/ruin5_brick"
        //   319: ldc_w           "minecraft:underwater_ruin/brick_5"
        //   322: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   325: ldc_w           "minecraft:ruin/ruin6_brick"
        //   328: ldc_w           "minecraft:underwater_ruin/brick_6"
        //   331: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   334: ldc_w           "minecraft:ruin/ruin7_brick"
        //   337: ldc_w           "minecraft:underwater_ruin/brick_7"
        //   340: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   343: ldc_w           "minecraft:ruin/ruin8_brick"
        //   346: ldc_w           "minecraft:underwater_ruin/brick_8"
        //   349: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   352: ldc_w           "minecraft:ruin/ruin1_cracked"
        //   355: ldc_w           "minecraft:underwater_ruin/cracked_1"
        //   358: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   361: ldc_w           "minecraft:ruin/ruin2_cracked"
        //   364: ldc_w           "minecraft:underwater_ruin/cracked_2"
        //   367: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   370: ldc_w           "minecraft:ruin/ruin3_cracked"
        //   373: ldc_w           "minecraft:underwater_ruin/cracked_3"
        //   376: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   379: ldc_w           "minecraft:ruin/ruin4_cracked"
        //   382: ldc_w           "minecraft:underwater_ruin/cracked_4"
        //   385: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   388: ldc_w           "minecraft:ruin/ruin5_cracked"
        //   391: ldc_w           "minecraft:underwater_ruin/cracked_5"
        //   394: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   397: ldc_w           "minecraft:ruin/ruin6_cracked"
        //   400: ldc_w           "minecraft:underwater_ruin/cracked_6"
        //   403: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   406: ldc_w           "minecraft:ruin/ruin7_cracked"
        //   409: ldc_w           "minecraft:underwater_ruin/cracked_7"
        //   412: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   415: ldc_w           "minecraft:ruin/ruin8_cracked"
        //   418: ldc_w           "minecraft:underwater_ruin/cracked_8"
        //   421: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   424: ldc_w           "minecraft:ruin/ruin1_mossy"
        //   427: ldc_w           "minecraft:underwater_ruin/mossy_1"
        //   430: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   433: ldc_w           "minecraft:ruin/ruin2_mossy"
        //   436: ldc_w           "minecraft:underwater_ruin/mossy_2"
        //   439: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   442: ldc_w           "minecraft:ruin/ruin3_mossy"
        //   445: ldc_w           "minecraft:underwater_ruin/mossy_3"
        //   448: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   451: ldc_w           "minecraft:ruin/ruin4_mossy"
        //   454: ldc_w           "minecraft:underwater_ruin/mossy_4"
        //   457: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   460: ldc_w           "minecraft:ruin/ruin5_mossy"
        //   463: ldc_w           "minecraft:underwater_ruin/mossy_5"
        //   466: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   469: ldc_w           "minecraft:ruin/ruin6_mossy"
        //   472: ldc_w           "minecraft:underwater_ruin/mossy_6"
        //   475: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   478: ldc_w           "minecraft:ruin/ruin7_mossy"
        //   481: ldc_w           "minecraft:underwater_ruin/mossy_7"
        //   484: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   487: ldc_w           "minecraft:ruin/ruin8_mossy"
        //   490: ldc_w           "minecraft:underwater_ruin/mossy_8"
        //   493: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   496: ldc_w           "minecraft:ruin/ruin_warm1"
        //   499: ldc_w           "minecraft:underwater_ruin/warm_1"
        //   502: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   505: ldc_w           "minecraft:ruin/ruin_warm2"
        //   508: ldc_w           "minecraft:underwater_ruin/warm_2"
        //   511: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   514: ldc_w           "minecraft:ruin/ruin_warm3"
        //   517: ldc_w           "minecraft:underwater_ruin/warm_3"
        //   520: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   523: ldc_w           "minecraft:ruin/ruin_warm4"
        //   526: ldc_w           "minecraft:underwater_ruin/warm_4"
        //   529: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   532: ldc_w           "minecraft:ruin/ruin_warm5"
        //   535: ldc_w           "minecraft:underwater_ruin/warm_5"
        //   538: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   541: ldc_w           "minecraft:ruin/ruin_warm6"
        //   544: ldc_w           "minecraft:underwater_ruin/warm_6"
        //   547: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   550: ldc_w           "minecraft:ruin/ruin_warm7"
        //   553: ldc_w           "minecraft:underwater_ruin/warm_7"
        //   556: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   559: ldc_w           "minecraft:ruin/ruin_warm8"
        //   562: ldc_w           "minecraft:underwater_ruin/warm_8"
        //   565: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   568: ldc_w           "minecraft:ruin/big_brick_1"
        //   571: ldc_w           "minecraft:underwater_ruin/big_brick_1"
        //   574: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   577: ldc_w           "minecraft:ruin/big_brick_2"
        //   580: ldc_w           "minecraft:underwater_ruin/big_brick_2"
        //   583: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   586: ldc_w           "minecraft:ruin/big_brick_3"
        //   589: ldc_w           "minecraft:underwater_ruin/big_brick_3"
        //   592: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   595: ldc_w           "minecraft:ruin/big_brick_8"
        //   598: ldc_w           "minecraft:underwater_ruin/big_brick_8"
        //   601: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   604: ldc_w           "minecraft:ruin/big_mossy_1"
        //   607: ldc_w           "minecraft:underwater_ruin/big_mossy_1"
        //   610: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   613: ldc_w           "minecraft:ruin/big_mossy_2"
        //   616: ldc_w           "minecraft:underwater_ruin/big_mossy_2"
        //   619: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   622: ldc_w           "minecraft:ruin/big_mossy_3"
        //   625: ldc_w           "minecraft:underwater_ruin/big_mossy_3"
        //   628: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   631: ldc_w           "minecraft:ruin/big_mossy_8"
        //   634: ldc_w           "minecraft:underwater_ruin/big_mossy_8"
        //   637: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   640: ldc_w           "minecraft:ruin/big_cracked_1"
        //   643: ldc_w           "minecraft:underwater_ruin/big_cracked_1"
        //   646: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   649: ldc_w           "minecraft:ruin/big_cracked_2"
        //   652: ldc_w           "minecraft:underwater_ruin/big_cracked_2"
        //   655: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   658: ldc_w           "minecraft:ruin/big_cracked_3"
        //   661: ldc_w           "minecraft:underwater_ruin/big_cracked_3"
        //   664: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   667: ldc_w           "minecraft:ruin/big_cracked_8"
        //   670: ldc_w           "minecraft:underwater_ruin/big_cracked_8"
        //   673: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   676: ldc_w           "minecraft:ruin/big_warm_4"
        //   679: ldc_w           "minecraft:underwater_ruin/big_warm_4"
        //   682: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   685: ldc_w           "minecraft:ruin/big_warm_5"
        //   688: ldc_w           "minecraft:underwater_ruin/big_warm_5"
        //   691: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   694: ldc_w           "minecraft:ruin/big_warm_6"
        //   697: ldc_w           "minecraft:underwater_ruin/big_warm_6"
        //   700: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   703: ldc_w           "minecraft:ruin/big_warm_7"
        //   706: ldc_w           "minecraft:underwater_ruin/big_warm_7"
        //   709: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   712: invokevirtual   com/google/common/collect/ImmutableMap$Builder.build:()Lcom/google/common/collect/ImmutableMap;
        //   715: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   718: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   721: invokevirtual   com/google/common/collect/ImmutableMap$Builder.build:()Lcom/google/common/collect/ImmutableMap;
        //   724: putstatic       net/minecraft/util/datafix/fixes/ChunkStructuresTemplateRenameFix.RENAMES:Lcom/google/common/collect/ImmutableMap;
        //   727: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.assembler.metadata.MetadataHelper.areSameTypes(MetadataHelper.java:1429)
        //     at com.strobel.assembler.metadata.MetadataHelper$StrictSameTypeVisitor.containsTypes(MetadataHelper.java:2584)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2386)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2440)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitParameterizedType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.decompiler.ast.TypeAnalysis.isSameType(TypeAnalysis.java:3072)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:790)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2454)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1072)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
        //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
        //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
        //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
        //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}

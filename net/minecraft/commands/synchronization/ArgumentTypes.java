package net.minecraft.commands.synchronization;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.CommandDispatcher;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class ArgumentTypes {
    private static final Logger LOGGER;
    private static final Map<Class<?>, Entry<?>> BY_CLASS;
    private static final Map<ResourceLocation, Entry<?>> BY_NAME;
    
    public static <T extends ArgumentType<?>> void register(final String string, final Class<T> class2, final ArgumentSerializer<T> fj) {
        final ResourceLocation vk4 = new ResourceLocation(string);
        if (ArgumentTypes.BY_CLASS.containsKey(class2)) {
            throw new IllegalArgumentException("Class " + class2.getName() + " already has a serializer!");
        }
        if (ArgumentTypes.BY_NAME.containsKey(vk4)) {
            throw new IllegalArgumentException(new StringBuilder().append("'").append(vk4).append("' is already a registered serializer!").toString());
        }
        final Entry<T> a5 = new Entry<T>((Class)class2, (ArgumentSerializer)fj, vk4);
        ArgumentTypes.BY_CLASS.put(class2, a5);
        ArgumentTypes.BY_NAME.put(vk4, a5);
    }
    
    public static void bootStrap() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc             "entity"
        //     5: ldc             Lnet/minecraft/commands/arguments/EntityArgument;.class
        //     7: new             Lnet/minecraft/commands/arguments/EntityArgument$Serializer;
        //    10: dup            
        //    11: invokespecial   net/minecraft/commands/arguments/EntityArgument$Serializer.<init>:()V
        //    14: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    17: ldc             "game_profile"
        //    19: ldc             Lnet/minecraft/commands/arguments/GameProfileArgument;.class
        //    21: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //    24: dup            
        //    25: invokedynamic   BootstrapMethod #0, get:()Ljava/util/function/Supplier;
        //    30: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //    33: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    36: ldc             "block_pos"
        //    38: ldc             Lnet/minecraft/commands/arguments/coordinates/BlockPosArgument;.class
        //    40: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //    43: dup            
        //    44: invokedynamic   BootstrapMethod #1, get:()Ljava/util/function/Supplier;
        //    49: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //    52: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    55: ldc             "column_pos"
        //    57: ldc             Lnet/minecraft/commands/arguments/coordinates/ColumnPosArgument;.class
        //    59: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //    62: dup            
        //    63: invokedynamic   BootstrapMethod #2, get:()Ljava/util/function/Supplier;
        //    68: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //    71: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    74: ldc             "vec3"
        //    76: ldc             Lnet/minecraft/commands/arguments/coordinates/Vec3Argument;.class
        //    78: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //    81: dup            
        //    82: invokedynamic   BootstrapMethod #3, get:()Ljava/util/function/Supplier;
        //    87: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //    90: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //    93: ldc             "vec2"
        //    95: ldc             Lnet/minecraft/commands/arguments/coordinates/Vec2Argument;.class
        //    97: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   100: dup            
        //   101: invokedynamic   BootstrapMethod #4, get:()Ljava/util/function/Supplier;
        //   106: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   109: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   112: ldc             "block_state"
        //   114: ldc             Lnet/minecraft/commands/arguments/blocks/BlockStateArgument;.class
        //   116: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   119: dup            
        //   120: invokedynamic   BootstrapMethod #5, get:()Ljava/util/function/Supplier;
        //   125: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   128: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   131: ldc             "block_predicate"
        //   133: ldc             Lnet/minecraft/commands/arguments/blocks/BlockPredicateArgument;.class
        //   135: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   138: dup            
        //   139: invokedynamic   BootstrapMethod #6, get:()Ljava/util/function/Supplier;
        //   144: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   147: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   150: ldc             "item_stack"
        //   152: ldc             Lnet/minecraft/commands/arguments/item/ItemArgument;.class
        //   154: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   157: dup            
        //   158: invokedynamic   BootstrapMethod #7, get:()Ljava/util/function/Supplier;
        //   163: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   166: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   169: ldc             "item_predicate"
        //   171: ldc             Lnet/minecraft/commands/arguments/item/ItemPredicateArgument;.class
        //   173: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   176: dup            
        //   177: invokedynamic   BootstrapMethod #8, get:()Ljava/util/function/Supplier;
        //   182: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   185: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   188: ldc             "color"
        //   190: ldc             Lnet/minecraft/commands/arguments/ColorArgument;.class
        //   192: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   195: dup            
        //   196: invokedynamic   BootstrapMethod #9, get:()Ljava/util/function/Supplier;
        //   201: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   204: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   207: ldc             "component"
        //   209: ldc             Lnet/minecraft/commands/arguments/ComponentArgument;.class
        //   211: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   214: dup            
        //   215: invokedynamic   BootstrapMethod #10, get:()Ljava/util/function/Supplier;
        //   220: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   223: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   226: ldc_w           "message"
        //   229: ldc_w           Lnet/minecraft/commands/arguments/MessageArgument;.class
        //   232: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   235: dup            
        //   236: invokedynamic   BootstrapMethod #11, get:()Ljava/util/function/Supplier;
        //   241: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   244: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   247: ldc_w           "nbt_compound_tag"
        //   250: ldc_w           Lnet/minecraft/commands/arguments/CompoundTagArgument;.class
        //   253: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   256: dup            
        //   257: invokedynamic   BootstrapMethod #12, get:()Ljava/util/function/Supplier;
        //   262: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   265: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   268: ldc_w           "nbt_tag"
        //   271: ldc_w           Lnet/minecraft/commands/arguments/NbtTagArgument;.class
        //   274: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   277: dup            
        //   278: invokedynamic   BootstrapMethod #13, get:()Ljava/util/function/Supplier;
        //   283: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   286: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   289: ldc_w           "nbt_path"
        //   292: ldc_w           Lnet/minecraft/commands/arguments/NbtPathArgument;.class
        //   295: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   298: dup            
        //   299: invokedynamic   BootstrapMethod #14, get:()Ljava/util/function/Supplier;
        //   304: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   307: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   310: ldc_w           "objective"
        //   313: ldc_w           Lnet/minecraft/commands/arguments/ObjectiveArgument;.class
        //   316: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   319: dup            
        //   320: invokedynamic   BootstrapMethod #15, get:()Ljava/util/function/Supplier;
        //   325: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   328: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   331: ldc_w           "objective_criteria"
        //   334: ldc_w           Lnet/minecraft/commands/arguments/ObjectiveCriteriaArgument;.class
        //   337: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   340: dup            
        //   341: invokedynamic   BootstrapMethod #16, get:()Ljava/util/function/Supplier;
        //   346: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   349: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   352: ldc_w           "operation"
        //   355: ldc_w           Lnet/minecraft/commands/arguments/OperationArgument;.class
        //   358: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   361: dup            
        //   362: invokedynamic   BootstrapMethod #17, get:()Ljava/util/function/Supplier;
        //   367: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   370: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   373: ldc_w           "particle"
        //   376: ldc_w           Lnet/minecraft/commands/arguments/ParticleArgument;.class
        //   379: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   382: dup            
        //   383: invokedynamic   BootstrapMethod #18, get:()Ljava/util/function/Supplier;
        //   388: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   391: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   394: ldc_w           "angle"
        //   397: ldc_w           Lnet/minecraft/commands/arguments/AngleArgument;.class
        //   400: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   403: dup            
        //   404: invokedynamic   BootstrapMethod #19, get:()Ljava/util/function/Supplier;
        //   409: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   412: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   415: ldc_w           "rotation"
        //   418: ldc_w           Lnet/minecraft/commands/arguments/coordinates/RotationArgument;.class
        //   421: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   424: dup            
        //   425: invokedynamic   BootstrapMethod #20, get:()Ljava/util/function/Supplier;
        //   430: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   433: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   436: ldc_w           "scoreboard_slot"
        //   439: ldc_w           Lnet/minecraft/commands/arguments/ScoreboardSlotArgument;.class
        //   442: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   445: dup            
        //   446: invokedynamic   BootstrapMethod #21, get:()Ljava/util/function/Supplier;
        //   451: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   454: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   457: ldc_w           "score_holder"
        //   460: ldc             Lnet/minecraft/commands/arguments/ScoreHolderArgument;.class
        //   462: new             Lnet/minecraft/commands/arguments/ScoreHolderArgument$Serializer;
        //   465: dup            
        //   466: invokespecial   net/minecraft/commands/arguments/ScoreHolderArgument$Serializer.<init>:()V
        //   469: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   472: ldc_w           "swizzle"
        //   475: ldc_w           Lnet/minecraft/commands/arguments/coordinates/SwizzleArgument;.class
        //   478: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   481: dup            
        //   482: invokedynamic   BootstrapMethod #22, get:()Ljava/util/function/Supplier;
        //   487: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   490: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   493: ldc_w           "team"
        //   496: ldc_w           Lnet/minecraft/commands/arguments/TeamArgument;.class
        //   499: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   502: dup            
        //   503: invokedynamic   BootstrapMethod #23, get:()Ljava/util/function/Supplier;
        //   508: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   511: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   514: ldc_w           "item_slot"
        //   517: ldc_w           Lnet/minecraft/commands/arguments/SlotArgument;.class
        //   520: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   523: dup            
        //   524: invokedynamic   BootstrapMethod #24, get:()Ljava/util/function/Supplier;
        //   529: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   532: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   535: ldc_w           "resource_location"
        //   538: ldc_w           Lnet/minecraft/commands/arguments/ResourceLocationArgument;.class
        //   541: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   544: dup            
        //   545: invokedynamic   BootstrapMethod #25, get:()Ljava/util/function/Supplier;
        //   550: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   553: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   556: ldc_w           "mob_effect"
        //   559: ldc_w           Lnet/minecraft/commands/arguments/MobEffectArgument;.class
        //   562: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   565: dup            
        //   566: invokedynamic   BootstrapMethod #26, get:()Ljava/util/function/Supplier;
        //   571: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   574: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   577: ldc_w           "function"
        //   580: ldc_w           Lnet/minecraft/commands/arguments/item/FunctionArgument;.class
        //   583: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   586: dup            
        //   587: invokedynamic   BootstrapMethod #27, get:()Ljava/util/function/Supplier;
        //   592: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   595: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   598: ldc_w           "entity_anchor"
        //   601: ldc_w           Lnet/minecraft/commands/arguments/EntityAnchorArgument;.class
        //   604: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   607: dup            
        //   608: invokedynamic   BootstrapMethod #28, get:()Ljava/util/function/Supplier;
        //   613: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   616: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   619: ldc_w           "int_range"
        //   622: ldc             Lnet/minecraft/commands/arguments/RangeArgument$Ints;.class
        //   624: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   627: dup            
        //   628: invokedynamic   BootstrapMethod #29, get:()Ljava/util/function/Supplier;
        //   633: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   636: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   639: ldc_w           "float_range"
        //   642: ldc             Lnet/minecraft/commands/arguments/RangeArgument$Floats;.class
        //   644: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   647: dup            
        //   648: invokedynamic   BootstrapMethod #30, get:()Ljava/util/function/Supplier;
        //   653: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   656: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   659: ldc_w           "item_enchantment"
        //   662: ldc_w           Lnet/minecraft/commands/arguments/ItemEnchantmentArgument;.class
        //   665: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   668: dup            
        //   669: invokedynamic   BootstrapMethod #31, get:()Ljava/util/function/Supplier;
        //   674: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   677: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   680: ldc_w           "entity_summon"
        //   683: ldc_w           Lnet/minecraft/commands/arguments/EntitySummonArgument;.class
        //   686: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   689: dup            
        //   690: invokedynamic   BootstrapMethod #32, get:()Ljava/util/function/Supplier;
        //   695: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   698: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   701: ldc_w           "dimension"
        //   704: ldc_w           Lnet/minecraft/commands/arguments/DimensionArgument;.class
        //   707: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   710: dup            
        //   711: invokedynamic   BootstrapMethod #33, get:()Ljava/util/function/Supplier;
        //   716: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   719: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   722: ldc_w           "time"
        //   725: ldc_w           Lnet/minecraft/commands/arguments/TimeArgument;.class
        //   728: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   731: dup            
        //   732: invokedynamic   BootstrapMethod #34, get:()Ljava/util/function/Supplier;
        //   737: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   740: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   743: ldc_w           "uuid"
        //   746: ldc_w           Lnet/minecraft/commands/arguments/UuidArgument;.class
        //   749: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   752: dup            
        //   753: invokedynamic   BootstrapMethod #35, get:()Ljava/util/function/Supplier;
        //   758: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   761: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   764: getstatic       net/minecraft/SharedConstants.IS_RUNNING_IN_IDE:Z
        //   767: ifeq            812
        //   770: ldc_w           "test_argument"
        //   773: ldc_w           Lnet/minecraft/gametest/framework/TestFunctionArgument;.class
        //   776: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   779: dup            
        //   780: invokedynamic   BootstrapMethod #36, get:()Ljava/util/function/Supplier;
        //   785: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   788: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   791: ldc_w           "test_class"
        //   794: ldc_w           Lnet/minecraft/gametest/framework/TestClassNameArgument;.class
        //   797: new             Lnet/minecraft/commands/synchronization/EmptyArgumentSerializer;
        //   800: dup            
        //   801: invokedynamic   BootstrapMethod #37, get:()Ljava/util/function/Supplier;
        //   806: invokespecial   net/minecraft/commands/synchronization/EmptyArgumentSerializer.<init>:(Ljava/util/function/Supplier;)V
        //   809: invokestatic    net/minecraft/commands/synchronization/ArgumentTypes.register:(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/commands/synchronization/ArgumentSerializer;)V
        //   812: return         
        //    StackMapTable: 00 01 FB 03 2C
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2611)
        //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2608)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getInterfaces(MetadataHelper.java:702)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2027)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitParameterizedType(MetadataHelper.java:1882)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitParameterizedType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:922)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
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
    
    @Nullable
    private static Entry<?> get(final ResourceLocation vk) {
        return ArgumentTypes.BY_NAME.get(vk);
    }
    
    @Nullable
    private static Entry<?> get(final ArgumentType<?> argumentType) {
        return ArgumentTypes.BY_CLASS.get(argumentType.getClass());
    }
    
    public static <T extends ArgumentType<?>> void serialize(final FriendlyByteBuf nf, final T argumentType) {
        final Entry<T> a3 = (Entry<T>)get(argumentType);
        if (a3 == null) {
            ArgumentTypes.LOGGER.error("Could not serialize {} ({}) - will not be sent to client!", argumentType, argumentType.getClass());
            nf.writeResourceLocation(new ResourceLocation(""));
            return;
        }
        nf.writeResourceLocation(a3.name);
        a3.serializer.serializeToNetwork(argumentType, nf);
    }
    
    @Nullable
    public static ArgumentType<?> deserialize(final FriendlyByteBuf nf) {
        final ResourceLocation vk2 = nf.readResourceLocation();
        final Entry<?> a3 = get(vk2);
        if (a3 == null) {
            ArgumentTypes.LOGGER.error("Could not deserialize {}", vk2);
            return null;
        }
        return a3.serializer.deserializeFromNetwork(nf);
    }
    
    private static <T extends ArgumentType<?>> void serializeToJson(final JsonObject jsonObject, final T argumentType) {
        final Entry<T> a3 = (Entry<T>)get(argumentType);
        if (a3 == null) {
            ArgumentTypes.LOGGER.error("Could not serialize argument {} ({})!", argumentType, argumentType.getClass());
            jsonObject.addProperty("type", "unknown");
        }
        else {
            jsonObject.addProperty("type", "argument");
            jsonObject.addProperty("parser", a3.name.toString());
            final JsonObject jsonObject2 = new JsonObject();
            a3.serializer.serializeToJson(argumentType, jsonObject2);
            if (jsonObject2.size() > 0) {
                jsonObject.add("properties", (JsonElement)jsonObject2);
            }
        }
    }
    
    public static <S> JsonObject serializeNodeToJson(final CommandDispatcher<S> commandDispatcher, final CommandNode<S> commandNode) {
        final JsonObject jsonObject3 = new JsonObject();
        if (commandNode instanceof RootCommandNode) {
            jsonObject3.addProperty("type", "root");
        }
        else if (commandNode instanceof LiteralCommandNode) {
            jsonObject3.addProperty("type", "literal");
        }
        else if (commandNode instanceof ArgumentCommandNode) {
            ArgumentTypes.<ArgumentType>serializeToJson(jsonObject3, ((ArgumentCommandNode)commandNode).getType());
        }
        else {
            ArgumentTypes.LOGGER.error("Could not serialize node {} ({})!", commandNode, commandNode.getClass());
            jsonObject3.addProperty("type", "unknown");
        }
        final JsonObject jsonObject4 = new JsonObject();
        for (final CommandNode<S> commandNode2 : commandNode.getChildren()) {
            jsonObject4.add(commandNode2.getName(), ArgumentTypes.serializeNodeToJson((CommandDispatcher<Object>)commandDispatcher, (CommandNode<Object>)commandNode2));
        }
        if (jsonObject4.size() > 0) {
            jsonObject3.add("children", (JsonElement)jsonObject4);
        }
        if (commandNode.getCommand() != null) {
            jsonObject3.addProperty("executable", Boolean.valueOf(true));
        }
        if (commandNode.getRedirect() != null) {
            final Collection<String> collection5 = commandDispatcher.getPath(commandNode.getRedirect());
            if (!collection5.isEmpty()) {
                final JsonArray jsonArray6 = new JsonArray();
                for (final String string8 : collection5) {
                    jsonArray6.add(string8);
                }
                jsonObject3.add("redirect", (JsonElement)jsonArray6);
            }
        }
        return jsonObject3;
    }
    
    public static boolean isTypeRegistered(final ArgumentType<?> argumentType) {
        return get(argumentType) != null;
    }
    
    public static <T> Set<ArgumentType<?>> findUsedArgumentTypes(final CommandNode<T> commandNode) {
        final Set<CommandNode<T>> set2 = Sets.<CommandNode<T>>newIdentityHashSet();
        final Set<ArgumentType<?>> set3 = Sets.newHashSet();
        ArgumentTypes.<T>findUsedArgumentTypes(commandNode, set3, set2);
        return set3;
    }
    
    private static <T> void findUsedArgumentTypes(final CommandNode<T> commandNode, final Set<ArgumentType<?>> set2, final Set<CommandNode<T>> set3) {
        if (!set3.add(commandNode)) {
            return;
        }
        if (commandNode instanceof ArgumentCommandNode) {
            set2.add(((ArgumentCommandNode)commandNode).getType());
        }
        commandNode.getChildren().forEach(commandNode -> ArgumentTypes.findUsedArgumentTypes(commandNode, set2, (java.util.Set<CommandNode<Object>>)set3));
        final CommandNode<T> commandNode2 = commandNode.getRedirect();
        if (commandNode2 != null) {
            ArgumentTypes.findUsedArgumentTypes((CommandNode<Object>)commandNode2, set2, (java.util.Set<CommandNode<Object>>)set3);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        BY_CLASS = (Map)Maps.newHashMap();
        BY_NAME = (Map)Maps.newHashMap();
    }
    
    static class Entry<T extends ArgumentType<?>> {
        public final Class<T> clazz;
        public final ArgumentSerializer<T> serializer;
        public final ResourceLocation name;
        
        private Entry(final Class<T> class1, final ArgumentSerializer<T> fj, final ResourceLocation vk) {
            this.clazz = class1;
            this.serializer = fj;
            this.name = vk;
        }
    }
}

package net.minecraft.world.entity;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Map;

public class SpawnPlacements {
    private static final Map<EntityType<?>, Data> DATA_BY_TYPE;
    
    private static <T extends Mob> void register(final EntityType<T> aqb, final Type c, final Heightmap.Types a, final SpawnPredicate<T> b) {
        final Data a2 = (Data)SpawnPlacements.DATA_BY_TYPE.put(aqb, new Data(a, c, b));
        if (a2 != null) {
            throw new IllegalStateException(new StringBuilder().append("Duplicate registration for type ").append(Registry.ENTITY_TYPE.getKey(aqb)).toString());
        }
    }
    
    public static Type getPlacementType(final EntityType<?> aqb) {
        final Data a2 = (Data)SpawnPlacements.DATA_BY_TYPE.get(aqb);
        return (a2 == null) ? Type.NO_RESTRICTIONS : a2.placement;
    }
    
    public static Heightmap.Types getHeightmapType(@Nullable final EntityType<?> aqb) {
        final Data a2 = (Data)SpawnPlacements.DATA_BY_TYPE.get(aqb);
        return (a2 == null) ? Heightmap.Types.MOTION_BLOCKING_NO_LEAVES : a2.heightMap;
    }
    
    public static <T extends Entity> boolean checkSpawnRules(final EntityType<T> aqb, final ServerLevelAccessor bsh, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        final Data a6 = (Data)SpawnPlacements.DATA_BY_TYPE.get(aqb);
        return a6 == null || a6.predicate.test(aqb, bsh, aqm, fx, random);
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: putstatic       net/minecraft/world/entity/SpawnPlacements.DATA_BY_TYPE:Ljava/util/Map;
        //     6: getstatic       net/minecraft/world/entity/EntityType.COD:Lnet/minecraft/world/entity/EntityType;
        //     9: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.IN_WATER:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //    12: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //    15: invokedynamic   BootstrapMethod #0, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //    20: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //    23: getstatic       net/minecraft/world/entity/EntityType.DOLPHIN:Lnet/minecraft/world/entity/EntityType;
        //    26: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.IN_WATER:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //    29: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //    32: invokedynamic   BootstrapMethod #1, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //    37: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //    40: getstatic       net/minecraft/world/entity/EntityType.DROWNED:Lnet/minecraft/world/entity/EntityType;
        //    43: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.IN_WATER:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //    46: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //    49: invokedynamic   BootstrapMethod #2, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //    54: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //    57: getstatic       net/minecraft/world/entity/EntityType.GUARDIAN:Lnet/minecraft/world/entity/EntityType;
        //    60: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.IN_WATER:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //    63: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //    66: invokedynamic   BootstrapMethod #3, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //    71: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //    74: getstatic       net/minecraft/world/entity/EntityType.PUFFERFISH:Lnet/minecraft/world/entity/EntityType;
        //    77: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.IN_WATER:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //    80: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //    83: invokedynamic   BootstrapMethod #0, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //    88: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //    91: getstatic       net/minecraft/world/entity/EntityType.SALMON:Lnet/minecraft/world/entity/EntityType;
        //    94: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.IN_WATER:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //    97: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   100: invokedynamic   BootstrapMethod #0, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   105: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   108: getstatic       net/minecraft/world/entity/EntityType.SQUID:Lnet/minecraft/world/entity/EntityType;
        //   111: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.IN_WATER:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   114: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   117: invokedynamic   BootstrapMethod #4, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   122: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   125: getstatic       net/minecraft/world/entity/EntityType.TROPICAL_FISH:Lnet/minecraft/world/entity/EntityType;
        //   128: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.IN_WATER:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   131: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   134: invokedynamic   BootstrapMethod #0, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   139: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   142: getstatic       net/minecraft/world/entity/EntityType.BAT:Lnet/minecraft/world/entity/EntityType;
        //   145: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   148: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   151: invokedynamic   BootstrapMethod #5, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   156: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   159: getstatic       net/minecraft/world/entity/EntityType.BLAZE:Lnet/minecraft/world/entity/EntityType;
        //   162: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   165: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   168: invokedynamic   BootstrapMethod #6, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   173: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   176: getstatic       net/minecraft/world/entity/EntityType.CAVE_SPIDER:Lnet/minecraft/world/entity/EntityType;
        //   179: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   182: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   185: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   190: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   193: getstatic       net/minecraft/world/entity/EntityType.CHICKEN:Lnet/minecraft/world/entity/EntityType;
        //   196: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   199: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   202: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   207: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   210: getstatic       net/minecraft/world/entity/EntityType.COW:Lnet/minecraft/world/entity/EntityType;
        //   213: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   216: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   219: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   224: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   227: getstatic       net/minecraft/world/entity/EntityType.CREEPER:Lnet/minecraft/world/entity/EntityType;
        //   230: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   233: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   236: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   241: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   244: getstatic       net/minecraft/world/entity/EntityType.DONKEY:Lnet/minecraft/world/entity/EntityType;
        //   247: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   250: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   253: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   258: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   261: getstatic       net/minecraft/world/entity/EntityType.ENDERMAN:Lnet/minecraft/world/entity/EntityType;
        //   264: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   267: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   270: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   275: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   278: getstatic       net/minecraft/world/entity/EntityType.ENDERMITE:Lnet/minecraft/world/entity/EntityType;
        //   281: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   284: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   287: invokedynamic   BootstrapMethod #9, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   292: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   295: getstatic       net/minecraft/world/entity/EntityType.ENDER_DRAGON:Lnet/minecraft/world/entity/EntityType;
        //   298: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   301: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   304: invokedynamic   BootstrapMethod #10, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   309: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   312: getstatic       net/minecraft/world/entity/EntityType.GHAST:Lnet/minecraft/world/entity/EntityType;
        //   315: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   318: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   321: invokedynamic   BootstrapMethod #11, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   326: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   329: getstatic       net/minecraft/world/entity/EntityType.GIANT:Lnet/minecraft/world/entity/EntityType;
        //   332: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   335: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   338: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   343: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   346: getstatic       net/minecraft/world/entity/EntityType.HORSE:Lnet/minecraft/world/entity/EntityType;
        //   349: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   352: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   355: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   360: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   363: getstatic       net/minecraft/world/entity/EntityType.HUSK:Lnet/minecraft/world/entity/EntityType;
        //   366: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   369: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   372: invokedynamic   BootstrapMethod #12, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   377: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   380: getstatic       net/minecraft/world/entity/EntityType.IRON_GOLEM:Lnet/minecraft/world/entity/EntityType;
        //   383: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   386: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   389: invokedynamic   BootstrapMethod #10, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   394: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   397: getstatic       net/minecraft/world/entity/EntityType.LLAMA:Lnet/minecraft/world/entity/EntityType;
        //   400: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   403: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   406: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   411: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   414: getstatic       net/minecraft/world/entity/EntityType.MAGMA_CUBE:Lnet/minecraft/world/entity/EntityType;
        //   417: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   420: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   423: invokedynamic   BootstrapMethod #13, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   428: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   431: getstatic       net/minecraft/world/entity/EntityType.MOOSHROOM:Lnet/minecraft/world/entity/EntityType;
        //   434: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   437: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   440: invokedynamic   BootstrapMethod #14, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   445: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   448: getstatic       net/minecraft/world/entity/EntityType.MULE:Lnet/minecraft/world/entity/EntityType;
        //   451: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   454: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   457: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   462: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   465: getstatic       net/minecraft/world/entity/EntityType.OCELOT:Lnet/minecraft/world/entity/EntityType;
        //   468: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   471: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   474: invokedynamic   BootstrapMethod #15, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   479: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   482: getstatic       net/minecraft/world/entity/EntityType.PARROT:Lnet/minecraft/world/entity/EntityType;
        //   485: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   488: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   491: invokedynamic   BootstrapMethod #16, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   496: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   499: getstatic       net/minecraft/world/entity/EntityType.PIG:Lnet/minecraft/world/entity/EntityType;
        //   502: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   505: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   508: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   513: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   516: getstatic       net/minecraft/world/entity/EntityType.HOGLIN:Lnet/minecraft/world/entity/EntityType;
        //   519: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   522: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   525: invokedynamic   BootstrapMethod #17, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   530: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   533: getstatic       net/minecraft/world/entity/EntityType.PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //   536: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   539: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   542: invokedynamic   BootstrapMethod #18, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   547: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   550: getstatic       net/minecraft/world/entity/EntityType.PILLAGER:Lnet/minecraft/world/entity/EntityType;
        //   553: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   556: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   559: invokedynamic   BootstrapMethod #19, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   564: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   567: getstatic       net/minecraft/world/entity/EntityType.POLAR_BEAR:Lnet/minecraft/world/entity/EntityType;
        //   570: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   573: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   576: invokedynamic   BootstrapMethod #20, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   581: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   584: getstatic       net/minecraft/world/entity/EntityType.RABBIT:Lnet/minecraft/world/entity/EntityType;
        //   587: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   590: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   593: invokedynamic   BootstrapMethod #21, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   598: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   601: getstatic       net/minecraft/world/entity/EntityType.SHEEP:Lnet/minecraft/world/entity/EntityType;
        //   604: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   607: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   610: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   615: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   618: getstatic       net/minecraft/world/entity/EntityType.SILVERFISH:Lnet/minecraft/world/entity/EntityType;
        //   621: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   624: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   627: invokedynamic   BootstrapMethod #22, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   632: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   635: getstatic       net/minecraft/world/entity/EntityType.SKELETON:Lnet/minecraft/world/entity/EntityType;
        //   638: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   641: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   644: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   649: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   652: getstatic       net/minecraft/world/entity/EntityType.SKELETON_HORSE:Lnet/minecraft/world/entity/EntityType;
        //   655: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   658: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   661: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   666: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   669: getstatic       net/minecraft/world/entity/EntityType.SLIME:Lnet/minecraft/world/entity/EntityType;
        //   672: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   675: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   678: invokedynamic   BootstrapMethod #23, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   683: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   686: getstatic       net/minecraft/world/entity/EntityType.SNOW_GOLEM:Lnet/minecraft/world/entity/EntityType;
        //   689: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   692: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   695: invokedynamic   BootstrapMethod #10, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   700: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   703: getstatic       net/minecraft/world/entity/EntityType.SPIDER:Lnet/minecraft/world/entity/EntityType;
        //   706: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   709: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   712: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   717: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   720: getstatic       net/minecraft/world/entity/EntityType.STRAY:Lnet/minecraft/world/entity/EntityType;
        //   723: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   726: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   729: invokedynamic   BootstrapMethod #24, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   734: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   737: getstatic       net/minecraft/world/entity/EntityType.STRIDER:Lnet/minecraft/world/entity/EntityType;
        //   740: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.IN_LAVA:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   743: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   746: invokedynamic   BootstrapMethod #25, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   751: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   754: getstatic       net/minecraft/world/entity/EntityType.TURTLE:Lnet/minecraft/world/entity/EntityType;
        //   757: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   760: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   763: invokedynamic   BootstrapMethod #26, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   768: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   771: getstatic       net/minecraft/world/entity/EntityType.VILLAGER:Lnet/minecraft/world/entity/EntityType;
        //   774: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   777: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   780: invokedynamic   BootstrapMethod #10, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   785: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   788: getstatic       net/minecraft/world/entity/EntityType.WITCH:Lnet/minecraft/world/entity/EntityType;
        //   791: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   794: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   797: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   802: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   805: getstatic       net/minecraft/world/entity/EntityType.WITHER:Lnet/minecraft/world/entity/EntityType;
        //   808: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   811: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   814: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   819: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   822: getstatic       net/minecraft/world/entity/EntityType.WITHER_SKELETON:Lnet/minecraft/world/entity/EntityType;
        //   825: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   828: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   831: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   836: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   839: getstatic       net/minecraft/world/entity/EntityType.WOLF:Lnet/minecraft/world/entity/EntityType;
        //   842: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   845: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   848: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   853: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   856: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE:Lnet/minecraft/world/entity/EntityType;
        //   859: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   862: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   865: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   870: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   873: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE_HORSE:Lnet/minecraft/world/entity/EntityType;
        //   876: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   879: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   882: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   887: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   890: getstatic       net/minecraft/world/entity/EntityType.ZOMBIFIED_PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //   893: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   896: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   899: invokedynamic   BootstrapMethod #27, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   904: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   907: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE_VILLAGER:Lnet/minecraft/world/entity/EntityType;
        //   910: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   913: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   916: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   921: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   924: getstatic       net/minecraft/world/entity/EntityType.CAT:Lnet/minecraft/world/entity/EntityType;
        //   927: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.ON_GROUND:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   930: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   933: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   938: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   941: getstatic       net/minecraft/world/entity/EntityType.ELDER_GUARDIAN:Lnet/minecraft/world/entity/EntityType;
        //   944: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.IN_WATER:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   947: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   950: invokedynamic   BootstrapMethod #3, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   955: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   958: getstatic       net/minecraft/world/entity/EntityType.EVOKER:Lnet/minecraft/world/entity/EntityType;
        //   961: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   964: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   967: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   972: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   975: getstatic       net/minecraft/world/entity/EntityType.FOX:Lnet/minecraft/world/entity/EntityType;
        //   978: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   981: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   984: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //   989: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //   992: getstatic       net/minecraft/world/entity/EntityType.ILLUSIONER:Lnet/minecraft/world/entity/EntityType;
        //   995: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //   998: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  1001: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //  1006: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //  1009: getstatic       net/minecraft/world/entity/EntityType.PANDA:Lnet/minecraft/world/entity/EntityType;
        //  1012: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //  1015: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  1018: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //  1023: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //  1026: getstatic       net/minecraft/world/entity/EntityType.PHANTOM:Lnet/minecraft/world/entity/EntityType;
        //  1029: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //  1032: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  1035: invokedynamic   BootstrapMethod #10, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //  1040: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //  1043: getstatic       net/minecraft/world/entity/EntityType.RAVAGER:Lnet/minecraft/world/entity/EntityType;
        //  1046: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //  1049: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  1052: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //  1057: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //  1060: getstatic       net/minecraft/world/entity/EntityType.SHULKER:Lnet/minecraft/world/entity/EntityType;
        //  1063: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //  1066: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  1069: invokedynamic   BootstrapMethod #10, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //  1074: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //  1077: getstatic       net/minecraft/world/entity/EntityType.TRADER_LLAMA:Lnet/minecraft/world/entity/EntityType;
        //  1080: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //  1083: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  1086: invokedynamic   BootstrapMethod #8, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //  1091: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //  1094: getstatic       net/minecraft/world/entity/EntityType.VEX:Lnet/minecraft/world/entity/EntityType;
        //  1097: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //  1100: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  1103: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //  1108: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //  1111: getstatic       net/minecraft/world/entity/EntityType.VINDICATOR:Lnet/minecraft/world/entity/EntityType;
        //  1114: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //  1117: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  1120: invokedynamic   BootstrapMethod #7, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //  1125: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //  1128: getstatic       net/minecraft/world/entity/EntityType.WANDERING_TRADER:Lnet/minecraft/world/entity/EntityType;
        //  1131: getstatic       net/minecraft/world/entity/SpawnPlacements$Type.NO_RESTRICTIONS:Lnet/minecraft/world/entity/SpawnPlacements$Type;
        //  1134: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  1137: invokedynamic   BootstrapMethod #10, test:()Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;
        //  1142: invokestatic    net/minecraft/world/entity/SpawnPlacements.register:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V
        //  1145: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1051)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
    
    static class Data {
        private final Heightmap.Types heightMap;
        private final Type placement;
        private final SpawnPredicate<?> predicate;
        
        public Data(final Heightmap.Types a, final Type c, final SpawnPredicate<?> b) {
            this.heightMap = a;
            this.placement = c;
            this.predicate = b;
        }
    }
    
    public enum Type {
        ON_GROUND, 
        IN_WATER, 
        NO_RESTRICTIONS, 
        IN_LAVA;
    }
    
    @FunctionalInterface
    public interface SpawnPredicate<T extends Entity> {
        boolean test(final EntityType<T> aqb, final ServerLevelAccessor bsh, final MobSpawnType aqm, final BlockPos fx, final Random random);
    }
}

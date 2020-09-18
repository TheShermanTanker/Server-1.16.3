package net.minecraft.world.entity.ai.attributes;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class DefaultAttributes {
    private static final Logger LOGGER;
    private static final Map<EntityType<? extends LivingEntity>, AttributeSupplier> SUPPLIERS;
    
    public static AttributeSupplier getSupplier(final EntityType<? extends LivingEntity> aqb) {
        return (AttributeSupplier)DefaultAttributes.SUPPLIERS.get(aqb);
    }
    
    public static boolean hasSupplier(final EntityType<?> aqb) {
        return DefaultAttributes.SUPPLIERS.containsKey(aqb);
    }
    
    public static void validate() {
        Registry.ENTITY_TYPE.stream().filter(aqb -> aqb.getCategory() != MobCategory.MISC).filter(aqb -> !hasSupplier(aqb)).map(Registry.ENTITY_TYPE::getKey).forEach(vk -> {
            if (SharedConstants.IS_RUNNING_IN_IDE) {
                throw new IllegalStateException(new StringBuilder().append("Entity ").append(vk).append(" has no attributes").toString());
            }
            DefaultAttributes.LOGGER.error("Entity {} has no attributes", vk);
        });
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: putstatic       net/minecraft/world/entity/ai/attributes/DefaultAttributes.LOGGER:Lorg/apache/logging/log4j/Logger;
        //     6: invokestatic    com/google/common/collect/ImmutableMap.builder:()Lcom/google/common/collect/ImmutableMap$Builder;
        //     9: getstatic       net/minecraft/world/entity/EntityType.ARMOR_STAND:Lnet/minecraft/world/entity/EntityType;
        //    12: invokestatic    net/minecraft/world/entity/LivingEntity.createLivingAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //    15: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //    18: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    21: getstatic       net/minecraft/world/entity/EntityType.BAT:Lnet/minecraft/world/entity/EntityType;
        //    24: invokestatic    net/minecraft/world/entity/ambient/Bat.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //    27: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //    30: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    33: getstatic       net/minecraft/world/entity/EntityType.BEE:Lnet/minecraft/world/entity/EntityType;
        //    36: invokestatic    net/minecraft/world/entity/animal/Bee.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //    39: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //    42: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    45: getstatic       net/minecraft/world/entity/EntityType.BLAZE:Lnet/minecraft/world/entity/EntityType;
        //    48: invokestatic    net/minecraft/world/entity/monster/Blaze.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //    51: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //    54: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    57: getstatic       net/minecraft/world/entity/EntityType.CAT:Lnet/minecraft/world/entity/EntityType;
        //    60: invokestatic    net/minecraft/world/entity/animal/Cat.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //    63: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //    66: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    69: getstatic       net/minecraft/world/entity/EntityType.CAVE_SPIDER:Lnet/minecraft/world/entity/EntityType;
        //    72: invokestatic    net/minecraft/world/entity/monster/CaveSpider.createCaveSpider:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //    75: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //    78: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    81: getstatic       net/minecraft/world/entity/EntityType.CHICKEN:Lnet/minecraft/world/entity/EntityType;
        //    84: invokestatic    net/minecraft/world/entity/animal/Chicken.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //    87: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //    90: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    93: getstatic       net/minecraft/world/entity/EntityType.COD:Lnet/minecraft/world/entity/EntityType;
        //    96: invokestatic    net/minecraft/world/entity/animal/AbstractFish.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //    99: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   102: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   105: getstatic       net/minecraft/world/entity/EntityType.COW:Lnet/minecraft/world/entity/EntityType;
        //   108: invokestatic    net/minecraft/world/entity/animal/Cow.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   111: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   114: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   117: getstatic       net/minecraft/world/entity/EntityType.CREEPER:Lnet/minecraft/world/entity/EntityType;
        //   120: invokestatic    net/minecraft/world/entity/monster/Creeper.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   123: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   126: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   129: getstatic       net/minecraft/world/entity/EntityType.DOLPHIN:Lnet/minecraft/world/entity/EntityType;
        //   132: invokestatic    net/minecraft/world/entity/animal/Dolphin.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   135: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   138: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   141: getstatic       net/minecraft/world/entity/EntityType.DONKEY:Lnet/minecraft/world/entity/EntityType;
        //   144: invokestatic    net/minecraft/world/entity/animal/horse/AbstractChestedHorse.createBaseChestedHorseAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   147: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   150: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   153: getstatic       net/minecraft/world/entity/EntityType.DROWNED:Lnet/minecraft/world/entity/EntityType;
        //   156: invokestatic    net/minecraft/world/entity/monster/Zombie.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   159: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   162: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   165: getstatic       net/minecraft/world/entity/EntityType.ELDER_GUARDIAN:Lnet/minecraft/world/entity/EntityType;
        //   168: invokestatic    net/minecraft/world/entity/monster/ElderGuardian.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   171: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   174: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   177: getstatic       net/minecraft/world/entity/EntityType.ENDERMAN:Lnet/minecraft/world/entity/EntityType;
        //   180: invokestatic    net/minecraft/world/entity/monster/EnderMan.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   183: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   186: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   189: getstatic       net/minecraft/world/entity/EntityType.ENDERMITE:Lnet/minecraft/world/entity/EntityType;
        //   192: invokestatic    net/minecraft/world/entity/monster/Endermite.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   195: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   198: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   201: getstatic       net/minecraft/world/entity/EntityType.ENDER_DRAGON:Lnet/minecraft/world/entity/EntityType;
        //   204: invokestatic    net/minecraft/world/entity/boss/enderdragon/EnderDragon.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   207: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   210: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   213: getstatic       net/minecraft/world/entity/EntityType.EVOKER:Lnet/minecraft/world/entity/EntityType;
        //   216: invokestatic    net/minecraft/world/entity/monster/Evoker.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   219: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   222: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   225: getstatic       net/minecraft/world/entity/EntityType.FOX:Lnet/minecraft/world/entity/EntityType;
        //   228: invokestatic    net/minecraft/world/entity/animal/Fox.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   231: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   234: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   237: getstatic       net/minecraft/world/entity/EntityType.GHAST:Lnet/minecraft/world/entity/EntityType;
        //   240: invokestatic    net/minecraft/world/entity/monster/Ghast.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   243: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   246: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   249: getstatic       net/minecraft/world/entity/EntityType.GIANT:Lnet/minecraft/world/entity/EntityType;
        //   252: invokestatic    net/minecraft/world/entity/monster/Giant.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   255: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   258: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   261: getstatic       net/minecraft/world/entity/EntityType.GUARDIAN:Lnet/minecraft/world/entity/EntityType;
        //   264: invokestatic    net/minecraft/world/entity/monster/Guardian.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   267: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   270: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   273: getstatic       net/minecraft/world/entity/EntityType.HOGLIN:Lnet/minecraft/world/entity/EntityType;
        //   276: invokestatic    net/minecraft/world/entity/monster/hoglin/Hoglin.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   279: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   282: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   285: getstatic       net/minecraft/world/entity/EntityType.HORSE:Lnet/minecraft/world/entity/EntityType;
        //   288: invokestatic    net/minecraft/world/entity/animal/horse/AbstractHorse.createBaseHorseAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   291: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   294: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   297: getstatic       net/minecraft/world/entity/EntityType.HUSK:Lnet/minecraft/world/entity/EntityType;
        //   300: invokestatic    net/minecraft/world/entity/monster/Zombie.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   303: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   306: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   309: getstatic       net/minecraft/world/entity/EntityType.ILLUSIONER:Lnet/minecraft/world/entity/EntityType;
        //   312: invokestatic    net/minecraft/world/entity/monster/Illusioner.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   315: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   318: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   321: getstatic       net/minecraft/world/entity/EntityType.IRON_GOLEM:Lnet/minecraft/world/entity/EntityType;
        //   324: invokestatic    net/minecraft/world/entity/animal/IronGolem.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   327: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   330: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   333: getstatic       net/minecraft/world/entity/EntityType.LLAMA:Lnet/minecraft/world/entity/EntityType;
        //   336: invokestatic    net/minecraft/world/entity/animal/horse/Llama.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   339: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   342: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   345: getstatic       net/minecraft/world/entity/EntityType.MAGMA_CUBE:Lnet/minecraft/world/entity/EntityType;
        //   348: invokestatic    net/minecraft/world/entity/monster/MagmaCube.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   351: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   354: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   357: getstatic       net/minecraft/world/entity/EntityType.MOOSHROOM:Lnet/minecraft/world/entity/EntityType;
        //   360: invokestatic    net/minecraft/world/entity/animal/Cow.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   363: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   366: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   369: getstatic       net/minecraft/world/entity/EntityType.MULE:Lnet/minecraft/world/entity/EntityType;
        //   372: invokestatic    net/minecraft/world/entity/animal/horse/AbstractChestedHorse.createBaseChestedHorseAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   375: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   378: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   381: getstatic       net/minecraft/world/entity/EntityType.OCELOT:Lnet/minecraft/world/entity/EntityType;
        //   384: invokestatic    net/minecraft/world/entity/animal/Ocelot.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   387: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   390: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   393: getstatic       net/minecraft/world/entity/EntityType.PANDA:Lnet/minecraft/world/entity/EntityType;
        //   396: invokestatic    net/minecraft/world/entity/animal/Panda.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   399: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   402: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   405: getstatic       net/minecraft/world/entity/EntityType.PARROT:Lnet/minecraft/world/entity/EntityType;
        //   408: invokestatic    net/minecraft/world/entity/animal/Parrot.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   411: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   414: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   417: getstatic       net/minecraft/world/entity/EntityType.PHANTOM:Lnet/minecraft/world/entity/EntityType;
        //   420: invokestatic    net/minecraft/world/entity/monster/Monster.createMonsterAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   423: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   426: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   429: getstatic       net/minecraft/world/entity/EntityType.PIG:Lnet/minecraft/world/entity/EntityType;
        //   432: invokestatic    net/minecraft/world/entity/animal/Pig.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   435: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   438: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   441: getstatic       net/minecraft/world/entity/EntityType.PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //   444: invokestatic    net/minecraft/world/entity/monster/piglin/Piglin.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   447: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   450: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   453: getstatic       net/minecraft/world/entity/EntityType.PIGLIN_BRUTE:Lnet/minecraft/world/entity/EntityType;
        //   456: invokestatic    net/minecraft/world/entity/monster/piglin/PiglinBrute.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   459: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   462: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   465: getstatic       net/minecraft/world/entity/EntityType.PILLAGER:Lnet/minecraft/world/entity/EntityType;
        //   468: invokestatic    net/minecraft/world/entity/monster/Pillager.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   471: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   474: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   477: getstatic       net/minecraft/world/entity/EntityType.PLAYER:Lnet/minecraft/world/entity/EntityType;
        //   480: invokestatic    net/minecraft/world/entity/player/Player.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   483: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   486: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   489: getstatic       net/minecraft/world/entity/EntityType.POLAR_BEAR:Lnet/minecraft/world/entity/EntityType;
        //   492: invokestatic    net/minecraft/world/entity/animal/PolarBear.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   495: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   498: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   501: getstatic       net/minecraft/world/entity/EntityType.PUFFERFISH:Lnet/minecraft/world/entity/EntityType;
        //   504: invokestatic    net/minecraft/world/entity/animal/AbstractFish.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   507: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   510: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   513: getstatic       net/minecraft/world/entity/EntityType.RABBIT:Lnet/minecraft/world/entity/EntityType;
        //   516: invokestatic    net/minecraft/world/entity/animal/Rabbit.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   519: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   522: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   525: getstatic       net/minecraft/world/entity/EntityType.RAVAGER:Lnet/minecraft/world/entity/EntityType;
        //   528: invokestatic    net/minecraft/world/entity/monster/Ravager.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   531: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   534: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   537: getstatic       net/minecraft/world/entity/EntityType.SALMON:Lnet/minecraft/world/entity/EntityType;
        //   540: invokestatic    net/minecraft/world/entity/animal/AbstractFish.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   543: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   546: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   549: getstatic       net/minecraft/world/entity/EntityType.SHEEP:Lnet/minecraft/world/entity/EntityType;
        //   552: invokestatic    net/minecraft/world/entity/animal/Sheep.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   555: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   558: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   561: getstatic       net/minecraft/world/entity/EntityType.SHULKER:Lnet/minecraft/world/entity/EntityType;
        //   564: invokestatic    net/minecraft/world/entity/monster/Shulker.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   567: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   570: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   573: getstatic       net/minecraft/world/entity/EntityType.SILVERFISH:Lnet/minecraft/world/entity/EntityType;
        //   576: invokestatic    net/minecraft/world/entity/monster/Silverfish.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   579: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   582: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   585: getstatic       net/minecraft/world/entity/EntityType.SKELETON:Lnet/minecraft/world/entity/EntityType;
        //   588: invokestatic    net/minecraft/world/entity/monster/AbstractSkeleton.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   591: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   594: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   597: getstatic       net/minecraft/world/entity/EntityType.SKELETON_HORSE:Lnet/minecraft/world/entity/EntityType;
        //   600: invokestatic    net/minecraft/world/entity/animal/horse/SkeletonHorse.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   603: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   606: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   609: getstatic       net/minecraft/world/entity/EntityType.SLIME:Lnet/minecraft/world/entity/EntityType;
        //   612: invokestatic    net/minecraft/world/entity/monster/Monster.createMonsterAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   615: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   618: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   621: getstatic       net/minecraft/world/entity/EntityType.SNOW_GOLEM:Lnet/minecraft/world/entity/EntityType;
        //   624: invokestatic    net/minecraft/world/entity/animal/SnowGolem.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   627: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   630: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   633: getstatic       net/minecraft/world/entity/EntityType.SPIDER:Lnet/minecraft/world/entity/EntityType;
        //   636: invokestatic    net/minecraft/world/entity/monster/Spider.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   639: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   642: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   645: getstatic       net/minecraft/world/entity/EntityType.SQUID:Lnet/minecraft/world/entity/EntityType;
        //   648: invokestatic    net/minecraft/world/entity/animal/Squid.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   651: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   654: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   657: getstatic       net/minecraft/world/entity/EntityType.STRAY:Lnet/minecraft/world/entity/EntityType;
        //   660: invokestatic    net/minecraft/world/entity/monster/AbstractSkeleton.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   663: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   666: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   669: getstatic       net/minecraft/world/entity/EntityType.STRIDER:Lnet/minecraft/world/entity/EntityType;
        //   672: invokestatic    net/minecraft/world/entity/monster/Strider.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   675: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   678: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   681: getstatic       net/minecraft/world/entity/EntityType.TRADER_LLAMA:Lnet/minecraft/world/entity/EntityType;
        //   684: invokestatic    net/minecraft/world/entity/animal/horse/Llama.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   687: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   690: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   693: getstatic       net/minecraft/world/entity/EntityType.TROPICAL_FISH:Lnet/minecraft/world/entity/EntityType;
        //   696: invokestatic    net/minecraft/world/entity/animal/AbstractFish.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   699: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   702: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   705: getstatic       net/minecraft/world/entity/EntityType.TURTLE:Lnet/minecraft/world/entity/EntityType;
        //   708: invokestatic    net/minecraft/world/entity/animal/Turtle.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   711: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   714: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   717: getstatic       net/minecraft/world/entity/EntityType.VEX:Lnet/minecraft/world/entity/EntityType;
        //   720: invokestatic    net/minecraft/world/entity/monster/Vex.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   723: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   726: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   729: getstatic       net/minecraft/world/entity/EntityType.VILLAGER:Lnet/minecraft/world/entity/EntityType;
        //   732: invokestatic    net/minecraft/world/entity/npc/Villager.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   735: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   738: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   741: getstatic       net/minecraft/world/entity/EntityType.VINDICATOR:Lnet/minecraft/world/entity/EntityType;
        //   744: invokestatic    net/minecraft/world/entity/monster/Vindicator.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   747: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   750: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   753: getstatic       net/minecraft/world/entity/EntityType.WANDERING_TRADER:Lnet/minecraft/world/entity/EntityType;
        //   756: invokestatic    net/minecraft/world/entity/Mob.createMobAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   759: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   762: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   765: getstatic       net/minecraft/world/entity/EntityType.WITCH:Lnet/minecraft/world/entity/EntityType;
        //   768: invokestatic    net/minecraft/world/entity/monster/Witch.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   771: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   774: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   777: getstatic       net/minecraft/world/entity/EntityType.WITHER:Lnet/minecraft/world/entity/EntityType;
        //   780: invokestatic    net/minecraft/world/entity/boss/wither/WitherBoss.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   783: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   786: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   789: getstatic       net/minecraft/world/entity/EntityType.WITHER_SKELETON:Lnet/minecraft/world/entity/EntityType;
        //   792: invokestatic    net/minecraft/world/entity/monster/AbstractSkeleton.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   795: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   798: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   801: getstatic       net/minecraft/world/entity/EntityType.WOLF:Lnet/minecraft/world/entity/EntityType;
        //   804: invokestatic    net/minecraft/world/entity/animal/Wolf.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   807: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   810: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   813: getstatic       net/minecraft/world/entity/EntityType.ZOGLIN:Lnet/minecraft/world/entity/EntityType;
        //   816: invokestatic    net/minecraft/world/entity/monster/Zoglin.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   819: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   822: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   825: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE:Lnet/minecraft/world/entity/EntityType;
        //   828: invokestatic    net/minecraft/world/entity/monster/Zombie.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   831: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   834: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   837: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE_HORSE:Lnet/minecraft/world/entity/EntityType;
        //   840: invokestatic    net/minecraft/world/entity/animal/horse/ZombieHorse.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   843: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   846: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   849: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE_VILLAGER:Lnet/minecraft/world/entity/EntityType;
        //   852: invokestatic    net/minecraft/world/entity/monster/Zombie.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   855: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   858: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   861: getstatic       net/minecraft/world/entity/EntityType.ZOMBIFIED_PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //   864: invokestatic    net/minecraft/world/entity/monster/ZombifiedPiglin.createAttributes:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;
        //   867: invokevirtual   net/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder.build:()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;
        //   870: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   873: invokevirtual   com/google/common/collect/ImmutableMap$Builder.build:()Lcom/google/common/collect/ImmutableMap;
        //   876: putstatic       net/minecraft/world/entity/ai/attributes/DefaultAttributes.SUPPLIERS:Ljava/util/Map;
        //   879: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.resolve(ParameterizedType.java:108)
        //     at com.strobel.assembler.metadata.MetadataHelper.getGenericSubTypeMappings(MetadataHelper.java:761)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2503)
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

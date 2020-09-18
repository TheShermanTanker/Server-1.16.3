package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Sets;
import java.util.Iterator;
import com.mojang.datafixers.DSL;
import java.util.Optional;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Set;

public class EntityUUIDFix extends AbstractUUIDFix {
    private static final Set<String> ABSTRACT_HORSES;
    private static final Set<String> TAMEABLE_ANIMALS;
    private static final Set<String> ANIMALS;
    private static final Set<String> MOBS;
    private static final Set<String> LIVING_ENTITIES;
    private static final Set<String> PROJECTILES;
    
    public EntityUUIDFix(final Schema schema) {
        super(schema, References.ENTITY);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityUUIDFixes", this.getInputSchema().getType(this.typeReference), (Function<Typed<?>, Typed<?>>)(typed -> {
            typed = typed.<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateEntityUUID);
            for (final String string4 : EntityUUIDFix.ABSTRACT_HORSES) {
                typed = this.updateNamedChoice(typed, string4, (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateAnimalOwner);
            }
            for (final String string4 : EntityUUIDFix.TAMEABLE_ANIMALS) {
                typed = this.updateNamedChoice(typed, string4, (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateAnimalOwner);
            }
            for (final String string4 : EntityUUIDFix.ANIMALS) {
                typed = this.updateNamedChoice(typed, string4, (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateAnimal);
            }
            for (final String string4 : EntityUUIDFix.MOBS) {
                typed = this.updateNamedChoice(typed, string4, (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateMob);
            }
            for (final String string4 : EntityUUIDFix.LIVING_ENTITIES) {
                typed = this.updateNamedChoice(typed, string4, (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateLivingEntity);
            }
            for (final String string4 : EntityUUIDFix.PROJECTILES) {
                typed = this.updateNamedChoice(typed, string4, (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateProjectile);
            }
            typed = this.updateNamedChoice(typed, "minecraft:bee", (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateHurtBy);
            typed = this.updateNamedChoice(typed, "minecraft:zombified_piglin", (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateHurtBy);
            typed = this.updateNamedChoice(typed, "minecraft:fox", (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateFox);
            typed = this.updateNamedChoice(typed, "minecraft:item", (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateItem);
            typed = this.updateNamedChoice(typed, "minecraft:shulker_bullet", (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateShulkerBullet);
            typed = this.updateNamedChoice(typed, "minecraft:area_effect_cloud", (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateAreaEffectCloud);
            typed = this.updateNamedChoice(typed, "minecraft:zombie_villager", (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateZombieVillager);
            typed = this.updateNamedChoice(typed, "minecraft:evoker_fangs", (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updateEvokerFangs);
            typed = this.updateNamedChoice(typed, "minecraft:piglin", (Function<Dynamic<?>, Dynamic<?>>)EntityUUIDFix::updatePiglin);
            return typed;
        }));
    }
    
    private static Dynamic<?> updatePiglin(final Dynamic<?> dynamic) {
        return dynamic.update("Brain", (Function<Dynamic<?>, Dynamic<?>>)(dynamic -> dynamic.update("memories", dynamic -> dynamic.update("minecraft:angry_at", dynamic -> (Dynamic)AbstractUUIDFix.replaceUUIDString(dynamic, "value", "value").orElseGet(() -> {
            EntityUUIDFix.LOGGER.warn("angry_at has no value.");
            return dynamic;
        })))));
    }
    
    private static Dynamic<?> updateEvokerFangs(final Dynamic<?> dynamic) {
        return AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "OwnerUUID", "Owner").orElse(dynamic);
    }
    
    private static Dynamic<?> updateZombieVillager(final Dynamic<?> dynamic) {
        return AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "ConversionPlayer", "ConversionPlayer").orElse(dynamic);
    }
    
    private static Dynamic<?> updateAreaEffectCloud(final Dynamic<?> dynamic) {
        return AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "OwnerUUID", "Owner").orElse(dynamic);
    }
    
    private static Dynamic<?> updateShulkerBullet(Dynamic<?> dynamic) {
        dynamic = AbstractUUIDFix.replaceUUIDMLTag(dynamic, "Owner", "Owner").orElse(dynamic);
        return AbstractUUIDFix.replaceUUIDMLTag(dynamic, "Target", "Target").orElse(dynamic);
    }
    
    private static Dynamic<?> updateItem(Dynamic<?> dynamic) {
        dynamic = AbstractUUIDFix.replaceUUIDMLTag(dynamic, "Owner", "Owner").orElse(dynamic);
        return AbstractUUIDFix.replaceUUIDMLTag(dynamic, "Thrower", "Thrower").orElse(dynamic);
    }
    
    private static Dynamic<?> updateFox(final Dynamic<?> dynamic) {
        final Optional<Dynamic<?>> optional2 = (Optional<Dynamic<?>>)dynamic.get("TrustedUUIDs").result().map(dynamic2 -> dynamic.createList(dynamic2.asStream().map(dynamic -> (Dynamic)AbstractUUIDFix.createUUIDFromML(dynamic).orElseGet(() -> {
            EntityUUIDFix.LOGGER.warn("Trusted contained invalid data.");
            return dynamic;
        }))));
        return DataFixUtils.<Dynamic<?>>orElse((java.util.Optional<? extends Dynamic<?>>)optional2.map(dynamic2 -> dynamic.remove("TrustedUUIDs").set("Trusted", dynamic2)), dynamic);
    }
    
    private static Dynamic<?> updateHurtBy(final Dynamic<?> dynamic) {
        return AbstractUUIDFix.replaceUUIDString(dynamic, "HurtBy", "HurtBy").orElse(dynamic);
    }
    
    private static Dynamic<?> updateAnimalOwner(final Dynamic<?> dynamic) {
        final Dynamic<?> dynamic2 = updateAnimal(dynamic);
        return AbstractUUIDFix.replaceUUIDString(dynamic2, "OwnerUUID", "Owner").orElse(dynamic2);
    }
    
    private static Dynamic<?> updateAnimal(final Dynamic<?> dynamic) {
        final Dynamic<?> dynamic2 = updateMob(dynamic);
        return AbstractUUIDFix.replaceUUIDLeastMost(dynamic2, "LoveCause", "LoveCause").orElse(dynamic2);
    }
    
    private static Dynamic<?> updateMob(final Dynamic<?> dynamic) {
        return updateLivingEntity(dynamic).update("Leash", (Function<Dynamic<?>, Dynamic<?>>)(dynamic -> (Dynamic)AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "UUID", "UUID").orElse(dynamic)));
    }
    
    public static Dynamic<?> updateLivingEntity(final Dynamic<?> dynamic) {
        return dynamic.update("Attributes", (Function<Dynamic<?>, Dynamic<?>>)(dynamic2 -> dynamic.createList(dynamic2.asStream().map(dynamic -> dynamic.update("Modifiers", dynamic2 -> dynamic.createList(dynamic2.asStream().map(dynamic -> (Dynamic)AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "UUID", "UUID").orElse(dynamic))))))));
    }
    
    private static Dynamic<?> updateProjectile(final Dynamic<?> dynamic) {
        return DataFixUtils.<Dynamic<?>>orElse((java.util.Optional<? extends Dynamic<?>>)dynamic.get("OwnerUUID").result().map(dynamic2 -> dynamic.remove("OwnerUUID").set("Owner", dynamic2)), dynamic);
    }
    
    public static Dynamic<?> updateEntityUUID(final Dynamic<?> dynamic) {
        return AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "UUID", "UUID").orElse(dynamic);
    }
    
    static {
        ABSTRACT_HORSES = (Set)Sets.newHashSet();
        TAMEABLE_ANIMALS = (Set)Sets.newHashSet();
        ANIMALS = (Set)Sets.newHashSet();
        MOBS = (Set)Sets.newHashSet();
        LIVING_ENTITIES = (Set)Sets.newHashSet();
        PROJECTILES = (Set)Sets.newHashSet();
        EntityUUIDFix.ABSTRACT_HORSES.add("minecraft:donkey");
        EntityUUIDFix.ABSTRACT_HORSES.add("minecraft:horse");
        EntityUUIDFix.ABSTRACT_HORSES.add("minecraft:llama");
        EntityUUIDFix.ABSTRACT_HORSES.add("minecraft:mule");
        EntityUUIDFix.ABSTRACT_HORSES.add("minecraft:skeleton_horse");
        EntityUUIDFix.ABSTRACT_HORSES.add("minecraft:trader_llama");
        EntityUUIDFix.ABSTRACT_HORSES.add("minecraft:zombie_horse");
        EntityUUIDFix.TAMEABLE_ANIMALS.add("minecraft:cat");
        EntityUUIDFix.TAMEABLE_ANIMALS.add("minecraft:parrot");
        EntityUUIDFix.TAMEABLE_ANIMALS.add("minecraft:wolf");
        EntityUUIDFix.ANIMALS.add("minecraft:bee");
        EntityUUIDFix.ANIMALS.add("minecraft:chicken");
        EntityUUIDFix.ANIMALS.add("minecraft:cow");
        EntityUUIDFix.ANIMALS.add("minecraft:fox");
        EntityUUIDFix.ANIMALS.add("minecraft:mooshroom");
        EntityUUIDFix.ANIMALS.add("minecraft:ocelot");
        EntityUUIDFix.ANIMALS.add("minecraft:panda");
        EntityUUIDFix.ANIMALS.add("minecraft:pig");
        EntityUUIDFix.ANIMALS.add("minecraft:polar_bear");
        EntityUUIDFix.ANIMALS.add("minecraft:rabbit");
        EntityUUIDFix.ANIMALS.add("minecraft:sheep");
        EntityUUIDFix.ANIMALS.add("minecraft:turtle");
        EntityUUIDFix.ANIMALS.add("minecraft:hoglin");
        EntityUUIDFix.MOBS.add("minecraft:bat");
        EntityUUIDFix.MOBS.add("minecraft:blaze");
        EntityUUIDFix.MOBS.add("minecraft:cave_spider");
        EntityUUIDFix.MOBS.add("minecraft:cod");
        EntityUUIDFix.MOBS.add("minecraft:creeper");
        EntityUUIDFix.MOBS.add("minecraft:dolphin");
        EntityUUIDFix.MOBS.add("minecraft:drowned");
        EntityUUIDFix.MOBS.add("minecraft:elder_guardian");
        EntityUUIDFix.MOBS.add("minecraft:ender_dragon");
        EntityUUIDFix.MOBS.add("minecraft:enderman");
        EntityUUIDFix.MOBS.add("minecraft:endermite");
        EntityUUIDFix.MOBS.add("minecraft:evoker");
        EntityUUIDFix.MOBS.add("minecraft:ghast");
        EntityUUIDFix.MOBS.add("minecraft:giant");
        EntityUUIDFix.MOBS.add("minecraft:guardian");
        EntityUUIDFix.MOBS.add("minecraft:husk");
        EntityUUIDFix.MOBS.add("minecraft:illusioner");
        EntityUUIDFix.MOBS.add("minecraft:magma_cube");
        EntityUUIDFix.MOBS.add("minecraft:pufferfish");
        EntityUUIDFix.MOBS.add("minecraft:zombified_piglin");
        EntityUUIDFix.MOBS.add("minecraft:salmon");
        EntityUUIDFix.MOBS.add("minecraft:shulker");
        EntityUUIDFix.MOBS.add("minecraft:silverfish");
        EntityUUIDFix.MOBS.add("minecraft:skeleton");
        EntityUUIDFix.MOBS.add("minecraft:slime");
        EntityUUIDFix.MOBS.add("minecraft:snow_golem");
        EntityUUIDFix.MOBS.add("minecraft:spider");
        EntityUUIDFix.MOBS.add("minecraft:squid");
        EntityUUIDFix.MOBS.add("minecraft:stray");
        EntityUUIDFix.MOBS.add("minecraft:tropical_fish");
        EntityUUIDFix.MOBS.add("minecraft:vex");
        EntityUUIDFix.MOBS.add("minecraft:villager");
        EntityUUIDFix.MOBS.add("minecraft:iron_golem");
        EntityUUIDFix.MOBS.add("minecraft:vindicator");
        EntityUUIDFix.MOBS.add("minecraft:pillager");
        EntityUUIDFix.MOBS.add("minecraft:wandering_trader");
        EntityUUIDFix.MOBS.add("minecraft:witch");
        EntityUUIDFix.MOBS.add("minecraft:wither");
        EntityUUIDFix.MOBS.add("minecraft:wither_skeleton");
        EntityUUIDFix.MOBS.add("minecraft:zombie");
        EntityUUIDFix.MOBS.add("minecraft:zombie_villager");
        EntityUUIDFix.MOBS.add("minecraft:phantom");
        EntityUUIDFix.MOBS.add("minecraft:ravager");
        EntityUUIDFix.MOBS.add("minecraft:piglin");
        EntityUUIDFix.LIVING_ENTITIES.add("minecraft:armor_stand");
        EntityUUIDFix.PROJECTILES.add("minecraft:arrow");
        EntityUUIDFix.PROJECTILES.add("minecraft:dragon_fireball");
        EntityUUIDFix.PROJECTILES.add("minecraft:firework_rocket");
        EntityUUIDFix.PROJECTILES.add("minecraft:fireball");
        EntityUUIDFix.PROJECTILES.add("minecraft:llama_spit");
        EntityUUIDFix.PROJECTILES.add("minecraft:small_fireball");
        EntityUUIDFix.PROJECTILES.add("minecraft:snowball");
        EntityUUIDFix.PROJECTILES.add("minecraft:spectral_arrow");
        EntityUUIDFix.PROJECTILES.add("minecraft:egg");
        EntityUUIDFix.PROJECTILES.add("minecraft:ender_pearl");
        EntityUUIDFix.PROJECTILES.add("minecraft:experience_bottle");
        EntityUUIDFix.PROJECTILES.add("minecraft:potion");
        EntityUUIDFix.PROJECTILES.add("minecraft:trident");
        EntityUUIDFix.PROJECTILES.add("minecraft:wither_skull");
    }
}

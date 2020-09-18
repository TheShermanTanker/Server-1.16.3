package net.minecraft.world.entity;

import net.minecraft.util.datafix.fixes.References;
import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.ListTag;
import java.util.function.Function;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Consumer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.Util;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.stream.Stream;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.core.Direction;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import java.util.Optional;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.apache.logging.log4j.Logger;

public class EntityType<T extends Entity> {
    private static final Logger LOGGER;
    public static final EntityType<AreaEffectCloud> AREA_EFFECT_CLOUD;
    public static final EntityType<ArmorStand> ARMOR_STAND;
    public static final EntityType<Arrow> ARROW;
    public static final EntityType<Bat> BAT;
    public static final EntityType<Bee> BEE;
    public static final EntityType<Blaze> BLAZE;
    public static final EntityType<Boat> BOAT;
    public static final EntityType<Cat> CAT;
    public static final EntityType<CaveSpider> CAVE_SPIDER;
    public static final EntityType<Chicken> CHICKEN;
    public static final EntityType<Cod> COD;
    public static final EntityType<Cow> COW;
    public static final EntityType<Creeper> CREEPER;
    public static final EntityType<Dolphin> DOLPHIN;
    public static final EntityType<Donkey> DONKEY;
    public static final EntityType<DragonFireball> DRAGON_FIREBALL;
    public static final EntityType<Drowned> DROWNED;
    public static final EntityType<ElderGuardian> ELDER_GUARDIAN;
    public static final EntityType<EndCrystal> END_CRYSTAL;
    public static final EntityType<EnderDragon> ENDER_DRAGON;
    public static final EntityType<EnderMan> ENDERMAN;
    public static final EntityType<Endermite> ENDERMITE;
    public static final EntityType<Evoker> EVOKER;
    public static final EntityType<EvokerFangs> EVOKER_FANGS;
    public static final EntityType<ExperienceOrb> EXPERIENCE_ORB;
    public static final EntityType<EyeOfEnder> EYE_OF_ENDER;
    public static final EntityType<FallingBlockEntity> FALLING_BLOCK;
    public static final EntityType<FireworkRocketEntity> FIREWORK_ROCKET;
    public static final EntityType<Fox> FOX;
    public static final EntityType<Ghast> GHAST;
    public static final EntityType<Giant> GIANT;
    public static final EntityType<Guardian> GUARDIAN;
    public static final EntityType<Hoglin> HOGLIN;
    public static final EntityType<Horse> HORSE;
    public static final EntityType<Husk> HUSK;
    public static final EntityType<Illusioner> ILLUSIONER;
    public static final EntityType<IronGolem> IRON_GOLEM;
    public static final EntityType<ItemEntity> ITEM;
    public static final EntityType<ItemFrame> ITEM_FRAME;
    public static final EntityType<LargeFireball> FIREBALL;
    public static final EntityType<LeashFenceKnotEntity> LEASH_KNOT;
    public static final EntityType<LightningBolt> LIGHTNING_BOLT;
    public static final EntityType<Llama> LLAMA;
    public static final EntityType<LlamaSpit> LLAMA_SPIT;
    public static final EntityType<MagmaCube> MAGMA_CUBE;
    public static final EntityType<Minecart> MINECART;
    public static final EntityType<MinecartChest> CHEST_MINECART;
    public static final EntityType<MinecartCommandBlock> COMMAND_BLOCK_MINECART;
    public static final EntityType<MinecartFurnace> FURNACE_MINECART;
    public static final EntityType<MinecartHopper> HOPPER_MINECART;
    public static final EntityType<MinecartSpawner> SPAWNER_MINECART;
    public static final EntityType<MinecartTNT> TNT_MINECART;
    public static final EntityType<Mule> MULE;
    public static final EntityType<MushroomCow> MOOSHROOM;
    public static final EntityType<Ocelot> OCELOT;
    public static final EntityType<Painting> PAINTING;
    public static final EntityType<Panda> PANDA;
    public static final EntityType<Parrot> PARROT;
    public static final EntityType<Phantom> PHANTOM;
    public static final EntityType<Pig> PIG;
    public static final EntityType<Piglin> PIGLIN;
    public static final EntityType<PiglinBrute> PIGLIN_BRUTE;
    public static final EntityType<Pillager> PILLAGER;
    public static final EntityType<PolarBear> POLAR_BEAR;
    public static final EntityType<PrimedTnt> TNT;
    public static final EntityType<Pufferfish> PUFFERFISH;
    public static final EntityType<Rabbit> RABBIT;
    public static final EntityType<Ravager> RAVAGER;
    public static final EntityType<Salmon> SALMON;
    public static final EntityType<Sheep> SHEEP;
    public static final EntityType<Shulker> SHULKER;
    public static final EntityType<ShulkerBullet> SHULKER_BULLET;
    public static final EntityType<Silverfish> SILVERFISH;
    public static final EntityType<Skeleton> SKELETON;
    public static final EntityType<SkeletonHorse> SKELETON_HORSE;
    public static final EntityType<Slime> SLIME;
    public static final EntityType<SmallFireball> SMALL_FIREBALL;
    public static final EntityType<SnowGolem> SNOW_GOLEM;
    public static final EntityType<Snowball> SNOWBALL;
    public static final EntityType<SpectralArrow> SPECTRAL_ARROW;
    public static final EntityType<Spider> SPIDER;
    public static final EntityType<Squid> SQUID;
    public static final EntityType<Stray> STRAY;
    public static final EntityType<Strider> STRIDER;
    public static final EntityType<ThrownEgg> EGG;
    public static final EntityType<ThrownEnderpearl> ENDER_PEARL;
    public static final EntityType<ThrownExperienceBottle> EXPERIENCE_BOTTLE;
    public static final EntityType<ThrownPotion> POTION;
    public static final EntityType<ThrownTrident> TRIDENT;
    public static final EntityType<TraderLlama> TRADER_LLAMA;
    public static final EntityType<TropicalFish> TROPICAL_FISH;
    public static final EntityType<Turtle> TURTLE;
    public static final EntityType<Vex> VEX;
    public static final EntityType<Villager> VILLAGER;
    public static final EntityType<Vindicator> VINDICATOR;
    public static final EntityType<WanderingTrader> WANDERING_TRADER;
    public static final EntityType<Witch> WITCH;
    public static final EntityType<WitherBoss> WITHER;
    public static final EntityType<WitherSkeleton> WITHER_SKELETON;
    public static final EntityType<WitherSkull> WITHER_SKULL;
    public static final EntityType<Wolf> WOLF;
    public static final EntityType<Zoglin> ZOGLIN;
    public static final EntityType<Zombie> ZOMBIE;
    public static final EntityType<ZombieHorse> ZOMBIE_HORSE;
    public static final EntityType<ZombieVillager> ZOMBIE_VILLAGER;
    public static final EntityType<ZombifiedPiglin> ZOMBIFIED_PIGLIN;
    public static final EntityType<Player> PLAYER;
    public static final EntityType<FishingHook> FISHING_BOBBER;
    private final EntityFactory<T> factory;
    private final MobCategory category;
    private final ImmutableSet<Block> immuneTo;
    private final boolean serialize;
    private final boolean summon;
    private final boolean fireImmune;
    private final boolean canSpawnFarFromPlayer;
    private final int clientTrackingRange;
    private final int updateInterval;
    @Nullable
    private String descriptionId;
    @Nullable
    private Component description;
    @Nullable
    private ResourceLocation lootTable;
    private final EntityDimensions dimensions;
    
    private static <T extends Entity> EntityType<T> register(final String string, final Builder<T> a) {
        return Registry.<EntityType<T>>register(Registry.ENTITY_TYPE, string, a.build(string));
    }
    
    public static ResourceLocation getKey(final EntityType<?> aqb) {
        return Registry.ENTITY_TYPE.getKey(aqb);
    }
    
    public static Optional<EntityType<?>> byString(final String string) {
        return Registry.ENTITY_TYPE.getOptional(ResourceLocation.tryParse(string));
    }
    
    public EntityType(final EntityFactory<T> b, final MobCategory aql, final boolean boolean3, final boolean boolean4, final boolean boolean5, final boolean boolean6, final ImmutableSet<Block> immutableSet, final EntityDimensions apy, final int integer9, final int integer10) {
        this.factory = b;
        this.category = aql;
        this.canSpawnFarFromPlayer = boolean6;
        this.serialize = boolean3;
        this.summon = boolean4;
        this.fireImmune = boolean5;
        this.immuneTo = immutableSet;
        this.dimensions = apy;
        this.clientTrackingRange = integer9;
        this.updateInterval = integer10;
    }
    
    @Nullable
    public Entity spawn(final ServerLevel aag, @Nullable final ItemStack bly, @Nullable final Player bft, final BlockPos fx, final MobSpawnType aqm, final boolean boolean6, final boolean boolean7) {
        return this.spawn(aag, (bly == null) ? null : bly.getTag(), (bly != null && bly.hasCustomHoverName()) ? bly.getHoverName() : null, bft, fx, aqm, boolean6, boolean7);
    }
    
    @Nullable
    public T spawn(final ServerLevel aag, @Nullable final CompoundTag md, @Nullable final Component nr, @Nullable final Player bft, final BlockPos fx, final MobSpawnType aqm, final boolean boolean7, final boolean boolean8) {
        final T apx10 = this.create(aag, md, nr, bft, fx, aqm, boolean7, boolean8);
        if (apx10 != null) {
            aag.addFreshEntityWithPassengers(apx10);
        }
        return apx10;
    }
    
    @Nullable
    public T create(final ServerLevel aag, @Nullable final CompoundTag md, @Nullable final Component nr, @Nullable final Player bft, final BlockPos fx, final MobSpawnType aqm, final boolean boolean7, final boolean boolean8) {
        final T apx10 = this.create(aag);
        if (apx10 == null) {
            return null;
        }
        double double11;
        if (boolean7) {
            apx10.setPos(fx.getX() + 0.5, fx.getY() + 1, fx.getZ() + 0.5);
            double11 = getYOffset(aag, fx, boolean8, apx10.getBoundingBox());
        }
        else {
            double11 = 0.0;
        }
        apx10.moveTo(fx.getX() + 0.5, fx.getY() + double11, fx.getZ() + 0.5, Mth.wrapDegrees(aag.random.nextFloat() * 360.0f), 0.0f);
        if (apx10 instanceof Mob) {
            final Mob aqk13 = (Mob)apx10;
            aqk13.yHeadRot = aqk13.yRot;
            aqk13.yBodyRot = aqk13.yRot;
            aqk13.finalizeSpawn(aag, aag.getCurrentDifficultyAt(aqk13.blockPosition()), aqm, null, md);
            aqk13.playAmbientSound();
        }
        if (nr != null && apx10 instanceof LivingEntity) {
            apx10.setCustomName(nr);
        }
        updateCustomEntityTag(aag, bft, apx10, md);
        return apx10;
    }
    
    protected static double getYOffset(final LevelReader brw, final BlockPos fx, final boolean boolean3, final AABB dcf) {
        AABB dcf2 = new AABB(fx);
        if (boolean3) {
            dcf2 = dcf2.expandTowards(0.0, -1.0, 0.0);
        }
        final Stream<VoxelShape> stream6 = brw.getCollisions(null, dcf2, (Predicate<Entity>)(apx -> true));
        return 1.0 + Shapes.collide(Direction.Axis.Y, dcf, stream6, boolean3 ? -2.0 : -1.0);
    }
    
    public static void updateCustomEntityTag(final Level bru, @Nullable final Player bft, @Nullable final Entity apx, @Nullable final CompoundTag md) {
        if (md == null || !md.contains("EntityTag", 10)) {
            return;
        }
        final MinecraftServer minecraftServer5 = bru.getServer();
        if (minecraftServer5 == null || apx == null) {
            return;
        }
        if (!bru.isClientSide && apx.onlyOpCanSetNbt() && (bft == null || !minecraftServer5.getPlayerList().isOp(bft.getGameProfile()))) {
            return;
        }
        final CompoundTag md2 = apx.saveWithoutId(new CompoundTag());
        final UUID uUID7 = apx.getUUID();
        md2.merge(md.getCompound("EntityTag"));
        apx.setUUID(uUID7);
        apx.load(md2);
    }
    
    public boolean canSerialize() {
        return this.serialize;
    }
    
    public boolean canSummon() {
        return this.summon;
    }
    
    public boolean fireImmune() {
        return this.fireImmune;
    }
    
    public boolean canSpawnFarFromPlayer() {
        return this.canSpawnFarFromPlayer;
    }
    
    public MobCategory getCategory() {
        return this.category;
    }
    
    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("entity", Registry.ENTITY_TYPE.getKey(this));
        }
        return this.descriptionId;
    }
    
    public Component getDescription() {
        if (this.description == null) {
            this.description = new TranslatableComponent(this.getDescriptionId());
        }
        return this.description;
    }
    
    public String toString() {
        return this.getDescriptionId();
    }
    
    public ResourceLocation getDefaultLootTable() {
        if (this.lootTable == null) {
            final ResourceLocation vk2 = Registry.ENTITY_TYPE.getKey(this);
            this.lootTable = new ResourceLocation(vk2.getNamespace(), "entities/" + vk2.getPath());
        }
        return this.lootTable;
    }
    
    public float getWidth() {
        return this.dimensions.width;
    }
    
    public float getHeight() {
        return this.dimensions.height;
    }
    
    @Nullable
    public T create(final Level bru) {
        return this.factory.create(this, bru);
    }
    
    public static Optional<Entity> create(final CompoundTag md, final Level bru) {
        return Util.<Entity>ifElse((java.util.Optional<Entity>)by(md).map(aqb -> aqb.create(bru)), (java.util.function.Consumer<Entity>)(apx -> apx.load(md)), () -> EntityType.LOGGER.warn("Skipping Entity with id {}", md.getString("id")));
    }
    
    public AABB getAABB(final double double1, final double double2, final double double3) {
        final float float8 = this.getWidth() / 2.0f;
        return new AABB(double1 - float8, double2, double3 - float8, double1 + float8, double2 + this.getHeight(), double3 + float8);
    }
    
    public boolean isBlockDangerous(final BlockState cee) {
        return !this.immuneTo.contains(cee.getBlock()) && ((!this.fireImmune && (cee.is(BlockTags.FIRE) || cee.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(cee) || cee.is(Blocks.LAVA))) || cee.is(Blocks.WITHER_ROSE) || cee.is(Blocks.SWEET_BERRY_BUSH) || cee.is(Blocks.CACTUS));
    }
    
    public EntityDimensions getDimensions() {
        return this.dimensions;
    }
    
    public static Optional<EntityType<?>> by(final CompoundTag md) {
        return Registry.ENTITY_TYPE.getOptional(new ResourceLocation(md.getString("id")));
    }
    
    @Nullable
    public static Entity loadEntityRecursive(final CompoundTag md, final Level bru, final Function<Entity, Entity> function) {
        return (Entity)loadStaticEntity(md, bru).map((Function)function).map(apx -> {
            if (md.contains("Passengers", 9)) {
                final ListTag mj5 = md.getList("Passengers", 10);
                for (int integer6 = 0; integer6 < mj5.size(); ++integer6) {
                    final Entity apx2 = loadEntityRecursive(mj5.getCompound(integer6), bru, function);
                    if (apx2 != null) {
                        apx2.startRiding(apx, true);
                    }
                }
            }
            return apx;
        }).orElse(null);
    }
    
    private static Optional<Entity> loadStaticEntity(final CompoundTag md, final Level bru) {
        try {
            return create(md, bru);
        }
        catch (RuntimeException runtimeException3) {
            EntityType.LOGGER.warn("Exception loading entity: ", (Throwable)runtimeException3);
            return (Optional<Entity>)Optional.empty();
        }
    }
    
    public int clientTrackingRange() {
        return this.clientTrackingRange;
    }
    
    public int updateInterval() {
        return this.updateInterval;
    }
    
    public boolean trackDeltas() {
        return this != EntityType.PLAYER && this != EntityType.LLAMA_SPIT && this != EntityType.WITHER && this != EntityType.BAT && this != EntityType.ITEM_FRAME && this != EntityType.LEASH_KNOT && this != EntityType.PAINTING && this != EntityType.END_CRYSTAL && this != EntityType.EVOKER_FANGS;
    }
    
    public boolean is(final Tag<EntityType<?>> aej) {
        return aej.contains(this);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        AREA_EFFECT_CLOUD = EntityType.<AreaEffectCloud>register("area_effect_cloud", Builder.<AreaEffectCloud>of(AreaEffectCloud::new, MobCategory.MISC).fireImmune().sized(6.0f, 0.5f).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE));
        ARMOR_STAND = EntityType.<ArmorStand>register("armor_stand", Builder.<ArmorStand>of(ArmorStand::new, MobCategory.MISC).sized(0.5f, 1.975f).clientTrackingRange(10));
        ARROW = EntityType.<Arrow>register("arrow", Builder.<Arrow>of(Arrow::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20));
        BAT = EntityType.<Bat>register("bat", Builder.<Bat>of(Bat::new, MobCategory.AMBIENT).sized(0.5f, 0.9f).clientTrackingRange(5));
        BEE = EntityType.<Bee>register("bee", Builder.<Bee>of(Bee::new, MobCategory.CREATURE).sized(0.7f, 0.6f).clientTrackingRange(8));
        BLAZE = EntityType.<Blaze>register("blaze", Builder.<Blaze>of(Blaze::new, MobCategory.MONSTER).fireImmune().sized(0.6f, 1.8f).clientTrackingRange(8));
        BOAT = EntityType.<Boat>register("boat", Builder.<Boat>of(Boat::new, MobCategory.MISC).sized(1.375f, 0.5625f).clientTrackingRange(10));
        CAT = EntityType.<Cat>register("cat", Builder.<Cat>of(Cat::new, MobCategory.CREATURE).sized(0.6f, 0.7f).clientTrackingRange(8));
        CAVE_SPIDER = EntityType.<CaveSpider>register("cave_spider", Builder.<CaveSpider>of(CaveSpider::new, MobCategory.MONSTER).sized(0.7f, 0.5f).clientTrackingRange(8));
        CHICKEN = EntityType.<Chicken>register("chicken", Builder.<Chicken>of(Chicken::new, MobCategory.CREATURE).sized(0.4f, 0.7f).clientTrackingRange(10));
        COD = EntityType.<Cod>register("cod", Builder.<Cod>of(Cod::new, MobCategory.WATER_AMBIENT).sized(0.5f, 0.3f).clientTrackingRange(4));
        COW = EntityType.<Cow>register("cow", Builder.<Cow>of(Cow::new, MobCategory.CREATURE).sized(0.9f, 1.4f).clientTrackingRange(10));
        CREEPER = EntityType.<Creeper>register("creeper", Builder.<Creeper>of(Creeper::new, MobCategory.MONSTER).sized(0.6f, 1.7f).clientTrackingRange(8));
        DOLPHIN = EntityType.<Dolphin>register("dolphin", Builder.<Dolphin>of(Dolphin::new, MobCategory.WATER_CREATURE).sized(0.9f, 0.6f));
        DONKEY = EntityType.<Donkey>register("donkey", Builder.<Donkey>of(Donkey::new, MobCategory.CREATURE).sized(1.3964844f, 1.5f).clientTrackingRange(10));
        DRAGON_FIREBALL = EntityType.<DragonFireball>register("dragon_fireball", Builder.<DragonFireball>of(DragonFireball::new, MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(4).updateInterval(10));
        DROWNED = EntityType.<Drowned>register("drowned", Builder.<Drowned>of(Drowned::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(8));
        ELDER_GUARDIAN = EntityType.<ElderGuardian>register("elder_guardian", Builder.<ElderGuardian>of(ElderGuardian::new, MobCategory.MONSTER).sized(1.9975f, 1.9975f).clientTrackingRange(10));
        END_CRYSTAL = EntityType.<EndCrystal>register("end_crystal", Builder.<EndCrystal>of(EndCrystal::new, MobCategory.MISC).sized(2.0f, 2.0f).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE));
        ENDER_DRAGON = EntityType.<EnderDragon>register("ender_dragon", Builder.<EnderDragon>of(EnderDragon::new, MobCategory.MONSTER).fireImmune().sized(16.0f, 8.0f).clientTrackingRange(10));
        ENDERMAN = EntityType.<EnderMan>register("enderman", Builder.<EnderMan>of(EnderMan::new, MobCategory.MONSTER).sized(0.6f, 2.9f).clientTrackingRange(8));
        ENDERMITE = EntityType.<Endermite>register("endermite", Builder.<Endermite>of(Endermite::new, MobCategory.MONSTER).sized(0.4f, 0.3f).clientTrackingRange(8));
        EVOKER = EntityType.<Evoker>register("evoker", Builder.<Evoker>of(Evoker::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(8));
        EVOKER_FANGS = EntityType.<EvokerFangs>register("evoker_fangs", Builder.<EvokerFangs>of(EvokerFangs::new, MobCategory.MISC).sized(0.5f, 0.8f).clientTrackingRange(6).updateInterval(2));
        EXPERIENCE_ORB = EntityType.<ExperienceOrb>register("experience_orb", Builder.<ExperienceOrb>of(ExperienceOrb::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(6).updateInterval(20));
        EYE_OF_ENDER = EntityType.<EyeOfEnder>register("eye_of_ender", Builder.<EyeOfEnder>of(EyeOfEnder::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(4));
        FALLING_BLOCK = EntityType.<FallingBlockEntity>register("falling_block", Builder.<FallingBlockEntity>of(FallingBlockEntity::new, MobCategory.MISC).sized(0.98f, 0.98f).clientTrackingRange(10).updateInterval(20));
        FIREWORK_ROCKET = EntityType.<FireworkRocketEntity>register("firework_rocket", Builder.<FireworkRocketEntity>of(FireworkRocketEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10));
        FOX = EntityType.<Fox>register("fox", Builder.<Fox>of(Fox::new, MobCategory.CREATURE).sized(0.6f, 0.7f).clientTrackingRange(8).immuneTo(Blocks.SWEET_BERRY_BUSH));
        GHAST = EntityType.<Ghast>register("ghast", Builder.<Ghast>of(Ghast::new, MobCategory.MONSTER).fireImmune().sized(4.0f, 4.0f).clientTrackingRange(10));
        GIANT = EntityType.<Giant>register("giant", Builder.<Giant>of(Giant::new, MobCategory.MONSTER).sized(3.6f, 12.0f).clientTrackingRange(10));
        GUARDIAN = EntityType.<Guardian>register("guardian", Builder.<Guardian>of(Guardian::new, MobCategory.MONSTER).sized(0.85f, 0.85f).clientTrackingRange(8));
        HOGLIN = EntityType.<Hoglin>register("hoglin", Builder.<Hoglin>of(Hoglin::new, MobCategory.MONSTER).sized(1.3964844f, 1.4f).clientTrackingRange(8));
        HORSE = EntityType.<Horse>register("horse", Builder.<Horse>of(Horse::new, MobCategory.CREATURE).sized(1.3964844f, 1.6f).clientTrackingRange(10));
        HUSK = EntityType.<Husk>register("husk", Builder.<Husk>of(Husk::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(8));
        ILLUSIONER = EntityType.<Illusioner>register("illusioner", Builder.<Illusioner>of(Illusioner::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(8));
        IRON_GOLEM = EntityType.<IronGolem>register("iron_golem", Builder.<IronGolem>of(IronGolem::new, MobCategory.MISC).sized(1.4f, 2.7f).clientTrackingRange(10));
        ITEM = EntityType.<ItemEntity>register("item", Builder.<ItemEntity>of(ItemEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(6).updateInterval(20));
        ITEM_FRAME = EntityType.<ItemFrame>register("item_frame", Builder.<ItemFrame>of(ItemFrame::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE));
        FIREBALL = EntityType.<LargeFireball>register("fireball", Builder.<LargeFireball>of(LargeFireball::new, MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(4).updateInterval(10));
        LEASH_KNOT = EntityType.<LeashFenceKnotEntity>register("leash_knot", Builder.<LeashFenceKnotEntity>of(LeashFenceKnotEntity::new, MobCategory.MISC).noSave().sized(0.5f, 0.5f).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE));
        LIGHTNING_BOLT = EntityType.<LightningBolt>register("lightning_bolt", Builder.<LightningBolt>of(LightningBolt::new, MobCategory.MISC).noSave().sized(0.0f, 0.0f).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE));
        LLAMA = EntityType.<Llama>register("llama", Builder.<Llama>of(Llama::new, MobCategory.CREATURE).sized(0.9f, 1.87f).clientTrackingRange(10));
        LLAMA_SPIT = EntityType.<LlamaSpit>register("llama_spit", Builder.<LlamaSpit>of(LlamaSpit::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10));
        MAGMA_CUBE = EntityType.<MagmaCube>register("magma_cube", Builder.<MagmaCube>of(MagmaCube::new, MobCategory.MONSTER).fireImmune().sized(2.04f, 2.04f).clientTrackingRange(8));
        MINECART = EntityType.<Minecart>register("minecart", Builder.<Minecart>of(Minecart::new, MobCategory.MISC).sized(0.98f, 0.7f).clientTrackingRange(8));
        CHEST_MINECART = EntityType.<MinecartChest>register("chest_minecart", Builder.<MinecartChest>of(MinecartChest::new, MobCategory.MISC).sized(0.98f, 0.7f).clientTrackingRange(8));
        COMMAND_BLOCK_MINECART = EntityType.<MinecartCommandBlock>register("command_block_minecart", Builder.<MinecartCommandBlock>of(MinecartCommandBlock::new, MobCategory.MISC).sized(0.98f, 0.7f).clientTrackingRange(8));
        FURNACE_MINECART = EntityType.<MinecartFurnace>register("furnace_minecart", Builder.<MinecartFurnace>of(MinecartFurnace::new, MobCategory.MISC).sized(0.98f, 0.7f).clientTrackingRange(8));
        HOPPER_MINECART = EntityType.<MinecartHopper>register("hopper_minecart", Builder.<MinecartHopper>of(MinecartHopper::new, MobCategory.MISC).sized(0.98f, 0.7f).clientTrackingRange(8));
        SPAWNER_MINECART = EntityType.<MinecartSpawner>register("spawner_minecart", Builder.<MinecartSpawner>of(MinecartSpawner::new, MobCategory.MISC).sized(0.98f, 0.7f).clientTrackingRange(8));
        TNT_MINECART = EntityType.<MinecartTNT>register("tnt_minecart", Builder.<MinecartTNT>of(MinecartTNT::new, MobCategory.MISC).sized(0.98f, 0.7f).clientTrackingRange(8));
        MULE = EntityType.<Mule>register("mule", Builder.<Mule>of(Mule::new, MobCategory.CREATURE).sized(1.3964844f, 1.6f).clientTrackingRange(8));
        MOOSHROOM = EntityType.<MushroomCow>register("mooshroom", Builder.<MushroomCow>of(MushroomCow::new, MobCategory.CREATURE).sized(0.9f, 1.4f).clientTrackingRange(10));
        OCELOT = EntityType.<Ocelot>register("ocelot", Builder.<Ocelot>of(Ocelot::new, MobCategory.CREATURE).sized(0.6f, 0.7f).clientTrackingRange(10));
        PAINTING = EntityType.<Painting>register("painting", Builder.<Painting>of(Painting::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE));
        PANDA = EntityType.<Panda>register("panda", Builder.<Panda>of(Panda::new, MobCategory.CREATURE).sized(1.3f, 1.25f).clientTrackingRange(10));
        PARROT = EntityType.<Parrot>register("parrot", Builder.<Parrot>of(Parrot::new, MobCategory.CREATURE).sized(0.5f, 0.9f).clientTrackingRange(8));
        PHANTOM = EntityType.<Phantom>register("phantom", Builder.<Phantom>of(Phantom::new, MobCategory.MONSTER).sized(0.9f, 0.5f).clientTrackingRange(8));
        PIG = EntityType.<Pig>register("pig", Builder.<Pig>of(Pig::new, MobCategory.CREATURE).sized(0.9f, 0.9f).clientTrackingRange(10));
        PIGLIN = EntityType.<Piglin>register("piglin", Builder.<Piglin>of(Piglin::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(8));
        PIGLIN_BRUTE = EntityType.<PiglinBrute>register("piglin_brute", Builder.<PiglinBrute>of(PiglinBrute::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(8));
        PILLAGER = EntityType.<Pillager>register("pillager", Builder.<Pillager>of(Pillager::new, MobCategory.MONSTER).canSpawnFarFromPlayer().sized(0.6f, 1.95f).clientTrackingRange(8));
        POLAR_BEAR = EntityType.<PolarBear>register("polar_bear", Builder.<PolarBear>of(PolarBear::new, MobCategory.CREATURE).sized(1.4f, 1.4f).clientTrackingRange(10));
        TNT = EntityType.<PrimedTnt>register("tnt", Builder.<PrimedTnt>of(PrimedTnt::new, MobCategory.MISC).fireImmune().sized(0.98f, 0.98f).clientTrackingRange(10).updateInterval(10));
        PUFFERFISH = EntityType.<Pufferfish>register("pufferfish", Builder.<Pufferfish>of(Pufferfish::new, MobCategory.WATER_AMBIENT).sized(0.7f, 0.7f).clientTrackingRange(4));
        RABBIT = EntityType.<Rabbit>register("rabbit", Builder.<Rabbit>of(Rabbit::new, MobCategory.CREATURE).sized(0.4f, 0.5f).clientTrackingRange(8));
        RAVAGER = EntityType.<Ravager>register("ravager", Builder.<Ravager>of(Ravager::new, MobCategory.MONSTER).sized(1.95f, 2.2f).clientTrackingRange(10));
        SALMON = EntityType.<Salmon>register("salmon", Builder.<Salmon>of(Salmon::new, MobCategory.WATER_AMBIENT).sized(0.7f, 0.4f).clientTrackingRange(4));
        SHEEP = EntityType.<Sheep>register("sheep", Builder.<Sheep>of(Sheep::new, MobCategory.CREATURE).sized(0.9f, 1.3f).clientTrackingRange(10));
        SHULKER = EntityType.<Shulker>register("shulker", Builder.<Shulker>of(Shulker::new, MobCategory.MONSTER).fireImmune().canSpawnFarFromPlayer().sized(1.0f, 1.0f).clientTrackingRange(10));
        SHULKER_BULLET = EntityType.<ShulkerBullet>register("shulker_bullet", Builder.<ShulkerBullet>of(ShulkerBullet::new, MobCategory.MISC).sized(0.3125f, 0.3125f).clientTrackingRange(8));
        SILVERFISH = EntityType.<Silverfish>register("silverfish", Builder.<Silverfish>of(Silverfish::new, MobCategory.MONSTER).sized(0.4f, 0.3f).clientTrackingRange(8));
        SKELETON = EntityType.<Skeleton>register("skeleton", Builder.<Skeleton>of(Skeleton::new, MobCategory.MONSTER).sized(0.6f, 1.99f).clientTrackingRange(8));
        SKELETON_HORSE = EntityType.<SkeletonHorse>register("skeleton_horse", Builder.<SkeletonHorse>of(SkeletonHorse::new, MobCategory.CREATURE).sized(1.3964844f, 1.6f).clientTrackingRange(10));
        SLIME = EntityType.<Slime>register("slime", Builder.<Slime>of(Slime::new, MobCategory.MONSTER).sized(2.04f, 2.04f).clientTrackingRange(10));
        SMALL_FIREBALL = EntityType.<SmallFireball>register("small_fireball", Builder.<SmallFireball>of(SmallFireball::new, MobCategory.MISC).sized(0.3125f, 0.3125f).clientTrackingRange(4).updateInterval(10));
        SNOW_GOLEM = EntityType.<SnowGolem>register("snow_golem", Builder.<SnowGolem>of(SnowGolem::new, MobCategory.MISC).sized(0.7f, 1.9f).clientTrackingRange(8));
        SNOWBALL = EntityType.<Snowball>register("snowball", Builder.<Snowball>of(Snowball::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10));
        SPECTRAL_ARROW = EntityType.<SpectralArrow>register("spectral_arrow", Builder.<SpectralArrow>of(SpectralArrow::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20));
        SPIDER = EntityType.<Spider>register("spider", Builder.<Spider>of(Spider::new, MobCategory.MONSTER).sized(1.4f, 0.9f).clientTrackingRange(8));
        SQUID = EntityType.<Squid>register("squid", Builder.<Squid>of(Squid::new, MobCategory.WATER_CREATURE).sized(0.8f, 0.8f).clientTrackingRange(8));
        STRAY = EntityType.<Stray>register("stray", Builder.<Stray>of(Stray::new, MobCategory.MONSTER).sized(0.6f, 1.99f).clientTrackingRange(8));
        STRIDER = EntityType.<Strider>register("strider", Builder.<Strider>of(Strider::new, MobCategory.CREATURE).fireImmune().sized(0.9f, 1.7f).clientTrackingRange(10));
        EGG = EntityType.<ThrownEgg>register("egg", Builder.<ThrownEgg>of(ThrownEgg::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10));
        ENDER_PEARL = EntityType.<ThrownEnderpearl>register("ender_pearl", Builder.<ThrownEnderpearl>of(ThrownEnderpearl::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10));
        EXPERIENCE_BOTTLE = EntityType.<ThrownExperienceBottle>register("experience_bottle", Builder.<ThrownExperienceBottle>of(ThrownExperienceBottle::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10));
        POTION = EntityType.<ThrownPotion>register("potion", Builder.<ThrownPotion>of(ThrownPotion::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10));
        TRIDENT = EntityType.<ThrownTrident>register("trident", Builder.<ThrownTrident>of(ThrownTrident::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20));
        TRADER_LLAMA = EntityType.<TraderLlama>register("trader_llama", Builder.<TraderLlama>of(TraderLlama::new, MobCategory.CREATURE).sized(0.9f, 1.87f).clientTrackingRange(10));
        TROPICAL_FISH = EntityType.<TropicalFish>register("tropical_fish", Builder.<TropicalFish>of(TropicalFish::new, MobCategory.WATER_AMBIENT).sized(0.5f, 0.4f).clientTrackingRange(4));
        TURTLE = EntityType.<Turtle>register("turtle", Builder.<Turtle>of(Turtle::new, MobCategory.CREATURE).sized(1.2f, 0.4f).clientTrackingRange(10));
        VEX = EntityType.<Vex>register("vex", Builder.<Vex>of(Vex::new, MobCategory.MONSTER).fireImmune().sized(0.4f, 0.8f).clientTrackingRange(8));
        VILLAGER = EntityType.<Villager>register("villager", Builder.<Villager>of(Villager::new, MobCategory.MISC).sized(0.6f, 1.95f).clientTrackingRange(10));
        VINDICATOR = EntityType.<Vindicator>register("vindicator", Builder.<Vindicator>of(Vindicator::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(8));
        WANDERING_TRADER = EntityType.<WanderingTrader>register("wandering_trader", Builder.<WanderingTrader>of(WanderingTrader::new, MobCategory.CREATURE).sized(0.6f, 1.95f).clientTrackingRange(10));
        WITCH = EntityType.<Witch>register("witch", Builder.<Witch>of(Witch::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(8));
        WITHER = EntityType.<WitherBoss>register("wither", Builder.<WitherBoss>of(WitherBoss::new, MobCategory.MONSTER).fireImmune().immuneTo(Blocks.WITHER_ROSE).sized(0.9f, 3.5f).clientTrackingRange(10));
        WITHER_SKELETON = EntityType.<WitherSkeleton>register("wither_skeleton", Builder.<WitherSkeleton>of(WitherSkeleton::new, MobCategory.MONSTER).fireImmune().immuneTo(Blocks.WITHER_ROSE).sized(0.7f, 2.4f).clientTrackingRange(8));
        WITHER_SKULL = EntityType.<WitherSkull>register("wither_skull", Builder.<WitherSkull>of(WitherSkull::new, MobCategory.MISC).sized(0.3125f, 0.3125f).clientTrackingRange(4).updateInterval(10));
        WOLF = EntityType.<Wolf>register("wolf", Builder.<Wolf>of(Wolf::new, MobCategory.CREATURE).sized(0.6f, 0.85f).clientTrackingRange(10));
        ZOGLIN = EntityType.<Zoglin>register("zoglin", Builder.<Zoglin>of(Zoglin::new, MobCategory.MONSTER).fireImmune().sized(1.3964844f, 1.4f).clientTrackingRange(8));
        ZOMBIE = EntityType.<Zombie>register("zombie", Builder.<Zombie>of(Zombie::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(8));
        ZOMBIE_HORSE = EntityType.<ZombieHorse>register("zombie_horse", Builder.<ZombieHorse>of(ZombieHorse::new, MobCategory.CREATURE).sized(1.3964844f, 1.6f).clientTrackingRange(10));
        ZOMBIE_VILLAGER = EntityType.<ZombieVillager>register("zombie_villager", Builder.<ZombieVillager>of(ZombieVillager::new, MobCategory.MONSTER).sized(0.6f, 1.95f).clientTrackingRange(8));
        ZOMBIFIED_PIGLIN = EntityType.<ZombifiedPiglin>register("zombified_piglin", Builder.<ZombifiedPiglin>of(ZombifiedPiglin::new, MobCategory.MONSTER).fireImmune().sized(0.6f, 1.95f).clientTrackingRange(8));
        PLAYER = EntityType.<Player>register("player", (Builder<Player>)Builder.<T>createNothing(MobCategory.MISC).noSave().noSummon().sized(0.6f, 1.8f).clientTrackingRange(32).updateInterval(2));
        FISHING_BOBBER = EntityType.<FishingHook>register("fishing_bobber", (Builder<FishingHook>)Builder.<T>createNothing(MobCategory.MISC).noSave().noSummon().sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(5));
    }
    
    public static class Builder<T extends Entity> {
        private final EntityFactory<T> factory;
        private final MobCategory category;
        private ImmutableSet<Block> immuneTo;
        private boolean serialize;
        private boolean summon;
        private boolean fireImmune;
        private boolean canSpawnFarFromPlayer;
        private int clientTrackingRange;
        private int updateInterval;
        private EntityDimensions dimensions;
        
        private Builder(final EntityFactory<T> b, final MobCategory aql) {
            this.immuneTo = ImmutableSet.<Block>of();
            this.serialize = true;
            this.summon = true;
            this.clientTrackingRange = 5;
            this.updateInterval = 3;
            this.dimensions = EntityDimensions.scalable(0.6f, 1.8f);
            this.factory = b;
            this.category = aql;
            this.canSpawnFarFromPlayer = (aql == MobCategory.CREATURE || aql == MobCategory.MISC);
        }
        
        public static <T extends Entity> Builder<T> of(final EntityFactory<T> b, final MobCategory aql) {
            return new Builder<T>(b, aql);
        }
        
        public static <T extends Entity> Builder<T> createNothing(final MobCategory aql) {
            return new Builder<T>((aqb, bru) -> null, aql);
        }
        
        public Builder<T> sized(final float float1, final float float2) {
            this.dimensions = EntityDimensions.scalable(float1, float2);
            return this;
        }
        
        public Builder<T> noSummon() {
            this.summon = false;
            return this;
        }
        
        public Builder<T> noSave() {
            this.serialize = false;
            return this;
        }
        
        public Builder<T> fireImmune() {
            this.fireImmune = true;
            return this;
        }
        
        public Builder<T> immuneTo(final Block... arr) {
            this.immuneTo = ImmutableSet.<Block>copyOf(arr);
            return this;
        }
        
        public Builder<T> canSpawnFarFromPlayer() {
            this.canSpawnFarFromPlayer = true;
            return this;
        }
        
        public Builder<T> clientTrackingRange(final int integer) {
            this.clientTrackingRange = integer;
            return this;
        }
        
        public Builder<T> updateInterval(final int integer) {
            this.updateInterval = integer;
            return this;
        }
        
        public EntityType<T> build(final String string) {
            if (this.serialize) {
                Util.fetchChoiceType(References.ENTITY_TREE, string);
            }
            return new EntityType<T>(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions, this.clientTrackingRange, this.updateInterval);
        }
    }
    
    public static class Builder<T extends Entity> {
        private final EntityFactory<T> factory;
        private final MobCategory category;
        private ImmutableSet<Block> immuneTo;
        private boolean serialize;
        private boolean summon;
        private boolean fireImmune;
        private boolean canSpawnFarFromPlayer;
        private int clientTrackingRange;
        private int updateInterval;
        private EntityDimensions dimensions;
        
        private Builder(final EntityFactory<T> b, final MobCategory aql) {
            this.immuneTo = ImmutableSet.<Block>of();
            this.serialize = true;
            this.summon = true;
            this.clientTrackingRange = 5;
            this.updateInterval = 3;
            this.dimensions = EntityDimensions.scalable(0.6f, 1.8f);
            this.factory = b;
            this.category = aql;
            this.canSpawnFarFromPlayer = (aql == MobCategory.CREATURE || aql == MobCategory.MISC);
        }
        
        public static <T extends Entity> Builder<T> of(final EntityFactory<T> b, final MobCategory aql) {
            return new Builder<T>(b, aql);
        }
        
        public static <T extends Entity> Builder<T> createNothing(final MobCategory aql) {
            return new Builder<T>((aqb, bru) -> null, aql);
        }
        
        public Builder<T> sized(final float float1, final float float2) {
            this.dimensions = EntityDimensions.scalable(float1, float2);
            return this;
        }
        
        public Builder<T> noSummon() {
            this.summon = false;
            return this;
        }
        
        public Builder<T> noSave() {
            this.serialize = false;
            return this;
        }
        
        public Builder<T> fireImmune() {
            this.fireImmune = true;
            return this;
        }
        
        public Builder<T> immuneTo(final Block... arr) {
            this.immuneTo = ImmutableSet.<Block>copyOf(arr);
            return this;
        }
        
        public Builder<T> canSpawnFarFromPlayer() {
            this.canSpawnFarFromPlayer = true;
            return this;
        }
        
        public Builder<T> clientTrackingRange(final int integer) {
            this.clientTrackingRange = integer;
            return this;
        }
        
        public Builder<T> updateInterval(final int integer) {
            this.updateInterval = integer;
            return this;
        }
        
        public EntityType<T> build(final String string) {
            if (this.serialize) {
                Util.fetchChoiceType(References.ENTITY_TREE, string);
            }
            return new EntityType<T>(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions, this.clientTrackingRange, this.updateInterval);
        }
    }
    
    public interface EntityFactory<T extends Entity> {
        T create(final EntityType<T> aqb, final Level bru);
    }
}

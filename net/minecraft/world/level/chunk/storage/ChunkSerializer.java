package net.minecraft.world.level.chunk.storage;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.material.Fluids;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import net.minecraft.nbt.ShortTag;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Locale;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.EntityType;
import javax.annotation.Nullable;
import net.minecraft.nbt.LongArrayTag;
import java.util.Map;
import java.util.Collection;
import net.minecraft.world.entity.Entity;
import java.util.Arrays;
import net.minecraft.nbt.Tag;
import net.minecraft.SharedConstants;
import java.util.Iterator;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.TickList;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import java.util.BitSet;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import java.util.Set;
import java.util.EnumSet;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.BlockPos;
import java.util.function.Consumer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.world.level.ChunkTickList;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.Fluid;
import java.util.function.Predicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ProtoTickList;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.IdMap;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import java.util.Objects;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.server.level.ServerLevel;
import org.apache.logging.log4j.Logger;

public class ChunkSerializer {
    private static final Logger LOGGER;
    
    public static ProtoChunk read(final ServerLevel aag, final StructureManager cst, final PoiManager azl, final ChunkPos bra, final CompoundTag md) {
        final ChunkGenerator cfv6 = aag.getChunkSource().getGenerator();
        final BiomeSource bsv7 = cfv6.getBiomeSource();
        final CompoundTag md2 = md.getCompound("Level");
        final ChunkPos bra2 = new ChunkPos(md2.getInt("xPos"), md2.getInt("zPos"));
        if (!Objects.equals(bra, bra2)) {
            ChunkSerializer.LOGGER.error("Chunk file at {} is in the wrong location; relocating. (Expected {}, got {})", bra, bra, bra2);
        }
        final ChunkBiomeContainer cfu10 = new ChunkBiomeContainer(aag.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY), bra, bsv7, (int[])(md2.contains("Biomes", 11) ? md2.getIntArray("Biomes") : null));
        final UpgradeData cgo11 = md2.contains("UpgradeData", 10) ? new UpgradeData(md2.getCompound("UpgradeData")) : UpgradeData.EMPTY;
        final ProtoTickList<Block> cgn12 = new ProtoTickList<Block>((java.util.function.Predicate<Block>)(bul -> bul == null || bul.defaultBlockState().isAir()), bra, md2.getList("ToBeTicked", 9));
        final ProtoTickList<Fluid> cgn13 = new ProtoTickList<Fluid>((java.util.function.Predicate<Fluid>)(cut -> cut == null || cut == Fluids.EMPTY), bra, md2.getList("LiquidsToBeTicked", 9));
        final boolean boolean14 = md2.getBoolean("isLightOn");
        final ListTag mj15 = md2.getList("Sections", 10);
        final int integer16 = 16;
        final LevelChunkSection[] arr17 = new LevelChunkSection[16];
        final boolean boolean15 = aag.dimensionType().hasSkyLight();
        final ChunkSource cfw19 = aag.getChunkSource();
        final LevelLightEngine cul20 = cfw19.getLightEngine();
        if (boolean14) {
            cul20.retainData(bra, true);
        }
        for (int integer17 = 0; integer17 < mj15.size(); ++integer17) {
            final CompoundTag md3 = mj15.getCompound(integer17);
            final int integer18 = md3.getByte("Y");
            if (md3.contains("Palette", 9) && md3.contains("BlockStates", 12)) {
                final LevelChunkSection cgf24 = new LevelChunkSection(integer18 << 4);
                cgf24.getStates().read(md3.getList("Palette", 10), md3.getLongArray("BlockStates"));
                cgf24.recalcBlockCounts();
                if (!cgf24.isEmpty()) {
                    arr17[integer18] = cgf24;
                }
                azl.checkConsistencyWithBlocks(bra, cgf24);
            }
            if (boolean14) {
                if (md3.contains("BlockLight", 7)) {
                    cul20.queueSectionData(LightLayer.BLOCK, SectionPos.of(bra, integer18), new DataLayer(md3.getByteArray("BlockLight")), true);
                }
                if (boolean15 && md3.contains("SkyLight", 7)) {
                    cul20.queueSectionData(LightLayer.SKY, SectionPos.of(bra, integer18), new DataLayer(md3.getByteArray("SkyLight")), true);
                }
            }
        }
        final long long21 = md2.getLong("InhabitedTime");
        final ChunkStatus.ChunkType a23 = getChunkTypeFromTag(md);
        ChunkAccess cft24;
        if (a23 == ChunkStatus.ChunkType.LEVELCHUNK) {
            TickList<Block> bsl25;
            if (md2.contains("TileTicks", 9)) {
                bsl25 = ChunkTickList.create(md2.getList("TileTicks", 10), (java.util.function.Function<Object, ResourceLocation>)Registry.BLOCK::getKey, (java.util.function.Function<ResourceLocation, Object>)Registry.BLOCK::get);
            }
            else {
                bsl25 = cgn12;
            }
            TickList<Fluid> bsl26;
            if (md2.contains("LiquidTicks", 9)) {
                bsl26 = ChunkTickList.create(md2.getList("LiquidTicks", 10), (java.util.function.Function<Object, ResourceLocation>)Registry.FLUID::getKey, (java.util.function.Function<ResourceLocation, Object>)Registry.FLUID::get);
            }
            else {
                bsl26 = cgn13;
            }
            cft24 = new LevelChunk(aag.getLevel(), bra, cfu10, cgo11, bsl25, bsl26, long21, arr17, (Consumer<LevelChunk>)(cge -> postLoadChunk(md2, cge)));
        }
        else {
            final ProtoChunk cgm25 = new ProtoChunk(bra, cgo11, arr17, cgn12, cgn13);
            cgm25.setBiomes(cfu10);
            cft24 = cgm25;
            cft24.setInhabitedTime(long21);
            cgm25.setStatus(ChunkStatus.byName(md2.getString("Status")));
            if (cft24.getStatus().isOrAfter(ChunkStatus.FEATURES)) {
                cgm25.setLightEngine(cul20);
            }
            if (!boolean14 && cft24.getStatus().isOrAfter(ChunkStatus.LIGHT)) {
                for (final BlockPos fx27 : BlockPos.betweenClosed(bra.getMinBlockX(), 0, bra.getMinBlockZ(), bra.getMaxBlockX(), 255, bra.getMaxBlockZ())) {
                    if (cft24.getBlockState(fx27).getLightEmission() != 0) {
                        cgm25.addLight(fx27);
                    }
                }
            }
        }
        cft24.setLightCorrect(boolean14);
        final CompoundTag md4 = md2.getCompound("Heightmaps");
        final EnumSet<Heightmap.Types> enumSet26 = (EnumSet<Heightmap.Types>)EnumSet.noneOf((Class)Heightmap.Types.class);
        for (final Heightmap.Types a24 : cft24.getStatus().heightmapsAfter()) {
            final String string29 = a24.getSerializationKey();
            if (md4.contains(string29, 12)) {
                cft24.setHeightmap(a24, md4.getLongArray(string29));
            }
            else {
                enumSet26.add(a24);
            }
        }
        Heightmap.primeHeightmaps(cft24, (Set<Heightmap.Types>)enumSet26);
        final CompoundTag md5 = md2.getCompound("Structures");
        cft24.setAllStarts(unpackStructureStart(cst, md5, aag.getSeed()));
        cft24.setAllReferences(unpackStructureReferences(bra, md5));
        if (md2.getBoolean("shouldSave")) {
            cft24.setUnsaved(true);
        }
        final ListTag mj16 = md2.getList("PostProcessing", 9);
        for (int integer19 = 0; integer19 < mj16.size(); ++integer19) {
            final ListTag mj17 = mj16.getList(integer19);
            for (int integer20 = 0; integer20 < mj17.size(); ++integer20) {
                cft24.addPackedPostProcess(mj17.getShort(integer20), integer19);
            }
        }
        if (a23 == ChunkStatus.ChunkType.LEVELCHUNK) {
            return new ImposterProtoChunk((LevelChunk)cft24);
        }
        final ProtoChunk cgm26 = (ProtoChunk)cft24;
        final ListTag mj17 = md2.getList("Entities", 10);
        for (int integer20 = 0; integer20 < mj17.size(); ++integer20) {
            cgm26.addEntity(mj17.getCompound(integer20));
        }
        final ListTag mj18 = md2.getList("TileEntities", 10);
        for (int integer21 = 0; integer21 < mj18.size(); ++integer21) {
            final CompoundTag md6 = mj18.getCompound(integer21);
            cft24.setBlockEntityNbt(md6);
        }
        final ListTag mj19 = md2.getList("Lights", 9);
        for (int integer22 = 0; integer22 < mj19.size(); ++integer22) {
            final ListTag mj20 = mj19.getList(integer22);
            for (int integer23 = 0; integer23 < mj20.size(); ++integer23) {
                cgm26.addLight(mj20.getShort(integer23), integer22);
            }
        }
        final CompoundTag md6 = md2.getCompound("CarvingMasks");
        for (final String string30 : md6.getAllKeys()) {
            final GenerationStep.Carving a25 = GenerationStep.Carving.valueOf(string30);
            cgm26.setCarvingMask(a25, BitSet.valueOf(md6.getByteArray(string30)));
        }
        return cgm26;
    }
    
    public static CompoundTag write(final ServerLevel aag, final ChunkAccess cft) {
        final ChunkPos bra3 = cft.getPos();
        final CompoundTag md4 = new CompoundTag();
        final CompoundTag md5 = new CompoundTag();
        md4.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
        md4.put("Level", (Tag)md5);
        md5.putInt("xPos", bra3.x);
        md5.putInt("zPos", bra3.z);
        md5.putLong("LastUpdate", aag.getGameTime());
        md5.putLong("InhabitedTime", cft.getInhabitedTime());
        md5.putString("Status", cft.getStatus().getName());
        final UpgradeData cgo6 = cft.getUpgradeData();
        if (!cgo6.isEmpty()) {
            md5.put("UpgradeData", (Tag)cgo6.write());
        }
        final LevelChunkSection[] arr7 = cft.getSections();
        final ListTag mj8 = new ListTag();
        final LevelLightEngine cul9 = aag.getChunkSource().getLightEngine();
        final boolean boolean10 = cft.isLightCorrect();
        for (int integer11 = -1; integer11 < 17; ++integer11) {
            final int integer12 = integer11;
            final LevelChunkSection cgf13 = (LevelChunkSection)Arrays.stream((Object[])arr7).filter(cgf -> cgf != null && cgf.bottomBlockY() >> 4 == integer12).findFirst().orElse(LevelChunk.EMPTY_SECTION);
            final DataLayer cfy14 = cul9.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(bra3, integer12));
            final DataLayer cfy15 = cul9.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(bra3, integer12));
            if (cgf13 != LevelChunk.EMPTY_SECTION || cfy14 != null || cfy15 != null) {
                final CompoundTag md6 = new CompoundTag();
                md6.putByte("Y", (byte)(integer12 & 0xFF));
                if (cgf13 != LevelChunk.EMPTY_SECTION) {
                    cgf13.getStates().write(md6, "Palette", "BlockStates");
                }
                if (cfy14 != null && !cfy14.isEmpty()) {
                    md6.putByteArray("BlockLight", cfy14.getData());
                }
                if (cfy15 != null && !cfy15.isEmpty()) {
                    md6.putByteArray("SkyLight", cfy15.getData());
                }
                mj8.add(md6);
            }
        }
        md5.put("Sections", (Tag)mj8);
        if (boolean10) {
            md5.putBoolean("isLightOn", true);
        }
        final ChunkBiomeContainer cfu11 = cft.getBiomes();
        if (cfu11 != null) {
            md5.putIntArray("Biomes", cfu11.writeBiomes());
        }
        final ListTag mj9 = new ListTag();
        for (final BlockPos fx14 : cft.getBlockEntitiesPos()) {
            final CompoundTag md7 = cft.getBlockEntityNbtForSaving(fx14);
            if (md7 != null) {
                mj9.add(md7);
            }
        }
        md5.put("TileEntities", (Tag)mj9);
        final ListTag mj10 = new ListTag();
        if (cft.getStatus().getChunkType() == ChunkStatus.ChunkType.LEVELCHUNK) {
            final LevelChunk cge14 = (LevelChunk)cft;
            cge14.setLastSaveHadEntities(false);
            for (int integer13 = 0; integer13 < cge14.getEntitySections().length; ++integer13) {
                for (final Entity apx17 : cge14.getEntitySections()[integer13]) {
                    final CompoundTag md8 = new CompoundTag();
                    if (apx17.save(md8)) {
                        cge14.setLastSaveHadEntities(true);
                        mj10.add(md8);
                    }
                }
            }
        }
        else {
            final ProtoChunk cgm14 = (ProtoChunk)cft;
            mj10.addAll((Collection)cgm14.getEntities());
            md5.put("Lights", (Tag)packOffsets(cgm14.getPackedLights()));
            final CompoundTag md7 = new CompoundTag();
            for (final GenerationStep.Carving a19 : GenerationStep.Carving.values()) {
                final BitSet bitSet20 = cgm14.getCarvingMask(a19);
                if (bitSet20 != null) {
                    md7.putByteArray(a19.toString(), bitSet20.toByteArray());
                }
            }
            md5.put("CarvingMasks", (Tag)md7);
        }
        md5.put("Entities", (Tag)mj10);
        final TickList<Block> bsl14 = cft.getBlockTicks();
        if (bsl14 instanceof ProtoTickList) {
            md5.put("ToBeTicked", (Tag)((ProtoTickList)bsl14).save());
        }
        else if (bsl14 instanceof ChunkTickList) {
            md5.put("TileTicks", (Tag)((ChunkTickList)bsl14).save());
        }
        else {
            md5.put("TileTicks", (Tag)aag.getBlockTicks().save(bra3));
        }
        final TickList<Fluid> bsl15 = cft.getLiquidTicks();
        if (bsl15 instanceof ProtoTickList) {
            md5.put("LiquidsToBeTicked", (Tag)((ProtoTickList)bsl15).save());
        }
        else if (bsl15 instanceof ChunkTickList) {
            md5.put("LiquidTicks", (Tag)((ChunkTickList)bsl15).save());
        }
        else {
            md5.put("LiquidTicks", (Tag)aag.getLiquidTicks().save(bra3));
        }
        md5.put("PostProcessing", (Tag)packOffsets(cft.getPostProcessing()));
        final CompoundTag md6 = new CompoundTag();
        for (final Map.Entry<Heightmap.Types, Heightmap> entry18 : cft.getHeightmaps()) {
            if (cft.getStatus().heightmapsAfter().contains(entry18.getKey())) {
                md6.put(((Heightmap.Types)entry18.getKey()).getSerializationKey(), new LongArrayTag(((Heightmap)entry18.getValue()).getRawData()));
            }
        }
        md5.put("Heightmaps", (Tag)md6);
        md5.put("Structures", (Tag)packStructureData(bra3, cft.getAllStarts(), cft.getAllReferences()));
        return md4;
    }
    
    public static ChunkStatus.ChunkType getChunkTypeFromTag(@Nullable final CompoundTag md) {
        if (md != null) {
            final ChunkStatus cfx2 = ChunkStatus.byName(md.getCompound("Level").getString("Status"));
            if (cfx2 != null) {
                return cfx2.getChunkType();
            }
        }
        return ChunkStatus.ChunkType.PROTOCHUNK;
    }
    
    private static void postLoadChunk(final CompoundTag md, final LevelChunk cge) {
        final ListTag mj3 = md.getList("Entities", 10);
        final Level bru4 = cge.getLevel();
        for (int integer5 = 0; integer5 < mj3.size(); ++integer5) {
            final CompoundTag md2 = mj3.getCompound(integer5);
            EntityType.loadEntityRecursive(md2, bru4, (Function<Entity, Entity>)(apx -> {
                cge.addEntity(apx);
                return apx;
            }));
            cge.setLastSaveHadEntities(true);
        }
        final ListTag mj4 = md.getList("TileEntities", 10);
        for (int integer6 = 0; integer6 < mj4.size(); ++integer6) {
            final CompoundTag md3 = mj4.getCompound(integer6);
            final boolean boolean8 = md3.getBoolean("keepPacked");
            if (boolean8) {
                cge.setBlockEntityNbt(md3);
            }
            else {
                final BlockPos fx9 = new BlockPos(md3.getInt("x"), md3.getInt("y"), md3.getInt("z"));
                final BlockEntity ccg10 = BlockEntity.loadStatic(cge.getBlockState(fx9), md3);
                if (ccg10 != null) {
                    cge.addBlockEntity(ccg10);
                }
            }
        }
    }
    
    private static CompoundTag packStructureData(final ChunkPos bra, final Map<StructureFeature<?>, StructureStart<?>> map2, final Map<StructureFeature<?>, LongSet> map3) {
        final CompoundTag md4 = new CompoundTag();
        final CompoundTag md5 = new CompoundTag();
        for (final Map.Entry<StructureFeature<?>, StructureStart<?>> entry7 : map2.entrySet()) {
            md5.put(((StructureFeature)entry7.getKey()).getFeatureName(), ((StructureStart)entry7.getValue()).createTag(bra.x, bra.z));
        }
        md4.put("Starts", (Tag)md5);
        final CompoundTag md6 = new CompoundTag();
        for (final Map.Entry<StructureFeature<?>, LongSet> entry8 : map3.entrySet()) {
            md6.put(((StructureFeature)entry8.getKey()).getFeatureName(), new LongArrayTag((LongSet)entry8.getValue()));
        }
        md4.put("References", (Tag)md6);
        return md4;
    }
    
    private static Map<StructureFeature<?>, StructureStart<?>> unpackStructureStart(final StructureManager cst, final CompoundTag md, final long long3) {
        final Map<StructureFeature<?>, StructureStart<?>> map5 = Maps.newHashMap();
        final CompoundTag md2 = md.getCompound("Starts");
        for (final String string8 : md2.getAllKeys()) {
            final String string9 = string8.toLowerCase(Locale.ROOT);
            final StructureFeature<?> ckx10 = StructureFeature.STRUCTURES_REGISTRY.get(string9);
            if (ckx10 == null) {
                ChunkSerializer.LOGGER.error("Unknown structure start: {}", string9);
            }
            else {
                final StructureStart<?> crs11 = StructureFeature.loadStaticStart(cst, md2.getCompound(string8), long3);
                if (crs11 == null) {
                    continue;
                }
                map5.put(ckx10, crs11);
            }
        }
        return map5;
    }
    
    private static Map<StructureFeature<?>, LongSet> unpackStructureReferences(final ChunkPos bra, final CompoundTag md) {
        final Map<StructureFeature<?>, LongSet> map3 = Maps.newHashMap();
        final CompoundTag md2 = md.getCompound("References");
        for (final String string6 : md2.getAllKeys()) {
            map3.put(StructureFeature.STRUCTURES_REGISTRY.get(string6.toLowerCase(Locale.ROOT)), new LongOpenHashSet(Arrays.stream(md2.getLongArray(string6)).filter(long3 -> {
                final ChunkPos bra2 = new ChunkPos(long3);
                if (bra2.getChessboardDistance(bra) > 8) {
                    ChunkSerializer.LOGGER.warn("Found invalid structure reference [ {} @ {} ] for chunk {}.", (Object)string6, (Object)bra2, (Object)bra);
                    return false;
                }
                return true;
            }).toArray()));
        }
        return map3;
    }
    
    public static ListTag packOffsets(final ShortList[] arr) {
        final ListTag mj2 = new ListTag();
        for (final ShortList shortList6 : arr) {
            final ListTag mj3 = new ListTag();
            if (shortList6 != null) {
                for (final Short short9 : shortList6) {
                    mj3.add(ShortTag.valueOf(short9));
                }
            }
            mj2.add(mj3);
        }
        return mj2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}

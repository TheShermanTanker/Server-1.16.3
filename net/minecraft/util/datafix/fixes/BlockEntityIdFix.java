package net.minecraft.util.datafix.fixes;

import java.util.function.Consumer;
import com.mojang.datafixers.DataFixUtils;
import com.google.common.collect.Maps;
import java.util.HashMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import com.mojang.datafixers.DataFix;

public class BlockEntityIdFix extends DataFix {
    private static final Map<String, String> ID_MAP;
    
    public BlockEntityIdFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.ITEM_STACK);
        final Type<?> type3 = this.getOutputSchema().getType(References.ITEM_STACK);
        final TaggedChoice.TaggedChoiceType<String> taggedChoiceType4 = (TaggedChoice.TaggedChoiceType<String>)this.getInputSchema().findChoiceType(References.BLOCK_ENTITY);
        final TaggedChoice.TaggedChoiceType<String> taggedChoiceType5 = (TaggedChoice.TaggedChoiceType<String>)this.getOutputSchema().findChoiceType(References.BLOCK_ENTITY);
        return TypeRewriteRule.seq(this.convertUnchecked("item stack block entity name hook converter", type2, type3), this.<String, String>fixTypeEverywhere("BlockEntityIdFix", (Type<String>)taggedChoiceType4, (Type<String>)taggedChoiceType5, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<String, String>>)(dynamicOps -> pair -> pair.mapFirst(string -> (String)BlockEntityIdFix.ID_MAP.getOrDefault(string, string)))));
    }
    
    static {
        ID_MAP = DataFixUtils.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put("Airportal", "minecraft:end_portal");
            hashMap.put("Banner", "minecraft:banner");
            hashMap.put("Beacon", "minecraft:beacon");
            hashMap.put("Cauldron", "minecraft:brewing_stand");
            hashMap.put("Chest", "minecraft:chest");
            hashMap.put("Comparator", "minecraft:comparator");
            hashMap.put("Control", "minecraft:command_block");
            hashMap.put("DLDetector", "minecraft:daylight_detector");
            hashMap.put("Dropper", "minecraft:dropper");
            hashMap.put("EnchantTable", "minecraft:enchanting_table");
            hashMap.put("EndGateway", "minecraft:end_gateway");
            hashMap.put("EnderChest", "minecraft:ender_chest");
            hashMap.put("FlowerPot", "minecraft:flower_pot");
            hashMap.put("Furnace", "minecraft:furnace");
            hashMap.put("Hopper", "minecraft:hopper");
            hashMap.put("MobSpawner", "minecraft:mob_spawner");
            hashMap.put("Music", "minecraft:noteblock");
            hashMap.put("Piston", "minecraft:piston");
            hashMap.put("RecordPlayer", "minecraft:jukebox");
            hashMap.put("Sign", "minecraft:sign");
            hashMap.put("Skull", "minecraft:skull");
            hashMap.put("Structure", "minecraft:structure_block");
            hashMap.put("Trap", "minecraft:dispenser");
        }));
    }
}

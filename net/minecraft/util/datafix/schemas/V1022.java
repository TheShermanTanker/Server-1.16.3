package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1022 extends Schema {
    public V1022(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    @Override
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        super.registerTypes(schema, map2, map3);
        schema.registerType(false, References.RECIPE, (Supplier<TypeTemplate>)(() -> DSL.constType(NamespacedSchema.namespacedString())));
        schema.registerType(false, References.PLAYER, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(schema)), "Inventory", DSL.list(References.ITEM_STACK.in(schema)), "EnderItems", DSL.list(References.ITEM_STACK.in(schema)), DSL.optionalFields("ShoulderEntityLeft", References.ENTITY_TREE.in(schema), "ShoulderEntityRight", References.ENTITY_TREE.in(schema), "recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(schema)), "toBeDisplayed", DSL.list(References.RECIPE.in(schema)))))));
        schema.registerType(false, References.HOTBAR, (Supplier<TypeTemplate>)(() -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(schema)))));
    }
}

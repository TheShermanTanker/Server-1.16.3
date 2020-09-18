package net.minecraft.world.inventory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.core.Registry;

public class MenuType<T extends AbstractContainerMenu> {
    public static final MenuType<ChestMenu> GENERIC_9x1;
    public static final MenuType<ChestMenu> GENERIC_9x2;
    public static final MenuType<ChestMenu> GENERIC_9x3;
    public static final MenuType<ChestMenu> GENERIC_9x4;
    public static final MenuType<ChestMenu> GENERIC_9x5;
    public static final MenuType<ChestMenu> GENERIC_9x6;
    public static final MenuType<DispenserMenu> GENERIC_3x3;
    public static final MenuType<AnvilMenu> ANVIL;
    public static final MenuType<BeaconMenu> BEACON;
    public static final MenuType<BlastFurnaceMenu> BLAST_FURNACE;
    public static final MenuType<BrewingStandMenu> BREWING_STAND;
    public static final MenuType<CraftingMenu> CRAFTING;
    public static final MenuType<EnchantmentMenu> ENCHANTMENT;
    public static final MenuType<FurnaceMenu> FURNACE;
    public static final MenuType<GrindstoneMenu> GRINDSTONE;
    public static final MenuType<HopperMenu> HOPPER;
    public static final MenuType<LecternMenu> LECTERN;
    public static final MenuType<LoomMenu> LOOM;
    public static final MenuType<MerchantMenu> MERCHANT;
    public static final MenuType<ShulkerBoxMenu> SHULKER_BOX;
    public static final MenuType<SmithingMenu> SMITHING;
    public static final MenuType<SmokerMenu> SMOKER;
    public static final MenuType<CartographyTableMenu> CARTOGRAPHY_TABLE;
    public static final MenuType<StonecutterMenu> STONECUTTER;
    private final MenuSupplier<T> constructor;
    
    private static <T extends AbstractContainerMenu> MenuType<T> register(final String string, final MenuSupplier<T> a) {
        return Registry.<MenuType<T>>register(Registry.MENU, string, new MenuType<T>(a));
    }
    
    private MenuType(final MenuSupplier<T> a) {
        this.constructor = a;
    }
    
    static {
        GENERIC_9x1 = MenuType.<ChestMenu>register("generic_9x1", (MenuSupplier<ChestMenu>)ChestMenu::oneRow);
        GENERIC_9x2 = MenuType.<ChestMenu>register("generic_9x2", (MenuSupplier<ChestMenu>)ChestMenu::twoRows);
        GENERIC_9x3 = MenuType.<ChestMenu>register("generic_9x3", (MenuSupplier<ChestMenu>)ChestMenu::threeRows);
        GENERIC_9x4 = MenuType.<ChestMenu>register("generic_9x4", (MenuSupplier<ChestMenu>)ChestMenu::fourRows);
        GENERIC_9x5 = MenuType.<ChestMenu>register("generic_9x5", (MenuSupplier<ChestMenu>)ChestMenu::fiveRows);
        GENERIC_9x6 = MenuType.<ChestMenu>register("generic_9x6", (MenuSupplier<ChestMenu>)ChestMenu::sixRows);
        GENERIC_3x3 = MenuType.<DispenserMenu>register("generic_3x3", (MenuSupplier<DispenserMenu>)DispenserMenu::new);
        ANVIL = MenuType.<AnvilMenu>register("anvil", (MenuSupplier<AnvilMenu>)AnvilMenu::new);
        BEACON = MenuType.<BeaconMenu>register("beacon", (MenuSupplier<BeaconMenu>)BeaconMenu::new);
        BLAST_FURNACE = MenuType.<BlastFurnaceMenu>register("blast_furnace", (MenuSupplier<BlastFurnaceMenu>)BlastFurnaceMenu::new);
        BREWING_STAND = MenuType.<BrewingStandMenu>register("brewing_stand", (MenuSupplier<BrewingStandMenu>)BrewingStandMenu::new);
        CRAFTING = MenuType.<CraftingMenu>register("crafting", (MenuSupplier<CraftingMenu>)CraftingMenu::new);
        ENCHANTMENT = MenuType.<EnchantmentMenu>register("enchantment", (MenuSupplier<EnchantmentMenu>)EnchantmentMenu::new);
        FURNACE = MenuType.<FurnaceMenu>register("furnace", (MenuSupplier<FurnaceMenu>)FurnaceMenu::new);
        GRINDSTONE = MenuType.<GrindstoneMenu>register("grindstone", (MenuSupplier<GrindstoneMenu>)GrindstoneMenu::new);
        HOPPER = MenuType.<HopperMenu>register("hopper", (MenuSupplier<HopperMenu>)HopperMenu::new);
        LECTERN = MenuType.<LecternMenu>register("lectern", (MenuSupplier<LecternMenu>)((integer, bfs) -> new LecternMenu(integer)));
        LOOM = MenuType.<LoomMenu>register("loom", (MenuSupplier<LoomMenu>)LoomMenu::new);
        MERCHANT = MenuType.<MerchantMenu>register("merchant", (MenuSupplier<MerchantMenu>)MerchantMenu::new);
        SHULKER_BOX = MenuType.<ShulkerBoxMenu>register("shulker_box", (MenuSupplier<ShulkerBoxMenu>)ShulkerBoxMenu::new);
        SMITHING = MenuType.<SmithingMenu>register("smithing", (MenuSupplier<SmithingMenu>)SmithingMenu::new);
        SMOKER = MenuType.<SmokerMenu>register("smoker", (MenuSupplier<SmokerMenu>)SmokerMenu::new);
        CARTOGRAPHY_TABLE = MenuType.<CartographyTableMenu>register("cartography_table", (MenuSupplier<CartographyTableMenu>)CartographyTableMenu::new);
        STONECUTTER = MenuType.<StonecutterMenu>register("stonecutter", (MenuSupplier<StonecutterMenu>)StonecutterMenu::new);
    }
    
    interface MenuSupplier<T extends AbstractContainerMenu> {
    }
}

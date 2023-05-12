package to.tinypota.ollivanders.registry.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.item.WandItem;

public class OllivandersItems {
    public static final WandItem WAND_ITEM = register("wand", new WandItem(new Item.Settings().maxCount(1).fireproof()), OllivandersItemGroups.OLLIVANDERED_WANDS_KEY);

    public static void init() {

    }

    public static <I extends Item> I register(String name, I item) {
        return register(name, item, OllivandersItemGroups.OLLIVANDERED_KEY);
    }

    public static <I extends Item> I register(String name, I item, RegistryKey<ItemGroup> group) {
        I result = Registry.register(Registries.ITEM, Ollivanders.id(name), item);
        OllivandersItemGroups.addToItemGroup(group, item);
        return result;
    }
}

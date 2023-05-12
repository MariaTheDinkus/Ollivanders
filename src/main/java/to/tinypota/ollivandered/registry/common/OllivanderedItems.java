package to.tinypota.ollivandered.registry.common;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import to.tinypota.ollivandered.Ollivandered;
import to.tinypota.ollivandered.common.item.WandItem;

public class OllivanderedItems {
    public static final WandItem WAND_ITEM = register("wand", new WandItem(new Item.Settings().maxCount(1).fireproof()));

    public static void init() {

    }

    public static <I extends Item> I register(String name, I item) {
        I result = Registry.register(Registries.ITEM, Ollivandered.id(name), item);
        OllivanderedItemGroups.addToDefault(result);
        return result;
    }
}

package to.tinypota.ollivanders.registry.common;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.item.WandItem;

public class OllivandersItems {
    public static final WandItem WAND_ITEM = register("wand", new WandItem(new Item.Settings().maxCount(1).fireproof()));

    public static void init() {

    }

    public static <I extends Item> I register(String name, I item) {
        I result = Registry.register(Registries.ITEM, Ollivanders.id(name), item);
        OllivandersItemGroups.addToDefault(result);
        return result;
    }
}

package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import to.tinypota.ollivanders.Ollivanders;

public class OllivandersItemGroups {
    public static final RegistryKey<ItemGroup> OLLIVANDERED_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Ollivanders.id("ollivanders"));
    public static final ItemGroup OLLIVANDERED = FabricItemGroup.builder().icon(OllivandersItems.WAND_ITEM::getDefaultStack).displayName(Text.translatable("group.ollivanders")).build();

    public static void init() {
        register(OLLIVANDERED_KEY, OLLIVANDERED);
    }

    public static void addToItemGroup(RegistryKey<ItemGroup> group, Item item) {
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
    }

    public static void addToDefault(Item item) {
        ItemGroupEvents.modifyEntriesEvent(OLLIVANDERED_KEY).register(entries -> entries.add(item));
    }

    public static ItemGroup register(RegistryKey<ItemGroup> key, ItemGroup itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, key, itemGroup);
    }
}

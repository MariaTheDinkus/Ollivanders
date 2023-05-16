package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.util.WandHelper;

public class OllivandersItemGroups {
	public static final RegistryKey<ItemGroup> OLLIVANDERS_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Ollivanders.id("ollivanders_general"));
	public static final ItemGroup OLLIVANDERS = FabricItemGroup.builder().icon(() -> new ItemStack(OllivandersBlocks.LAUREL_WOOD.getLog())).entries((displayContext, entries) -> {}).displayName(Text.translatable("itemGroup.ollivanders.general")).build();
	public static final RegistryKey<ItemGroup> OLLIVANDERS_WANDS_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Ollivanders.id("ollivanders_wands"));
	public static final ItemGroup OLLIVANDERS_WANDS = FabricItemGroup.builder().icon(() -> new ItemStack(OllivandersItems.OAK_WAND)).entries((displayContext, entries) -> {}).displayName(Text.translatable("itemGroup.ollivanders.wands")).build();
	
	public static void init() {
		register(OLLIVANDERS_KEY, OLLIVANDERS);
		register(OLLIVANDERS_WANDS_KEY, OLLIVANDERS_WANDS);
	}
	
	public static void addToItemGroup(RegistryKey<ItemGroup> group, Item item) {
		ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
	}
	
	public static void addWandsToItemGroup(Item item) {
		ItemGroupEvents.modifyEntriesEvent(OLLIVANDERS_WANDS_KEY).register(entries -> {
			for (var core : OllivandersRegistries.CORE) {
				if (core != OllivandersCores.EMPTY) {
					var stack = new ItemStack(item);
					WandHelper.setCore(stack, OllivandersRegistries.CORE.getId(core));
					entries.add(stack);
				}
			}
		});
	}
	
	public static void addToDefault(Item item) {
		ItemGroupEvents.modifyEntriesEvent(OLLIVANDERS_KEY).register(entries -> entries.add(item));
	}
	
	public static ItemGroup register(RegistryKey<ItemGroup> key, ItemGroup itemGroup) {
		return Registry.register(Registries.ITEM_GROUP, key, itemGroup);
	}
}

package to.tinypota.ollivanders.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import to.tinypota.ollivanders.api.wand.WandMatchLevel;
import to.tinypota.ollivanders.common.core.Core;
import to.tinypota.ollivanders.common.item.WandItem;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.common.OllivandersCores;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.UUID;
import java.util.WeakHashMap;

public class WandCraftHelper {
	private static final WeakHashMap<Item, WandItem> WAND_CRAFTS = new WeakHashMap<>();
	
	public static WeakHashMap<Item, WandItem> getWandCrafts() {
		return WAND_CRAFTS;
	}
	
	public static void putCraft(Item planks, WandItem wandItem) {
		WAND_CRAFTS.put(planks, wandItem);
	}
	
	public static boolean canCraft(Item item) {
		return WAND_CRAFTS.containsKey(item);
	}
	
	public static WandItem getCraft(Item planks) {
		return WAND_CRAFTS.get(planks);
	}
}

package to.tinypota.ollivanders.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import to.tinypota.ollivanders.api.wand.WandMatchLevel;
import to.tinypota.ollivanders.common.core.Core;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.common.OllivandersCores;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.UUID;

public class WandHelper {
	public static boolean hasOwner(ItemStack stack) {
		var compound = stack.getOrCreateNbt();
		return compound.contains("owner", NbtElement.INT_ARRAY_TYPE) && compound.contains("owner_name", NbtElement.STRING_TYPE);
	}
	
	public static Pair<String, UUID> getOwnerDetails(ItemStack stack) {
		return new Pair<>(getOwnerName(stack), getOwnerUUID(stack));
	}
	
	public static String getOwnerName(ItemStack stack) {
		var compound = stack.getOrCreateNbt();
		if (compound.contains("owner_name", NbtElement.STRING_TYPE)) {
			return compound.getString("owner_name");
		}
		return null;
	}
	
	public static UUID getOwnerUUID(ItemStack stack) {
		var compound = stack.getOrCreateNbt();
		if (compound.contains("owner", NbtElement.INT_ARRAY_TYPE)) {
			return compound.getUuid("owner");
		}
		return null;
	}
	
	public static void setOwner(ItemStack stack, String name, UUID uuid) {
		var compound = stack.getOrCreateNbt();
		compound.putUuid("owner", uuid);
		compound.putString("owner_name", name);
	}
	
	public static void setOwner(ItemStack stack, PlayerEntity entity) {
		setOwner(stack, entity.getEntityName(), entity.getUuid());
	}
	
	public static PlayerEntity getOwner(ItemStack stack, MinecraftServer server) {
		var compound = stack.getOrCreateNbt();
		if (compound.contains("owner", NbtElement.INT_ARRAY_TYPE)) {
			var uuid = compound.getUuid("owner");
			if (server.getPlayerManager().getPlayer(uuid) != null) {
				return server.getPlayerManager().getPlayer(uuid);
			}
		}
		return null;
	}
	
	public static WandMatchLevel getWandMatch(ItemStack stack, PlayerEntity user) {
		if (hasCore(stack)) {
			var isSuitableWand = isSuitableWand(user, stack);
			var hasSuitableCore = hasSuitableCore(user, stack);
			if (isSuitableWand && hasSuitableCore) {
				return WandMatchLevel.PERFECT;
			} else if (isSuitableWand != hasSuitableCore) {
				return WandMatchLevel.AVERAGE;
			} else {
				return WandMatchLevel.AWFUL;
			}
		}
		return null;
	}
	
	public static void setCore(ItemStack stack, Identifier identifier) {
		var compound = stack.getOrCreateNbt();
		compound.putString("core", identifier.toString());
	}
	
	public static boolean hasCore(ItemStack stack) {
		var compound = stack.getOrCreateNbt();
		if (compound.contains("core", NbtElement.STRING_TYPE)) {
			var id = new Identifier(compound.getString("core"));
			return OllivandersRegistries.CORE.containsId(id);
		}
		return false;
	}
	
	public static Core getCore(ItemStack stack) {
		var compound = stack.getOrCreateNbt();
		if (hasCore(stack)) {
			return OllivandersRegistries.CORE.get(new Identifier(compound.getString("core")));
		}
		return OllivandersCores.EMPTY;
	}
	
	public static boolean isSuitableWand(PlayerEntity entity, ItemStack stack) {
		var suitedWandId = OllivandersServerState.getSuitedWand(entity);
		
		return suitedWandId.equals(Registries.ITEM.getId(stack.getItem()).toString());
	}
	
	public static boolean hasSuitableCore(PlayerEntity entity, ItemStack stack) {
		if (hasCore(stack)) {
			var suitedCoreId = OllivandersServerState.getSuitedCore(entity);
			var suitedCore = OllivandersRegistries.CORE.get(new Identifier(suitedCoreId));
			var core = getCore(stack);
			
			return suitedCore == core;
		} else {
			return false;
		}
	}
}

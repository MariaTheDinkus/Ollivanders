package to.tinypota.ollivanders.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.common.core.Core;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.registry.common.OllivandersCores;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;

import java.util.List;
import java.util.UUID;

public class WandItem extends Item {
	private static final String KEY_PLAYER_UUID = "player_uuid";
	private final Block woodPlanks;
	
	public WandItem(Block woodPlanks, Settings settings) {
		super(settings);
		this.woodPlanks = woodPlanks;
	}
	
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
	
	public static void setOwner(ItemStack stack, LivingEntity entity) {
		setOwner(stack, entity.getEntityName(), entity.getUuid());
	}
	
	public static LivingEntity getOwner(ItemStack stack, MinecraftServer server) {
		var compound = stack.getOrCreateNbt();
		if (compound.contains("owner", NbtElement.INT_ARRAY_TYPE)) {
			var uuid = compound.getUuid("owner");
			if (server.getPlayerManager().getPlayer(uuid) != null) {
				return server.getPlayerManager().getPlayer(uuid);
			}
		}
		return null;
	}
	
	public static WandMatchLevel getWandMatch(ItemStack stack, LivingEntity user) {
		if (hasCore(stack)) {
			var isSuitableWand = isSuitableWand(user, stack);
			var hasSuitableCore = hasSuitableCore(user, stack);
			if (isSuitableWand && hasSuitableCore) {
				return WandMatchLevel.PERFECT;
			} else if (isSuitableWand ? !hasSuitableCore : hasSuitableCore) {
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
	
	public static boolean isSuitableWand(LivingEntity entity, ItemStack stack) {
		var suitedWandId = OllivandersServerState.getSuitedWand(entity);
		
		return suitedWandId.equals(Registries.ITEM.getId(stack.getItem()).toString());
	}
	
	public static boolean hasSuitableCore(LivingEntity entity, ItemStack stack) {
		if (hasCore(stack)) {
			var suitedCoreId = OllivandersServerState.getSuitedCore(entity);
			var suitedCore = OllivandersRegistries.CORE.get(new Identifier(suitedCoreId));
			var core = getCore(stack);
			
			return suitedCore == core;
		} else {
			return false;
		}
	}
	
	@Override
	public String getTranslationKey() {
		return super.getTranslationKey();
	}
	
	public Block getWoodPlanks() {
		return woodPlanks;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		var compound = stack.getOrCreateNbt();
		if (hasCore(stack)) {
			var core = getCore(stack);
			tooltip.add(Text.literal("The core is ").formatted(Formatting.GRAY).append(Text.translatable(core.getTranslationKey())));
		} else {
			tooltip.add(Text.literal("It seems this wand does not yet have a core.").formatted(Formatting.GRAY));
		}
		
		if (hasOwner(stack)) {
			var details = getOwnerDetails(stack);
			tooltip.add(Text.literal("This wand is bound to " + details.getLeft() + ".").formatted(Formatting.GRAY));
		} else {
			tooltip.add(Text.literal("You sense this wand is not bound to anyone.").formatted(Formatting.GRAY));
		}
		super.appendTooltip(stack, world, tooltip, context);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		var stack = user.getStackInHand(hand);
		if (!world.isClient()) {
			if (hasCore(stack)) {
				if (!hasOwner(stack)) {
					var wandMatchLevel = getWandMatch(stack, user);
					if (wandMatchLevel == WandMatchLevel.PERFECT) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 1F, 1F);
						if (user.isSneaking()) {
							user.sendMessage(Text.literal("This wand now belongs to you. The strength of the bond makes it perform excellently.").formatted(Formatting.GOLD), true);
							setOwner(stack, user);
						} else {
							user.sendMessage(Text.literal("A perfect match. Sneak if you wish to take ownership.").formatted(Formatting.GOLD), true);
						}
					} else if (wandMatchLevel == WandMatchLevel.AVERAGE) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 1F, 1F);
						if (user.isSneaking()) {
							user.sendMessage(Text.literal("This wand now belongs to you. The strength of the bond makes it perform averagely.").formatted(Formatting.GOLD), true);
							setOwner(stack, user);
						} else {
							user.sendMessage(Text.literal("I suppose it's usable, in a pinch. Sneak if you wish to take ownership.").formatted(Formatting.GRAY), true);
						}
					} else if (wandMatchLevel == WandMatchLevel.AWFUL) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.NEUTRAL, 1F, 1F);
						if (user.isSneaking()) {
							user.sendMessage(Text.literal("This wand now belongs to you. The strength of the bond makes it nigh unusable.").formatted(Formatting.GOLD), true);
							setOwner(stack, user);
						} else {
							user.sendMessage(Text.literal("It sure didn't provoke a strong reaction... Sneak if you wish to take ownership.").formatted(Formatting.GRAY), true);
						}
					}
				} else if (!getOwnerUUID(stack).equals(user.getUuid())) {
					world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_STONE_BREAK, SoundCategory.NEUTRAL, 1F, 1F);
					user.sendMessage(Text.literal("This wand is not yours to use.").formatted(Formatting.GRAY), true);
				} else {
					world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_STONE_BREAK, SoundCategory.NEUTRAL, 1F, 1F);
					user.sendMessage(Text.literal("This wand already belongs to you.").formatted(Formatting.GRAY), true);
				}
			} else {
				user.sendMessage(Text.literal("Nothing seems to happen...").formatted(Formatting.GRAY), true);
			}
		}
		return TypedActionResult.pass(user.getStackInHand(hand));
	}
	
	
}

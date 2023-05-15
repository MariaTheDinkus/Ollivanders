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
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.core.Core;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.spell.SpellType;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.RaycastUtil;
import to.tinypota.ollivanders.registry.common.OllivandersCores;
import to.tinypota.ollivanders.registry.common.OllivandersRegistries;
import to.tinypota.ollivanders.registry.common.OllivandersSpells;

import java.util.List;
import java.util.UUID;

public class WandItem extends Item {
	private static final String KEY_PLAYER_UUID = "player_uuid";
	private final Block woodPlanks;
	
	public WandItem(Block woodPlanks, Settings settings) {
		super(settings);
		this.woodPlanks = woodPlanks;
	}
	
	@Override
	public String getTranslationKey() {
		return super.getTranslationKey();
	}
	
	public Block getWoodPlanks() {
		return woodPlanks;
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
	
	public static void setCore(ItemStack stack, Identifier identifier) {
		var compound = stack.getOrCreateNbt();
		compound.putString("core", identifier.toString());
	}
	
	public static boolean hasCore(ItemStack stack) {
		var compound = stack.getOrCreateNbt();
		if (compound.contains("core", NbtElement.STRING_TYPE)) {
			Identifier id = new Identifier(compound.getString("core"));
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
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		var compound = stack.getOrCreateNbt();
		if (hasCore(stack)) {
			Core core = getCore(stack);
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
					var isSuitableWand = isSuitableWand(user, stack);
					var hasSuitableCore = hasSuitableCore(user, stack);
					if (isSuitableWand && hasSuitableCore) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 1F, 1F);
						if (user.isSneaking()) {
							user.sendMessage(Text.literal("This wand now belongs to you. The strength of the bond makes it perform excellently.").formatted(Formatting.GOLD), true);
							setOwner(stack, user);
						} else {
							user.sendMessage(Text.literal("A perfect match. Sneak if you wish to take ownership.").formatted(Formatting.GOLD), true);
						}
					} else if (isSuitableWand && !hasSuitableCore || !isSuitableWand && hasSuitableCore) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 1F, 1F);
						if (user.isSneaking()) {
							user.sendMessage(Text.literal("This wand now belongs to you. The strength of the bond makes it perform averagely.").formatted(Formatting.GOLD), true);
							setOwner(stack, user);
						} else {
							user.sendMessage(Text.literal("I suppose it's usable, in a pinch. Sneak if you wish to take ownership.").formatted(Formatting.GRAY), true);
						}
					} else {
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
//		if (!currentSpell.isEmpty()) {
//			user.sendMessage(Text.literal("You just casted the spell " + currentSpell.getCastName() + "!"), true);
//			if (currentSpell.getType() == SpellType.SELF) {
//				currentSpell.onSelfCast(user);
//				OllivandersSpells.emptyCurrentSpell(user);
//			} else if (currentSpell.getType() == SpellType.RAYCAST) {
//				BlockHitResult blockHitResult = RaycastUtil.raycastBlocks(world, user, 100, currentSpell.shouldHitWater());
//				EntityHitResult entityHitResult = RaycastUtil.raycastEntities(world, user, 100);
//				if (entityHitResult != null) {
//					currentSpell.onHitEntity(world, entityHitResult);
//					OllivandersSpells.emptyCurrentSpell(user);
//				} else if (blockHitResult != null) {
//					currentSpell.onHitBlock(world, blockHitResult);
//					OllivandersSpells.emptyCurrentSpell(user);
//				}
//			} else if (currentSpell.getType() == SpellType.PROJECTILE) {
//				world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
//
//				if (!world.isClient) {
//					var spellProjectileEntity = new SpellProjectileEntity(Spell.EMPTY, user, world);
//					spellProjectileEntity.setPosition(spellProjectileEntity.getPos().add(0, 0.1 - 2 / 16F, 0));
//					world.spawnEntity(spellProjectileEntity);
//				}
//
//				user.incrementStat(Stats.USED.getOrCreateStat(this));
//				OllivandersSpells.emptyCurrentSpell(user);
//			}
//			return TypedActionResult.success(stack, world.isClient());
//		}
		return TypedActionResult.pass(user.getStackInHand(hand));
	}
	
	
}

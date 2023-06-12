package to.tinypota.ollivanders.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.api.wand.WandMatchLevel;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.SpellHelper;
import to.tinypota.ollivanders.common.util.WandHelper;

import java.util.List;

public class WandItem extends Item {
	private static final String KEY_PLAYER_UUID = "player_uuid";
	private final Block[] craftBlocks;
	
	public WandItem(Settings settings, Block... craftBlocks) {
		super(settings);
		this.craftBlocks = craftBlocks;
	}
	
	@Override
	public String getTranslationKey() {
		return super.getTranslationKey();
	}
	
	public Block[] getCraftBlocks() {
		return craftBlocks;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		var compound = stack.getOrCreateNbt();
		if (WandHelper.hasCore(stack)) {
			var core = WandHelper.getCore(stack);
			tooltip.add(Text.literal("The core is ").formatted(Formatting.GRAY).append(Text.translatable(core.getTranslationKey())));
		} else {
			tooltip.add(Text.literal("It seems this wand does not yet have a core.").formatted(Formatting.GRAY));
		}
		
		if (WandHelper.hasOwner(stack)) {
			var details = WandHelper.getOwnerDetails(stack);
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
			if (WandHelper.hasCore(stack)) {
				if (!WandHelper.hasOwner(stack)) {
					var wandMatchLevel = WandHelper.getWandMatch(stack, user);
					if (wandMatchLevel == WandMatchLevel.PERFECT) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 1F, 1F);
						if (user.isSneaking()) {
							user.sendMessage(Text.literal("This wand now belongs to you. The strength of the bond makes it perform excellently.").formatted(Formatting.GOLD), true);
							WandHelper.setOwner(stack, user);
						} else {
							user.sendMessage(Text.literal("A perfect match. Sneak if you wish to take ownership.").formatted(Formatting.GOLD), true);
						}
					} else if (wandMatchLevel == WandMatchLevel.AVERAGE) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 1F, 1F);
						if (user.isSneaking()) {
							user.sendMessage(Text.literal("This wand now belongs to you. The strength of the bond makes it perform averagely.").formatted(Formatting.GOLD), true);
							WandHelper.setOwner(stack, user);
						} else {
							user.sendMessage(Text.literal("I suppose it's usable, in a pinch. Sneak if you wish to take ownership.").formatted(Formatting.GRAY), true);
						}
					} else if (wandMatchLevel == WandMatchLevel.AWFUL) {
						world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.NEUTRAL, 1F, 1F);
						if (user.isSneaking()) {
							user.sendMessage(Text.literal("This wand now belongs to you. The strength of the bond makes it nigh unusable.").formatted(Formatting.GOLD), true);
							WandHelper.setOwner(stack, user);
						} else {
							user.sendMessage(Text.literal("It sure didn't provoke a strong reaction... Sneak if you wish to take ownership.").formatted(Formatting.GRAY), true);
						}
					}
				} else if (!WandHelper.getOwnerUUID(stack).equals(user.getUuid())) {
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
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!world.isClient() && entity instanceof PlayerEntity && selected && world.getTime() % 10 == 0) {
			var player = (PlayerEntity) entity;
			var serverState = OllivandersServerState.getServerState(player.getServer());
			var spell = SpellHelper.getCurrentSpell(player);
			if (spell != Spell.EMPTY) {
				var wandMatchLevel = WandHelper.getWandMatch(stack, player);
				var powerLevel = spell.getAvailablePowerLevel(wandMatchLevel, serverState.getSkillLevel(player, spell));
				serverState.setCurrentSpellPowerLevel(player, powerLevel);
			} else {
				serverState.setCurrentSpellPowerLevel(player, SpellPowerLevel.MAXIMUM);
			}
			serverState.syncPowerLevels((PlayerEntity) entity);
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}
}

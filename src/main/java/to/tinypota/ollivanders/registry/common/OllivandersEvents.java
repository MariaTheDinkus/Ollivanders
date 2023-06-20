package to.tinypota.ollivanders.registry.common;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.message.SentMessage;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.apache.logging.log4j.core.jmx.Server;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.floo.FlooActivation;
import to.tinypota.ollivanders.common.block.ChildFlooFireBlock;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.item.WandItem;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.SpellHelper;
import to.tinypota.ollivanders.common.util.WandHelper;

import java.time.Instant;

public class OllivandersEvents {
	public static void init() {
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if (entity instanceof EnderDragonEntity) {
				var itemEntity = new ItemEntity(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(OllivandersItems.DRAGON_HEARTSTRING, 3));
				entity.getWorld().spawnEntity(itemEntity);
			}
		});
		
		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
			var stringMessage = message.getContent().getString();
			var server = sender.getServer();
			var stack = sender.getStackInHand(Hand.MAIN_HAND);
			var world = sender.getWorld();
			var pos = sender.getBlockPos();
			var serverState = OllivandersServerState.getServerState(sender.getServer());
			var playerState = serverState.getPlayerState(sender);
			var flooState = serverState.getFlooState();

			if (!sender.getStackInHand(Hand.MAIN_HAND).isEmpty() && sender.getStackInHand(Hand.MAIN_HAND).getItem() instanceof WandItem) {
				var wandMatchLevel = WandHelper.getWandMatch(stack, sender);
				if (SpellHelper.containsSpellName(stringMessage)) {
					var verified = message.hasSignature() && !message.isExpiredOnServer(Instant.now());
					server.logChatMessage(message.getContent(), params, verified ? null : "Not Secure");
					var sentMessage = SentMessage.of(message);
					for (ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList()) {
						if (sender.getUuid() == serverPlayerEntity.getUuid() || serverPlayerEntity.getPos().distanceTo(sender.getPos()) < 100) {
							serverPlayerEntity.sendChatMessage(sentMessage, false, params);
						}
					}

					var spell = SpellHelper.getSpellFromMessage(stringMessage);
					if (spell != Spell.EMPTY) {
						SpellHelper.setCurrentSpell(sender, spell);
						var powerLevel = spell.getAvailablePowerLevel(wandMatchLevel, playerState.getSkillLevel(spell));
						var castPercentage = SpellHelper.getCastPercentage(spell, wandMatchLevel, powerLevel);
						var curPowerLevel = playerState.getPowerLevel();
						var curCastPercentage = SpellHelper.getCastPercentage(spell, wandMatchLevel, curPowerLevel);
						playerState.setCurrentSpellPowerLevel(powerLevel);
					}
					return false;
				}
			} else {
				var state = world.getBlockState(pos);
				if (state.isOf(OllivandersBlocks.CHILD_FLOO_FIRE)) {
					var newPos = pos.offset(state.get(Properties.HORIZONTAL_FACING));
					var newState = world.getBlockState(newPos);
					if (newState.isOf(OllivandersBlocks.FLOO_FIRE)) {
						pos = newPos;
						state = newState;
					}
				}
				
				if (world.getBlockState(pos).getBlock() instanceof FlooFireBlock) {
					if (state.get(FlooFireBlock.ACTIVATION) == FlooActivation.ACTIVE) {
						var storage = flooState.getFlooByNameOrRandom(stringMessage);
						var telKey = RegistryKey.of(RegistryKeys.WORLD, storage.getDimension());
						var telWorld = server.getWorld(telKey);
						
						sender.teleport(telWorld, storage.getPos().getX() + 0.5, storage.getPos().getY(), storage.getPos().getZ() + 0.5, storage.getDirection().asRotation(), sender.getPitch());
						sender.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20 * 15, 0, false, false));
						world.setBlockState(pos, state.with(FlooFireBlock.ACTIVATION, FlooActivation.LEFT));
						world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, 0);
						telWorld.setBlockState(storage.getPos(), state.with(FlooFireBlock.ACTIVATION, FlooActivation.ARRIVED));
						telWorld.playSound(null, storage.getPos().getX() + 0.5, storage.getPos().getY() + 0.5, storage.getPos().getZ() + 0.5, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, 0);
					}
				}
			}
			return true;
		});
		
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			var state = world.getBlockState(pos);
			if (player.isCreative() && state.getBlock() instanceof FlooFireBlock) {
				if (!world.isClient()) {
					if (state.get(Properties.LIT)) {
						world.syncWorldEvent(null, 1009, pos, 0);
						world.setBlockState(pos, state.with(Properties.LIT, false).with(FlooFireBlock.ACTIVATION, FlooActivation.OFF));
						return ActionResult.FAIL;
					} else {
						return ActionResult.PASS;
					}
				}
			}
			
			if (player.isCreative() && state.getBlock() instanceof ChildFlooFireBlock) {
				if (!world.isClient()) {
					var newPos = pos.offset(state.get(Properties.HORIZONTAL_FACING));
					var newState = world.getBlockState(newPos);
					if (newState.getBlock() == OllivandersBlocks.FLOO_FIRE) {
						if (newState.get(Properties.LIT)) {
							world.syncWorldEvent(null, 1009, newPos, 0);
							world.setBlockState(newPos, newState.with(Properties.LIT, false).with(FlooFireBlock.ACTIVATION, FlooActivation.OFF));
							return ActionResult.FAIL;
						} else {
							return ActionResult.PASS;
						}
					}
				}
			}
			
			if (!player.getStackInHand(hand).isEmpty() && player.getStackInHand(hand).getItem() instanceof WandItem) {
				return ActionResult.FAIL;
			}
			return ActionResult.PASS;
		});
	}
}

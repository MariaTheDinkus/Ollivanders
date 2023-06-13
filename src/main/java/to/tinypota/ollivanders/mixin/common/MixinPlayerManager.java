package to.tinypota.ollivanders.mixin.common;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.floo.FlooActivation;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.item.WandItem;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.SpellHelper;
import to.tinypota.ollivanders.common.util.WandHelper;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

import java.util.List;
import java.util.function.Predicate;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {
	@Shadow
	public static Text FILTERED_FULL_TEXT;
	@Shadow
	private MinecraftServer server;
	@Shadow
	private List<ServerPlayerEntity> players;
	
	@Shadow
	abstract boolean verify(SignedMessage message);
	
//	@Inject(method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V", at = @At("HEAD"), cancellable = true)
//	private void onBroadcastChatMessage(SignedMessage message, Predicate<ServerPlayerEntity> shouldSendFiltered, @Nullable ServerPlayerEntity sender, MessageType.Parameters params, CallbackInfo callbackInfo) {
//		var stringMessage = message.getContent().getString();
//
//		if (sender != null && !sender.getStackInHand(Hand.MAIN_HAND).isEmpty() && sender.getStackInHand(Hand.MAIN_HAND).getItem() instanceof WandItem) {
//			var stack = sender.getStackInHand(Hand.MAIN_HAND);
//			var world = sender.getWorld();
//			var pos = sender.getBlockPos();
//			var serverState = OllivandersServerState.getServerState(sender.getServer());
//			var wandMatchLevel = WandHelper.getWandMatch(stack, sender);
//			if (SpellHelper.containsSpellName(stringMessage)) {
//				var verified = verify(message);
//				server.logChatMessage(message.getContent(), params, verified ? null : "Not Secure");
//				var sentMessage = SentMessage.of(message);
//				var wasFiltered = false;
//				for (ServerPlayerEntity serverPlayerEntity : players) {
//					if (sender.getUuid() == serverPlayerEntity.getUuid() || serverPlayerEntity.getPos().distanceTo(sender.getPos()) < 100) {
//						var shouldFilter = shouldSendFiltered.test(serverPlayerEntity);
//						serverPlayerEntity.sendChatMessage(sentMessage, shouldFilter, params);
//						wasFiltered |= shouldFilter && message.isFullyFiltered();
//					}
//				}
//				if (wasFiltered && sender != null) {
//					sender.sendMessage(FILTERED_FULL_TEXT);
//				}
//
//				var spell = SpellHelper.getSpellFromMessage(stringMessage);
//				if (spell != Spell.EMPTY) {
//					SpellHelper.setCurrentSpell(sender, spell);
//					var powerLevel = spell.getAvailablePowerLevel(wandMatchLevel, serverState.getSkillLevel(sender, spell));
//					var castPercentage = SpellHelper.getCastPercentage(spell, wandMatchLevel, powerLevel);
//					var curPowerLevel = serverState.getPowerLevel(sender);
//					var curCastPercentage = SpellHelper.getCastPercentage(spell, wandMatchLevel, curPowerLevel);
//					serverState.setCurrentSpellPowerLevel(sender, powerLevel);
////					sender.sendMessage(Text.literal("Set spell " + spell.getCastName() + " with a cast percentage of " + castPercentage + " for the max power level of " + powerLevel.getName() + "."));
////					sender.sendMessage(Text.literal("The players current chosen power level " + curPowerLevel.getName() + " has a cast percentage of " + curCastPercentage + "."));
//				}
//				callbackInfo.cancel();
//			} else {
//				var state = world.getBlockState(pos);
//				if (state.isOf(OllivandersBlocks.CHILD_FLOO_FIRE)) {
//					var newPos = pos.offset(state.get(Properties.HORIZONTAL_FACING));
//					var newState = world.getBlockState(newPos);
//					if (newState.isOf(OllivandersBlocks.FLOO_FIRE)) {
//						pos = newPos;
//						state = newState;
//					}
//				}
//
//				if (world.getBlockState(pos).getBlock() instanceof FlooFireBlock) {
//					if (state.get(FlooFireBlock.ACTIVATION) == FlooActivation.ACTIVE) {
//						var storage = serverState.getFlooByNameOrRandom(stringMessage);
//						var server = world.getServer();
//						var telKey = RegistryKey.of(RegistryKeys.WORLD, storage.getDimension());
//						var telWorld = server.getWorld(telKey);
//
//						sender.teleport(telWorld, storage.getPos().getX() + 0.5, storage.getPos().getY(), storage.getPos().getZ() + 0.5, storage.getDirection().asRotation(), sender.getPitch());
//						sender.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20 * 15, 0, false, false));
//						world.setBlockState(pos, state.with(FlooFireBlock.ACTIVATION, FlooActivation.LEFT));
//						world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, 0);
//						telWorld.setBlockState(storage.getPos(), state.with(FlooFireBlock.ACTIVATION, FlooActivation.ARRIVED));
//						telWorld.playSound(null, storage.getPos().getX() + 0.5, storage.getPos().getY() + 0.5, storage.getPos().getZ() + 0.5, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f, 0);
//					}
//				}
//			}
//		}
//	}
}

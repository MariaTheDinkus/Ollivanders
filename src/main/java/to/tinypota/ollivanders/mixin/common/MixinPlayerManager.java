package to.tinypota.ollivanders.mixin.common;

import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.spell.Spell;
import to.tinypota.ollivanders.common.storage.OllivandersServerState;
import to.tinypota.ollivanders.common.util.SpellHelper;

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
	
	@Inject(method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V", at = @At("HEAD"), cancellable = true)
	private void onBroadcastChatMessage(SignedMessage message, Predicate<ServerPlayerEntity> shouldSendFiltered, @Nullable ServerPlayerEntity sender, MessageType.Parameters params, CallbackInfo callbackInfo) {
		var stringMessage = message.getContent().getString();
		
		if (sender != null) {
			var world = sender.getWorld();
			var pos = sender.getBlockPos();
			var serverState = OllivandersServerState.getServerState(sender.getServer());
			if (SpellHelper.containsSpellName(stringMessage)) {
				var verified = verify(message);
				server.logChatMessage(message.getContent(), params, verified ? null : "Not Secure");
				var sentMessage = SentMessage.of(message);
				var wasFiltered = false;
				for (ServerPlayerEntity serverPlayerEntity : players) {
					if (sender.getUuid() == serverPlayerEntity.getUuid() || serverPlayerEntity.getPos().distanceTo(sender.getPos()) < 100) {
						var shouldFilter = shouldSendFiltered.test(serverPlayerEntity);
						serverPlayerEntity.sendChatMessage(sentMessage, shouldFilter, params);
						wasFiltered |= shouldFilter && message.isFullyFiltered();
					}
				}
				if (wasFiltered && sender != null) {
					sender.sendMessage(FILTERED_FULL_TEXT);
				}
				
				var spell = SpellHelper.getSpellFromMessage(stringMessage);
				if (spell != Spell.EMPTY) {
					SpellHelper.setCurrentSpell(sender, spell);
					Ollivanders.LOGGER.info("THIS IS MIXIN: " + spell.getAvailablePowerLevel(OllivandersServerState.getSkillLevel(sender, spell)));
					serverState.setCurrentSpellPowerLevel(sender, spell.getAvailablePowerLevel(OllivandersServerState.getSkillLevel(sender, spell)));
				}
				callbackInfo.cancel();
			} else if (world.getBlockState(pos).getBlock() instanceof FlooFireBlock) {
				var state = world.getBlockState(pos);
				if (state.get(FlooFireBlock.ACTIVE)) {
					var pair = serverState.getFlooByNameOrRandom(stringMessage);
					
					sender.teleport((ServerWorld) world, pair.getLeft().getX() + 0.5, pair.getLeft().getY(), pair.getLeft().getZ() + 0.5, pair.getRight().asRotation(), sender.getPitch());
					world.setBlockState(pos, state.with(FlooFireBlock.ACTIVE, false));
				}
			}
		}
	}
}

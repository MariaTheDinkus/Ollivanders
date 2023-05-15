package to.tinypota.ollivanders.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import to.tinypota.ollivanders.common.item.WandItem;
import to.tinypota.ollivanders.registry.common.OllivandersNetworking;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(method = "doAttack", at = @At("HEAD"))
	private void onAttack(CallbackInfoReturnable<Boolean> ci) {
		var player = MinecraftClient.getInstance().player;
		
		if (player == null) {
			return;
		}
		
		if (player.getMainHandStack().getItem() instanceof WandItem) {
			var buf = PacketByteBufs.create();
			buf.writeUuid(player.getUuid());
			ClientPlayNetworking.send(OllivandersNetworking.SWING_WAND_PACKET_ID, buf);
		}
	}
}

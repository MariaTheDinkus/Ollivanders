package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.item.WandItem;
import to.tinypota.ollivanders.registry.common.OllivandersNetworking;

public class OllivandersEvents {
	public static SpellPowerLevel currentSpellPowerLevel;
	public static SpellPowerLevel powerLevel;
	
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				if (currentSpellPowerLevel == null || powerLevel == null) {
					ClientPlayNetworking.send(OllivandersNetworking.SYNC_POWER_LEVELS, PacketByteBufs.create());
				} else {
					if (OllivandersKeyBindings.DECREASE_POWER_LEVEL.wasPressed() && client.player != null) {
						ClientPlayNetworking.send(OllivandersNetworking.DECREASE_POWER_LEVEL, PacketByteBufs.create());
					}
					
					if (OllivandersKeyBindings.INCREASE_POWER_LEVEL.wasPressed() && client.player != null) {
						ClientPlayNetworking.send(OllivandersNetworking.INCREASE_POWER_LEVEL, PacketByteBufs.create());
					}
				}
			}
		});
		
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
			var client = MinecraftClient.getInstance();
			
			if (client.player != null) {
				if (currentSpellPowerLevel != null && powerLevel != null) {
					var stack = client.player.getStackInHand(Hand.MAIN_HAND);
					if (!stack.isEmpty() && stack.getItem() instanceof WandItem) {
						var scaledWidth = client.getWindow().getScaledWidth();
						var scaledHeight = client.getWindow().getScaledHeight();
						
						var textureWidth = 21;
						var textureHeight = 12;
						var u = 0;
						var v = 0;
						var x = (scaledWidth - textureWidth) / 2;
						var y = (scaledHeight - textureHeight) / 2 + 5;

						drawContext.drawTexture(Ollivanders.id("textures/gui/power_bar.png"), x, y, u + (textureWidth * (4 - currentSpellPowerLevel.getId())), v + (textureHeight * powerLevel.getId()), textureWidth, textureHeight);
					}
				}
			}
		});
	}
}

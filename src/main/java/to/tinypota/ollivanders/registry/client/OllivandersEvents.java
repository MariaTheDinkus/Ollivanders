package to.tinypota.ollivanders.registry.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.item.WandItem;
import to.tinypota.ollivanders.registry.common.OllivandersNetworking;

import java.awt.*;
import java.text.DecimalFormat;

public class OllivandersEvents {
	public static SpellPowerLevel currentSpellPowerLevel;
	public static SpellPowerLevel powerLevel;
	public static double castPercentage;
	
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
						
						var castTextureWidth = 21;
						var castTextureHeight = 8;
						var castU = 0;
						var castV = 72;
						var castX = (scaledWidth - castTextureWidth) / 2;
						var castY = (scaledHeight - castTextureHeight) / 2 + 17;
						if (castPercentage >= 0.95) {
							castV += castTextureHeight * 2;
						} else if (castPercentage >= 0.85) {
							castV += castTextureHeight;
						}
						
						DecimalFormat formatter = new DecimalFormat("#0.00%");
						var text = formatter.format(castPercentage);
						var originalRGB = RenderSystem.getShaderColor().clone();
						var rgb = getRGBFromPercentage(castPercentage);
						drawContext.drawTexture(Ollivanders.id("textures/gui/power_bar.png"), x, y, u + (textureWidth * (4 - currentSpellPowerLevel.getId())), v + (textureHeight * powerLevel.getId()), textureWidth, textureHeight);
						drawContext.setShaderColor(rgb[0], rgb[1], rgb[2], 1);
						drawContext.drawTexture(Ollivanders.id("textures/gui/power_bar.png"), castX, castY, castU, castV, castTextureWidth, castTextureHeight);
						drawContext.setShaderColor(originalRGB[0], originalRGB[1], originalRGB[2], 1);
						if (FabricLoader.getInstance().isDevelopmentEnvironment() && client.options.advancedItemTooltips) {
							drawContext.drawText(client.textRenderer, Text.literal(text), (scaledWidth - client.textRenderer.getWidth(text)) / 2, y + 45, 0xFFFFFF, true);
						}
					}
				}
			}
		});
	}
	
	public static float[] getRGBFromPercentage(double castPercentage) {
		// minHue and maxHue define the color range for the transition
		// adjust these values to achieve the desired color range
		float minHue = 0.00f;  // slightly more than pure red
		float maxHue = 0.28f;  // slightly less than pure green
		
		// interpolate between minHue and maxHue based on the castPercentage
		float hue = minHue + (float) castPercentage * (maxHue - minHue);
		
		float saturation = 1.0f;
		float brightness = 1.0f;
		
		int rgb = Color.HSBtoRGB(hue, saturation, brightness);
		
		float red = ((rgb >> 16) & 0xFF) / 255.0f;
		float green = ((rgb >> 8) & 0xFF) / 255.0f;
		float blue = (rgb & 0xFF) / 255.0f;
		
		return new float[]{red, green, blue};
	}
}

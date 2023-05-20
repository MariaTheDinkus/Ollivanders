package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.client.screen.FlooFireNameScreen;

public class OllivandersNetworking {
	public static final Identifier OPEN_FLOO_FIRE_NAME_SCREEN = Ollivanders.id("open_floo_fire_name_screen");
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(OPEN_FLOO_FIRE_NAME_SCREEN, (client, handler, buf, responseSender) -> {
			var pos = buf.readBlockPos();
			client.execute(() -> {
				client.setScreen(new FlooFireNameScreen(pos));
			});
		});
	}
}
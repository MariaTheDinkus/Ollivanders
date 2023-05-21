package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import to.tinypota.ollivanders.client.screen.FlooFireNameScreen;
import static to.tinypota.ollivanders.registry.common.OllivandersNetworking.OPEN_FLOO_FIRE_NAME_SCREEN;

public class OllivandersNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(OPEN_FLOO_FIRE_NAME_SCREEN, (client, handler, buf, responseSender) -> {
			var name = buf.readString();
			var pos = buf.readBlockPos();
			client.execute(() -> {
				client.setScreen(new FlooFireNameScreen(name, pos));
			});
		});
	}
}
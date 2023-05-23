package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.math.Direction;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.client.screen.FlooFireNameScreen;

import static to.tinypota.ollivanders.registry.common.OllivandersNetworking.OPEN_FLOO_FIRE_NAME_SCREEN;
import static to.tinypota.ollivanders.registry.common.OllivandersNetworking.SYNC_POWER_LEVELS;

public class OllivandersNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC_POWER_LEVELS, (client, handler, buf, responseSender) -> {
			var currentSpellPowerLevel = SpellPowerLevel.byId(buf.readInt());
			var powerLevel = SpellPowerLevel.byId(buf.readInt());
			client.execute(() -> {
				OllivandersEvents.currentSpellPowerLevel = currentSpellPowerLevel;
				OllivandersEvents.powerLevel = powerLevel;
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(OPEN_FLOO_FIRE_NAME_SCREEN, (client, handler, buf, responseSender) -> {
			var name = buf.readString();
			var pos = buf.readBlockPos();
			var direction = buf.readInt();
			client.execute(() -> client.setScreen(new FlooFireNameScreen(name, pos, Direction.byId(direction))));
		});
	}
}
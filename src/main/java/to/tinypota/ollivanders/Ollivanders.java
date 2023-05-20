package to.tinypota.ollivanders;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import to.tinypota.ollivanders.registry.client.OllivandersParticleTypes;
import to.tinypota.ollivanders.registry.common.*;

public class Ollivanders implements ModInitializer {
	public static final String ID = "ollivanders";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	
	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}
	
	@Override
	public void onInitialize() {
		OllivandersRegistries.init();
		OllivandersItemGroups.init();
		OllivandersItems.init();
		OllivandersBlocks.init();
		OllivandersCores.init();
		OllivandersSpells.init();
		OllivandersBlockEntityTypes.init();
		OllivandersEntityTypes.init();
		OllivandersEvents.init();
		OllivandersTrackedData.init();
		OllivandersCommands.init();
		OllivandersDimensions.init();
		OllivandersEntityAttributes.init();
		OllivandersStatusEffects.init();
		OllivandersNetworking.init();
		OllivandersParticleTypes.init();
	}
}

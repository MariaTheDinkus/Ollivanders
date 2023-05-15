package to.tinypota.ollivanders;

import net.fabricmc.api.ClientModInitializer;
import to.tinypota.ollivanders.registry.client.*;

public class OllivandersClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		OllivandersEvents.init();
		OllivandersKeyBindings.init();
		OllivandersEntityRenderers.init();
		OllivandersBlockRenderLayers.init();
		OllivandersModels.init();
	}
}

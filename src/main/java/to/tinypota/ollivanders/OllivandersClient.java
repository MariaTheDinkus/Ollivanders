package to.tinypota.ollivanders;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;
import to.tinypota.ollivanders.registry.client.*;

public class OllivandersClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		OllivandersNetworking.init();
		OllivandersEvents.init();
		OllivandersKeyBindings.init();
		OllivandersEntityRenderers.init();
		OllivandersBlockEntityRenderers.init();
		OllivandersBlockRenderLayers.init();
		OllivandersModels.init();
		
		ParticleFactoryRegistry.getInstance().register(OllivandersParticleTypes.FLOO_FLAME, FlameParticle.Factory::new);
	}
}

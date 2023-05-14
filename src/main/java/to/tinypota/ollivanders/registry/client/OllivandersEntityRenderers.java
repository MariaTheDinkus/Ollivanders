package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import to.tinypota.ollivanders.client.entity.renderer.SpellProjectileEntityRenderer;
import to.tinypota.ollivanders.registry.common.OllivandersEntityTypes;

public class OllivandersEntityRenderers {
	public static void init() {
		EntityRendererRegistry.register(OllivandersEntityTypes.SPELL_PROJECTILE, SpellProjectileEntityRenderer::new);
	}
}

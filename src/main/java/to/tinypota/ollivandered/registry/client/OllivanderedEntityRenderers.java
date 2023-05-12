package to.tinypota.ollivandered.registry.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import to.tinypota.ollivandered.client.entity.renderer.SpellProjectileEntityRenderer;
import to.tinypota.ollivandered.registry.common.OllivanderedEntityTypes;

public class OllivanderedEntityRenderers {
    public static void init() {
        EntityRendererRegistry.register(OllivanderedEntityTypes.SPELL_PROJECTILE, SpellProjectileEntityRenderer::new);
    }
}

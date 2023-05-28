package to.tinypota.ollivanders.registry.client;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import to.tinypota.ollivanders.client.block.entity.CoreBlockEntityRenderer;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;
import to.tinypota.ollivanders.registry.common.OllivandersBlockEntityTypes;

public class OllivandersBlockEntityRenderers {
	public static void init() {
		BlockEntityRendererFactories.register(OllivandersBlockEntityTypes.CORE, CoreBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(OllivandersBlockEntityTypes.SIGN, SignBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(OllivandersBlockEntityTypes.HANGING_SIGN, HangingSignBlockEntityRenderer::new);
	}
}
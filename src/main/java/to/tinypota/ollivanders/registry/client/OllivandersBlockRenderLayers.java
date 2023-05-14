package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;

public class OllivandersBlockRenderLayers {
	public static void init() {
		WoodBlockRegistry.WOOD_BLOCK_STORAGES.forEach(woodBlockStorage -> {
			BlockRenderLayerMap.INSTANCE.putBlock(woodBlockStorage.getDoor(), RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(woodBlockStorage.getTrapdoor(), RenderLayer.getCutout());
		});
	}
}

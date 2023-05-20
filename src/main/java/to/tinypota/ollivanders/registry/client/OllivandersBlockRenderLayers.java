package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import to.tinypota.ollivanders.registry.builder.WoodBlockRegistry;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

public class OllivandersBlockRenderLayers {
	public static void init() {
		BlockRenderLayerMap.INSTANCE.putBlock(OllivandersBlocks.FLOO_FIRE, RenderLayer.getCutout());
		WoodBlockRegistry.WOOD_BLOCK_STORAGES.forEach(woodBlockStorage -> {
			BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), woodBlockStorage.getDoor(), woodBlockStorage.getTrapdoor(), woodBlockStorage.getSapling());
		});
	}
}

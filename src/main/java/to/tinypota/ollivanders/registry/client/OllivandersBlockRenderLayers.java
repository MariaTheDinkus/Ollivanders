package to.tinypota.ollivanders.registry.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

public class OllivandersBlockRenderLayers {
    public static void init() {
        BlockRenderLayerMap.INSTANCE.putBlock(OllivandersBlocks.TEST_BLOCK, RenderLayer.getCutout());
    }
}

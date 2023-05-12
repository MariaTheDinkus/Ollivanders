package to.tinypota.ollivandered.registry.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import to.tinypota.ollivandered.registry.common.OllivanderedBlocks;

public class OllivanderedBlockRenderLayers {
    public static void init() {
        BlockRenderLayerMap.INSTANCE.putBlock(OllivanderedBlocks.TEST_BLOCK, RenderLayer.getCutout());
    }
}

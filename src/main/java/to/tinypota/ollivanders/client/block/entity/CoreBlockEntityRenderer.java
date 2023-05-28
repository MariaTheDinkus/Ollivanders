package to.tinypota.ollivanders.client.block.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.Heightmap;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.common.block.CoreBlock;
import to.tinypota.ollivanders.common.block.entity.CoreBlockEntity;
import to.tinypota.ollivanders.registry.common.OllivandersItems;

import java.util.List;
import java.util.Random;

public class CoreBlockEntityRenderer implements BlockEntityRenderer<CoreBlockEntity> {
	private ItemStack stack = null;
	
	public CoreBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
	
	}
	
	@Override
	public void render(CoreBlockEntity coreBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var client = MinecraftClient.getInstance();
		var itemRenderer = client.getItemRenderer();
		var pos = coreBlockEntity.getPos();
		var randomPosSeed = pos.getX() + pos.getY() + pos.getZ();
		var random = new Random(randomPosSeed);
		var block = (CoreBlock) coreBlockEntity.getCachedState().getBlock();
		var stack = new ItemStack(block.getCore());
		
		if (!(coreBlockEntity.getCachedState().getBlock() instanceof CoreBlock))
			return;
		
		// Store current render state
		matrices.push();
		matrices.translate(0.5, 0.5 / 16, 0.5);
		matrices.scale(0.625f, 0.625f, 0.625f);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) random.nextDouble(360)));
		matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) Math.toRadians(-90)));
		itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
		matrices.pop();
		
//		int worldHeight = coreBlockEntity.getWorld().getHeight();
//		// Prepare to draw lines
//		Tessellator tessellator = Tessellator.getInstance();
//		BufferBuilder buffer = tessellator.getBuffer();
//		matrices.push();
//
//		// Set color to red
//		float red = stack.getItem() == OllivandersItems.PHOENIX_FEATHER ? 1.0F : 0.0F;
//		float green = stack.getItem() == OllivandersItems.THESTRAL_TAIL_HAIR ? 1.0F : 0.0F;;
//		float blue = stack.getItem() == OllivandersItems.UNICORN_TAIL_HAIR ? 1.0F : 0.0F;
//		float alpha = 1.0F;
//		float lineThickness = 0.05F;
//
//		RenderSystem.enableDepthTest();
//		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
//		int adjustedHeight = worldHeight - pos.getY();
//		for (int i = 0; i < 4; i++) {
//			matrices.push();
//			matrices.translate(0, 0, 1);
//			if (i == 1) {
//				matrices.translate(1, 0, 0);
//			} else if (i == 2) {
//				matrices.translate(1, 0, -1);
//			} else if (i == 3) {
//				matrices.translate(0, 0, -1);
//			}
//			matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.toRadians(90 * i)));
//			buffer.vertex(matrices.peek().getPositionMatrix(), 0, 1 + worldHeight, 0).color(red, green, blue, 1f).texture(0f, 0f).next();
//			buffer.vertex(matrices.peek().getPositionMatrix(), 0, 0, 0).color(red, green, blue, 1f).texture(0f, 1f).next();
//			buffer.vertex(matrices.peek().getPositionMatrix(), 1, 0, 0).color(red, green, blue, 1f).texture(1f, 1f).next();
//			buffer.vertex(matrices.peek().getPositionMatrix(), 1, 1 + worldHeight, 0).color(red, green, blue, 1f).texture(1f, 0f).next();
//			matrices.pop();
//		}
//
//		RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
//		RenderSystem.setShaderTexture(0, new Identifier("minecraft", "textures/entity/beacon_beam.png"));
//		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//
//		tessellator.draw();
//		RenderSystem.disableDepthTest();
//
//		matrices.pop();
	}
	
	@Override
	public boolean rendersOutsideBoundingBox(CoreBlockEntity blockEntity) {
		return false;
	}
	
	@Override
	public int getRenderDistance() {
		return 128;
	}
}
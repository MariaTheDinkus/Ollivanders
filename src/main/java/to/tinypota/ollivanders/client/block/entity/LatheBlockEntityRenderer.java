package to.tinypota.ollivanders.client.block.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import to.tinypota.ollivanders.common.block.CoreBlock;
import to.tinypota.ollivanders.common.block.entity.CoreBlockEntity;
import to.tinypota.ollivanders.common.block.entity.LatheBlockEntity;

import java.util.Random;

public class LatheBlockEntityRenderer implements BlockEntityRenderer<LatheBlockEntity> {
	private ItemStack stack = null;
	
	public LatheBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
	
	}
	
	@Override
	public void render(LatheBlockEntity latheBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var client = MinecraftClient.getInstance();
		var itemRenderer = client.getItemRenderer();
		var pos = latheBlockEntity.getPos();
		var randomPosSeed = pos.getX() + pos.getY() + pos.getZ();
		var random = new Random(randomPosSeed);
		var stack = latheBlockEntity.getStack();
		
		if (latheBlockEntity.getStack().isEmpty())
			return;
		
		// Store current render state
		matrices.push();
		matrices.translate(0, 1, 0);
		matrices.translate(0.5, 0.5 / 16, 0.5);
		matrices.scale(0.625f, 0.625f, 0.625f);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) random.nextDouble(360)));
		matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) Math.toRadians(-90)));
		itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
		matrices.pop();
	}
}
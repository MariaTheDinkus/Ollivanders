package to.tinypota.ollivanders.client.block.entity;

import net.minecraft.block.VineBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import to.tinypota.ollivanders.common.block.CoreBlock;
import to.tinypota.ollivanders.common.block.LatheBlock;
import to.tinypota.ollivanders.common.block.entity.CoreBlockEntity;
import to.tinypota.ollivanders.common.block.entity.LatheBlockEntity;
import to.tinypota.ollivanders.common.item.WandItem;

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
		var facing = latheBlockEntity.getCachedState().get(LatheBlock.FACING);
		var progress = latheBlockEntity.getProgress();
		var prevProgress = latheBlockEntity.getPrevProgress();
		var multiplier = 20;
		var deltaProgress = MathHelper.lerp(tickDelta, prevProgress * multiplier, progress * multiplier);
		
		if (latheBlockEntity.getStack().isEmpty())
			return;
		
		// Store current render state
		matrices.push();
		matrices.translate(0, 9.5 / 16f, 0);
		matrices.translate(0.5, 0.5 / 16, 0.5);
		matrices.scale(0.625f, 0.625f, 0.625f);
		if (facing == Direction.NORTH || facing == Direction.SOUTH) {
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.toRadians(180)));
		}
		matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.toRadians(facing.asRotation())));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) Math.toRadians(90)));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.toRadians(deltaProgress)));
		if (stack.getItem() == Items.VINE) {
			matrices.scale(0.4f, 0.4f, 0.4f);
		} else if (stack.getItem() instanceof WandItem) {
			matrices.translate(0.3 / 16f, 0, 0);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) Math.toRadians(-45)));
			matrices.scale(0.4f, 0.4f, 0.4f);
		}
//		matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) Math.toRadians(90)));
		itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
		matrices.pop();
	}
}
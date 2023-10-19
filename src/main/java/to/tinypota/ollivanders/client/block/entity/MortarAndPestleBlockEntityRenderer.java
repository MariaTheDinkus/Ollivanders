package to.tinypota.ollivanders.client.block.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.client.BobbingMotion;
import to.tinypota.ollivanders.common.block.LatheBlock;
import to.tinypota.ollivanders.common.block.entity.LatheBlockEntity;
import to.tinypota.ollivanders.common.block.entity.MortarAndPestleBlockEntity;
import to.tinypota.ollivanders.common.item.WandItem;

import java.util.Random;

public class MortarAndPestleBlockEntityRenderer implements BlockEntityRenderer<MortarAndPestleBlockEntity> {
	private ItemStack stack = null;
	
	public MortarAndPestleBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
	
	}
	
	@Override
	public void render(MortarAndPestleBlockEntity mortarAndPestleBlockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var client = MinecraftClient.getInstance();
		var itemRenderer = client.getItemRenderer();
		var pos = mortarAndPestleBlockEntity.getPos();
		var randomPosSeed = pos.getX() + pos.getY() + pos.getZ();
		var random = new Random(randomPosSeed);
		var stack = mortarAndPestleBlockEntity.getStack();
		var progress = mortarAndPestleBlockEntity.getProgress();
		var prevProgress = mortarAndPestleBlockEntity.getPrevProgress();
		var multiplier = 20;
		var deltaProgress = MathHelper.lerp(tickDelta, prevProgress * 1f, progress * 1f);
		
		matrices.push();
		
		if (!stack.isEmpty()) {
			//TODO: Fancy pile rendering
			matrices.push();
			matrices.translate(0.5, 0, 0.5);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
			matrices.scale(0.5F, 0.5F, 0.5F);
			matrices.translate(0, 0, -0.125);
			itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
			matrices.pop();
		}
		
		matrices.translate(0.5, 0, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.toRadians(deltaProgress) * 20));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(10));
		matrices.translate(-0.5, 0, -0.5);
		
		BobbingMotion motion = new BobbingMotion(12f, 0.15f, 90f);
		matrices.translate(0, motion.getPosition(deltaProgress) + 0.15f, 0);
		BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier(Ollivanders.id("pestle" ),""));
		client.getBlockRenderManager().getModelRenderer().render(client.world, model, mortarAndPestleBlockEntity.getCachedState(), pos, matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), false, net.minecraft.util.math.random.Random.create(), 0, overlay);
		matrices.pop();
	}
}
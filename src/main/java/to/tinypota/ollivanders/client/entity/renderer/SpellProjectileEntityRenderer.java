package to.tinypota.ollivanders.client.entity.renderer;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;

public class SpellProjectileEntityRenderer extends EntityRenderer<SpellProjectileEntity> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/slime/slime.png");

    public SpellProjectileEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(SpellProjectileEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(SpellProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.scale(0.5F, 0.5F, 0.5F);
        matrices.translate(0.0F, 0.25F, 0.0F);

//		var scale = entity.age < 10 ? ((entity.age % 10 + tickDelta) / 10.0F) : 1.0F;
//
//		matrices.scale(scale, scale, scale);

        var itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        itemRenderer.renderItem(Blocks.SLIME_BLOCK.asItem().getDefaultStack(), ModelTransformationMode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);

        matrices.pop();
    }
}

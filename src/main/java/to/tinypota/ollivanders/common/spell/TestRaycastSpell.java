package to.tinypota.ollivanders.common.spell;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class TestRaycastSpell extends Spell {
	public TestRaycastSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public ActionResult onHitBlock(PowerLevel powerLevel, World world, BlockHitResult hitResult) {
		return world.setBlockState(hitResult.getBlockPos().offset(hitResult.getSide()), Blocks.FIRE.getDefaultState()) ? ActionResult.SUCCESS : ActionResult.FAIL;
	}
	
	@Override
	public ActionResult onHitEntity(PowerLevel powerLevel, World world, EntityHitResult hitResult) {
		if (hitResult.getEntity() instanceof LivingEntity) {
			hitResult.getEntity().damage(world.getDamageSources().cactus(), 2);
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}
}

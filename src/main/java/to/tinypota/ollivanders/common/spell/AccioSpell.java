package to.tinypota.ollivanders.common.spell;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.util.SpellHelper;

public class AccioSpell extends Spell {
	public AccioSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public SpellPowerLevel getMaximumPowerLevel() {
		return SpellPowerLevel.NORMAL;
	}
	
	@Override
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult, PlayerEntity playerEntity) {
		var pos = hitResult.getPos();
		var itemEntities = world.getEntitiesByClass(ItemEntity.class, new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), entity -> true);
		Ollivanders.LOGGER.info("WJFIOPWJ");
		for (ItemEntity entity : itemEntities) {
			Ollivanders.LOGGER.info("WJFIOPWJ");
			var pointVector = SpellHelper.getRotationVector(entity.getPos(), playerEntity.getPos());
			pointVector = pointVector.multiply(2);
			entity.setVelocity(pointVector.x, pointVector.y, pointVector.z);
			entity.velocityModified = true;
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}
	
	@Override
	public ActionResult onHitEntity(SpellPowerLevel powerLevel, World world, EntityHitResult hitResult, PlayerEntity playerEntity) {
		var pos = hitResult.getPos();
		var itemEntities = world.getEntitiesByClass(ItemEntity.class, new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), entity -> true);
		if (hitResult.getEntity() instanceof ItemEntity) {
			var entity = hitResult.getEntity();
			var pointVector = SpellHelper.getRotationVector(entity.getPos(), playerEntity.getPos().add(0, playerEntity.getStandingEyeHeight(), 0));
			pointVector = pointVector.multiply(2);
			entity.setVelocity(pointVector.x, pointVector.y, pointVector.z);
			entity.velocityModified = true;
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}
}

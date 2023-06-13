package to.tinypota.ollivanders.common.spell;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.util.SpellHelper;

public class ExpelliarmusSpell extends Spell {
	public ExpelliarmusSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public SpellPowerLevel getMaximumPowerLevel() {
		return SpellPowerLevel.NORMAL;
	}
	
	@Override
	public ActionResult onHitEntity(SpellPowerLevel powerLevel, World world, EntityHitResult hitResult, PlayerEntity playerEntity) {
		var pos = hitResult.getPos();
		var itemEntities = world.getEntitiesByClass(ItemEntity.class, new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), entity -> true);
		if (hitResult.getEntity() instanceof PlayerEntity) {
			var entity = (PlayerEntity) hitResult.getEntity();
			if (!entity.getMainHandStack().isEmpty()) {
				var stack = entity.getMainHandStack();
				var pointVector = SpellHelper.getRotationVector(entity.getPos(), playerEntity.getPos().add(0, playerEntity.getStandingEyeHeight(), 0));
				pointVector = pointVector.multiply(0.25);
				var itemEntity = new ItemEntity(world, entity.getX(), entity.getEyeY(), entity.getZ(), stack, pointVector.x, pointVector.y, pointVector.z);
				itemEntity.setThrower(entity.getUuid());
				itemEntity.setPickupDelay(40);
				world.spawnEntity(itemEntity);
				entity.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
				return ActionResult.SUCCESS;
			} else if (!entity.getOffHandStack().isEmpty()) {
				var stack = entity.getOffHandStack();
				var pointVector = SpellHelper.getRotationVector(entity.getPos(), playerEntity.getPos().add(0, playerEntity.getStandingEyeHeight(), 0));
				pointVector = pointVector.multiply(0.25);
				var itemEntity = new ItemEntity(world, entity.getX(), entity.getEyeY(), entity.getZ(), stack, pointVector.x, pointVector.y, pointVector.z);
				itemEntity.setThrower(entity.getUuid());
				itemEntity.setPickupDelay(40);
				world.spawnEntity(itemEntity);
				entity.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.FAIL;
	}
}

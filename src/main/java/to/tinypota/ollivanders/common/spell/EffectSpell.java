package to.tinypota.ollivanders.common.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class EffectSpell extends Spell {
	private final StatusEffectInstance statusEffect;
	
	public EffectSpell(String castName, StatusEffectInstance statusEffect, Settings settings) {
		super(castName, settings);
		this.statusEffect = statusEffect;
	}
	
	@Override
	public PowerLevel getMaxPowerLevel() {
		return PowerLevel.MAXIMUM;
	}
	
	@Override
	public PowerLevel getAvailablePowerLevel(double skillLevel) {
		return new PowerLevelSkillGuide(2000, 8000, 16000, 32000).availableForSkill(skillLevel);
	}
	
	public StatusEffectInstance getStatusEffect() {
		return new StatusEffectInstance(statusEffect);
	}
	
	@Override
	public ActionResult onHitBlock(PowerLevel powerLevel, World world, BlockHitResult hitResult) {
		return ActionResult.FAIL;
	}
	
	@Override
	public ActionResult onHitEntity(PowerLevel powerLevel, World world, EntityHitResult hitResult) {
		if (hitResult.getEntity() instanceof LivingEntity) {
			return ((LivingEntity) hitResult.getEntity()).addStatusEffect(getStatusEffect()) ? ActionResult.SUCCESS : ActionResult.FAIL;
		}
		return ActionResult.FAIL;
	}
	
	@Override
	public ActionResult onSelfCast(PowerLevel powerLevel, LivingEntity entity) {
		return entity.addStatusEffect(getStatusEffect()) ? ActionResult.SUCCESS : ActionResult.FAIL;
	}
}

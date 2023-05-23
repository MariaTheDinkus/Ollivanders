package to.tinypota.ollivanders.common.spell;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import to.tinypota.ollivanders.api.spell.SpellPowerEvaluator;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.api.spell.SpellPowerStorage;

public class EffectSpell extends Spell {
	private final StatusEffect statusEffect;
	private final SpellPowerStorage<Integer> durations;
	
	public EffectSpell(String castName, StatusEffect statusEffect, SpellPowerStorage<Integer> durations, Settings settings) {
		super(castName, settings);
		this.statusEffect = statusEffect;
		this.durations = durations;
	}
	
	@Override
	public SpellPowerLevel getAvailablePowerLevel(double skillLevel) {
		return SpellPowerLevel.MAXIMUM;
	}
	
	public StatusEffectInstance getStatusEffect(SpellPowerLevel powerLevel) {
		return new StatusEffectInstance(statusEffect, durations.getValue(powerLevel));
	}
	
	@Override
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult) {
		return ActionResult.FAIL;
	}
	
	@Override
	public ActionResult onHitEntity(SpellPowerLevel powerLevel, World world, EntityHitResult hitResult) {
		if (hitResult.getEntity() instanceof LivingEntity) {
			return ((LivingEntity) hitResult.getEntity()).addStatusEffect(getStatusEffect(powerLevel)) ? ActionResult.SUCCESS : ActionResult.FAIL;
		}
		return ActionResult.FAIL;
	}
	
	@Override
	public ActionResult onSelfCast(SpellPowerLevel powerLevel, PlayerEntity playerEntity) {
		return playerEntity.addStatusEffect(getStatusEffect(powerLevel)) ? ActionResult.SUCCESS : ActionResult.FAIL;
	}
}

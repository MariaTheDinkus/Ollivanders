package to.tinypota.ollivanders.common.spell;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import to.tinypota.ollivanders.api.floo.FlooActivation;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

public class EpiskeySpell extends Spell {
	public EpiskeySpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public SpellPowerLevel getMaximumPowerLevel() {
		return SpellPowerLevel.NORMAL;
	}
	
	@Override
	public double getCustomCastPercents(SpellPowerLevel powerLevel) {
		return 0.20;
	}
	
	@Override
	protected ActionResult onHitEntity(SpellPowerLevel powerLevel, World world, EntityHitResult hitResult) {
		var entity = hitResult.getEntity();
		if (entity instanceof LivingEntity) {
			((LivingEntity) entity).heal(6);
		}
		return ActionResult.SUCCESS;
	}
}

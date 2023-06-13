package to.tinypota.ollivanders.common.spell;

import net.minecraft.block.*;
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

public class AguamentiSpell extends Spell {
	public AguamentiSpell(String castName, Settings settings) {
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
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult, PlayerEntity playerEntity, SpellProjectileEntity spellProjectileEntity) {
		var pos = hitResult.getBlockPos();
		var sidePos = pos.offset(hitResult.getSide());
		if (world.getBlockState(pos).contains(Properties.WATERLOGGED)) {
			world.setBlockState(pos, world.getBlockState(pos).with(Properties.WATERLOGGED, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
			return ActionResult.SUCCESS;
		} else if (world.isAir(sidePos)) {
			world.setBlockState(sidePos, Blocks.WATER.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}
	
	@Override
	protected ActionResult onHitEntity(SpellPowerLevel powerLevel, World world, EntityHitResult hitResult) {
		var entity = hitResult.getEntity();
		entity.damage(world.getDamageSources().onFire(), 4F);
		entity.setOnFireFor(6);
		return ActionResult.SUCCESS;
	}
}

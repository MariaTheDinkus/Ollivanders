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

public class IncendioSpell extends Spell {
	public IncendioSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public SpellPowerLevel getAvailablePowerLevel(double skillLevel) {
		return SpellPowerLevel.NORMAL;
	}
	
	@Override
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult, PlayerEntity playerEntity, SpellProjectileEntity spellProjectileEntity) {
		var pos = spellProjectileEntity.getBlockPos();
		var state = world.getBlockState(pos);
		if (state.getBlock() == OllivandersBlocks.FLOO_FIRE) {
			if (!state.get(Properties.LIT)) {
				world.setBlockState(pos, state.with(Properties.LIT, true).with(FlooFireBlock.ACTIVATION, FlooActivation.OFF));
				return ActionResult.SUCCESS;
			}
			return ActionResult.FAIL;
		} else {
			var blockPos = hitResult.getBlockPos();
			var blockState = world.getBlockState(blockPos);
			if (CampfireBlock.canBeLit(blockState) || CandleBlock.canBeLit(blockState) || CandleCakeBlock.canBeLit(blockState)) {
				world.setBlockState(blockPos, blockState.with(Properties.LIT, true), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
				world.emitGameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
				return ActionResult.SUCCESS;
			}
			BlockPos blockPos2 = blockPos.offset(hitResult.getSide());
			if (AbstractFireBlock.canPlaceAt(world, blockPos2, hitResult.getSide().getOpposite())) {
				BlockState blockState2 = AbstractFireBlock.getState(world, blockPos2);
				world.setBlockState(blockPos2, blockState2, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
				world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
				return ActionResult.SUCCESS;
			}
			return ActionResult.FAIL;
		}
	}
	
	@Override
	protected ActionResult onHitEntity(SpellPowerLevel powerLevel, World world, EntityHitResult hitResult) {
		var entity = hitResult.getEntity();
		entity.damage(world.getDamageSources().onFire(), 4F);
		entity.setOnFireFor(6);
		return ActionResult.SUCCESS;
	}
}

package to.tinypota.ollivanders.common.spell;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

public class AliquamFlooSpell extends Spell {
	public AliquamFlooSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public ActionResult onHitBlock(PowerLevel powerLevel, World world, BlockHitResult hitResult, Entity caster) {
		var pos = hitResult.getBlockPos();
		var state = world.getBlockState(pos);
		Ollivanders.LOGGER.info(state.getBlock().getName().getString());
		if (state.getBlock() == Blocks.FIRE && world.getBlockState(pos.up()).getBlock() instanceof AbstractSignBlock) {
			var newState = OllivandersBlocks.FLOO_FIRE.getDefaultState();
			world.setBlockState(pos, newState);
			return ActionResult.FAIL;
		}
		return ActionResult.FAIL;
	}
}

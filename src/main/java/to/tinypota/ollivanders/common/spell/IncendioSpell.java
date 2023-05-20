package to.tinypota.ollivanders.common.spell;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

public class IncendioSpell extends Spell {
	public IncendioSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public ActionResult onHitBlock(PowerLevel powerLevel, World world, BlockHitResult hitResult, Entity caster) {
		var state = world.getBlockState(caster.getBlockPos());
		if (state.getBlock() == OllivandersBlocks.FLOO_FIRE) {
			if (!state.get(Properties.LIT)) {
				world.setBlockState(caster.getBlockPos(), state.with(Properties.LIT, true).with(FlooFireBlock.ACTIVE, false));
				return ActionResult.SUCCESS;
			}
			return ActionResult.FAIL;
		} else {
			return world.setBlockState(hitResult.getBlockPos().offset(hitResult.getSide()), Blocks.FIRE.getDefaultState()) ? ActionResult.SUCCESS : ActionResult.FAIL;
		}
	}
}

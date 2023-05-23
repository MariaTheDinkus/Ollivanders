package to.tinypota.ollivanders.common.spell;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import to.tinypota.ollivanders.Ollivanders;
import to.tinypota.ollivanders.api.floo.FlooActivation;
import to.tinypota.ollivanders.api.spell.SpellPowerLevel;
import to.tinypota.ollivanders.common.block.FlooFireBlock;
import to.tinypota.ollivanders.common.entity.SpellProjectileEntity;
import to.tinypota.ollivanders.registry.common.OllivandersBlocks;

public class ApparateSpell extends Spell {
	public ApparateSpell(String castName, Settings settings) {
		super(castName, settings);
	}
	
	@Override
	public SpellPowerLevel getAvailablePowerLevel(double skillLevel) {
		return SpellPowerLevel.NORMAL;
	}
	
	@Override
	public ActionResult onHitBlock(SpellPowerLevel powerLevel, World world, BlockHitResult hitResult, PlayerEntity playerEntity) {
		var pos = hitResult.getBlockPos().offset(hitResult.getSide());
		var state = world.getBlockState(hitResult.getBlockPos());
		if (state.getCollisionShape(world, hitResult.getBlockPos()).equals(VoxelShapes.empty())) {
			Ollivanders.LOGGER.info("HEY BUDDY YOU NOT SOLID");
			pos = hitResult.getBlockPos();
		}
		((ServerPlayerEntity) playerEntity).teleport((ServerWorld) world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, playerEntity.getYaw(), playerEntity.getPitch());
		return ActionResult.SUCCESS;
	}
}
